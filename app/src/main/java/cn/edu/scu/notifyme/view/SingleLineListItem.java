package cn.edu.scu.notifyme.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;

import androidx.annotation.Nullable;
import cn.edu.scu.notifyme.R;

public class SingleLineListItem extends LinearLayout {
    public SingleLineListItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.single_line_list_item, this);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 56));
        this.setOrientation(HORIZONTAL);
        this.setClickable(true);
        this.setFocusable(true);

        TextView tvText = findViewById(R.id.tv_text);
        ImageView ivIcon = findViewById(R.id.iv_icon);


        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, typedValue, true);
        int[] attribute = new int[]{android.R.attr.selectableItemBackground};
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                typedValue.resourceId, attribute);
        this.setBackground(typedArray.getDrawable(0));
        typedArray.recycle();

        TypedArray attributes = context.obtainStyledAttributes(
                attrs, R.styleable.SingleLineListItem);

        String text = attributes.getString(R.styleable.SingleLineListItem_itemText);
        if (!StringUtils.isEmpty(text)) {
            tvText.setText(text);
        }
        int iconResourceId = attributes.getResourceId(
                R.styleable.SingleLineListItem_itemIcon, -1);
        if (iconResourceId != -1) {
            ivIcon.setImageResource(iconResourceId);
            ivIcon.setColorFilter(
                    getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        } else {
            removeView(ivIcon);
        }
        attributes.recycle();
    }
}
