package com.wyu.iwork.util;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by lx on 2017/2/28.
 * 计时器
 */

public class TimeCount extends CountDownTimer{

 private TextView btn;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private TimeCount(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval); // 参数依次为总时长,和计时的时间间隔
    }

    public TimeCount(TextView view,long millisInFuture){
        super(millisInFuture,1000);
        this.btn = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        btn.setText("重 发 "+"( "+millisUntilFinished/1000+" S )");
        btn.setTextColor(Color.WHITE);
        btn.setClickable(false);
    }

    @Override
    public void onFinish() { // 计时完毕时触发
        btn.setClickable(true);
        btn.setText("重 新 发 送");

    }


}
