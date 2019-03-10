package cn.edu.scu.notifyme;

import android.content.Context;
import android.util.LongSparseArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.interfaces.IStateMachine;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class MessageFilter implements IStateMachine {

    private LongSparseArray<Message> map_rule;

    private Context context;
    private DatabaseManager databaseManager;

    public MessageFilter(
            LongSparseArray<Message> map) {
        map_rule = map;
    }

    public void start() {
        //LogUtils.d("Registering MessageFilter to EventBus...");
        EventBus.getDefault().register(this);
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        //LogUtils.d("event: " + event);
        switch (event.getId()) {
            case EventID.EVENT_HAS_FETCHED_RESULT:
                if (event.getMessage().getRule().getId() != 0
                        && event.getMessage().getRule().getIconUrl() != null) {
                    updateIconOfRule(event.getMessage().getRule());
                }

                if (!isEquals(
                        map_rule.get(event.getMessage().getRule().getId()),
                        event.getMessage()))
                    pushNotification(event.getMessage());
                break;
        }
    }

    private void updateIconOfRule(Rule rule) {
        databaseManager.updateRule(databaseManager.getCategoryByRuleId(rule.getId()), rule);
    }

    public boolean isEquals(Message msg1, Message msg2) {
        if (msg1 == null) return false;
        if (msg1.getTitle() == null || msg1.getContent() == null) return false;
        if (msg1.getTitle().equals(msg2.getTitle())
                && msg1.getContent().equals(msg2.getContent()))
            return true;
        return false;
    }

    private void pushNotification(Message msg) {
        map_rule.remove(msg.getRule().getId());
        map_rule.put(msg.getRule().getId(), msg);
        // TODO:消息推送给各个组件,更新数据库和内存数据
        //LogUtils.d("Message is new , which is added!!!!!");

        databaseManager.addMessage(msg.getRule(), msg);

        msg.setRule(databaseManager.getRuleById(msg.getRule().getId()));
        NotificationService.newMessage(context, msg);
    }

    public IStateMachine bind(DatabaseManager databaseManager, Context context) {
        this.databaseManager = databaseManager;
        this.context = context;
        return this;
    }
}
