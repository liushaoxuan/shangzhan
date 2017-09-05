package com.wyu.iwork.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.NetUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.FindPassWordActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.RegisterActivity;

import org.json.JSONObject;
import org.litepal.crud.callback.SaveCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class SignInFragment extends BaseFragment<UserInfo> {
    private static final String TAG = SignInFragment.class.getSimpleName();
    private EditText mAccount;
    private EditText mPwd;

    private UserInfo userInfo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 如果之前登录过直接跳转到主功能界面
        if (NetUtil.checkLoginStatus(getActivity())) {
            //退出登录界面并跳转至主功能界面
            jumpToMain();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, (ViewGroup) inflater
                .inflate(R.layout.fragment_sign_in, container, false), savedInstanceState);
    }

    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
        mAccount = (EditText) rootView.findViewById(R.id.account);
        mPwd = (EditText) rootView.findViewById(R.id.pwd);
        rootView.findViewById(R.id.sign_in).setOnClickListener(this);
        rootView.findViewById(R.id.sign_up).setOnClickListener(this);
        rootView.findViewById(R.id.find_pwd).setOnClickListener(this);
        rootView.findViewById(R.id.settings).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sign_in:
                String name = mAccount.getText().toString();
                String pass = mPwd.getText().toString();
                if (name.isEmpty()){
                    Toast.makeText(getActivity(),"请输入用户名",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (pass.isEmpty()){
                    Toast.makeText(getActivity(),"请输入密码",Toast.LENGTH_SHORT).show();
                    break;
                }

                UserLogin(name,pass);
                return;
            case R.id.sign_up:
                 startActivity(new Intent(getActivity(), RegisterActivity.class));
                return;
            case R.id.find_pwd:
                Intent intent = new Intent(getActivity(), FindPassWordActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                break;
        }
    }


    private void jumpToMain() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
    //登录

    private void UserLogin(final String username, String password){
        String url = Constant.URL_SIGN_IN;
        String user_name = username;
        String user_pass = Md5Util.getMD5(password);
        String jpush_id = MyApplication.JpushId;
        String F = Constant.F;
        String V = Constant.V;
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
        Logger.e("Login_url",murl);

        OkGo.get(murl)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONObject data = object.optJSONObject("data");
                                userInfo = JSON.parseObject(data.toString(),UserInfo.class);
                                saveUserInfo(userInfo);
                                MyApplication.userInfo = userInfo;
                                MyApplication.getToken();
                                jumpToMain();
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.e("login_------------>", s);
                    }
                });
    }

    public void saveUserInfo(UserInfo userInfo) {
        //保存用户信息对象,供当前在应用内其他功能使用
  /*      AppManager.getInstance(getActivity()).setUserInfo(userInfo);
        // 缓存用户信息(json 格式)
        Prefs.setUserInfo(getActivity(), GsonManager.getInstance(getActivity()).toJson(userInfo));*/
        userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
    }
}
