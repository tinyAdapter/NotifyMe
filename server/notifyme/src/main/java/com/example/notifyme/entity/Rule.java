package com.example.notifyme.entity;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class Rule {

    private long ruleId;

    private String ruleName;

    private boolean isActive;

    private int duration;

    private String iconUrl;

    private String toLoadUrl;

    private String script;

    private long categoryId;

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public String getToLoadUrl() {
        return toLoadUrl;
    }

    public void setToLoadUrl(String toLoadUrl) {
        this.toLoadUrl = toLoadUrl;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
