package cn.edu.scu.notifyme;

import android.app.Application;
import android.content.Context;
import android.util.LongSparseArray;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

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

    public static void init(Context context) {
        LongSparseArray<Message> map_latestMsg = new LongSparseArray<>();
        for (Rule rule : DatabaseManager.getInstance().getRules()) {
            if (rule.getMsg().isEmpty())
                continue;
            Message latestMsg = rule.getMsg().get(0);
            for (int i = 1; i < rule.getMsg().size(); i++)
                if (latestMsg.getUpdateTime().getTime() <
                        rule.getMsg().get(i).getUpdateTime().getTime())
                    latestMsg = rule.getMsg().get(i);
            map_latestMsg.put(rule.getId(), latestMsg);
        }

        BackgroundWorker.getInstance().bind(context);
        DatabaseManager.getInstance().initial();
        messageFilter = new MessageFilter(map_latestMsg)
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
}