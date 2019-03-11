package cn.edu.scu.notifyme;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.dlli_language)
    LinearLayout dlliLanguage;
    @BindView(R.id.clear_cache)
    LinearLayout clearCache;
    @BindView(R.id.msg_amount)
    TextView msgAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        tvLanguage.setText(
                LocaleUtils.getLocale().equals(LocaleUtils.EN)
                        ? LocaleUtils.getString(R.string.english)
                        : LocaleUtils.getString(R.string.simplified_chinese));

        dlliLanguage.setOnClickListener(v -> {
            LocaleUtils.store(LocaleUtils.getLocale().equals(LocaleUtils.EN)
                    ? LocaleUtils.ZH_CN
                    : LocaleUtils.EN);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        refreshamount();
        clearCache.setOnClickListener(view -> {
            if (Integer.valueOf(String.valueOf(msgAmount.getText())) < 1)
                return;
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle("确认")
                    .setMessage("确认要清除所有已推送消息?")
                    .setNegativeButton("否", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("是", (dialog, which) -> {
                        DatabaseManager.getInstance().clearMessages();
                        refreshamount();
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    private void refreshamount() {
        msgAmount.setText(DatabaseManager.getInstance().getMessages().size() + "");
    }
}
