package com.wyu.iwork.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： sxliu on 2017/8/9.09:55
 * 邮箱：2587294424@qq.com
 * 广告联盟 领取并发布任务的util
 */

public class AdgetAndReleaseTaskUtil {

    private static String TAG = UpLoadFileUtil.class.getSimpleName();
    private static String url ="";
    public static void initParama(BaseActivity activity, String ad_sense_id, int station, String title, String article_details, DialogCallback callback){
        url = Constant.URL_AD_TASK_SETTINGS;
        String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id+ad_sense_id);
//        HttpParams parama = new HttpParams();
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("ad_sense_id", ad_sense_id);
        map.put("station", station+"");//广告位置（1：文章顶部 2：文章底部）
        map.put("title", title+"");//文章标题
        map.put("article_details", article_details+"");//分享信息内容（格式：array(array("type"=>"1：文字，2：图片"，"msg"=>"文字内容/图片地址"))）
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        String murl = RequestUtils.getRequestUrl(url,map);
        OkGo.post(url)
                .tag(TAG)
                .params(map)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }
}
