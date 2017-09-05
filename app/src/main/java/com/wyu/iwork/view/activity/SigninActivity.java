package com.wyu.iwork.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.JpushIdModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.NetUtil;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class SigninActivity extends BaseActivity {
    private static final String TAG = SigninActivity.class.getSimpleName();

    /**
     * 账号
     */
    @BindView(R.id.sign_in_userName)
    EditText mAccount;
    /**
     * 账号
     */
    @BindView(R.id.sign_in_userName_del)
    ImageView mAccount_del;

    /**
     * 密码
     */
    @BindView(R.id.sign_in_password)
    EditText mPwd;

    /**
     * 密码_删除
     */
    @BindView(R.id.sign_in_password_del)
    ImageView mPwd_del;

    /**
     * 登录
     */
    @BindView(R.id.sign_in_login)
    Button btn_login;

    private UserInfo userInfo;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        getUserName();
        if (NetUtil.checkLoginStatus(this)) {
            //退出登录界面并跳转至主功能界面
            jumpToMain();
        }
        mAccount_del.setVisibility(View.GONE);
        mPwd_del.setVisibility(View.GONE);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        // 自定义颜色
        tintManager.setTintColor(getResources().getColor(R.color.white));
        inputListener();
        initClickListener();
    }

    private void initClickListener(){
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mAccount.getText().toString();
                String pass = mPwd.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(SigninActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.isEmpty()){
                    Toast.makeText(SigninActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }

                btn_login.setClickable(false);
                UserLogin(name,pass);
            }
        });
    }

    private void getUserName(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String user_name = bundle.getString("name");
            mAccount.setText(user_name);
        }
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @OnClick({R.id.sign_in_login,R.id.sign_in_sign_up,R.id.sign_in_loss_pass,R.id.sign_in_userName_del,R.id.sign_in_password_del})
      void onClick(View v) {
        switch (v.getId()) {
            /**
            case R.id.sign_in_login://登录
                String name = mAccount.getText().toString();
                String pass = mPwd.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(SigninActivity.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (pass.isEmpty()){
                    Toast.makeText(SigninActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    break;
                }

                btn_login.setClickable(false);
                UserLogin(name,pass);
                return;*/
            case R.id.sign_in_sign_up: //快速注册
                startActivity(new Intent(SigninActivity.this, SignUpActivityStep1.class));
                return;
            case R.id.sign_in_loss_pass://忘记密码
                Intent intent = new Intent(SigninActivity.this, FindPassWordActivity.class);
                startActivity(intent);
                break;

            case R.id.sign_in_userName_del://删除输入的用户名
                mAccount.setText("");
                break;

            case R.id.sign_in_password_del://删除输入的密码
                mPwd.setText("");
                break;
        }
    }


    //登录

    private void UserLogin(final String username, String password){
        try {
            String url = Constant.URL_SIGN_IN;
            String user_name = username;
            String user_pass = Md5Util.getMD5(password);
            String F = Constant.F;
            String V = Constant.V;
            JpushIdModel jpushIdModel = DataSupport.findFirst(JpushIdModel.class);

            String jpush_id = jpushIdModel == null ? "" : jpushIdModel.getJpushId();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(F + V + RandStr + user_name + user_pass);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_name", user_name);
            map.put("user_pass", user_pass);
            map.put("jpush_id", jpush_id);
            map.put("F", F);
            map.put("V", V);
            map.put("RandStr", RandStr);
            map.put("Sign", sign);

            //将参数拼接到url后面
            String murl = RequestUtils.getRequestUrl(url, map);
            Logger.e("Login_url", murl);
            OkgoRequest(murl, callback());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private DialogCallback callback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                btn_login.setClickable(true);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    btn_login.setClickable(true);
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        userInfo = JSON.parseObject(data.toString(),UserInfo.class);
                        MyApplication.userInfo = userInfo;
                        saveUserInfo(userInfo);
                        MyApplication.getToken();
                        jumpToMain();
                    } else {
                        Toast.makeText(SigninActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Logger.e("login_------------>", s);
            }
        };
    }


    private void jumpToMain() {
        startActivity(new Intent(this, MainActivity.class));
         finish();
    }
    public void saveUserInfo(UserInfo userInfo) {
        //保存用户信息对象,供当前在应用内其他功能使用
        userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
        userInfo.getShare().saveOrUpdate("title=?",userInfo.getShare().getTitle());
    }

    //输入框监听
    private void inputListener(){

        //用户名监听
        mAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    mAccount_del.setVisibility(View.VISIBLE);
                }else {
                    mAccount_del.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //密码监听
        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    mPwd_del.setVisibility(View.VISIBLE);
                }else {
                    mPwd_del.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
