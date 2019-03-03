package cn.edu.scu.notifyme;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.bean.SearchResultBean;

public class MainActivity extends AppCompatActivity {

//    @BindView(R.id.wv_worker)
//    WebView wvWorker;
    @BindView(R.id.tv_result)
    TextView tvResult;

    long startTime = 0;

    String toExec = "(function() {\n" +
            "  let results = [];\n" +
            "  document.querySelectorAll(\"div.b_algoheader\").forEach(result => {\n" +
            "    results.push({\n" +
            "      text: result.querySelector(\"h2\").querySelectorAll(\"a\")[1].innerHTML\n" +
            "    });\n" +
            "  });\n" +
            "  return { results };\n" +
            "})();\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        wvWorker.getSettings().setJavaScriptEnabled(true);
//        wvWorker.getSettings().setLoadsImagesAutomatically(false);
//        wvWorker.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                startTime = System.currentTimeMillis();
//
//                wvWorker.evaluateJavascript(toExec, (String result) -> {
//                    if (!result.startsWith("{")) return;
//                    runOnUiThread(() -> {
//                        tvResult.setText("");
//                        SearchResultBean bean = new Gson().fromJson(result, SearchResultBean.class);
//                        Iterator<SearchResultBean.ResultsBean> it = bean.getResults().iterator();
//                        while (it.hasNext()) {
//                            String aResult = it.next().getText();
//                            tvResult.setText(tvResult.getText() + "\n" + aResult);
//                        }
//                        tvResult.setText(tvResult.getText() + "\nTime: " +
//                                (System.currentTimeMillis() - startTime));
//                    });
//                });
//            }
//        });
//        wvWorker.loadUrl("https://cn.bing.com/search?q=ts");
    }
}
