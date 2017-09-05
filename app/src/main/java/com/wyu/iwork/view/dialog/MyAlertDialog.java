package com.wyu.iwork.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.activity.MineCardActivity;

import io.rong.imkit.RongIM;

/**
 * Created by lx on 2017/4/15.
 */

public class MyAlertDialog extends AlertDialog implements View.OnClickListener{
    private Activity context;
    private LinearLayout cahrt;
    private LinearLayout call;
    private LinearLayout card;
    private TextView cancel;

    private String userId;
    private String user_phone;
    private String User_name;

    public MyAlertDialog(Activity context, String userId, String user_phone, String User_name) {
        super(context);
        this.context = context;
        this.userId = userId;
        this.user_phone = user_phone;
        this.User_name = User_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_caht_call_card);
        cahrt = (LinearLayout) findViewById(R.id.dialog_chart_call_card_cahrt);
        call = (LinearLayout) findViewById(R.id.dialog_chart_call_card_call);
        card = (LinearLayout) findViewById(R.id.dialog_chart_call_card_card);
        cancel = (TextView) findViewById(R.id.dialog_chart_call_card_cancel);
        cahrt.setOnClickListener(this);
        call.setOnClickListener(this);
        card.setOnClickListener(this);
        cancel.setOnClickListener(this);

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = this.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.72);    //宽度设置为屏幕的0.72
        this.getWindow().setAttributes(p);     //设置生效
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.dialog_chart_call_card_cahrt://会话
                RongIM.getInstance().startPrivateChat(context,userId==null?"":userId,User_name==null?"":User_name);
                dismiss();
                break;
            case R.id.dialog_chart_call_card_call://呼叫
                String phone = user_phone==null?"":user_phone;
                  intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                dismiss();
                break;
            case R.id.dialog_chart_call_card_card://名片
                intent = new Intent(context, MineCardActivity.class);
                intent.putExtra("type","4");
                intent.putExtra("id",userId);
                context.startActivity(intent);
                dismiss();
                break;
            case R.id.dialog_chart_call_card_cancel://取消
                dismiss();
                break;
        }
    }
}
