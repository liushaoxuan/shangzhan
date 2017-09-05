package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxliu on 2017/5/10.
 * 新增 编辑入库
 */

public class UpdateInStoreUtil {
    //请求地址
    private static String url = Constant.URL_INSTORE_UPDATE;

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity, DialogCallback callback,String purchase_id,String store_put_id){
        try {
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            store_put_id=store_put_id==null?"":store_put_id;//修改入库ID, 新增传0
            purchase_id=purchase_id==null?"":purchase_id;//采购订单ID
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+store_put_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("store_put_id",store_put_id);
            map.put("purchase_id",purchase_id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("goods_in_url",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
