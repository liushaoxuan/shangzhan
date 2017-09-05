package com.wyu.iwork.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.wyu.iwork.R;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        startMainActivity();
    }

    public void startMainActivity(){
        if(handler == null){
            handler = new Handler(){
                public void handleMessage(android.os.Message msg) {
                    if(msg.what == 1){
                        SharedPreferences sp = getSharedPreferences("SYSTEM_VERSION", Context.MODE_PRIVATE);
                        String version_code = sp.getString("VERSION","0");
                        Intent it = null;
                        Logger.i(TAG, CustomUtils.getVersionName(SplashActivity.this)+"version_code=="+version_code);
                        if("0".equals(version_code)){
                            it = new Intent(SplashActivity.this,ShowActivity.class);
                        }else{
                            it = new Intent(SplashActivity.this,SigninActivity.class);
                        }
                        startActivity(it);
                        finish();
                    }

                }
            };
        }
        handler.sendEmptyMessageDelayed(1, 2000);
    }


}
