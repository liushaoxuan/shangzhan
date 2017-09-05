package com.wyu.iwork.widget;

import android.content.Context;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * Created by lx on 2017/5/2.
 */

public class CustomCrmItem extends AutoLinearLayout {

    private Context context;
    private View view_crm_top_line;
    private View view_crm_bottom_line;
    private TextView tv_crm_left_title;
    private TextView tv_key_desc;
    private TextView tv_crm_value;
    private ImageView iv_crm_right;
    private EditText et_crm_value;

    public CustomCrmItem(Context context) {
        super(context);
        initView(context);
    }

    public CustomCrmItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CustomCrmItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public CustomCrmItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_crm_item,this);
        view_crm_top_line = view.findViewById(R.id.view_crm_top_line);
        view_crm_bottom_line = view.findViewById(R.id.view_crm_bottom_line);
        tv_crm_left_title = (TextView) view.findViewById(R.id.tv_crm_left_title);
        tv_key_desc = (TextView) view.findViewById(R.id.tv_key_desc);
        tv_crm_value = (TextView) view.findViewById(R.id.tv_crm_value);
        iv_crm_right = (ImageView) view.findViewById(R.id.iv_crm_right);
        et_crm_value = (EditText) view.findViewById(R.id.et_crm_value);
    }

    //设置顶部横线是否显示
    public void setTopLineVisible(boolean flag){
        view_crm_top_line.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //设置布局底部横线是否显示
    public void setBottomLineVisible(boolean flag){
        view_crm_bottom_line.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //设置布局左边的标题内容
    public void setTitle(String text){
        tv_crm_left_title.setText(text);
    }

    //设置标题右边的必选项提示是否显示
    public void setKeyDescVisible(boolean flag){
        tv_key_desc.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //设置右边的箭头是否显示
    public void setRightVisible(boolean flag){
        iv_crm_right.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //设置TextView的内容
    public void setValue(String text){
        tv_crm_value.setText(text);
    }

    //获取TextView的值
    public String getValue(){
        return tv_crm_value.getText().toString();
    }

    //设置TextView是否显示
    public void setValueVisible(boolean flag){
        tv_crm_value.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //设置TextView最大的行数
    public void setValueMaxLine(int maxLine){
        tv_crm_value.setMaxLines(maxLine);
    }

    //返回TextView对象
    public TextView getTextView(){
        return tv_crm_value;
    }

    public void setHintValue(String text){
        tv_crm_value.setHint(text);
    }

    public void setValueSingle(){
        tv_crm_value.setSingleLine(true);
    }

    //设置TextView字体颜色
    public void setValueTextColor(int color){
        tv_crm_value.setTextColor(color);
    }

    //设置EditText是否显示
    public void setEditVisible(boolean flag){
        et_crm_value.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //设置EditText显示内容
    public void setEditText(String text){
        et_crm_value.setText(text);
    }

    public void setHintText(String text){
        et_crm_value.setHint(text);
    }

    //返回Editext对象
    public EditText getEditText(){
        return et_crm_value;
    }

    //设置EditText最大行数
    public void setEditMaxLine(int maxLine){
        et_crm_value.setMaxLines(maxLine);
    }

    //设置EditText字体颜色
    public void setEditTextColor(int color){
        et_crm_value.setTextColor(color);
    }

    //获取EditText的值
    public String getEditTextValue(){
        return et_crm_value.getText().toString();
    }

    //设置输入类型
    public void setInputType(int inputType){
        et_crm_value.setInputType(inputType);
    }

    //设置不能输入汉字
    public void setDiaitals(){
        String digits = "1234567890-abcdefghijklmnopqrstuvwxyz/\\|:><,?';\".~!@#$&%^*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        et_crm_value.setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    public void setEditSingleLine(){
        et_crm_value.setSingleLine(true);
    }

}
