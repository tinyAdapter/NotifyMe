package cn.edu.scu.notifyme;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Setting extends AppCompatActivity {

    private ArrayList<String> settingList;

    @BindView(R.id.tb_base)
    Toolbar tbBase;
    @BindView(R.id.rv_setting)
    RecyclerView rvSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        tbBase.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rvSetting.findViewById(R.id.rv_setting);

        settingList = new ArrayList<String>(Arrays.asList("setting1","setting2","setting3"));
        MyAdapter myAdapter = new MyAdapter(R.layout.setting_item,settingList);
        rvSetting.setLayoutManager(new LinearLayoutManager(this));
        rvSetting.setAdapter(myAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savesetting();
    }

    public void savesetting(){

    }

    private class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder>{


        public MyAdapter(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.setting_name, item);

            SlideSwitch slideSwitch =  helper.getView(R.id.slide_switch);
            slideSwitch.setOnStateChangedListener(new SlideSwitch.OnStateChangedListener(){
                @Override
                public void onStateChanged(boolean state) {
                    if(true == state)
                    {
                        Toast.makeText(mContext, item+"的开关已打开", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(mContext, item+"的开关已关闭", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }
}
