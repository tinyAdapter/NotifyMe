package cn.edu.scu.notifyme.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.CreateOrEditTaskActivity;
import cn.edu.scu.notifyme.DatabaseManager;
import cn.edu.scu.notifyme.MainActivity;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.event.EventID;
import cn.edu.scu.notifyme.event.MessageEvent;
import cn.edu.scu.notifyme.model.Category;

public class CategoryRulesFragment extends Fragment {

    private static final int REQUEST_CREATE_RULE = 30001;
    private Unbinder unbinder;
    private AlertDialog renameDialog;

    @BindView(R.id.fab_add_rule)
    FloatingActionButton fabAddRule;
    @BindView(R.id.tb_base)
    Toolbar tbBase;
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    @BindView(R.id.tl_categories)
    TabLayout tlCategories;
    @BindView(R.id.tv_add_category)
    TextView mTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

        //tbBase.inflateMenu(R.menu.base_toolbar_menu);
        tbBase.getOverflowIcon().setColorFilter(
                getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        this.categories = DatabaseManager.getInstance().getCategories();

        uiReconstructFragments();

        adapter = new MainFragmentPagerAdapter(getChildFragmentManager());
        vpMain.setAdapter(adapter);
        tlCategories.setupWithViewPager(vpMain);

        LinearLayout categoryTabs = (LinearLayout) tlCategories.getChildAt(0);
        for (int i = 1; i < categoryTabs.getChildCount(); i++) {
            categoryTabs.getChildAt(i).setOnLongClickListener(new TabOnLongClickListener(i));
        }

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ToastUtils.showShort("add category");
                showAddDialog();
            }
        });

        return view;
    }

    private class TabOnLongClickListener implements View.OnLongClickListener {

        private int index;

        public TabOnLongClickListener(int index) {
            this.index = index;
        }

        @Override
        public boolean onLongClick(View v) {
            showPopupMenu(v, index);
            return true;
        }
    }

    private void uiReconstructFragments() {
        this.fragments = new ArrayList<>();
        for (int i = 0; i < this.categories.size(); i++) {
            this.createDummyFragment(this.categories.get(i));
        }
    }

    private List<Fragment> fragments;
    private List<Category> categories;
    private MainFragmentPagerAdapter adapter;

    private void createDummyFragment(Category category) {
        RuleListFragment ruleListFragment = new RuleListFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(RuleListFragment.PARAM_CATEGORY_ID, category.getId());
        ruleListFragment.setArguments(bundle);
        this.fragments.add(ruleListFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_add_rule)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), CreateOrEditTaskActivity.class);
        startActivity(intent);
    }

    private class MainFragmentPagerAdapter extends FragmentStatePagerAdapter {

        public MainFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return categories.get(position).getName();
        }
    }

    private void showPopupMenu(View view, int index) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.popup_edit_category) {
                    //ToastUtils.showShort("Rename " + index);
                    showRenameDialog(index);
                } else if (item.getItemId() == R.id.popup_delete_category) {
                    //ToastUtils.showShort("Delete " + index);
                    showDeleteDialog(index);
                }
                return true;
            }
        });
    }

    private void showRenameDialog(int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_edit_category, null);
        EditText editText = v.findViewById(R.id.tv_category_new_name);
        Button button_save = v.findViewById(R.id.save_rename_category);
        Button button_cancel = v.findViewById(R.id.cancel_rename_category);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (name.length() > 0) {
                    if(DatabaseManager.getInstance().getCategoryByName(name) == null){
                        Category category = DatabaseManager.getInstance().getCategoryById(
                                categories.get(index).getId());
                        category.setName(name);
                        DatabaseManager.getInstance().updateCategory(category);
                        EventBus.getDefault().post(new MessageEvent(
                                EventID.CATEGORY_HAS_UPDATED, null));
                        renameDialog.dismiss();
                        return;
                    } else {
                        ToastUtils.showShort("名称重复");
                        return;
                    }
                } else {
                    ToastUtils.showShort("不能为空");
                    return;
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                renameDialog.dismiss();
            }
        });

        builder.setView(v);
        renameDialog = builder.create();
        renameDialog.setCancelable(true);
        renameDialog.setCanceledOnTouchOutside(false);
        renameDialog.show();
    }

    private void showDeleteDialog(int index) {
        new AlertDialog.Builder(this.getContext())
                .setTitle("确认")
                .setMessage("确认要删除分组 " + categories.get(index).getName() + " ?")
                .setNegativeButton("否", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setPositiveButton("是", (dialog, which) -> {
                    DatabaseManager.getInstance().deleteCategory(categories.get(index).getName());
                    fragments.remove(index);
                    EventBus.getDefault().post(new MessageEvent(
                            EventID.CATEGORY_HAS_DELETE, null));
                    dialog.dismiss();
                })
                .show();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_add_category, null);
        EditText editText = v.findViewById(R.id.tv_new_category_name);
        Button button_save = v.findViewById(R.id.save_add_category);
        Button button_cancel = v.findViewById(R.id.cancel_add_category);

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (name.length() > 0) {
                    if (DatabaseManager.getInstance().getCategoryByName(name) == null) {

                        Category category = new Category();
                        category.setName(name);
                        DatabaseManager.getInstance().addCategory(category);
                        EventBus.getDefault().post(new MessageEvent(
                                EventID.CATEGORY_HAS_ADDED, null));
                        renameDialog.dismiss();
                    } else {
                        ToastUtils.showShort("名称重复");
                        return;
                    }
                } else {
                    ToastUtils.showShort("不能为空");
                    return;
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameDialog.dismiss();
            }
        });

        builder.setView(v);
        renameDialog = builder.create();
        renameDialog.setCancelable(true);
        renameDialog.setCanceledOnTouchOutside(false);
        renameDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        LinearLayout categoryTabs;
        switch (event.getId()) {
            case EventID.CATEGORY_HAS_UPDATED:
                DatabaseManager.getInstance().updateList();
                categories = new ArrayList<>(DatabaseManager.getInstance().getCategories());
                adapter.notifyDataSetChanged();
                break;
            case EventID.CATEGORY_HAS_DELETE:
                DatabaseManager.getInstance().updateList();
                categories = DatabaseManager.getInstance().getCategories();
                adapter.notifyDataSetChanged();

                break;
            case EventID.CATEGORY_HAS_ADDED:
                DatabaseManager.getInstance().updateList();
                categories = new ArrayList<>(DatabaseManager.getInstance().getCategories());
                createDummyFragment(categories.get(categories.size() - 1));
                adapter.notifyDataSetChanged();

                categoryTabs = (LinearLayout) tlCategories.getChildAt(0);
                for (int i = 1; i < categoryTabs.getChildCount(); i++) {
                    categoryTabs.getChildAt(i).setOnLongClickListener(new TabOnLongClickListener(i));
                }
                break;
        }
    }

}
