package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： sxliu on 2017/6/27.10:11
 * 邮箱：2587294424@qq.com
 * 获取生成二维码信息的接口帮助类
 */

public class getEr_q {
    private static String url = "";

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity ,String type_,String id,DialogCallback callback){
        try {
            url = Constant.URL_ER_Q;
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String type = type_;// 2：生成用户信息  3：生成名片
            String data = id;// 2：用户ID  3：名片ID
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + type);
            Map<String, String> map = new HashMap<String, String>();
            map.put("type",type);
            map.put("data",data);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("build_store",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
