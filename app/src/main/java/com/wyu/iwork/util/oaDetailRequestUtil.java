package com.wyu.iwork.util;

import android.app.Activity;
import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： sxliu on 2017/6/9.14:25
 * 邮箱：2587294424@qq.com
 * OA——请假、加班、报销、出差详情的请求
 */

public class oaDetailRequestUtil {

    private static String url = "";

    public static void initParama(BaseActivity activity, String contact_id,String meetingId, DialogCallback callback){
        try {
            url = Constant.URL_OA_APPLY_DETAIL;
            String user_id = activity.userInfo.getUser_id();
            String meeting_id = meetingId;
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + contact_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("meeting_id",meeting_id);
            map.put("contact_id",contact_id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("oa_detail",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
