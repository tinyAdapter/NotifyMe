package cn.edu.scu.notifyme.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;
import cn.edu.scu.notifyme.R;
import cn.edu.scu.notifyme.model.Message;

public class MessageAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

    private Context context;

    public MessageAdapter(int layoutResId, @Nullable List<Message> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        helper.setImageResource(R.id.msg_image, R.drawable.msg_pic_notfound)
                .setImageResource(R.id.msg_icon, R.mipmap.ic_launcher)
                .setText(R.id.msg_title, item.getTitle())
                .setText(R.id.msg_date, String.valueOf(item.getUpdateTime()))
                .setText(R.id.msg_content, item.getContent());

        if (item.getImgUrl().length() > 0)
            Glide.with(context)
                    .load(item.getImgUrl())
                    .apply(new RequestOptions().error(R.drawable.msg_pic_notfound))
                    .into((ImageView) helper.getView(R.id.msg_image));
        if (item.getRule() != null && item.getRule().getIconUrl().length() > 0)
            Glide.with(context)
                    .load(item.getRule().getIconUrl())
                    .apply(new RequestOptions().error(R.mipmap.ic_launcher))
                    .into((ImageView) helper.getView(R.id.msg_icon));


        helper.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(String.valueOf(item.getTargetUrl())));
            context.startActivity(intent);
        });
    }
}
