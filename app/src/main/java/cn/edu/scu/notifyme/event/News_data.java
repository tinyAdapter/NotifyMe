package cn.edu.scu.notifyme.event;

public class News_data {
    private int frist_pic;
    private String _title;
    private String _content;
    private String _time;

    public News_data(int frist_pic, String _title, String _content, String _time) {
        this.frist_pic = frist_pic;
        this._title = _title;
        this._content =_content;
        this._time = _time;
    }

    public int getFrist_pic() {
        return frist_pic;
    }

    public String get_content() {
        return _content;
    }

    public String get_time() {
        return _time;
    }

    public String get_title() {
        return _title;
    }
}
