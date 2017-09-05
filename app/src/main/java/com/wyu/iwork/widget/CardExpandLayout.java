package com.wyu.iwork.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import static com.wyu.iwork.R.id.et_value;

/**
 * Created by lx on 2017/7/18.
 */

public class CardExpandLayout extends AutoLinearLayout implements View.OnClickListener{

    private Context context;
    private AutoRelativeLayout rl_head;
    private AutoLinearLayout rl_body;
    private TextView tv_title;
    private ImageView iv_dismiss;
    private TextView body_title;
    private EditText body_value;
    private LinearLayout line_top;
    private LinearLayout line_bottom;
    private int height;
    private int newHeight;
    ValueAnimator animator = null;
    private Animation animationDown;
    private Animation animationUp;

    public CardExpandLayout(Context context) {
        super(context);
        initView(context);
    }

    public CardExpandLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CardExpandLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout_expand,this);
        this.rl_head = (AutoRelativeLayout) view.findViewById(R.id.rl_head);
        this.rl_body = (AutoLinearLayout) view.findViewById(R.id.rl_body);
        this.tv_title = (TextView) view.findViewById(R.id.tv_title);
        this.iv_dismiss = (ImageView) findViewById(R.id.iv_dismiss);
        this.body_title = (TextView) view.findViewById(R.id.body_title);
        this.body_value = (EditText) view.findViewById(R.id.body_value);
        this.line_bottom = (LinearLayout) view.findViewById(R.id.line_bottom);
        this.line_top = (LinearLayout) view.findViewById(R.id.line_top);
        this.rl_body.setVisibility(View.GONE);
        initClick();
    }

    private void initClick(){
        rl_head.setOnClickListener(this);
        iv_dismiss.setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = this.rl_body.getLayoutParams().height;
    }

    public void setTopLineGone(){
        this.line_top.setVisibility(GONE);
    }

    public void setBottomLineGone(){
        this.line_bottom.setVisibility(GONE);
    }

    public void setTitle(String str){
        if(!TextUtils.isEmpty(str)){
            tv_title.setText(str);
        }
    }

    public void setBodyTitle(String str){
        if(!TextUtils.isEmpty(str)){
            body_title.setText(str);
        }
    }

    public void setImageViewShow(boolean flag){
        iv_dismiss.setVisibility(flag?VISIBLE:GONE);
    }

    public String getValueStr(){
        return body_value.getText().toString();
    }

    public EditText getEditTextValue(){
        return body_value;
    }

    //设置输入类型
    public void setInputType(int inputType){
        body_value.setInputType(inputType);
    }

    //设置不能输入汉字
    public void setDiaitals(){
        String digits = "1234567890-abcdefghijklmnopqrstuvwxyz/\\|:><,?';\".~!@#$&%^*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        body_value.setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    /**
    public void dropAnimation(boolean flag){
        clearAnimation();
        if(flag){
            animator = ValueAnimator.ofInt(0,height);
        }else{
            animator = ValueAnimator.ofInt(height,0);
        }
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                newHeight = (int) animation.getAnimatedValue();
                rl_body.getLayoutParams().height = newHeight;
                rl_body.this.requestLayout();

            }
        });
        if (rl_body.getVisibility() == View.GONE) {
            rl_body.setVisibility(View.VISIBLE);
        }
        animator.setDuration(1000);
        animator.start();
    }
*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_head:
                rl_body.setVisibility(View.VISIBLE);
                setImageViewShow(true);
                break;
            case R.id.iv_dismiss:
                rl_body.setVisibility(View.GONE);
                setImageViewShow(false);
                break;
        }
    }

}
