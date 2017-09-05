package com.wyu.iwork.util;

import android.util.Log;
import android.widget.Toast;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/6/9.17:29
 * 邮箱：2587294424@qq.com
 * OA  撤销 催办utils、
 */

public class oaRevokeRemindersUtils {


    /**
     * 撤销
     */
    public static void Revoke(final BaseActivity activity,String id){
        try {
            String   url = Constant.URL_OA_REVOKE;
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("contact_id",id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("oa_detail",url);
            activity.OkgoRequest(url,new DialogCallback(activity) {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    try {

                        JSONObject object = new JSONObject(s);
                        String code = object.optString("code");
                        if ("0".equals(code)){
                            activity.Refresh();
                        }
                        Toast.makeText(activity, object.optString("msg"), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 催办
     */
    public static void  Reminders(final BaseActivity activity,String id){
        try {
          String   url = Constant.URL_OA_REMINDERS;
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("contact_id",id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("oa_detail",url);
            activity.OkgoRequest(url, new DialogCallback(activity) {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    try {

                        JSONObject object = new JSONObject(s);
                        String code = object.optString("code");

                        Toast.makeText(activity, object.optString("msg"), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
