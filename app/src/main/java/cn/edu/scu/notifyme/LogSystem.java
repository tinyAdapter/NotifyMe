package cn.edu.scu.notifyme;

import java.util.LinkedList;
import java.util.List;

import cn.edu.scu.notifyme.interfaces.ILogListener;
import cn.edu.scu.notifyme.model.Log;

class LogSystem {
    private static final LogSystem instance = new LogSystem();

    static LogSystem getInstance() {
        return instance;
    }

    private LogSystem() {
    }


    private List<Log> logs = new LinkedList<>();
    private List<ILogListener> subscribers = new LinkedList<>();

    public List<Log> getLogs() {
        return this.logs;
    }

    public void register(ILogListener listener) {
        subscribers.add(listener);
    }

    public void unregister(ILogListener listener) {
        int indexOfSubscriber = subscribers.indexOf(listener);
        if (indexOfSubscriber != -1) {
            subscribers.remove(indexOfSubscriber);
        }
    }

    private void emit(Log log) {
        for (ILogListener subscriber : subscribers) {
            subscriber.onLog(log);
        }
    }

    public void d(Log log) {
        logs.add(log);
        this.emit(log);
    }

    public void d(String message) {
        Log log = new Log(message);
        this.d(log);
    }

    public void removeAllListeners() {
        subscribers = new LinkedList<>();
    }

    public void clear() {
        logs = new LinkedList<>();
    }
}
