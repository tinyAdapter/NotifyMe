package cn.edu.scu.notifyme.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.adapter.RulesAdapter;
import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Rule;

public class RuleListFragment extends Fragment {

    public static final String PARAM_TEXT = "text";
    public static final String PARAM_CATEGORY = "category";
    @BindView(R.id.rv_rules)
    RecyclerView rvRules;

//    @BindView(R.id.tv_text_from_args)
//    TextView tvTextFromArgs;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

//        tvTextFromArgs.setText(this.getArguments().getString(RuleListFragment.PARAM_TEXT));

        Category category = this.getArguments().getParcelable(PARAM_CATEGORY);
        this.rules = category.getRule();

        RulesAdapter adapter = new RulesAdapter(
                R.layout.item_rule_card,
                this.rules,
                this.getContext());

        adapter.setOnItemChildClickListener((adap, v, position) -> {
            switch (v.getId()) {
                case R.id.btn_edit:
                    ToastUtils.showShort("你点击了编辑按钮" + (position + 1));
                    break;
                case R.id.btn_delete:
                    ToastUtils.showShort("你点击了删除按钮" + (position + 1));
                    break;
                case R.id.ss_active:
                    if (this.rules.get(position).isActive()) {
                        ToastUtils.showShort("反激活" + (position + 1));
                    } else {
                        ToastUtils.showShort("激活" + (position + 1));
                    }
                    this.rules.get(position).setActive(!this.rules.get(position).isActive());
                    break;
            }
        });
        adapter.openLoadAnimation();
        rvRules.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvRules.setAdapter(adapter);

        return view;
    }

    private List<Rule> rules;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
