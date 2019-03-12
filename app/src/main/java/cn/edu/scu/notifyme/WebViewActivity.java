package cn.edu.scu.notifyme;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.tb_internal_browser)
    Toolbar tbInternalBrowser;
    @BindView(R.id.fl_internal_browser)
    FrameLayout flInternalBrowser;
    @BindView(R.id.btn_go)
    MaterialButton btnGo;
    @BindView(R.id.et_address)
    TextInputEditText etAddress;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        tbInternalBrowser.inflateMenu(R.menu.web_view_toolbar_menu);
        tbInternalBrowser.getOverflowIcon().setColorFilter(
                getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        tbInternalBrowser.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_enable_image_loading:
                    BackgroundWorker.getInstance().setImageShown(!item.isChecked());
                    item.setChecked(BackgroundWorker.getInstance().isImageShown());
                    return true;
                default:
                    return false;
            }
        });

        webView = BackgroundWorker.getInstance().getWebview();
        if (webView.getParent() != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            parent.removeView(webView);
        }
        ViewGroup parent = (ViewGroup) flInternalBrowser.getParent();
        int index = parent.indexOfChild(flInternalBrowser);
        parent.removeView(flInternalBrowser);
        parent.addView(BackgroundWorker.getInstance().getWebview(), index);

        etAddress.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                onViewClicked();
            }
            return true;
        });

        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.btn_go)
    public void onViewClicked() {
        String urlString = etAddress.getText().toString();
        if (urlString.isEmpty()) return;
        if (!urlString.startsWith("http://") && !urlString.startsWith("https://")) {
            urlString = "http://" + urlString;
            etAddress.setText(urlString);
        }

        webView.loadUrl(urlString);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getId()) {
            case EventID.EVENT_WEBVIEW_URL_CHANGED:
                etAddress.setText(event.getMessages().get(0).getTargetUrl());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        BackgroundWorker.getInstance().setImageShown(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }
}
