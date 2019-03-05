package cn.edu.scu.notifyme.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

import androidx.annotation.Nullable;

/**
 * Message
 * 消息对象
 */
public class Message extends LitePalSupport {
    /**
     * 消息ID，数据库主键
     */
    private long id;
    /**
     * 更新时间
     */
    @Column(nullable = false)
    private Date updateTime;
    /**
     * 标题
     */
    @Column(nullable = false)
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
    /**
     * 所属Rule
     */
    @Column(nullable = false)
    private Rule rule;

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

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
