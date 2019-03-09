package cn.edu.scu.notifyme.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.CreateOrEditTaskActivity;
import cn.edu.scu.notifyme.DatabaseManager;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.adapter.RulesAdapter;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Rule;

public class RuleListFragment extends Fragment {

    public static final String PARAM_CATEGORY_ID = "categoryId";
    @BindView(R.id.rv_rules)
    RecyclerView rvRules;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

        this.category = DatabaseManager.getInstance().getCategoryById(
                this.getArguments().getLong(PARAM_CATEGORY_ID)
        );
        this.rules = category.getRule();

        this.adapter = new RulesAdapter(
                R.layout.item_rule_card,
                this.rules,
                this.getContext());

        this.adapter.setOnItemChildClickListener((adap, v, position) -> {
            Rule theRule = DatabaseManager.getInstance().getRuleById(
                    this.rules.get(position).getId()
            );

            switch (v.getId()) {
                case R.id.btn_edit:
                    this.redirectToEditRulePage(theRule);
                    break;
                case R.id.btn_delete:
                    this.showDeleteRuleConfirmDialog(theRule);
                    break;
                case R.id.ss_active:
                    if (theRule.isActive()) {
                        ToastUtils.showShort("反激活" + (position + 1));
                    } else {
                        ToastUtils.showShort("激活" + (position + 1));
                    }
                    theRule.setActive(!theRule.isActive());
                    DatabaseManager.getInstance().updateRule(category, theRule);
                    break;
            }
        });
        this.adapter.openLoadAnimation();
        rvRules.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvRules.setAdapter(adapter);

        EventBus.getDefault().register(this);

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getId()) {
            case EventID.EVENT_HAS_UPDATED_DATA:
                this.category = DatabaseManager.getInstance().getCategoryById(
                        this.getArguments().getLong(PARAM_CATEGORY_ID)
                );
                this.adapter.setItems(this.category.getRule());
                this.adapter.notifyDataSetChanged();
                break;
        }
    }

    private void showDeleteRuleConfirmDialog(Rule theRule) {
        new AlertDialog.Builder(this.getContext())
                .setTitle("确认")
                .setMessage("确认要删除规则 " + theRule.getName() + " ?")
                .setNegativeButton("否", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("是", (dialog, which) -> {
                    DatabaseManager.getInstance().deleteRule(theRule.getId());
                    this.rules.remove(theRule);
                    this.adapter.notifyDataSetChanged();
                    dialog.dismiss();
                })
                .show();
    }

    private List<Rule> rules;
    private Category category;
    private RulesAdapter adapter;

    private void redirectToEditRulePage(Rule rule) {
        Intent intent = new Intent(getContext(), CreateOrEditTaskActivity.class);
        intent.putExtra(CreateOrEditTaskActivity.PARAM_CATEGORY_ID, category.getId());
        intent.putExtra(CreateOrEditTaskActivity.PARAM_RULE_TO_EDIT_ID, rule.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
