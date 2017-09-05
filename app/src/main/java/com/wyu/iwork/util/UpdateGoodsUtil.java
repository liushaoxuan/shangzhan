package com.wyu.iwork.util;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;
import com.wyu.iwork.widget.CustomeViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxliu on 2017/5/10.
 * 新增 编辑商品
 */

public class UpdateGoodsUtil {

    //请求地址
    private static String url = Constant.URL_GOODS_UPDATE;

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity, DialogCallback callback, String name,String price,String market_price, String type_id, String goods_id){

        try {
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+goods_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("goods_id",goods_id);
            map.put("name",name);
            map.put("price",price);
            map.put("market_price",market_price);
            map.put("type_id",type_id);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("UpdateGoodsUtil",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {e.printStackTrace();

        }
    }
}
