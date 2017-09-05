package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.OutStoreDetailModel;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lx on 2017/5/11.
 */

public class UpdateOutStoreUtil {


    //请求地址
    private static  String url = Constant.URL_OUTSTORE_UPDATE;

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity, DialogCallback callback, OutStoreDetailModel model){
        try {
            if (model==null){
                model = new OutStoreDetailModel("","","","","","","","","","","","","","","");
            }
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();

            String store_out_id = model.getId();//修改出库ID, 新增传0
            String sale_id = model.getSale_id();//销售订单ID
            String status = model.getStatus();//出库状态 1：正在拣货 2：完成出库

            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+store_out_id);


            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("store_out_id",store_out_id);
            map.put("sale_id",sale_id);
            map.put("status",status);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("**UpdateOutStoreUtil**",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
