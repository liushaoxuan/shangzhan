package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxliu on 2017/5/10.
 * 新增 编辑货位
 */

public class UpdateCargoUtil {
    private static String url = Constant.URL_CARGO_UPDATE;

    public static void initParama (BaseActivity activity, DialogCallback callback,String name, String store_id, String store_goods_id){
        try {
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();

            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+store_goods_id);

            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("store_goods_id",store_goods_id==null?"":store_goods_id);
            map.put("name",name==null?"":name);
            map.put("store_id",store_id==null?"":store_id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("*****url*****",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
