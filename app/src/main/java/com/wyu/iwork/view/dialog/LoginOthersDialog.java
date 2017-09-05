package com.wyu.iwork.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.view.activity.SigninActivity;

import org.litepal.crud.DataSupport;

/**
 * 作者： sxliu on 2017/6/29.10:17
 * 邮箱：2587294424@qq.com
 */

public class LoginOthersDialog extends AlertDialog implements View.OnClickListener {

    private Context context;

    //退出
    private TextView exit;
    //重新登录
    private TextView relogin;

    private String  userName = "";

    public LoginOthersDialog(Context context) {
        super(context);
        this.context = context;
        try {
            userName = MyApplication.userInfo.getUser_phone();
        } catch (Exception e) {
            userName = "";
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_others_login);
        exit = (TextView) findViewById(R.id.exit_app);
        relogin = (TextView) findViewById(R.id.relogin);
        exit.setOnClickListener(this);
        relogin.setOnClickListener(this);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = this.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.72);    //宽度设置为屏幕的0.72
        this.getWindow().setAttributes(p);     //设置生效
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit_app://退出

                break;

            case R.id.relogin://重新登录
                DataSupport.deleteAll(UserInfo.class);
                AppManager.getInstance(context).setUserInfo(null);
                ActivityManager.getInstance().finishAllActivity();
                Intent intent = new Intent(context, SigninActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("name",userName);
                context.startActivity(intent);
                dismiss();
                break;

        }
    }
}
