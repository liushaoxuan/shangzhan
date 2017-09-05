package com.wyu.iwork.widget;

import android.content.Context;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by lx on 2017/7/15.
 */

public class EditCardItem extends AutoLinearLayout {

    private Context context;
    private View bottom_line;
    private TextView title;
    private EditText value;

    public EditCardItem(Context context) {
        super(context);
        initView(context);
    }

    public EditCardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public EditCardItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public EditCardItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_edit_card,this);
        bottom_line = view.findViewById(R.id.bottom_line);
        title = (TextView) view.findViewById(R.id.title);
        value = (EditText) view.findViewById(R.id.value);
        bottom_line.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setBottomLineVisible(boolean flag){
        bottom_line.setVisibility(flag?VISIBLE:GONE);
    }

    public void setTitle(String str){
        if(!TextUtils.isEmpty(str)){
            title.setText(str);
        }
    }

    public void setValue(String str){
        if(!TextUtils.isEmpty(str)){
            value.setText(str);
        }
    }

    public String getValue(){
        return value.getText().toString();
    }

    public void setValueSingleLine(){
        value.setSingleLine(true);
    }

    public void setMaxLine(int line){
        value.setMaxLines(line);
    }

    public void setGravityRight(){
        value.setGravity(Gravity.RIGHT);
    }

    //设置输入类型
    public void setInputType(int inputType){
        value.setInputType(inputType);
    }

    //设置不能输入汉字
    public void setDiaitals(){
        String digits = "1234567890-abcdefghijklmnopqrstuvwxyz/\\|:><,?';\".~!@#$&%^*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        value.setKeyListener(DigitsKeyListener.getInstance(digits));
    }
}
