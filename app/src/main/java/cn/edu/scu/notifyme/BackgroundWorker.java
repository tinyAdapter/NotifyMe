package cn.edu.scu.notifyme;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;
import cn.edu.scu.notifyme.model.TaskResult;

/**
 * BackgroundWorker
 * 单例类，负责请求Web页面，注入JS代码并向Activity推送结果
 * 依据生产者/消费者模型构建，由信号量控制WebView一次仅运行一个任务
 * 注：该类仅负责执行任务，具体任务列表的维护由TaskManager类负责
 */
public class BackgroundWorker {
    private static BackgroundWorker instance;

    public static synchronized BackgroundWorker getInstance() {
        if (instance == null) {
            instance = new BackgroundWorker();
        }
        return instance;
    }

    private BackgroundWorker() {
    }


    private WebView webview;
    private JSInterface jsInterface;

    private Thread workerThread;
    private BlockingDeque<Rule> toProcessRules = new LinkedBlockingDeque<>();
    private Semaphore webviewSemaphore;
    private boolean isThreadStopping = false;

    private static long TIMEOUT = 20000;
    private Handler timeoutHandler = new Handler();
    private Runnable timeoutTask;

    public void bind(Context context) {
        if (this.webview != null) {
            this.webview.destroy();
            this.webview = null;
        }
        this.webview = new WebView(context);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(false);
        jsInterface = new JSInterface();
        webview.addJavascriptInterface(jsInterface, "App");
    }

    private class JSInterface {
        private Rule currentRule;

        void setRule(Rule rule) {
            currentRule = rule;
        }

        @JavascriptInterface
        public void Return(String result) {
            LogUtils.d(currentRule.getToLoadUrl() + " JS executed");
            if (!result.startsWith("{")) {
                if (webviewSemaphore != null) webviewSemaphore.release();
                return;
            }
            if (BackgroundWorker.this.isThreadStopping) {
                if (webviewSemaphore != null) webviewSemaphore.release();
                return;
            }

            TaskResult taskResult = new Gson().fromJson(result, TaskResult.class);
            List<TaskResult.MessagesBean> messagesBeans = taskResult.getMessages();

            if (taskResult.getIconUrl() != null) {
                currentRule.setIconUrl(taskResult.getIconUrl());
            }

            List<Message> messages = new ArrayList<>();
            for (TaskResult.MessagesBean messagesBean : messagesBeans) {
                Message msg = new Message();
                msg.setUpdateTime(new Date());
                msg.setTitle(messagesBean.getTitle().trim());
                msg.setContent(messagesBean.getContent().trim());
                msg.setImgUrl(messagesBean.getImgUrl());
                msg.setTargetUrl(messagesBean.getTargetUrl());
                msg.setRule(currentRule);
                messages.add(msg);
            }

            EventBus.getDefault().post(
                    new MessageEvent(EventID.EVENT_HAS_FETCHED_RESULT, messages));

            timeoutHandler.removeCallbacks(timeoutTask);
            if (webviewSemaphore != null) webviewSemaphore.release();
        }
    }

    public void newTask(Rule rule) {
        this.toProcessRules.addLast(rule);
    }

    public void insertTask(Rule rule) {
        this.toProcessRules.addFirst(rule);
    }

    public void stop() {
        isThreadStopping = true;
        workerThread.interrupt();

        toProcessRules.clear();
        webviewSemaphore.release();
        webviewSemaphore = null;
    }

    public void start() {
        if (workerThread != null && workerThread.isAlive()) return;

        isThreadStopping = false;
        webviewSemaphore = new Semaphore(1);
        workerThread = new Thread(() -> {
            LogUtils.d("Now in worker thread...");
            while (!isThreadStopping) {
                try {
                    Rule aRule = toProcessRules.takeFirst();
                    LogUtils.d("Take a new task");
                    webviewSemaphore.acquire();
                    LogUtils.d("WebView semaphore acquired");
                    new Handler(Looper.getMainLooper()).post(() -> {
                        this.process(aRule);
                    });
                } catch (InterruptedException ie) {
                    break;
                }
            }
        });
        workerThread.start();
    }

    private void process(Rule rule) {
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtils.d("On Page Started");
                if (timeoutTask != null) {
                    timeoutHandler.removeCallbacks(timeoutTask);
                    timeoutTask = null;
                }
                timeoutTask = () -> {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        BackgroundWorker.this.webview.stopLoading();
                        LogUtils.w("Timeout loading " + rule.getToLoadUrl());

                        EventBus.getDefault().post(
                                new MessageEvent(EventID.EVENT_FETCH_TIMEOUT, null));

                        if (webviewSemaphore != null) webviewSemaphore.release();
                    });
                };
                timeoutHandler.postDelayed(timeoutTask, TIMEOUT);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                LogUtils.d(rule.getToLoadUrl() + " loaded");

                jsInterface.setRule(rule);
                webview.loadUrl("javascript:" + rule.getScript());
            }
        });
        webview.loadUrl(rule.getToLoadUrl());
        LogUtils.d("Loading " + rule.getToLoadUrl() + " ...");
    }
}
