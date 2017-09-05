package com.wyu.iwork.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.NetParams;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class NetUtil {
    private static final String TAG = NetUtil.class.getSimpleName();

    public static boolean checkLoginStatus(Context ctxt) {
        UserInfo userInfo = AppManager.getInstance(ctxt).getUserInfo();
        return userInfo != null && !TextUtils.isEmpty(userInfo.getUser_id());
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static Map<String, String> mergeBaseParams(Context ctxt, Map<String, String> outParams,
                                                      Map<String, String> signParams)
    {
        final Map<String, String> resultParams =
                outParams == null ? new HashMap<String, String>() : outParams;
        StringBuilder signSb = new StringBuilder();
        String versionName = AppUtil.getVersionName(ctxt);
        String random = DateUtil.getTimeSecs() + "|" + UUID.randomUUID().toString();
        signSb.append(Constant.SECRETKEY).append(Constant.F).append(versionName).append(random);
        //sign 参数为空表明 out 参数都需要作为签名部分
        for (String value : signParams != null ? signParams.values() : resultParams.values()) {
            signSb.append(value);
        }
        resultParams.put("F", Constant.F);
        resultParams.put("V", versionName);
        resultParams.put("RandStr", random);
        resultParams.put("Sign", SignUtil.md5(signSb.toString()).substring(8, 20));
        return resultParams;
    }

    public static Map<String, String> buildImHeaders() {
        final String randomStr = DateUtil.getTimeMilliSecs() + "";
        final String timeSecs = DateUtil.getTimeSecs() + "";
        return new NetParams.Builder().buildParams("App-Key", Constant.IM_APP_KEY)
                .buildParams("Nonce", randomStr).buildParams("Timestamp", timeSecs)
                .buildParams("Signature",
                        SignUtil.SHA1(Constant.IM_APP_SECRET + randomStr + timeSecs)).build();
    }

}
