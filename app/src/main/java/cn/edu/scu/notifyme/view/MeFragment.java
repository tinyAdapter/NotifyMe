package cn.edu.scu.notifyme.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.LogsActivity;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.SettingsActivity;
import cn.edu.scu.notifyme.SignInSignUpActivity;
import cn.edu.scu.notifyme.WebViewActivity;

import static android.app.Activity.RESULT_OK;
import static cn.edu.scu.notifyme.SignInSignUpActivity.REQUEST_SIGN_IN_RESULT;

public class MeFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.slli_settings)
    LinearLayout slliSettings;
    @BindView(R.id.slli_internal_browser)
    SingleLineListItem slliInternalBrowser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, view);

        uiRefreshUserInfo();

        return view;
    }

    private void uiRefreshUserInfo() {
        String username = SPUtils.getInstance().getString("username");
        if (!username.isEmpty()) {
            tvUsername.setText(username);
        }
        String avatarUrl = SPUtils.getInstance().getString("avatarUrl");
        if (!avatarUrl.isEmpty()) {
            Glide.with(this.getContext())
                    .load(SPUtils.getInstance().getString("avatarUrl"))
                    .into(ivAvatar);
        } else {
            Glide.with(this.getContext())
                    .load(R.mipmap.ic_default_avatar)
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.ic_default_avatar))
                    .into(ivAvatar);
        }
    }

    private boolean isUserSignedIn() {
        return !SPUtils.getInstance().getString("username").isEmpty();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_avatar, R.id.slli_settings, R.id.slli_internal_browser, R.id.slli_logs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar: {
                if (!isUserSignedIn()) {
                    Intent intent = new Intent(getActivity(), SignInSignUpActivity.class);
                    startActivityForResult(intent, REQUEST_SIGN_IN_RESULT);
                }
                break;
            }
            case R.id.slli_settings: {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.slli_internal_browser: {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.slli_logs: {
                Intent intent = new Intent(getActivity(), LogsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SIGN_IN_RESULT:
                if (resultCode == RESULT_OK) {
                    uiRefreshUserInfo();
                }
                break;
        }
    }
}
