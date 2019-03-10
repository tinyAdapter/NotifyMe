package cn.edu.scu.notifyme;

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
            LocaleUtils.setLocale(LocaleUtils.getLocale().equals(LocaleUtils.EN)
                    ? LocaleUtils.ZH_CN
                    : LocaleUtils.EN);

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
