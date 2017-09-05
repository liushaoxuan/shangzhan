package com.wyu.iwork.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 * Created by SH-LX on 2016/10/28.
 */

public class MyDialog extends Dialog implements OnClickListener{
    private DialogClickListener listener;
    Context context;
    private Button btn_1,btn_2,btn_3;

    private TextView tv_Title,tv_Content;

    private String oneText,twoText,threeText,Title,Content,Determine;

    private int Flag;



    public MyDialog(Context context) {
        super(context);
        this.context=context;
    }
    public MyDialog(int Flag,Context context, int theme, String oneText, String twoText, String threeText, DialogClickListener listener){
        super(context,theme);
        this.context=context;
        this.oneText=oneText;
        this.twoText=twoText;
        this.threeText=threeText;
        this.listener=listener;
        this.Flag=Flag;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Flag==1){
            this.setContentView(R.layout.dialog_wxshare);
            initOne();
        }else if(Flag==2){
            this.setContentView(R.layout.dialog_setsex);
            initTwo();
        }else if(Flag==3){
            this.setContentView(R.layout.dialog_removebinding);
            initThree();
        }

    }

    private void initThree() {
        btn_1=(Button)findViewById(R.id.dialog_RemoveBinding_Determine);
        btn_2=(Button)findViewById(R.id.dialog_RemoveBinding_Cancel);
        tv_Content=(TextView)findViewById(R.id.dialog_RemoveBinding_Content) ;
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        initThreeView();
        initDialogOne(context);

    }


    private void initTwo() {
        btn_1=(Button)findViewById(R.id.dialog_setSex_Man);
        btn_2=(Button)findViewById(R.id.dialog_setSex_Woman);
        btn_3=(Button)findViewById(R.id.dialog_setSex_Cancel);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        initTwoView();
        initDialogOne(context);

    }


    private void initOne() {
        btn_1=(Button)findViewById(R.id.dialog_WXShare_WeiXin);
        btn_2=(Button)findViewById(R.id.dialog_WXShare_FriendCircle);
        btn_3=(Button)findViewById(R.id.dialog_WXShare_Cancel);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        initOneView();
        initDialogOne(context);
    }



    private void initDialogOne(Context context) {
        setCanceledOnTouchOutside(false);
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
        this.getWindow().setAttributes(lp);
    }
    private void initThreeView() {
        btn_1.setText(oneText);
        btn_2.setText(twoText);
        tv_Content.setText(threeText);
    }

    private void initTwoView() {
        btn_1.setText(oneText);
        btn_2.setText(twoText);
        btn_3.setText(threeText);
    }

    private void initOneView() {
        btn_1.setText("拍照");
        btn_2.setText("从相册中选取");
        btn_3.setText("取消");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_WXShare_WeiXin:
                listener.oneClick(this);
                break;
            case R.id.dialog_WXShare_FriendCircle:
                listener.twoClick(this);
                break;
            case R.id.dialog_WXShare_Cancel:
                listener.threeClick(this);
                break;
            case R.id.dialog_setSex_Man:
                listener.oneClick(this);
                break;
            case R.id.dialog_setSex_Woman:
                listener.twoClick(this);
                break;
            case R.id.dialog_setSex_Cancel:
                listener.threeClick(this);
                break;
            case R.id.dialog_RemoveBinding_Determine:
                listener.oneClick(this);
                break;
            case R.id.dialog_RemoveBinding_Cancel:
                listener.twoClick(this);
                break;
        }

    }

    public interface DialogClickListener {
        void oneClick(Dialog dialog);

        void twoClick(Dialog dialog);

        void threeClick(Dialog dialog);
    }
}
