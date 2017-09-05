package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.presenter.UpDatePasswdPresenter;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;

import org.json.JSONObject;


public class ModifyPasswordActivity extends BaseNetActivity implements View.OnClickListener {
    private static final String TAG = ModifyPasswordActivity.class.getSimpleName();
    private EditText ed_MPEnterOldPassword,ed_MPEnterPassword,ed_MPEnterPasswordAgain;
    private Button btn_MPSubmit;
    private String oldPassword,newPassword,againPassword;
    private UpDatePasswdPresenter mPresenter;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        setBackNaviAction(); if(AppManager.getInstance(this).getUserInfo()!=null&&!"".equals(AppManager.getInstance(this).getUserInfo())){
            userID= AppManager.getInstance(this).getUserInfo().getUser_id();
        }else {
            startActivity(new Intent(this,SigninActivity.class));
        }

        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("修改密码");
        init();
    }

    private void init() {
        ed_MPEnterOldPassword=(EditText) findViewById(R.id.ed_MPEnterOldPassword);
        ed_MPEnterPassword=(EditText) findViewById(R.id.ed_MPEnterPassword);
        ed_MPEnterPasswordAgain=(EditText) findViewById(R.id.ed_MPEnterPasswordAgain);
        btn_MPSubmit=(Button) findViewById(R.id.btn_MPSubmit);

        btn_MPSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        oldPassword=ed_MPEnterOldPassword.getText().toString();
        newPassword=ed_MPEnterPassword.getText().toString();
        againPassword=ed_MPEnterPasswordAgain.getText().toString();
        if(oldPassword.isEmpty()||newPassword.isEmpty()||againPassword.isEmpty()){
            MsgUtil.shortToast(ModifyPasswordActivity.this,"请检查信息是否填写完整");
        }else if(!newPassword.equals(againPassword)){
            MsgUtil.shortToast(ModifyPasswordActivity.this,"两次输入密码不一致");
        }else{
            updatePassword();
        }
    }
    public void updatePassword(){
        oldPassword= Md5Util.getMD5(oldPassword);
        newPassword= Md5Util.getMD5(newPassword);
        setNetPresenter(mPresenter=new UpDatePasswdPresenter(this),TAG);
        mPresenter=new UpDatePasswdPresenter(this);
        mPresenter.attachView(this);
        mPresenter.onLoadData();
        mPresenter.doSignIn(userID,oldPassword,newPassword);
    }

    @Override
    public void onSuccess(Object data, JSONObject origin) {
        super.onSuccess(data, origin);
        MsgUtil.shortToast(ModifyPasswordActivity.this,"修改成功");
        finish();
    }

    @Override
    public void onFailure(int errorCode, String errorMsg) {
        super.onFailure(errorCode, errorMsg);
        MsgUtil.shortToast(ModifyPasswordActivity.this,errorMsg);
    }
}
