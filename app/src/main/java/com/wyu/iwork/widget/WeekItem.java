package com.wyu.iwork.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by lx on 2017/7/25.
 */

public class WeekItem extends AutoRelativeLayout {

    private Context context;
    private ImageView imageView;
    private TextView value;

    public WeekItem(Context context) {
        super(context);
        initView(context);
    }

    public WeekItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WeekItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_week,this);
        this.imageView = (ImageView) view.findViewById(R.id.image_sele);
        this.value = (TextView) view.findViewById(R.id.text_week);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setSelected(!imageView.isSelected());
            }
        });
    }

    public void setValue(String str){
        value.setText(str);
    }

    public boolean getSelected(){
        return imageView.isSelected();
    }

    public void setSelected(boolean flag){
        imageView.setSelected(flag);
    }
}
