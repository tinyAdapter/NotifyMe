package cn.edu.scu.notifyme;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Category;
import cn.edu.scu.notifyme.model.Message;
import cn.edu.scu.notifyme.model.Rule;

public class CreateOrEditTaskActivity extends AppCompatActivity {

    public static final String PARAM_CATEGORY_ID = "categoryId";
    public static final String PARAM_RULE_TO_EDIT_ID = "ruleToEditId";

    private AlertDialog testprogress;
    private AlertDialog testresult;
    private AlertDialog testtimeout;

    private Category category;
    private Rule ruleToEdit;
    private boolean isEdit;
    private Category categorySelected;

    @BindView(R.id.task_name)
    EditText inputtaskName;
    @BindView(R.id.toload_url)
    EditText inputtoloadurl;
    @BindView(R.id.duration)
    EditText inputduration;
    @BindView(R.id.code)
    EditText inputcode;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ms_category)
    MaterialSpinner msCategory;
    @BindView(R.id.create_or_save)
    Button createOrSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_task);
        ButterKnife.bind(this);

        this.category = DatabaseManager.getInstance().getCategoryById(
                getIntent().getLongExtra(PARAM_CATEGORY_ID, 0));
        this.ruleToEdit = DatabaseManager.getInstance().getRuleById(
                getIntent().getLongExtra(PARAM_RULE_TO_EDIT_ID, 0)
        );
        if (this.ruleToEdit != null && this.category != null) { // 编辑任务
            isEdit = true;
            toolbar.setTitle(LocaleUtils.getString(R.string.edit_rule));
            this.uiSetRule(ruleToEdit);
            createOrSave.setText(LocaleUtils.getString(R.string.save));
        } else { // 新建任务
            isEdit = false;
            toolbar.setTitle(LocaleUtils.getString(R.string.new_rule));
            createOrSave.setText(LocaleUtils.getString(R.string.create));
        }

        msCategory.setBackgroundColor(getResources().getColor(R.color.ms_background));

        List<Category> allCategories = DatabaseManager.getInstance().getCategories();
        List<String> categoryNames = new ArrayList<>();
        int indexOfCategoryTheRuleBelongsTo = 0;
        for (int i = 0; i < allCategories.size(); i++) {
            Category ctgry = allCategories.get(i);
            categoryNames.add(ctgry.getName());
            if (this.ruleToEdit != null) {
                if (ctgry.getName().equals(this.category.getName())) {
                    indexOfCategoryTheRuleBelongsTo = i;
                }
            }
        }
        msCategory.setItems(categoryNames);
        msCategory.setSelectedIndex(indexOfCategoryTheRuleBelongsTo);
        categorySelected = allCategories.get(indexOfCategoryTheRuleBelongsTo);
        msCategory.setOnItemSelectedListener((view, position, id, item) -> {
            categorySelected = allCategories.get(position);
        });

        EventBus.getDefault().register(this);
        BackgroundWorker.getInstance().bind(this);
        BackgroundWorker.getInstance().start();
    }

    private void uiSetRule(Rule rule) {
        inputtaskName.setText(rule.getName());
        inputtoloadurl.setText(rule.getToLoadUrl());
        inputcode.setText(rule.getScript());
        inputduration.setText(String.valueOf(rule.getDuration()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getId()) {
            case EventID.EVENT_HAS_FETCHED_RESULT:
                if (testprogress.isShowing()) {
                    testprogress.dismiss();
                    showresult(event.getMessage());
                }
                break;
            case EventID.EVENT_FETCH_TIMEOUT:
                if (testprogress.isShowing()) {
                    testprogress.dismiss();
                    timeout();
                }
                break;
        }
    }

    @OnClick({R.id.test, R.id.cancel, R.id.create_or_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test:
                test();
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.create_or_save:
                createOrSave();
                break;
        }
    }

    private void test() {
        Rule rule = buildRule();
        if (rule == null) return;

        BackgroundWorker.getInstance().insertTask(rule);

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrEditTaskActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.test_progress, null);
        TextView tv_cancel = v.findViewById(R.id.cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testprogress.dismiss();
            }
        });
        builder.setView(v);
        testprogress = builder.create();
        testprogress.setCancelable(true);
        testprogress.setCanceledOnTouchOutside(false);
        testprogress.show();
    }

    private void showresult(Message msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrEditTaskActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.test_result, null);
        TextView tv_title = v.findViewById(R.id.result_title);
        TextView tv_message = v.findViewById(R.id.result_message);
        ImageView iv_image = v.findViewById(R.id.result_image);
        TextView tv_confirm = v.findViewById(R.id.result_confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testresult.dismiss();
            }
        });
        if (msg.getTitle() != null)
            tv_title.setText(msg.getTitle());
        if (msg.getContent() != null)
            tv_message.setText(msg.getContent());
        if (msg.getImgUrl() != null)
            Glide.with(this).load(msg.getImgUrl()).into(iv_image);
        builder.setView(v);
        testresult = builder.create();
        testresult.setCancelable(true);
        testresult.setCanceledOnTouchOutside(false);
        testresult.show();
    }

    private void timeout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateOrEditTaskActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.test_timeout, null);
        TextView tv_confirm = v.findViewById(R.id.confirm);
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testtimeout.dismiss();
            }
        });
        builder.setView(v);
        testtimeout = builder.create();
        testtimeout.setCancelable(true);
        testtimeout.setCanceledOnTouchOutside(false);
        testtimeout.show();
    }

    private void createOrSave() {
        Rule rule = buildRule();
        if (rule == null) return;

        if (isEdit) {
            DatabaseManager.getInstance().updateRule(categorySelected, rule);
        } else {
            DatabaseManager.getInstance().addRule(categorySelected, rule);
        }

        setResult(RESULT_OK);
        EventBus.getDefault().post(new MessageEvent(EventID.EVENT_HAS_UPDATED_DATA, null));
        this.finish();
    }

    private Rule buildRule() {
        String name = inputtaskName.getText().toString();
        String toLoadUrl = inputtoloadurl.getText().toString();
        String script = inputcode.getText().toString();
        String durationString = inputduration.getText().toString();
        if (script.length() < 1 || toLoadUrl.length() < 1 ||
                name.length() < 1 || durationString.length() < 1) {
            ToastUtils.showShort(LocaleUtils.getString(R.string.please_complete_fields));
            return null;
        }

        int duration = Integer.parseInt(durationString);
        Rule rule;
        if (isEdit) {
            rule = ruleToEdit;
        } else {
            rule = new Rule();
            rule.setActive(true);
        }
        rule.setName(name);
        rule.setToLoadUrl(toLoadUrl);
        rule.setScript(script);
        rule.setDuration(duration);

        return rule;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }
}
