package cn.edu.scu.notifyme;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.scu.notifyme.view.NotificationFragment;

public class RuleMessageList extends AppCompatActivity {

    @BindView(R.id.layout_rule_msg_list)
    FrameLayout layoutRuleMsgList;

    private long ruleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_message_list);
        ButterKnife.bind(this);

        this.ruleId = getIntent().getLongExtra("ruleId", -1);

        if (this.ruleId == -1)
            ToastUtils.showShort("the ruleId is: " + this.ruleId);

        showMessageList();
    }

    private void showMessageList() {
        Fragment fragment = new NotificationFragment();

        Bundle bundle = new Bundle();
        bundle.putLong("ruleId", ruleId);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layout_rule_msg_list, fragment);
        transaction.commit();
    }
}
