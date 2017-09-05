package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.SaleOrderDetailModel;
import com.wyu.iwork.model.StockDetailModel;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxliu on 2017/5/11.
 * 新增 编辑库存
 */

public class UpdateStockUtil {


    //请求地址
    private static  String url = Constant.URL_STOCK_UPDATE;

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity, DialogCallback callback, StockDetailModel model){
        try {
            if (model==null){
                model = new StockDetailModel("","","","","","","","","","","","","","","");
            }
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String store_remain_id = model.getId();//修改入库ID, 新增传0
            String goods_id = model.getGoods_id();//商品ID
            String store_id = model.getStore_id();//仓库ID
            String store_goods_id = model.getStore_goods_id();//货位ID
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+store_remain_id);


            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("store_remain_id",store_remain_id);
            map.put("goods_id",goods_id);
            map.put("store_id",store_id);
            map.put("store_goods_id",store_goods_id);
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
