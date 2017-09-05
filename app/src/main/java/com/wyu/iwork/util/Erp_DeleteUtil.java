package com.wyu.iwork.util;

import android.app.Dialog;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.view.activity.BaseActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者： sxliu on 2017/5/17.16:07
 * 邮箱：2587294424@qq.com
 * ERP部分的删除接口的请求参数初始化帮助类
 */

public class Erp_DeleteUtil {


    public static void init_delParama(final BaseActivity activity, final String  url,final String idName,final String id, final DialogCallback callback){



        new MyCustomDialogDialog(3, activity, R.style.MyDialog, "提示",  "删除后不可恢复，您确认要删除吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                delete( activity,  url,idName, id,callback);
                dialog.dismiss();
            }
            @Override
            public void twoClick(Dialog dialog) {
                dialog.dismiss();
            }

        }).show();
    }


    private static void delete( BaseActivity activity,  String  url, String idName, String id,  DialogCallback callback){
        try {
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + id);

            Map<String, String> map = new HashMap<String, String>();
            map.put(idName, id);
            map.put("user_id", user_id);
            map.put("F", Constant.F);
            map.put("V", Constant.V);
            map.put("RandStr", RandStr);
            map.put("Sign", sign);
            //将参数拼接到url后面
            url = RequestUtils.getRequestUrl(url, map);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
