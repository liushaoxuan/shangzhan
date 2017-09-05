package com.wyu.iwork;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.android.volley.Request;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.RequestCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.net.JsonUtil;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.net.NetworkManager;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.AppUtil;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Debug;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.NetUtil;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;


/**
 * @author HuaJian Jiang.
 *         Date 2016/11/25.
 */
public class AppManager {
    private static final String TAG = "AppManager";
    private static AppManager sAppLab;
    private Context mCtxt;
    private static UserInfo mUserInfo;

    public AppManager(Context ctxt) {
        mCtxt = ctxt.getApplicationContext();
    }

    public synchronized static AppManager getInstance(Context ctxt) {
        if (sAppLab == null) {
            sAppLab = new AppManager(ctxt);
        }
        return sAppLab;
    }

    public UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = DataSupport.findFirst(UserInfo.class);
        }
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public void delUser() {
        mUserInfo = null;
    }

    public static void connectImServer(final Context ctxt) {
        if (ctxt.getApplicationInfo().packageName
                .equals(AppUtil.getCurProcessName(ctxt.getApplicationContext())))
            if (NetUtil.checkLoginStatus(ctxt)&& MyApplication.userInfo!=null) {
                final UserInfo userInfo = MyApplication.userInfo;
                  String token = userInfo.getRongcloud_token();
                Logger.i(TAG, "connectImServer_token===>" + token);
                if (TextUtils.isEmpty(token)) {
                    MsgUtil.shortToast(ctxt, "无法连接 IM 服务器");
                    return;
                }

                RongIM.connect(token, new RongIMClient.ConnectCallback() {
                    /**
                     * Token 错误。可以从下面两点检查
                     * <p>
                     * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                     * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                     */
                    @Override
                    public void onTokenIncorrect() {
                        Logger.i(TAG, "RongIM_onTokenIncorrect");
                        MsgUtil.shortToast(ctxt, "Token 失效");
                    }

                    @Override
                    public void onSuccess(String s) {
                        Logger.i(TAG, "RongIM_onSuccess");
//                        MsgUtil.shortToast(ctxt, "成功连接 IM 服务器 ");

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Logger.i(TAG, "RongIM_onError=>" + errorCode.getMessage());
                        MsgUtil.shortToast(ctxt, "连接 IM 服务器失败");
                    }
                });

                //设置用户信息
                RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                    @Override
                    public io.rong.imlib.model.UserInfo getUserInfo(String s) {
                        String user_id = userInfo.getUser_id();
                        String name = userInfo.getReal_name();
                        Uri uri = Uri.parse(userInfo.getUser_face_img()==null?"":userInfo.getUser_face_img());
                        io.rong.imlib.model.UserInfo rong_userInfo = new io.rong.imlib.model.UserInfo(user_id,name,uri);
                        return rong_userInfo;
                    }
                },true);
                /**
                 * 刷新用户缓存数据。
                 *
                 * @param userInfo 需要更新的用户缓存数据。
                 */
                RongIM.getInstance().refreshUserInfoCache(new io.rong.imlib.model.UserInfo(userInfo.getUser_id(), userInfo.getReal_name(), Uri.parse(userInfo.getUser_face_img()==null?"":userInfo.getUser_face_img())));
            }
    }

    public static void requestImToken(final Context ctxt) {
        if (NetUtil.checkLoginStatus(ctxt)) {
                final UserInfo userInfo = AppManager.getInstance(ctxt).getUserInfo();
                Map<String, String> params = new NetParams.Builder()
                        .buildParams("userId", userInfo.getUser_id())
                        .buildParams("name", userInfo.getUser_name())
                        .buildParams("portraitUri", userInfo.getUser_face_img()).build();
                NetworkManager.getInstance(ctxt)
                        .jsonRequest(Request.Method.POST, Constant.URL_IM_TOKEN, params,
                                NetUtil.buildImHeaders(), TAG, new RequestCallback() {
                                    @Override
                                    public void onResponse(JSONObject jsonObject) {
                                        Logger.i(TAG, Debug.printPrettyJson(jsonObject));
                                        final int code = JsonUtil.getInt(jsonObject, "code");
                                        if (code == 200) {
                                            String token = JsonUtil.getString(jsonObject, "token");
                                            userInfo.setRongcloud_token(token);
                                            //保存用户信息对象,供当前在应用内其他功能使用
                                            AppManager.getInstance(ctxt).setUserInfo(userInfo);
                                            // 缓存用户信息(json 格式)
                                            userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
                                            AppManager.connectImServer(ctxt);
                                        }
                                    }

                                    @Override
                                    public void onErrorResponse(int code, String msg) {
                                        Logger.e(TAG, "requestImToken_error!!!" + code + "," + msg);
                                    }
                                });

                AppManager.connectImServer(ctxt);

        }
    }
}
