package cn.edu.scu.notifyme.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.notifyme.model.Message;

import androidx.annotation.Nullable;

/**
 * Rule
 * 规则对象
 */
public class Rule extends LitePalSupport implements Parcelable {
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
    private boolean isActive = false;
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

    public Rule() {}

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

    @Override
    public int hashCode() {
        return this.name.hashCode() ^ this.script.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Rule)) return false;
        Rule thatRule = (Rule) obj;
        if (!this.getName().equals(thatRule.getName())) return false;
        if (this.getDuration() != thatRule.getDuration()) return false;
        if (!this.getToLoadUrl().equals(thatRule.getToLoadUrl())) return false;
        if (this.isActive() != thatRule.isActive()) return false;
        if (!this.getScript().equals(thatRule.getScript())) return false;
        return true;
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


    protected Rule(Parcel in) {
        id = in.readLong();
        name = in.readString();
        isActive = in.readByte() != 0;
        duration = in.readInt();
        iconUrl = in.readString();
        toLoadUrl = in.readString();
        script = in.readString();
        msg = in.createTypedArrayList(Message.CREATOR);
    }

    public static final Creator<Rule> CREATOR = new Creator<Rule>() {
        @Override
        public Rule createFromParcel(Parcel in) {
            return new Rule(in);
        }

        @Override
        public Rule[] newArray(int size) {
            return new Rule[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeInt(duration);
        dest.writeString(iconUrl);
        dest.writeString(toLoadUrl);
        dest.writeString(script);
        dest.writeTypedList(msg);
    }
}
