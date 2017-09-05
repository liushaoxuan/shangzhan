package com.wyu.iwork.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;

/**
 * Created by lx on 2017/8/1.
 */

public class SignDialog extends Dialog implements View.OnClickListener{

    private int flag;
    private Context context;
    private TextView btn;
    private ClickDialogListener listener;
    private View view;
    private String time;
    private TextView hour_tenths;
    private TextView hour_units;
    private TextView minutes_tenths;
    private TextView minutes_units;

    public SignDialog(Context context,int flag,String time,ClickDialogListener listener) {
        super(context, R.style.MyDialog);
        this.listener = listener;
        this.flag = flag;
        this.context = context;
        this.time = time;
    }

    public SignDialog(Context context, int themeResId,int flag,String time,ClickDialogListener listener) {
        super(context, R.style.MyDialog);
        this.listener = listener;
        this.flag = flag;
        this.context = context;
        this.time = time;
    }

    protected SignDialog(Context context, boolean cancelable,String time, OnCancelListener cancelListener,int flag,ClickDialogListener listener) {
        super(context, R.style.MyDialog);
        this.listener = listener;
        this.flag = flag;
        this.context = context;
        this.time = time;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(flag == 1){
            this.setContentView(R.layout.dialog_clock_success);
        }else{
            this.setContentView(R.layout.dialog_clock_failed);
        }
        init();
    }

    private void init(){
        btn = (TextView) findViewById(R.id.tv_click);
        btn.setOnClickListener(this);
        view = findViewById(R.id.sign_line);
        view.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        hour_tenths = (TextView) findViewById(R.id.hour_tenths);
        hour_units = (TextView) findViewById(R.id.hour_units);
        minutes_tenths = (TextView) findViewById(R.id.minutes_tenths);
        minutes_units = (TextView) findViewById(R.id.minutes_units);
        setTime();
    }


    private void setTime(){
        try {
            String currentTime = time.split(" ")[1];
            if(TextUtils.isEmpty(currentTime)){
                return;
            }
            String[] times = currentTime.split(":");
            int hour = Integer.parseInt(times[0]);
            hour_tenths.setText(hour/10+"");
            hour_units.setText(hour%10+"");
            int minutes = Integer.parseInt(times[1]);
            minutes_tenths.setText(minutes/10+"");
            minutes_units.setText(minutes%10+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_click:
                if (listener != null){
                    listener.onClick(this);
                }
                break;

        }
    }

    public interface ClickDialogListener{

        void onClick(Dialog dialog);

    }
}
