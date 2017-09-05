package com.wyu.iwork.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.SignUtil;
import com.wyu.iwork.view.activity.RegisterActivity;

import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/27.
 */
public class SignInPresenter extends NetPresenter<UserInfo> {
    private static final String TAG = SignInPresenter.class.getSimpleName();

    public SignInPresenter(Context ctxt) {
        super(ctxt, UserInfo.class);
    }

    public void signIn(String account,String pwd) {
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(pwd)) {
            MsgUtil.shortToast(ctxt, R.string.msg_error_input);
            return;
        }
        Map<String, String> params = new NetParams.Builder()
                .buildParams("user_name", account)
                .buildParams("user_pass", SignUtil.md5(pwd)).build();
        doGetRequest(true, Constant.URL_SIGN_IN, params, null, TAG);
    }

    public void signUp(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    public void saveUserInfo(UserInfo userInfo) {
        //保存用户信息对象,供当前在应用内其他功能使用
//        AppManager.getInstance(ctxt).setUserInfo(userInfo);
//        // 缓存用户信息(json 格式)
//        Prefs.setUserInfo(ctxt, GsonManager.getInstance(ctxt).toJson(userInfo));
        userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
    }

}
