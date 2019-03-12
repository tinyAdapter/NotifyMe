package cn.edu.scu.notifyme;

import android.content.Context;
import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.view.SignInFragment;

public class SignInSignUpActivity extends AppCompatActivity {

    public static final int REQUEST_SIGN_IN_RESULT = 30002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_sign_up);
        ButterKnife.bind(this);

        setMainFragment(new SignInFragment());
    }

    public void setMainFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_sign_in_sign_up, fragment);
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastUtils.showShort(LocaleUtils.getString(R.string.sign_in_canceled));
        setResult(RESULT_CANCELED);
        finish();
    }
}
