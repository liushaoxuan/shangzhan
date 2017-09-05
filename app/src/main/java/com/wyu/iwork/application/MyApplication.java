package com.wyu.iwork.application;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.wyu.iwork.model.AssistantModel;
import com.wyu.iwork.model.JpushIdModel;
import com.wyu.iwork.model.ShareModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.AppUtil;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateManager;

import org.json.JSONObject;
import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2016/12/23.
 */

public class MyApplication extends LitePalApplication {

    private static final String TAG = "MyApplication";
    public static String JpushId = "";
    public static int screenWith;
    public static int screenHeight;
    public static UserInfo userInfo;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        userInfo = DataSupport.findFirst(UserInfo.class)==null?new UserInfo(): DataSupport.findFirst(UserInfo.class);
        ShareModel shareModel =   DataSupport.findFirst(ShareModel.class);
        userInfo.setShare(shareModel);
        /*极光推送*/
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


        JpushIdModel jpushIdModel = DataSupport.findFirst(JpushIdModel.class)==null?new JpushIdModel():DataSupport.findFirst(JpushIdModel.class);
        if (jpushIdModel.getJpushId().isEmpty()){
            JpushId = JPushInterface.getRegistrationID(getApplicationContext());
            jpushIdModel.setJpushId(JpushId);
            jpushIdModel.saveOrUpdate("JpushId=?",JpushId);
        }else {
            JpushId = jpushIdModel.getJpushId();
        }
        Logger.e("Jpush_id", JpushId);
        if (getApplicationInfo().packageName
                .equals(AppUtil.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(AppUtil.getCurProcessName(getApplicationContext()))) {
            Logger.e(TAG, "***********Init RongIMs***************");
            RongIM.init(this);
        }
        if (userInfo != null && userInfo.getUser_id() != null && !"".equals(userInfo.getUser_id())) {
            getToken();
        }

        OkGo.init(this);
        try {
            OkGo.getInstance()
                    //打开该调试开关,控制台会使用 红色error 级别打印log,并不是错误,是为了显眼,不需要就不要加入该行
//                    .debug("OkGo")
                    //如果使用默认的 60秒,以下三行也不需要传
                    .setConnectTimeout(5000)  //全局的连接超时时间  5秒
                    .setReadTimeOut(5000)     //全局的读取超时时间
                    .setWriteTimeOut(5000)    //全局的写入超时时间
                    .setRetryCount(3)
                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)//读取网络失败的时候读取缓存
                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //初始化扫描二维码zxing
        ZXingLibrary.initDisplayOpinion(this);
        getWithHeight();
        /**
         * 在SDK各功能组件使用之前都需要调用
         * 因此我们建议该方法放在Application的初始化方法中
         */
        SDKInitializer.initialize(getApplicationContext());
        ShareSDK.initSDK(this);
        ShareSDK.initSDK(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void getWithHeight() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        screenWith = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
    }

    //获取融云token和企业认证状态
    public static void getToken() {
        if (userInfo != null) {
            String url = Constant.URL_TOKEN_URL;
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String user_id = userInfo.getUser_id();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id", userInfo.getUser_id());
            map.put("F", Constant.F);
            map.put("V", Constant.V);
            map.put("RandStr", RandStr);
            map.put("Sign", sign);
            //将参数拼接到url后面
            url = RequestUtils.getRequestUrl(url, map);

            Logger.e(TAG, url);
            OkGo.get(url)
                    .execute(new StringCallback() {
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
                                    JSONObject data = object.optJSONObject("data");
                                    String rongcloud_token = data.optString("rongcloud_token");
                                    String company_auth = data.optString("company_auth");
                                    String company_name = data.optString("company_name");
                                    String is_admin = data.optString("is_admin");// [is_admin] => 是否为超级管理员  1：超级管理员 0：普通员工
                                    JSONObject version = data.optJSONObject("version");
                                    int is_update = version.optInt("is_update");// "是否需要更新 1：需要更新 0：不需要更新",
                                    String url = version.optString("url");//更新连接,不需更新时为空，ANDROID下载地址
                                    userInfo.setIs_update(is_update);
                                    userInfo.setUrl(url);
                                    userInfo.setRongcloud_token(rongcloud_token);
                                    userInfo.setCompany_auth(company_auth);
                                    userInfo.setCompany(company_name);
                                    userInfo.setIs_admin(is_admin);
                                    saveUserInfo(userInfo);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public static void saveUserInfo(UserInfo userInfo) {
        //保存用户信息对象,供当前在应用内其他功能使用
        userInfo.saveOrUpdate("user_id=?", userInfo.getUser_id());
    }

}
