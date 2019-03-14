package cn.edu.scu.notifyme;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.view.ManualCategoryFragment;
import cn.edu.scu.notifyme.view.ManualsNotificationFragment;

public class ManualsActivity extends AppCompatActivity {

    @BindView(R.id.bn_base)
    BottomNavigationView bnBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manuals);
        ButterKnife.bind(this);

        setMainfragment(new ManualCategoryFragment());

        bnBase.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.navigation_category:
                        setMainfragment(new ManualCategoryFragment());
                        break;
                    case R.id.navigation_notification:
                        setMainfragment(new ManualsNotificationFragment());
                        break;
                    case R.id.navigation_share:
                        break;
                    case R.id.navigation_me:
                        break;
                }
                return true;
            }
        });
    }

    private void setMainfragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fm_main, fragment);
        transaction.commit();
    }


}
