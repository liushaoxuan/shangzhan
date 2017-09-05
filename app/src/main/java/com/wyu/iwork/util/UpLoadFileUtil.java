package com.wyu.iwork.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者： sxliu on 2017/8/9.09:24
 * 邮箱：2587294424@qq.com
 * 上传图片帮助类
 */

public class UpLoadFileUtil {

    private static String TAG = UpLoadFileUtil.class.getSimpleName();
    private static String url = "";

    public  static void init(BaseActivity activity, File file, StringCallback callback){
          url = Constant.URL_UPLOAD_PIC;
        String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        Logger.e(TAG,murl);
        OkGo.post(murl)
                .tag(TAG)
                 .cacheMode(CacheMode.DEFAULT)
                .params("file",file)
                .execute(callback);
    }
}
