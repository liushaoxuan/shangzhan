package com.wyu.iwork.util;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxliu on 2017/5/10.
 * 获取商品分类
 */

public class getGoodsType {
    //请求地址
    private static String url = Constant.URL_GOODS_TYPE;

    /**
     * 初始化请求参数
     */
    public static void getGoodsType(BaseActivity activity, DialogCallback callback){

        try {
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
