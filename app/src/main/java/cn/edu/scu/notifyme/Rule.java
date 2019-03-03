package cn.edu.scu.notifyme;

/**
 * Rule
 * 规则对象
 */
public class Rule {
    /**
     * ID，数据库主键
     */
    private long id;
    /**
     * 规则名
     */
    private String name;
    /**
     * 是否启用
     */
    private boolean isActive;
    /**
     * 运行间隔
     */
    private int duration;
    /**
     * 图标URL
     */
    private String iconUrl;
    /**
     * 要访问的URL
     */
    private String toLoadUrl;
    /**
     * 运行的脚本字符串
     */
    private String script;

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
}
