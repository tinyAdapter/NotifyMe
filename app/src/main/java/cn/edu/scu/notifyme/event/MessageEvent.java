package cn.edu.scu.notifyme.event;

import cn.edu.scu.notifyme.Message;

/**
 * MessageEvent
 * 用于各模块间通信的消息对象
 */
public class MessageEvent {

    public MessageEvent(int id, Message message) {
        this.id = id;
        this.message = message;
    }

    /**
     * 消息ID
     */
    private int id;
    /**
     * 消息内容
     */
    private Message message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
