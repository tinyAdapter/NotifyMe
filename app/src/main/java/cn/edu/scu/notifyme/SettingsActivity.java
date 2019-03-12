package cn.edu.scu.notifyme;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.view.SingleLineListItem;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.dlli_language)
    LinearLayout dlliLanguage;
    @BindView(R.id.clear_cache)
    LinearLayout clearCache;
    @BindView(R.id.msg_amount)
    TextView msgAmount;
    @BindView(R.id.slli_sign_out)
    SingleLineListItem slliSignOut;
    @BindView(R.id.ll_settings)
    LinearLayout llSettings;

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

            App.restart();
        });

        uiSetMessagesCount();
        clearCache.setOnClickListener(view -> {
            if (Integer.valueOf(String.valueOf(msgAmount.getText())) < 1)
                return;
            new AlertDialog.Builder(SettingsActivity.this)
                    .setTitle(LocaleUtils.getString(R.string.confirm))
                    .setMessage(LocaleUtils.getString(
                            R.string.are_you_sure_to_clear_all_messages) + "?")
                    .setNegativeButton(LocaleUtils.getString(R.string.no), (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton(LocaleUtils.getString(R.string.yes), (dialog, which) -> {
                        DatabaseManager.getInstance().clearMessages();
                        uiSetMessagesCount();
                        dialog.dismiss();
                    })
                    .show();
        });

        if (isUserSignedIn()) {
            slliSignOut.setOnClickListener(view -> {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setTitle(LocaleUtils.getString(R.string.confirm))
                        .setMessage(LocaleUtils.getString(
                                R.string.are_you_sure_to_sign_out) + "?")
                        .setNegativeButton(LocaleUtils.getString(R.string.no), (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton(LocaleUtils.getString(R.string.yes), (dialog, which) -> {
                            SPUtils.getInstance().remove("username");
                            SPUtils.getInstance().remove("avatarUrl");
                            dialog.dismiss();
                            App.restart();
                        })
                        .show();
            });
        } else {
            llSettings.removeView(slliSignOut);
        }
    }

    private boolean isUserSignedIn() {
        return !SPUtils.getInstance().getString("username").isEmpty();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    private void uiSetMessagesCount() {
        msgAmount.setText(String.valueOf(DatabaseManager.getInstance().getMessages().size()));
    }
}
