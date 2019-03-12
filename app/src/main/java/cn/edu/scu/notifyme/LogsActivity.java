package cn.edu.scu.notifyme;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.scu.notifyme.interfaces.ILogListener;
import cn.edu.scu.notifyme.model.Log;

public class LogsActivity extends AppCompatActivity implements ILogListener {

    @BindView(R.id.tv_logs)
    TextView tvLogs;
    @BindView(R.id.btn_clear)
    MaterialButton btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        ButterKnife.bind(this);
        LogSystem.getInstance().register(this);

        uiSetLogs();
    }

    private StringBuilder logsString;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogSystem.getInstance().unregister(this);
    }

    @OnClick(R.id.btn_clear)
    public void onViewClicked() {
        LogSystem.getInstance().clear();
        uiSetLogs();
    }

    private void uiSetLogs() {
        logsString = new StringBuilder();
        for (Log log : LogSystem.getInstance().getLogs()) {
            logsString.append(String.format("[%s] %s\n", log.getDate(), log.getMessage()));
        }
        tvLogs.setText(logsString);
    }

    @Override
    public void onLog(Log log) {
        logsString.append(String.format("[%s] %s\n", log.getDate(), log.getMessage()));
        tvLogs.setText(logsString);
    }
}
