package com.wyu.iwork.util;

import android.content.Context;
import android.net.Uri;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.net.JsonUtil;
import com.wyu.iwork.stor.Prefs;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/4/15.
 * 获取融云Token
 */

public class RongIMutil {
   private  UserInfo userInfo;
   private  Context context;

    public RongIMutil(Context context) {
        this.context = context;
        userInfo = AppManager.getInstance(context).getUserInfo();
    }

    public   void ConnectIM(){
        String url = Constant.URL_IM_TOKEN;
        String uri = Uri.parse(userInfo.getUser_face_img()).toString();
        HttpParams params = new HttpParams();
        params.put("userId",userInfo.getUser_id());
        params.put("name",userInfo.getReal_name());
        params.put("portraitUri",uri);
        OkGo.post(url)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            final int code = JsonUtil.getInt(object, "code");
                            if (code == 200) {
                                String token = JsonUtil.getString(object, "token");
                                userInfo.setRongcloud_token(token);
                                //保存用户信息对象,供当前在应用内其他功能使用
                                AppManager.getInstance(context).setUserInfo(userInfo);
                                // 缓存用户信息(json 格式)
                                userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
                                AppManager.connectImServer(context);
                                connect_rong(token);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //链接融云服务器
    private void connect_rong(String  mtoken){
        RongIM.connect(mtoken, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查
             * <p>
             * 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {

                MsgUtil.shortToast(context, "Token 失效");
            }

            @Override
            public void onSuccess(String s) {

                MsgUtil.shortToast(context, "成功连接 IM 服务器 ");

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                MsgUtil.shortToast(context, "连接 IM 服务器失败");
            }
        });
    }
}
