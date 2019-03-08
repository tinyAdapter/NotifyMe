package cn.edu.scu.notifyme.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.event.News_data;
import cn.edu.scu.notifyme.model.nBrvahadapter;

public class NotificationFragment extends Fragment {
    private Unbinder unbinder;
    RecyclerView mrv;
    List<News_data> mNews_data = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        unbinder = ButterKnife.bind(this, view);
        mrv = view.findViewById(R.id.news_add );
        News_data fn = new News_data(R.mipmap.logo,"fff","ccccccccccc","sdadadadasd");
        mNews_data.add(fn);
        nBrvahadapter adapter = new nBrvahadapter(R.layout.news_pattern,mNews_data);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(),"gogogo"+(position+1),Toast.LENGTH_SHORT).show();
            }
        });
        mrv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mrv.setAdapter(adapter);
        return view;
    }

//    public View
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
