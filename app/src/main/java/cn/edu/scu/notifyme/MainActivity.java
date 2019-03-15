package cn.edu.scu.notifyme;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
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

    public static final String NAVIGATE_TO_NOTIFICATION_FRAGMENT = "navigateToNotificationFragment";
    public static final String NEED_MANUALS = "needmanuals";
    private boolean ManualsinCategoryRulesFragment = false;

    @BindView(R.id.bn_base)
    BottomNavigationView bnBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        App.init(this);

        uiCheckNavigation();

        checkhelp();

        CategoryRulesFragment categoryRulesfragment = new CategoryRulesFragment();
        if(ManualsinCategoryRulesFragment){
            Bundle bundle = new Bundle();
            bundle.putBoolean(CategoryRulesFragment.CATEGORY_RULES_NEED_MANUALS,true);
            categoryRulesfragment.setArguments(bundle);
            ManualsinCategoryRulesFragment = false;
        }
        setMainFragment(categoryRulesfragment);

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

    private void uiCheckNavigation() {
        if (getIntent().getBooleanExtra(NAVIGATE_TO_NOTIFICATION_FRAGMENT, false)) {
            setMainFragment(new NotificationFragment());
            bnBase.setSelectedItemId(R.id.navigation_notification);
        } else {
            setMainFragment(new CategoryRulesFragment());
            bnBase.setSelectedItemId(R.id.navigation_category);
        }
    }

    private void checkhelp(){
        if(getIntent().getBooleanExtra(NEED_MANUALS, false)){
            ManualsinCategoryRulesFragment = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        NotificationService.clearUnreadNotificationCount();
        uiCheckNavigation();
    }

    private void setMainFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fm_main, fragment);
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }
}
