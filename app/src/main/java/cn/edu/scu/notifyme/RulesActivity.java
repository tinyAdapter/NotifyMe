package cn.edu.scu.notifyme;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Rule;

public class RulesActivity extends AppCompatActivity {

    String toExec = "(function() {\n" +
            "  return { results: document.getElementsByTagName('body')[0].innerHTML };\n" +
            "})();\n";


    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_execute_baidu)
    Button btnExecuteBaidu;
    @BindView(R.id.btn_execute_csj)
    Button btnExecuteCsj;
    @BindView(R.id.btn_execute_bilibili)
    Button btnExecuteBilibili;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
        BackgroundWorker.getInstance().bind(this);
        BackgroundWorker.getInstance().start();

        Message msg = new Message();
        msg.setTitle("AA");
        msg.setContent("BB");
        NotificationService.newMessage(this, msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getId()) {
//            case EventID.EVENT_HAS_FETCHED_RESULT:
//                tvTitle.setText(event.getMessage().getTitle());
//                tvResult.setText(event.getMessage().getContent());
//                break;
            case EventID.EVENT_FETCH_TIMEOUT:
                tvTitle.setText(event.getMessage().getTitle());
                tvResult.setText("请求超时，请稍后再试");
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.btn_execute_baidu, R.id.btn_execute_csj, R.id.btn_execute_bilibili})
    public void onViewClicked(View view) {
        Rule rule;
        switch (view.getId()) {
            case R.id.btn_execute_baidu:
                rule = new Rule();
                rule.setName("BAIDU");
                rule.setDuration(15);
                rule.setScript(toExec);
                rule.setToLoadUrl("https://www.baidu.com");
                BackgroundWorker.getInstance().newTask(rule);
                break;
            case R.id.btn_execute_csj:
                rule = new Rule();
                rule.setName("SINA CSJ");
                rule.setDuration(15);
                rule.setScript(toExec);
                rule.setToLoadUrl("https://tech.sina.cn/csj");
                BackgroundWorker.getInstance().newTask(rule);
                break;
            case R.id.btn_execute_bilibili:
                rule = new Rule();
                rule.setName("BILIBILI");
                rule.setDuration(15);
                rule.setScript(toExec);
                rule.setToLoadUrl("https://m.bilibili.com");
                BackgroundWorker.getInstance().newTask(rule);
                break;
        }
    }
}
