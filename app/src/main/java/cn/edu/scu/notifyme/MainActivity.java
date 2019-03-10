package cn.edu.scu.notifyme;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.view.CategoryRulesFragment;
import cn.edu.scu.notifyme.view.MeFragment;
import cn.edu.scu.notifyme.view.NotificationFragment;
import cn.edu.scu.notifyme.view.ShareFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bn_base)
    BottomNavigationView bnBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setMainFragment(new CategoryRulesFragment());

        bnBase.setOnNavigationItemSelectedListener(menuItem -> {
            LogUtils.d("Selected navigation item " + menuItem.getItemId());
            switch (menuItem.getItemId()) {
                case R.id.navigation_category:
                    setMainFragment(new CategoryRulesFragment());
                    break;
                case R.id.navigation_notification:
                    setMainFragment(new NotificationFragment());
                    break;
                case R.id.navigation_share:
                    setMainFragment(new ShareFragment());
                    break;
                case R.id.navigation_me:
                    setMainFragment(new MeFragment());
                    break;
            }
            return true;
        });
    }

    private void setMainFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fm_main, fragment);
        transaction.commit();
    }


}
