package com.wyu.iwork.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 * Created by lx on 2017/4/15.
 */

public class NewVersionDialog extends Dialog implements View.OnClickListener{
    private Activity context;
    //取消按钮
    private TextView cancel;
    //确定按钮
    private TextView download;

    private DialogInterface listener;

    /**
     *
     * @param context 上下文
     * @param mlisteber 监听事件
     */
    public NewVersionDialog(Activity context, DialogInterface mlisteber) {
        super(context, R.style.ShareDialog);
        this.context = context;
        listener = mlisteber;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_version);
        cancel = (TextView) findViewById(R.id.dialog_new_version_cancel);
        download = (TextView) findViewById(R.id.dialog_new_version_download);
        download.setOnClickListener(this);
        cancel.setOnClickListener(this);


        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = this.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (d.getWidth() * 1.0);    //宽度设置为屏幕的
        this.getWindow().setAttributes(p);     //设置生效

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_new_version_cancel://取消
                dismiss();
                break;
            case R.id.dialog_new_version_download://确定
                listener.onDownLoad(this);
                dismiss();
                break;
        }
    }


    public interface DialogInterface{
        void onDownLoad(Dialog dialog);
    }
}
