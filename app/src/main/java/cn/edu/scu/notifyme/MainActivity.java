package cn.edu.scu.notifyme;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.view.*;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bn_base)
    BottomNavigationView bnBase;

    private CategoryRulesFragment categoryRulesFragment;
    private NotificationFragment notificationFragment;
    private ShareFragment shareFragment;
    private MeFragment meFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        categoryRulesFragment = new CategoryRulesFragment();
        notificationFragment = new NotificationFragment();
        shareFragment = new ShareFragment();
        meFragment = new MeFragment();

        setMainFragment(categoryRulesFragment);

        bnBase.setOnNavigationItemSelectedListener(menuItem -> {
            LogUtils.d("Selected navigation item " + menuItem.getItemId());
            switch (menuItem.getItemId()) {
                case R.id.navigation_category:
                    setMainFragment(categoryRulesFragment);
                    break;
                case R.id.navigation_notification:
                    setMainFragment(notificationFragment);
                    break;
                case R.id.navigation_share:
                    setMainFragment(shareFragment);
                    break;
                case R.id.navigation_me:
                    setMainFragment(meFragment);
                    break;
            }
            return true;
        });
    }

    private void setMainFragment(Fragment fragment)
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fm_main, fragment);
        transaction.commit();
    }


}
