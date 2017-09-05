package com.wyu.iwork.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.NewStyleDialog;
import com.wyu.iwork.view.fragment.FindPassWordFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class FindPassStep1 extends BaseActivity {


    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    @BindView(R.id.et_password)
    EditText password;//密码

    @BindView(R.id.et_password_too)
    EditText password_too;//确认密码

    @BindView(R.id.tv_complete_sumbmit)
    TextView submit; //提交


    //手机号
    private String mphone = "";
    //验证码
    private String mcode = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass_step1);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        getPhone_code();
        title.setText("重置密码");
        edit.setVisibility(View.GONE);
    }

    //获取手机号验证码
    private void getPhone_code(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            mphone = extras.getString("phone");
            mcode = extras.getString("code");
        }
    }
    
    
    @OnClick({R.id.action_back,R.id.tv_complete_sumbmit})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            
            case R.id.tv_complete_sumbmit://提交

                String mpass_too = password_too.getText().toString();
                String mpass = password.getText().toString();

                if (TextUtils.isEmpty(mpass)){
                    Toast.makeText(this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(mpass_too)){
                    Toast.makeText(this,"请再次输入密码",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!mpass.equals(mpass_too)){
                    Snackbar.make(v,"确认密码有误，请重新输入",Snackbar.LENGTH_LONG).show();
                    password_too.setText("");
                    break;
                }
                findPass(mphone,mcode,mpass);
                break;
        }
    }

    //找回密码
    private void findPass(String phone,String verify,String password){
        String url = Constant.URL_FIND_PASS;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + phone + verify);
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("verify", verify);
        map.put("password", Md5Util.getMD5(password));
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(murl,callback());
    }


    //找回密码回调
    private DialogCallback callback(){
        return new DialogCallback(this) {


            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        new NewStyleDialog(FindPassStep1.this, "恭喜你，修改密码成功!", "开启你的商栈之旅吧", true, new NewStyleDialog.DialogInterface() {
                            @Override
                            public void FalseCancel(AlertDialog dialog) {}
                            @Override
                            public void FalseSure(AlertDialog dialog) {}
                            @Override
                            public void Ture_sure(AlertDialog dialog) {
                                Intent intent = new Intent(FindPassStep1.this,FindPassWordActivity.class);
                                setResult(FindPassWordFragment.REQUESCODE,intent);
                                finish();
                            }
                        }).show();
                    }else {
                        new NewStyleDialog(FindPassStep1.this, "很抱歉，修改密码失败!", "请返回页面重新修改密码", false, new NewStyleDialog.DialogInterface() {
                            @Override
                            public void FalseCancel(AlertDialog dialog) {}
                            @Override
                            public void FalseSure(AlertDialog dialog) {}
                            @Override
                            public void Ture_sure(AlertDialog dialog) {
                            }
                        }).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.e("response------------>", s);
            }
        };
    }

}
