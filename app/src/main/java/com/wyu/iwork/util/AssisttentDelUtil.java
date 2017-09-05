package com.wyu.iwork.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

/**
 * 作者： sxliu on 2017/8/5.18:33
 * 邮箱：2587294424@qq.com
 */

public class AssisttentDelUtil {

    private static String url = Constant.URL_ASSISTENT_DEL;

    public static void message_delete(BaseActivity activity, String type, String message_id, DialogCallback callback) {
        String user_id = null;
        user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        HttpParams params = new HttpParams();
        params.put("user_id", user_id);
        params.put("type", type);
        params.put("message_id", message_id);
        params.put("F", F);
        params.put("V", V);
        params.put("RandStr", RandStr);
        params.put("Sign", sign);
        OkGo.get(url)
                .params(params)
                .execute(callback);
    }
}
