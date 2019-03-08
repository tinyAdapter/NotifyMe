package cn.edu.scu.notifyme.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.CreateTask;
import cn.edu.scu.notifyme.MainActivity;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.event.Task_data;
import cn.edu.scu.notifyme.model.Brvahadapter;

public class CategoryRulesFragment extends Fragment {
    RecyclerView rv;
    List<Task_data> mTask_data = new ArrayList<>();
    private Unbinder unbinder;

    @BindView(R.id.fab_add_rule)
    FloatingActionButton fabAddRule;
    @BindView(R.id.tb_base)
    Toolbar tbBase;
    @BindView(R.id.vp_main)
    ViewPager vpMain;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_rules, container, false);
        unbinder = ButterKnife.bind(this, view);

        tbBase.inflateMenu(R.menu.base_toolbar_menu);
        tbBase.getOverflowIcon().setColorFilter(
                getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        List<View> views = new ArrayList<>();
        //TODO: 修改为真实分类
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        views.add(View.inflate(this.getContext(), R.layout.fragment_dummy, null));
        vpMain.setAdapter(new MainViewPagerAdapter(views));

        rv = views.get(0).findViewById(R.id.task_add);//选择分段

        Task_data test = new Task_data(R.mipmap.logo,"ffff","ffffffff");//获取数据
        mTask_data.add(test);

        Brvahadapter adapter = new Brvahadapter(R.layout.task_pattern,mTask_data);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id._edit:
                        Toast.makeText(getActivity(),"你点击了编辑按钮"+(position+1),Toast.LENGTH_SHORT).show();
                        break;
                    case R.id._delete:
                        Toast.makeText(getActivity(),"你点击了删除按钮"+(position+1),Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        adapter.openLoadAnimation();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab_add_rule)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), CreateTask.class);
        startActivity(intent);
    }

    private class MainViewPagerAdapter extends PagerAdapter {

        private List<View> views;
        private String[] titles = {"未分类", "STEAM", "直播", "BILIBILI", "5", "6", "7", "8"};

        public MainViewPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return this.titles[position];
        }
    }
}
