package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RegexUtils;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.CustomUtils.getRandStr;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();


    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;


    //真实姓名
    @BindView(R.id.activity_register_real_name)
    EditText realName;

    //真实姓名_删除
    @BindView(R.id.activity_register_del_name)
    ImageView real_Name_del;

    //密码
    @BindView(R.id.activity_register_password)
    EditText password;

    //密码_删除
    @BindView(R.id.activity_register_password_del)
    ImageView password_del;

    //用户协议
    @BindView(R.id.activity_register_user_ruls)
    ImageView user_ruls;


    //手机号
    private String phone = "";

    //验证码
    private String verify = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        getPhone_code();
        initView();
        TextWarcher();
    }

    public void initView(){
        title.setText("注册");
        edit.setVisibility(View.GONE);

    }

    //获取手机号验证码
    private void getPhone_code(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            phone = extras.getString("phone");
            verify = extras.getString("code");
        }
    }

    @OnClick({R.id.activity_register_del_name,R.id.action_back,R.id.activity_register_password_del,R.id.activity_register_user_ruls,R.id.tv_protocol,R.id.activity_register_submit})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back://
                onBackPressed();
                break;

            case R.id.activity_register_del_name://清空用户名
                realName.setText("");
                real_Name_del.setVisibility(View.INVISIBLE);

                break;

            case R.id.activity_register_password_del://清空密码
                password.setText("");
                password_del.setVisibility(View.INVISIBLE);
                break;

            case R.id.activity_register_user_ruls://同意用户协议
                user_ruls.setSelected(!user_ruls.isSelected());
                break;

            case R.id.activity_register_submit://注册
                String trueName = realName.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (!user_ruls.isSelected()){
                    MsgUtil.shortToast(this,"请同意用户协议");
                    break;
                }

                if (trueName.isEmpty()){
                    MsgUtil.shortToast(this,"请输入姓名");
                    break;
                }
                if (pass.isEmpty()){
                    MsgUtil.shortToast(this,"请输入密码");
                    break;
                }
                if (pass.length()<6){
                    MsgUtil.shortToast(this,"密码长度不能低于6位");
                    break;
                }
                commitRegister(trueName,pass);
                break;
            case R.id.tv_protocol://用户协议
                Intent intent = new Intent(this,UserAgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void commitRegister(String real_name,String pass){

            String F = Constant.F;
            String V = Constant.V;
            String RandStr = getRandStr();
            String name = real_name;
            String Sign = Md5Util.getSign(F+V+RandStr+phone+name+verify);
            //经过MD5加密后的密码 密码非必须的传入参数
            String passwd = Md5Util.getMD5(pass);
            HashMap<String,String> data = new HashMap<>();
            data.put("phone",phone);
            data.put("verify",verify);
            data.put("real_name",name);
            if(!TextUtils.isEmpty(pass)){
                data.put("passwd",passwd);
            }
            data.put("F",F);
            data.put("V",V);
            data.put("RandStr",RandStr);
            data.put("Sign",Sign);
            String murl = RequestUtils.getRequestUrl(Constant.URL_REGISTER,data);
        OkgoRequest(murl,callback());

    }


    private DialogCallback callback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.i(TAG,s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if("0".equals(object.getString("code"))){
                        Intent intent = new Intent(RegisterActivity.this,SignUpActivityStep1.class);
                        setResult(SignUpActivityStep1.REQUESCODE,intent);
                        //注成功   则跳转到登录页面
                        finish();
                    }
                    MsgUtil.shortToastInCenter(RegisterActivity.this,object.getString("msg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }



    //输入框监听
    private void TextWarcher(){

        //真实姓名监听
        realName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    real_Name_del.setVisibility(View.INVISIBLE);
                }else {
                    real_Name_del.setVisibility(View.VISIBLE);
                }
            }
        });

        //密码输入监听
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    password_del.setVisibility(View.INVISIBLE);
                }else {
                    password_del.setVisibility(View.VISIBLE);
                }
            }
        });
    }




}
