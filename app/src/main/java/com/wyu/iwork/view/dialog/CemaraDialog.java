package com.wyu.iwork.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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

public class CemaraDialog extends AlertDialog implements View.OnClickListener{

    private Context context;
    private TextView takePhoto;
    private TextView Galary;//相册
    private TextView cancel;//取消

    private DialogClickListener listener;
    public CemaraDialog(Context context) {
        super(context);
    }
    public CemaraDialog(Context context,DialogClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.get_pic_pop);
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
        takePhoto = (TextView) findViewById(R.id.get_pic_pic);
        Galary = (TextView) findViewById(R.id.get_pic_loc);
        cancel = (TextView) findViewById(R.id.get_pic_cancel);

        takePhoto.setOnClickListener(this);
        Galary.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_pic_pic://拍照
                if (listener!=null){
                    listener.cemaraClick(this);
                }
                break;
            case R.id.get_pic_loc://相册
                listener.galaryClick(this);
                break;
            case R.id.get_pic_cancel://取消

                break;
        }
        this.dismiss();
    }

    public interface DialogClickListener{

        void cemaraClick(Dialog dialog);

        void galaryClick(Dialog dialog);
    }
}
