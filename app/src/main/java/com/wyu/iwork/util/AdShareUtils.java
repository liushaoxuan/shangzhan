package com.wyu.iwork.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.interfaces.DialogCallback;

/**
 * 作者： sxliu on 2017/8/10.11:43
 * 邮箱：2587294424@qq.com
 * 广告联盟 任务详情分享utils
 */

public class AdShareUtils {
    private static String TAG = AdShareUtils.class.getSimpleName();
    private static String url = Constant.URL_AD_SHARE;

    public static void getShareData(String ad_receive_id, DialogCallback callback){
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr +ad_receive_id);
        HttpParams map = new HttpParams();
        map.put("ad_receive_id", ad_receive_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        OkGo.get(url)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .params(map)
                .execute(callback);
    }
}
