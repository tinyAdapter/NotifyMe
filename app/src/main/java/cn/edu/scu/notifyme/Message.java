package cn.edu.scu.notifyme;

import java.util.Date;

import androidx.annotation.Nullable;

/**
 * Message
 * 消息对象
 */
public class Message {
    /**
     * 消息ID，数据库主键
     */
    private long id;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 标题
     */
    private String title;
    /**
     * 正文
     */
    @Nullable
    private String content;
    /**
     * 图片URL
     */
    @Nullable
    private String imgUrl;
    /**
     * 目标URL，点击消息跳转到的网站
     */
    @Nullable
    private String targetUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @Nullable
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(@Nullable String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Nullable
    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(@Nullable String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
