package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.SaleOrderDetailModel;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lx on 2017/5/11.
 */

public class UpdateSaleOrderUtil {

    //请求地址
    private static  String url = "";

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity, DialogCallback callback, SaleOrderDetailModel model){
        try {
            if (model==null){
                model = new SaleOrderDetailModel("","","","","","","","","","","","","","","","");
            }
            url = Constant.URL_SALE_ORDER_UPDATE;
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sale_order_id = model.getId();//修改销售订单ID，新增传0
            String customer_id = model.getCustomer_id();//客户ID
            String goods_id = model.getGoods_id();//商品ID
            String goods_num = model.getGoods_num();//商品数量
            String discount = model.getDiscount();//销售商品折扣
            String status = model.getDeliver();//订单状态 1：待审批 2：已审批
            String place_time = model.getPlace_time();//下单日期 格式：2017-12-12
            String deliver = model.getDeliver();//发货方式

            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+sale_order_id);


            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("sale_order_id",sale_order_id);
            map.put("customer_id",customer_id);
            map.put("goods_id",goods_id);
            map.put("goods_num",goods_num);
            map.put("discount",discount);
            map.put("status",status);
            map.put("place_time",place_time);
            map.put("deliver",deliver);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("****UpdateSale****",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
