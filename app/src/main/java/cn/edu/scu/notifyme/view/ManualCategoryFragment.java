package cn.edu.scu.notifyme.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.R;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class ManualCategoryFragment extends Fragment {

    private Unbinder unbinder;

    @BindView(R.id.switch_total)
    SwitchCompat switchTotal;
    @BindView(R.id.tab_category)
    TextView tabCategory;
    @BindView(R.id.tv_add_category)
    TextView tvAddCategory;
    @BindView(R.id.btn_edit)
    MaterialButton btnEdit;
    @BindView(R.id.btn_delete)
    MaterialButton btnDelete;
    @BindView(R.id.ss_active)
    SwitchCompat ssActive;
    @BindView(R.id.layout_rule_card)
    LinearLayout layoutRuleCard;
    @BindView(R.id.fab_add_rule)
    FloatingActionButton fabAddRule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manuals_category_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

        ShowcaseConfig config = new ShowcaseConfig();

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity());

        sequence.setConfig(config);

        sequence.addSequenceItem(tabCategory,
                "任务分类 长按编辑", "了解");

        sequence.addSequenceItem(tvAddCategory,
                "添加新分类", "了解");

        sequence.addSequenceItem(btnEdit,
                "编辑任务 包括修改名称、间隔，进行测试", "了解");

        sequence.addSequenceItem(ssActive,
                "任务开关", "了解");

        sequence.addSequenceItem(switchTotal,
                "总开关 启动/关闭所有任务", "了解");

        sequence.addSequenceItem(fabAddRule,
                "添加新任务", "了解");

        sequence.start();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
