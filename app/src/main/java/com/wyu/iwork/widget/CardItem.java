package com.wyu.iwork.widget;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * Created by lx on 2017/7/14.
 */

public class CardItem extends AutoRelativeLayout {

    private Context context;
    private TextView item_title;
    private TextView tv_value;
    private EditText et_value;

    public CardItem(Context context) {
        super(context);
        initView(context);
    }

    public CardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CardItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public CardItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_item,this);
        item_title = (TextView) view.findViewById(R.id.item_title);
        tv_value = (TextView) view.findViewById(R.id.tv_value);
        et_value = (EditText) view.findViewById(R.id.et_value);
    }

    public void setTitle(String str){
        if(!TextUtils.isEmpty(str)){
            item_title.setText(str);
        }
    }

    public void setValue(String str){
        if(!TextUtils.isEmpty(str)){
            tv_value.setText(str);
        }
    }

    public void setValueHint(String str){
        if(!TextUtils.isEmpty(str)){
            tv_value.setHint(str);
        }
    }

    public void setEdit(String str){
        if (!TextUtils.isEmpty(str)){
            et_value.setText(str);
        }
    }

    public void setEditHint(String str){
        if(!TextUtils.isEmpty(str)){
            et_value.setHint(str);
        }
    }

    public TextView getTv_value() {
        return tv_value;
    }

    public EditText getEt_value() {
        return et_value;
    }

    //设置输入类型
    public void setInputType(int inputType){
        et_value.setInputType(inputType);
    }

    //设置不能输入汉字
    public void setDiaitals(){
        String digits = "1234567890-abcdefghijklmnopqrstuvwxyz/\\|:><,?';\".~!@#$&%^*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        et_value.setKeyListener(DigitsKeyListener.getInstance(digits));
    }


}
