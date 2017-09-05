package com.wyu.iwork.util;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/6/28.15:46
 * 邮箱：2587294424@qq.com
 */

public class Exit_app {

    private static String url = "";


    /**
     * 初始化请求参数
     */
    public static void Exit(UserInfo userInfo ){
        try {
            url = Constant.URL_EXIT;
            String user_id = userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("build_store",url);
            OkGo.get(url)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
