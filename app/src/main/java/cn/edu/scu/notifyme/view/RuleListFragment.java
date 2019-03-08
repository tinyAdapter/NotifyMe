package cn.edu.scu.notifyme.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

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
import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Rule;

public class RuleListFragment extends Fragment {

    public static final String PARAM_TEXT = "text";
    public static final String PARAM_CATEGORY = "category";
    @BindView(R.id.rv_rules)
    RecyclerView rvRules;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

        this.category = this.getArguments().getParcelable(PARAM_CATEGORY);
        this.rules = category.getRule();

        RulesAdapter adapter = new RulesAdapter(
                R.layout.item_rule_card,
                this.rules,
                this.getContext());

        adapter.setOnItemChildClickListener((adap, v, position) -> {
            Rule theRule = this.rules.get(position);

            switch (v.getId()) {
                case R.id.btn_edit:
                    this.redirectToEditRulePage(theRule);
                    break;
                case R.id.btn_delete:
                    ToastUtils.showShort("你点击了删除按钮" + (position + 1));
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
        adapter.openLoadAnimation();
        rvRules.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvRules.setAdapter(adapter);

        return view;
    }

    private List<Rule> rules;
    private Category category;

    private void redirectToEditRulePage(Rule rule) {
        Intent intent = new Intent(getContext(), CreateOrEditTaskActivity.class);
        rule.setCategory(category);
        intent.putExtra(CreateOrEditTaskActivity.PARAM_CATEGORY, category);
        intent.putExtra(CreateOrEditTaskActivity.PARAM_RULE_TO_EDIT, rule);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
