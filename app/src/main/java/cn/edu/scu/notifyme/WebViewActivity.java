package cn.edu.scu.notifyme;

import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.tb_notification)
    Toolbar tbNotification;
    @BindView(R.id.fl_internal_browser)
    FrameLayout flInternalBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        WebView webView = BackgroundWorker.getInstance().getWebview();
        if (webView.getParent() != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            parent.removeView(webView);
        }
        ViewGroup parent = (ViewGroup) flInternalBrowser.getParent();
        int index = parent.indexOfChild(flInternalBrowser);
        parent.removeView(flInternalBrowser);
        parent.addView(BackgroundWorker.getInstance().getWebview(), index);
    }
}
