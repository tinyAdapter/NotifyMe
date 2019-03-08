package cn.edu.scu.notifyme.model;

import androidx.annotation.Nullable;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.event.Task_data;

public class Brvahadapter extends BaseQuickAdapter<Task_data, BaseViewHolder> {

    public Brvahadapter(int layoutResId, @Nullable List<Task_data> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Task_data item) {
        helper.setImageResource(R.id.logo_pic,item.getLogo())
                .setText(R.id.headline,item.getHeadline())
                .setText(R.id.secondline,item.getSecondline())
                .addOnClickListener(R.id._edit)
                .addOnClickListener(R.id._delete);

        SlideSwitch slideSwitch =  helper.getView(R.id.slide_switch);
        slideSwitch.setOnStateChangedListener(new SlideSwitch.OnStateChangedListener(){
            @Override
            public void onStateChanged(boolean state) {
                if(true == state)
                {
                    Toast.makeText(mContext, "开关已打开", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mContext, "开关已关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}


