package com.wyu.iwork.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 * 作者： sxliu on 2017/8/10.17:08
 * 邮箱：2587294424@qq.com
 * 相机拍照dialog
 */

public class ThreeItemDialog extends Dialog implements View.OnClickListener{

    private Activity context;
    private TextView Top; // 顶部
    private TextView bottom;//底部
    private TextView cancel;//取消

    private TwoItemClickListener listener;
    public ThreeItemDialog(Activity context, TwoItemClickListener listener) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.three_pic_dialog);
        WindowManager windowManager = this.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//// 设置宽度
        lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(lp);
        init();
    }


    /**
     * 初始化
     */
    private  void init(){
        Top = (TextView) findViewById(R.id.three_pic_top);
        bottom = (TextView) findViewById(R.id.three_pic_botoom);
        cancel = (TextView) findViewById(R.id.three_pic_cancel);
        Top.setOnClickListener(this);
        bottom.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.three_pic_top://顶部
                if (listener!=null){
                    listener.TopClick(this);
                }
                break;
            case R.id.three_pic_botoom://底部
                listener.BottomClick(this);
                break;
            case R.id.three_pic_cancel://取消
                break;
        }
        this.dismiss();
    }

    public interface TwoItemClickListener{

        void TopClick(Dialog dialog);

        void BottomClick(Dialog dialog);
    }
}
