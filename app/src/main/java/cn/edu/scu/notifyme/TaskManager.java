package cn.edu.scu.notifyme;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import cn.edu.scu.notifyme.interfaces.ITaskManager;
import cn.edu.scu.notifyme.model.Rule;

/**
 * TaskManager
 * 用于维护任务列表，在预定时间内触发fetch请求
 * 传入原始规则列表，调用start函数开始维护，调用destroy函数停止并销毁
 * 一旦调用destroy函数，则无法再次调用start函数，必须重新创建新的TaskManager对象
 */
class TaskManager implements ITaskManager {
    private Iterable<Rule> rules;
    private Timer timer = new Timer();
    private ConcurrentHashMap<Rule, TimerTask> timerTasks = new ConcurrentHashMap<>();
    private BackgroundWorker backgroundWorker;

    public TaskManager(Iterable<Rule> rules, BackgroundWorker backgroundWorker) {
        this.rules = rules;
        this.backgroundWorker = backgroundWorker;
        filterActiveRules();
        createTimers();
    }

    private void filterActiveRules() {
        ArrayList<Rule> activeRules = new ArrayList<>();
        for (Rule rule : this.rules) {
            if (rule.isActive()) {
                activeRules.add(rule);
            }
        }
        this.rules = activeRules;
    }

    private void createTimers() {
        for (Rule rule : this.rules) {
            timerTasks.put(rule, new TimerTask() {
                @Override
                public void run() {
                    TaskManager.this.backgroundWorker.newTask(rule);
                }
            });
        }
    }

    public void start() {
        if (this.timer == null) throw new IllegalStateException();

        this.backgroundWorker.start();
        for (Rule rule : this.timerTasks.keySet()) {
            this.timer.scheduleAtFixedRate(
                    this.timerTasks.get(rule),
                    0,
                    (long) rule.getDuration() * 60 * 1000);
        }
    }

    public void destroy() {
        for (Rule rule : this.timerTasks.keySet()) {
            this.timerTasks.get(rule).cancel();
        }
        this.timer.cancel();
        this.timer.purge();
        this.timer = null;
        this.backgroundWorker.stop();
    }
}
