package cn.edu.scu.notifyme.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ramotion.foldingcell.FoldingCell;

import java.util.List;

import androidx.annotation.Nullable;
import cn.edu.scu.notifyme.R;

public class RulesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public RulesAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.getView(R.id.folding_cell).setOnClickListener((v) -> {
            ((FoldingCell) v).toggle(false);
        });
    }
}
