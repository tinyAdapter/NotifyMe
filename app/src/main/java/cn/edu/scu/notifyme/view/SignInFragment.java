package cn.edu.scu.notifyme.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.LocaleUtils;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.SignInSignUpActivity;

public class SignInFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.et_username)
    TextInputEditText etUsername;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;

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
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.please_input_username_and_password));
                    return;
                }
                if (!RegexUtils.isUsername(username)) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.please_input_valid_username));
                    return;
                }
                //TODO: 向服务器发送登录请求
                onSignInSuccess();
                break;
            }
            case R.id.btn_sign_up:
                ((SignInSignUpActivity) getActivity()).setMainFragment(new SignUpFragment());
                break;
        }
    }

    private void onSignInSuccess() {
        ToastUtils.showShort(LocaleUtils.getString(R.string.sign_in_succeeded));
        storeUserInfo();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void storeUserInfo() {
        SPUtils.getInstance().put("username", etUsername.getText().toString());
        SPUtils.getInstance().put("avatarUrl", "https://www.2dfan.com/assets/title-94788316db9095cd858669609cff52a101b06da29ae78bbe129c3cdf68a6aee8.gif");
    }
}