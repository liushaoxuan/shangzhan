package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity {

    /**
     * 原密码
     */
    @BindView(R.id.activity_change_password_old_pass)
    EditText old_pass;
    /**
     * 新密码
     */
    @BindView(R.id.activity_change_password_new_pass)
    EditText new_pass;
    /**
     * 确认密码
     */
    @BindView(R.id.activity_change_password_new_pass_again)
    EditText new_pass_sure;

    private UserInfo  userInfo ;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        userInfo = MyApplication.userInfo;
        Logger.e("修改密码的user_id为空异常", JSON.toJSONString(userInfo==null?"userInfo他null了":userInfo));

    }

    @OnClick({R.id.activity_change_password_back,R.id.activity_change_password_save})
    void Click(View v){
        switch (v.getId()){
            case R.id.activity_change_password_back:
                onBackPressed();
                break;
            case R.id.activity_change_password_save://保存
                String pass = old_pass.getText().toString();
                String newpass = new_pass.getText().toString();
                String passagai = new_pass_sure.getText().toString();
                if (pass.isEmpty()){
                    Toast.makeText(this,"请输入原密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (newpass.isEmpty()){
                    Toast.makeText(this,"请输入新密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (passagai.isEmpty()){
                    Toast.makeText(this,"请再次输入新密码",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (!newpass.equals(passagai)){
                    Toast.makeText(this,"确认密码有误，请重新输入",Toast.LENGTH_SHORT).show();
                    break;
                }
                //TODO 修改密码
                String url = Constant.URL_UPDATE_USERPASSWORD;
                String user_id = "";
                String old_passwd = Md5Util.getMD5(pass);
                String new_passwd = Md5Util.getMD5(newpass);
                if (userInfo!=null){
                    user_id =userInfo.getUser_id();
                }

                String F = Constant.F;
                String V = Constant.V;
                String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
                String sign = Md5Util.getSign(F + V + RandStr + user_id+old_passwd+new_passwd);
                Map<String, String> map = new HashMap<String, String>();
                map.put("user_id", user_id);
                map.put("old_passwd", old_passwd);
                map.put("new_passwd", new_passwd);
                map.put("F", F);
                map.put("V", V);
                map.put("RandStr", RandStr);
                map.put("Sign", sign);
                //将参数拼接到url后面
                String murl = RequestUtils.getRequestUrl(url, map);
                Logger.e("修改密码的url",murl);
                OkgoRequest(murl,changePass());
                break;
        }
    }

    //修改密码
    private DialogCallback changePass(){
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
                    if ("0".equals(code)){
                        finish();
                    }
                    Toast.makeText(ChangePasswordActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
