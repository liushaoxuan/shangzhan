package com.wyu.iwork.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by lx on 2017/7/24.
 */

public class TaskItem extends AutoLinearLayout {

    private Context context;
    private LinearLayout top_line;
    private LinearLayout bottomLine;
    private TextView title;
    private TextView value;
    private ImageView right;

    public TaskItem(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TaskItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public TaskItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView(){
        View view = LayoutInflater.from(context).inflate(R.layout.layout_create_task,this);
        top_line = (LinearLayout) view.findViewById(R.id.top_line);
        bottomLine = (LinearLayout) view.findViewById(R.id.bottom_line);
        title = (TextView) view.findViewById(R.id.title);
        value = (TextView) view.findViewById(R.id.value);
        right = (ImageView) view.findViewById(R.id.right);
    }

    public void setTopLineVisible(boolean flag){
        top_line.setVisibility(flag?VISIBLE:GONE);
    }

    public void setBottomLineVisible(boolean flag){
        bottomLine.setVisibility(flag?VISIBLE:GONE);
    }

    public void setTitle(String str){
        title.setText(str);
    }

    public void setValue(String str){
        value.setText(str);
    }

    public String getValue(){
        return value.getText().toString();
    }

    public void setHintValue(String str){
        value.setHint(str);
    }

    public TextView getTextView(){
        return value;
    }

    public void setRightArrowGone(){
        right.setVisibility(GONE);
    }
}
