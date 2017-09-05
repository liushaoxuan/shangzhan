package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.ILoginView;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.NetworkManager;
import com.wyu.iwork.presenter.ILoginPresnter;
import com.wyu.iwork.presenter.LoginPresenterCompl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity implements ILoginView {

    /**
     * 用户名
     */
    @BindView(R.id.activity_login_account_edittext)
    EditText username;

    /**
     * 密码
     */
    @BindView(R.id.activity_login_password_edittext)
    EditText password;

    /**
     * 登录
     */
    @BindView(R.id.activity_login_btn_login)
    Button btn_login;

    /**
     * 注册
     */
    @BindView(R.id.activity_login_btn_register)
    Button btn_register;

    /**
     * 忘记密码
     */
    @BindView(R.id.activity_login_lost_password)
    TextView lost_password;

    /**
     * 服务器设置
     */
    @BindView(R.id.activity_login_service_set)
    TextView set_service;

    protected RequestQueue mRequestQueue;
    ILoginPresnter loginPresenter;
    UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mRequestQueue = NetworkManager.getInstance(this).getRequestQueue();
        userInfo = AppManager.getInstance(this).getUserInfo();
        //init
        loginPresenter = new LoginPresenterCompl(this);
        try {
            if (userInfo!=null&&!"".equals(userInfo.getUser_id())) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRequestQueue = NetworkManager.getInstance(this).getRequestQueue();
    }

    /**
     * 注入点击事件
     *
     * @param view
     */
    @OnClick({R.id.activity_login_btn_login, R.id.activity_login_btn_register, R.id.activity_login_lost_password, R.id.activity_login_service_set})
    void Click(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.activity_login_btn_login://登录

                String userName = username.getText().toString();
                String pass = password.getText().toString();

                if (userName == null || "".equals(userName)) {
                    Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show();
                    break;
                }

                if (pass == null || "".equals(pass)) {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
                    break;
                }
                loginPresenter.doLogin(this, userName, pass);

                break;

            case R.id.activity_login_btn_register://注册
//                intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
                clear();
                break;

            case R.id.activity_login_lost_password://忘记密码
//                intent = new Intent(LoginActivity.this, LostPasswordActivity.class);
//                startActivity(intent);
                clear();
                break;

            case R.id.activity_login_service_set://服务器设置
//                intent = new Intent(LoginActivity.this, SetServerActivity.class);
//                startActivity(intent);
                clear();
                break;

        }
    }

    /**
     * 清空所有输入框数据
     */
    private void clear() {
        username.setText("");
        password.setText("");
    }

    @Override
    public void onClearText() {
        username.setText("");
        password.setText("");

    }

    @Override
    protected void onStop() {
        super.onStop();
        loginPresenter.clear();
    }
}
