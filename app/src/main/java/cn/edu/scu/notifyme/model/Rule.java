package cn.edu.scu.notifyme.model;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.notifyme.model.Message;

/**
 * Rule
 * 规则对象
 */
public class Rule extends LitePalSupport {
    /**
     * ID，数据库主键
     */
    private long id;
    /**
     * 规则名
     */
    @Column(unique = true, nullable = false)
    private String name;
    /**
     * 是否启用
     */
    private boolean isActive;
    /**
     * 运行间隔
     */
    @Column(nullable = false)
    private int duration;
    /**
     * 图标URL
     */
    private String iconUrl;
    /**
     * 要访问的URL
     */
    @Column(nullable = false)
    private String toLoadUrl;
    /**
     * 运行的脚本字符串
     */
    @Column(nullable = false)
    private String script;

    @Column(nullable = false)
    private Category category;

    private List<Message> msg = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getToLoadUrl() {
        return toLoadUrl;
    }

    public void setToLoadUrl(String toLoadUrl) {
        this.toLoadUrl = toLoadUrl;
    }

    public List<Message> getMsg() {
        return msg;
    }

    public void setMsg(List<Message> msg) {
        this.msg = msg;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
