package cn.edu.scu.notifyme;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;

import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<Rule, Message> map_latestMsg = new HashMap<>();
        for (Rule rule : DatabaseManager.getInstance().getRules()) {
            if (rule.getMsg().isEmpty())
                continue;
            Message latestMsg = rule.getMsg().get(0);
            for (int i = 1; i < rule.getMsg().size(); i++)
                if (latestMsg.getUpdateTime().getTime() <
                        rule.getMsg().get(i).getUpdateTime().getTime())
                    latestMsg = rule.getMsg().get(i);
            map_latestMsg.put(rule, latestMsg);
        }

        BackgroundWorker.getInstance().bind(context);
        DatabaseManager.getInstance().initial();
        messageFilter = new MessageFilter(map_latestMsg)
                .bind(DatabaseManager.getInstance(), context);
        messageFilter.start();
//        startTasks(DatabaseManager.getInstance().getRules());
    }

    public static void startTasks(List<Rule> rules) {
        if (taskManager != null) return;

        taskManager = new TaskManager(rules)
                .bind(BackgroundWorker.getInstance());
        taskManager.start();
    }

    public static void stopTasks() {
        if (taskManager == null) return;

        taskManager.destroy();
        taskManager = null;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        stopTasks();
        messageFilter.destroy();
    }
}