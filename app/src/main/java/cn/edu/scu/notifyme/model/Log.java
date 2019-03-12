package cn.edu.scu.notifyme.model;

import java.util.Date;

/**
 * Log
 * 日志对象
 */
public class Log {
    /**
     * 日志消息
     */
    private String message;
    /**
     * 生成时间
     */
    private Date date;

    public Log(String message) {
        this.message = message;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
