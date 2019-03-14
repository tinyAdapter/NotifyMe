package cn.edu.scu.notifyme.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.RegexUtils;
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

public class SignUpFragment extends Fragment {
    private Unbinder unbinder;

    @BindView(R.id.et_username)
    TextInputEditText etUsername;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.et_repeat_password)
    TextInputEditText etRepeatPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
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
            case R.id.btn_sign_in:
                ((SignInSignUpActivity) getActivity()).setMainFragment(new SignInFragment());
                break;
            case R.id.btn_sign_up: {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String repeatPassword = etRepeatPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.please_input_username_and_password));
                    return;
                }
                if (!RegexUtils.isUsername(username)) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.please_input_valid_username));
                    return;
                }
                if (!password.equals(repeatPassword)) {
                    ToastUtils.showShort(LocaleUtils.getString(
                            R.string.incoherence_passwords
                    ));
                    return;
                }
                //TODO: 向服务器发送注册请求
                onSignUpSuccess();
                break;
            }
        }
    }

    private void onSignUpSuccess() {
        ToastUtils.showShort(LocaleUtils.getString(R.string.sign_up_succeeded_please_sign_in));
        ((SignInSignUpActivity) getActivity()).setMainFragment(new SignInFragment());
    }
}
