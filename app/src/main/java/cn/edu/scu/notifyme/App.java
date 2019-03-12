package cn.edu.scu.notifyme;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.LongSparseArray;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

import java.util.HashMap;

import cn.edu.scu.notifyme.interfaces.IStateMachine;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        LitePal.initialize(this);
        LocaleUtils.setBaseContext(getBaseContext());
    }

    private static IStateMachine messageFilter;
    private static IStateMachine taskManager;
    private static Context context;

    public static void init(Context context) {
        App.context = context;

        LongSparseArray<HashMap<String, Message>> map_ruleMsgList = new LongSparseArray<>();

        for (Rule rule : DatabaseManager.getInstance().getRules()) {
            HashMap<String, Message> map_msgList = new HashMap<>();
            if (rule.getMsg().isEmpty()) {
                map_ruleMsgList.put(rule.getId(), map_msgList);
                continue;
            }
            for (Message message : rule.getMsg()) {
                map_msgList.put(message.getTitle() + message.getContent(), message);
            }
            map_ruleMsgList.put(rule.getId(), map_msgList);
        }

        BackgroundWorker.getInstance().bind(context);
        DatabaseManager.getInstance().initial();
        messageFilter = new MessageFilter(map_ruleMsgList)
                .bind(DatabaseManager.getInstance(), context);
        messageFilter.start();
    }

    public static void startTasks() {
        if (taskManager != null) return;

        taskManager = new TaskManager(DatabaseManager.getInstance().getRules())
                .bind(BackgroundWorker.getInstance());
        taskManager.start();
        isTasksRunning = true;
    }

    public static void stopTasks() {
        if (taskManager == null) return;

        taskManager.destroy();
        taskManager = null;
        isTasksRunning = false;
    }

    private static boolean isTasksRunning = false;

    public static boolean isTasksRunning() {
        return isTasksRunning;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        stopTasks();
        messageFilter.destroy();
    }

    public static void restart() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}