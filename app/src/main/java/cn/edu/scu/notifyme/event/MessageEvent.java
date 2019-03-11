package cn.edu.scu.notifyme.event;

import java.util.List;

import cn.edu.scu.notifyme.model.Message;

/**
 * MessageEvent
 * 用于各模块间通信的消息对象
 */
public class MessageEvent {

    public MessageEvent(int id, List<Message> messages) {
        this.id = id;
        this.messages = messages;
    }

    /**
     * 消息ID
     */
    private int id;
    /**
     * 消息内容
     */
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
