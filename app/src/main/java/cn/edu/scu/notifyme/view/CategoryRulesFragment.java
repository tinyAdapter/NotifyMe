package cn.edu.scu.notifyme.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.CreateOrEditTaskActivity;
import cn.edu.scu.notifyme.DatabaseManager;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.model.Category;

public class CategoryRulesFragment extends Fragment {

    private static final int REQUEST_CREATE_RULE = 30001;
    private Unbinder unbinder;

    @BindView(R.id.fab_add_rule)
    FloatingActionButton fabAddRule;
    @BindView(R.id.tb_base)
    Toolbar tbBase;
    @BindView(R.id.vp_main)
    ViewPager vpMain;
    @BindView(R.id.tl_categories)
    TabLayout tlCategories;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

        tbBase.inflateMenu(R.menu.base_toolbar_menu);
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

        return view;
    }

    private class TabOnLongClickListener implements View.OnLongClickListener {

        private int index;

        public TabOnLongClickListener(int index) {
            this.index = index;
        }

        @Override
        public boolean onLongClick(View v) {
            ToastUtils.showShort("Long clicked " + index);
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

    private Category createDummyRule(String name) {
        Category category = new Category();
        category.setName(name);
        this.categories.add(category);
        return category;
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

    private class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        public MainFragmentPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
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
}
