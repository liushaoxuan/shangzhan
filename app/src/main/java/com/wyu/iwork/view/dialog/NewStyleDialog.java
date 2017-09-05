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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.activity.MineCardActivity;

import io.rong.imkit.RongIM;

/**
 * Created by lx on 2017/4/15.
 */

public class NewStyleDialog extends AlertDialog implements View.OnClickListener{
    private Activity context;
    //图标  成功  失败两种不同图标
    private ImageView icon;
    //标题
    private TextView title;
    //内容
    private TextView content;
    //成功的按钮的父布局
    private LinearLayout layout_true;
    //成功的按钮
    private TextView btn_true;
    //失败的按钮的父布局
    private LinearLayout layout_false;
    //取消按钮
    private TextView btn_false_cancel;
    //确定按钮
    private TextView btn_false_sure;

    private String Title = "";
    private String Content = "";

    private boolean issuccess ;

    private DialogInterface listener;
    private int icon_src = -1;

    /**
     *
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param issuccess  true  显示 我知道了  false  显示 确定  取消
     * @param mlisteber 监听事件
     */
    public NewStyleDialog(Activity context, String title,String content ,boolean issuccess,DialogInterface mlisteber) {
        super(context);
        this.context = context;
        Title = title;
        Content = content;
        this.issuccess = issuccess;
        listener = mlisteber;
    }

    /**
     *
     * @param context 上下文
     * @param icon_src 图片地址
     * @param title 标题
     * @param content 内容
     * @param issuccess  true  显示 我知道了  false  显示 确定  取消
     * @param mlisteber 监听事件
     */
    public NewStyleDialog(Activity context,int icon_src, String title,String content ,boolean issuccess,DialogInterface mlisteber) {
        super(context);
        this.context = context;
        Title = title;
        Content = content;
        this.issuccess = issuccess;
        this.icon_src = icon_src;
        listener = mlisteber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_style_true);
        icon = (ImageView) findViewById(R.id.dialog_icon);
        layout_true = (LinearLayout) findViewById(R.id.dialog_true_bottom);
        layout_false = (LinearLayout) findViewById(R.id.dialog_flase_bottom);
        title = (TextView) findViewById(R.id.dialog_title);
        content = (TextView) findViewById(R.id.dialog_content);
        btn_true = (TextView) findViewById(R.id.dialog_true_I_know);
        btn_false_cancel = (TextView) findViewById(R.id.dialog_false_cancel);
        btn_false_sure = (TextView) findViewById(R.id.dialog_false_sure);
        btn_true.setOnClickListener(this);
        btn_false_cancel.setOnClickListener(this);
        btn_false_sure.setOnClickListener(this);

        if (issuccess){
            layout_true.setVisibility(View.VISIBLE);
            layout_false.setVisibility(View.GONE);
            icon.setImageResource(R.drawable.dialog_true);
        }else {
            layout_true.setVisibility(View.GONE);
            layout_false.setVisibility(View.VISIBLE);
            icon.setImageResource(R.drawable.dialog_false);
        }

        if (icon_src!=-1){
            icon.setImageResource(icon_src);
        }

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = this.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 0.82);    //宽度设置为屏幕的0.72
        this.getWindow().setAttributes(p);     //设置生效

        title.setText(Title);
        content.setText(Content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_true_I_know://我知道了
               listener.Ture_sure(this);
                dismiss();
                break;
            case R.id.dialog_false_cancel://取消
                listener.FalseCancel(this);
                dismiss();
                break;
            case R.id.dialog_false_sure://确定
                listener.FalseSure(this);
                dismiss();
                break;
        }
    }


    public interface DialogInterface{
        void FalseCancel(AlertDialog dialog);
        void FalseSure(AlertDialog dialog);
        void Ture_sure(AlertDialog dialog);
    }
}
