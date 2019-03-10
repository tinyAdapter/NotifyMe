package cn.edu.scu.notifyme.adapter;

import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.model.Rule;

public class RulesAdapter extends BaseQuickAdapter<Rule, BaseViewHolder> {

    private Context context;
    private boolean isButtonsEnable = true;

    public RulesAdapter(int layoutResId, @Nullable List<Rule> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Rule item) {
        helper.setImageResource(R.id.logo_pic, R.mipmap.ic_launcher)
                .setText(R.id.headline, item.getName())
                .setText(R.id.secondline, String.valueOf(item.getDuration()))
                .setChecked(R.id.ss_active, item.isActive())
                .addOnClickListener(R.id.btn_edit)
                .addOnClickListener(R.id.btn_delete)
                .addOnClickListener(R.id.ss_active)
                .addOnClickListener(R.id.layout_rule_card);

        Glide.with(context).load(item.getIconUrl()).into((ImageView) helper.getView(R.id.logo_pic));

        Button btnEdit = helper.getView(R.id.btn_edit);
        Button btnDelete = helper.getView(R.id.btn_delete);
        SwitchCompat ssActive = helper.getView(R.id.ss_active);

        if (isButtonsEnable) {
            btnEdit.setEnabled(true);
            btnEdit.setTextColor(btnEdit.getResources().getColor(R.color.colorPrimary));
            btnDelete.setEnabled(true);
            btnDelete.setTextColor(btnDelete.getResources().getColor(R.color.red));
            ssActive.setEnabled(true);
        } else {
            btnEdit.setEnabled(false);
            btnEdit.setTextColor(btnEdit.getResources().getColor(R.color.gray));
            btnDelete.setEnabled(false);
            btnDelete.setTextColor(btnDelete.getResources().getColor(R.color.gray));
            ssActive.setEnabled(false);
        }
        ssActive.setChecked(item.isActive());
    }

    public void setItems(List<Rule> rules) {
        this.replaceData(new ArrayList<>(rules));
    }

    public void setButtonsEnable(boolean buttonsEnable) {
        isButtonsEnable = buttonsEnable;
    }
}


