package cn.edu.scu.notifyme.adapter;

import androidx.annotation.Nullable;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.model.Rule;

public class RulesAdapter extends BaseQuickAdapter<Rule, BaseViewHolder> {

    private Context context;

    public RulesAdapter(int layoutResId, @Nullable List<Rule> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Rule item) {
        helper.setImageResource(R.id.logo_pic, R.mipmap.ic_launcher)
                .setText(R.id.headline,item.getName())
                .setText(R.id.secondline, String.valueOf(item.getDuration()))
                .setChecked(R.id.ss_active, item.isActive())
                .addOnClickListener(R.id.btn_edit)
                .addOnClickListener(R.id.btn_delete)
                .addOnClickListener(R.id.ss_active);

        Glide.with(context).load(item.getIconUrl()).into((ImageView) helper.getView(R.id.logo_pic));
    }

    public void setItems(List<Rule> rules) {
        this.replaceData(new ArrayList<>(rules));
    }
}


