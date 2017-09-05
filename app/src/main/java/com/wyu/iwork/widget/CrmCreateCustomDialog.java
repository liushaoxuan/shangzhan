package com.wyu.iwork.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 * Created by lx on 2017/5/5.
 */

public class CrmCreateCustomDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private DialogClickListener listener;
    private TextView oneTextView;
    private TextView twoTextView;
    private TextView threeTextView;
    private String textOne;
    private String textTwo;

    public CrmCreateCustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    public CrmCreateCustomDialog(Context context,DialogClickListener listener){
        super(context, R.style.ShareDialog);
        this.context = context;
        this.listener = listener;
    }

    public CrmCreateCustomDialog(Context context,String textOne,String textTwo,DialogClickListener listener){
        super(context,R.style.ShareDialog);
        this.textOne = textOne;
        this.textTwo = textTwo;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_crm_create_custom);
        initDialog();
        initDialogFocus(context);
    }

    private void initDialog(){
        oneTextView = (TextView) findViewById(R.id.dialog_crm_manual_create);
        twoTextView = (TextView) findViewById(R.id.dialog_crm_scan_card);
        threeTextView = (TextView) findViewById(R.id.dialog_crm_cancel);
        if(!TextUtils.isEmpty(textOne)){
            oneTextView.setText(textOne);
        }
        if(!TextUtils.isEmpty(textTwo)){
            twoTextView.setText(textTwo);
        }
        oneTextView.setOnClickListener(this);
        twoTextView.setOnClickListener(this);
        threeTextView.setOnClickListener(this);
    }

    private void initDialogFocus(Context context) {
        //setCanceledOnTouchOutside(true);
        //setCancelable(true);
        /**
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
        });*/
        WindowManager windowManager = this.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;//// 设置宽度
        lp.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_crm_manual_create:
                //手动创建
                listener.oneClick(this);
                break;
            case R.id.dialog_crm_scan_card:
                //扫描名片
                listener.twoClick(this);
                break;
            case R.id.dialog_crm_cancel:
                //取消
                listener.threeClick(this);
                break;
        }
    }

    public interface DialogClickListener{

        void oneClick(Dialog dialog);

        void twoClick(Dialog dialog);

        void threeClick(Dialog dialog);
    }

}
