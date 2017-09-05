package com.wyu.iwork.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 *
 */

public class MyCustomDialogDialog extends Dialog implements OnClickListener{
    private DialogClickListener listener;
    Context context;
    private TextView btn_1,btn_2;
    private ImageView imageView;

    private int Flag;

    private String onetext,twotext,threetext;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_address;


    public MyCustomDialogDialog(Context context) {
        super(context);
        this.context=context;
    }
    public MyCustomDialogDialog(int Flag,Context context, int theme,String onetext,String twotext,String threetext, DialogClickListener listener){
        super(context,theme);
        this.context=context;
        this.listener=listener;
        this.Flag=Flag;
        this.onetext = onetext;
        this.twotext = twotext;
        this.threetext = threetext;
    }

    public MyCustomDialogDialog(int Flag,Context context, int theme,String onetext,String twotext, DialogClickListener listener){
        super(context,theme);
        this.context=context;
        this.listener=listener;
        this.Flag=Flag;
        this.onetext = onetext;
        this.twotext = twotext;
    }

    public MyCustomDialogDialog(int Flag,Context context, int theme,String onetext, DialogClickListener listener){
        super(context,theme);
        this.context=context;
        this.listener=listener;
        this.Flag=Flag;
        this.onetext = onetext;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Flag == 1){
            this.setContentView(R.layout.dialog_meeting_warn);//dialog_meeting_warn
            initOne();
        }else if(Flag == 2){
            this.setContentView(R.layout.dialog_check_in);
            initTwo();
        }else  if(Flag == 3){
            this.setContentView(R.layout.dialog_meeting_warn);//dialog_meeting_warn
            initthree();
        }else if(Flag == 4){
            this.setContentView(R.layout.dialog_crm_call);//CRM 打电话提示框
            initFour();
        }else if(Flag == 5){
            this.setContentView(R.layout.dialog_crm_message_incomplete);//CRM 提交消息不完整提示
            initFive();
        }else if(Flag == 6){
            this.setContentView(R.layout.dialog_crm_call);//CRM 删除客户
            initSix();
        }else if(Flag == 7){
            this.setContentView(R.layout.dialog_business_atten);//企业认证
            initSeven();
        }else if(Flag == 8){
            this.setContentView(R.layout.dialog_crm_message_incomplete);//CRM 提交消息不完整提示
            initEight();
        }else if(Flag == 9){
            this.setContentView(R.layout.dialog_crm_call);//crm 赢单/无效
            initNine();
        }else if(Flag == 10){
            this.setContentView(R.layout.dialog_start_metting);
            initTen();
        }
    }

    private void initOne() {
        btn_1=(TextView)findViewById(R.id.tv_sure);
        btn_2=(TextView)findViewById(R.id.tv_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_time);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        //initDialogOne(context);
        tv_title.setText(onetext);
        tv_content.setText(twotext);
    }

    private void initTwo(){
        btn_1=(TextView)findViewById(R.id.check_in_cancel);
        btn_2=(TextView)findViewById(R.id.check_in_sure);
        tv_title = (TextView) findViewById(R.id.check_in_title);
        tv_content = (TextView) findViewById(R.id.check_in_time);
        tv_address = (TextView) findViewById(R.id.check_in_address);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        initDialogOne(context);
        tv_title.setText(onetext);
        tv_content.setText(twotext);
        tv_address.setText(threetext);
    }

    private void initthree() {
        btn_1=(TextView)findViewById(R.id.tv_sure);
        btn_2=(TextView)findViewById(R.id.tv_cancel);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_time);
        imageView = (ImageView) findViewById(R.id.imageview_my);
        imageView.setVisibility(View.GONE);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        initDialogOne(context);
        tv_title.setText(onetext);
        tv_content.setText(twotext);
    }

    private void initFour(){
        btn_1 = (TextView) findViewById(R.id.dialog_crm_call_sure);
        btn_2 = (TextView) findViewById(R.id.dialog_crm_call_cancel);
        tv_title = (TextView) findViewById(R.id.dialog_crm_call_text);
        tv_title.setText("号码："+onetext+"\n是否拨打？");
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    private void initFive(){
        btn_1 = (TextView) findViewById(R.id.dialog_crm_message_sure);
        tv_title = (TextView) findViewById(R.id.textview);
        if(!TextUtils.isEmpty(onetext)){
            tv_title.setText(onetext);
        }
        btn_1.setOnClickListener(this);
    }

    private void initSix(){
        btn_1 = (TextView) findViewById(R.id.dialog_crm_call_sure);
        btn_2 = (TextView) findViewById(R.id.dialog_crm_call_cancel);
        tv_title = (TextView) findViewById(R.id.dialog_crm_call_text);
        if(!TextUtils.isEmpty(onetext)){
            tv_title.setText(onetext);
        }else{
            tv_title.setText("确定要删除这个客户吗？");
        }
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    private void initSeven(){
        btn_1 = (TextView) findViewById(R.id.dialog_crm_call_sure);
        btn_2 = (TextView) findViewById(R.id.dialog_crm_call_cancel);
        tv_title = (TextView) findViewById(R.id.dialog_crm_call_text);
        tv_title.setText(onetext);
        btn_1.setText(twotext);
        btn_2.setText(threetext);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    private void initEight(){
        tv_title = (TextView) findViewById(R.id.textview);
        tv_title.setText(onetext);
        btn_1 = (TextView) findViewById(R.id.dialog_crm_message_sure);
        btn_1.setOnClickListener(this);
    }

    private void initNine(){
        btn_1 = (TextView) findViewById(R.id.dialog_crm_call_sure);
        btn_2 = (TextView) findViewById(R.id.dialog_crm_call_cancel);
        tv_title = (TextView) findViewById(R.id.dialog_crm_call_text);
        tv_title.setText(onetext);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    private void initTen(){
        btn_1 = (TextView) findViewById(R.id.cancel);
        btn_2 = (TextView) findViewById(R.id.send);
        tv_title = (TextView) findViewById(R.id.meeting_dialog_title);
        tv_title.setText(onetext);
        tv_content = (TextView) findViewById(R.id.content);
        tv_content.setText(twotext);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
    }

    private void initDialogOne(Context context) {
        /**
        setCanceledOnTouchOutside(true);
        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getRepeatCount() == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        WindowManager windowManager = this.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//// 设置宽度
        lp.height=ViewGroup.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(lp);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                listener.oneClick(this);
                break;
            case R.id.tv_cancel:
                listener.twoClick(this);
                break;
            case R.id.check_in_cancel:
                listener.oneClick(this);
                break;
            case R.id.check_in_sure:
                listener.twoClick(this);
                break;
            case R.id.dialog_crm_call_cancel:
                listener.oneClick(this);
                break;
            case R.id.dialog_crm_call_sure:
                listener.twoClick(this);
                break;
            case R.id.dialog_crm_message_sure:
                listener.oneClick(this);
                break;
            case R.id.cancel:
                listener.oneClick(this);
                break;
            case R.id.send:
                listener.twoClick(this);
                break;
        }

    }

    public interface DialogClickListener {

        void oneClick(Dialog dialog);

        void twoClick(Dialog dialog);

    }
}
