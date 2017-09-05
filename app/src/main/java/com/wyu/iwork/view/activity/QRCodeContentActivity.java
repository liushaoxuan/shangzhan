package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.AutoLinearLayout;

public class QRCodeContentActivity extends BaseActivity implements View.OnClickListener{

    TextView activity_qrcode_content_textview;
    AutoLinearLayout ll_back;
    TextView tv_title;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_content);
        initView();
    }

    private void initView(){
        activity_qrcode_content_textview = (TextView) findViewById(R.id.activity_qrcode_content_textview);
        ll_back = (AutoLinearLayout) findViewById(R.id.ll_back);
        tv_title.setText("二维码信息");
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
