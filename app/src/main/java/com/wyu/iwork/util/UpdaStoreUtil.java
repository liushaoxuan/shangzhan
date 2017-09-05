package com.wyu.iwork.util;
import android.util.Log;

import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;
import com.wyu.iwork.widget.CustomeViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lx on 2017/5/9.
 */

public class UpdaStoreUtil {
    //请求地址
    private static String url = Constant.URL_STORE_UPDATE;

    /**
     * 初始化请求参数
     */
    public static void initParama(BaseActivity activity , List<CustomeViewGroup> viewlist, DialogCallback callback, String province, String city, String district,String id){
        try {
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String store_id = id;// 修改仓库ID, 新增传0
            String name = viewlist.get(0).getInput();//仓库名称
            String manager = viewlist.get(1).getInput();//仓库管理人
            String address =viewlist.get(2).getInput();//具体地址
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+store_id);
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id",user_id);
            map.put("store_id",store_id);
            map.put("name",name);
            map.put("manager",manager);
            map.put("province",province);
            map.put("city",city);
            map.put("district",district);
            map.put("address",address);
            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("build_store",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
