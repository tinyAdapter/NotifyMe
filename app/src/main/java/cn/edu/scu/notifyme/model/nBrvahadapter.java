package cn.edu.scu.notifyme.model;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.event.News_data;

public class nBrvahadapter extends BaseQuickAdapter<News_data, BaseViewHolder>{

    public nBrvahadapter(int layoutResId, @Nullable List<News_data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, News_data item) {
        helper.setImageResource(R.id.frist_pic,item.getFrist_pic())
                .setText(R.id._title,item.get_title())
                .setText(R.id._content,item.get_content())
                .setText(R.id._time,item.get_time())
                .addOnClickListener(R.id._goto);
    }
}
