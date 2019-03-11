package cn.edu.scu.notifyme;

import android.content.ClipData;
import android.content.Context;
import android.util.LongSparseArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.interfaces.IStateMachine;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class MessageFilter implements IStateMachine {

    private LongSparseArray<HashMap<String, Message>> map_rule;

    private Context context;
    private DatabaseManager databaseManager;

    public MessageFilter(
            LongSparseArray<HashMap<String, Message>> map) {
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
                if (event.getMessages().get(0).getRule().getId() != 0
                        && event.getMessages().get(0).getRule().getIconUrl() != null) {
                    updateIconOfRule(event.getMessages().get(0).getRule());
                }

                pushNotification(doFiltering(event.getMessages(),
                        this.map_rule.get(event.getMessages().get(0).getRule().getId())));

                break;
        }
    }

    private void updateIconOfRule(Rule rule) {
        databaseManager.updateRule(databaseManager.getCategoryByRuleId(rule.getId()), rule);
    }

    private List<Message> doFiltering(List<Message> messages, HashMap<String, Message> map_msgs) {
        List<Message> msg_filtered = new ArrayList<>();
        if (messages.size() == 0) return msg_filtered;
        else if (map_msgs == null) {
            HashMap<String, Message> map_new = new HashMap<>();
            for (Message message : messages)
                map_new.put(message.getTitle() + message.getContent(), message);
            map_rule.put(messages.get(0).getRule().getId(), map_new);
            return messages;
        } else if(map_msgs.size() == 0){
            for (Message message : messages)
                map_msgs.put(message.getTitle() + message.getContent(), message);
            return messages;
        }
        else {
            for (Message message : messages) {
                Message msg = map_msgs.get(message.getTitle() + message.getContent());
                if (msg == null)
                    msg_filtered.add(message);
            }
            return msg_filtered;
        }
    }

    private void pushNotification(List<Message> messages) {
        if (messages.size() == 0) return;

        HashMap<String, Message> map = this.map_rule.get(messages.get(0).getRule().getId());

        for (Message message : messages) {
            map.put(message.getTitle() + message.getContent(), message);
            databaseManager.addMessage(message.getRule(), message);

            message.setRule(databaseManager.getRuleById(message.getRule().getId()));
            NotificationService.newMessage(context, message);
        }
    }

    public IStateMachine bind(DatabaseManager databaseManager, Context context) {
        this.databaseManager = databaseManager;
        this.context = context;
        return this;
    }
}
