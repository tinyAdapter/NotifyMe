package cn.edu.scu.notifyme.event;

import android.view.View;

public class ViewEvent {
    private int id;
    private View view;

    public ViewEvent(int id, View view) {
        this.id = id;
        this.view = view;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
