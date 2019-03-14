package cn.edu.scu.notifyme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.AccountUtils;
import cn.edu.scu.notifyme.LocaleUtils;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.SignInSignUpActivity;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;

public class SignInFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.et_username)
    TextInputEditText etUsername;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_sign_in, R.id.btn_sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_in: {
                Long username = 0L;
                try {
                    username = Long.parseLong(etUsername.getText().toString());
                } catch (NumberFormatException nfe) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.please_input_valid_username));
                    return;
                }
                String password = etPassword.getText().toString();
                if (password.isEmpty()) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.please_input_username_and_password));
                    return;
                }
                AccountUtils.login(username, EncryptUtils.encryptMD5ToString(password).toLowerCase());
                break;
            }
            case R.id.btn_sign_up:
                ((SignInSignUpActivity) getActivity()).setMainFragment(new SignUpFragment());
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getId()) {
            case EventID.EVENT_LOGIN_SUCCEED:
                onSignInSuccess();
                break;
            case EventID.EVENT_LOGIN_FAILED:
                ToastUtils.showShort(LocaleUtils.getString(R.string.sign_in_failed));
                break;
        }
    }

    private void onSignInSuccess() {
        ToastUtils.showShort(LocaleUtils.getString(R.string.sign_in_succeeded));
        storeUserAvatar();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void storeUserAvatar() {
        SPUtils.getInstance().put("avatarUrl", "https://www.2dfan.com/assets/title-94788316db9095cd858669609cff52a101b06da29ae78bbe129c3cdf68a6aee8.gif");
    }
}
