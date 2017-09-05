package com.wyu.iwork.util;

import android.util.Log;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： sxliu on 2017/6/8.13:49
 * 邮箱：2587294424@qq.com
 * OA申请请求类
 */

public class oaApplyUtils {

    private static String url = "";

    //初始化请求参数
    public static void initParama(BaseActivity activity, DialogCallback callback,String mtype,String mapply_type,String mstart_time,String mend_time,double mnumber,
                                  String mcontent,String mway,String  mpayment,List<CompanyContacts> mapprove_user,List<CompanyContacts> mcopy_user){
        try {
            url = Constant.URL_OA_APPLY;
            String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
            String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
            String  type = mtype;//1:请假 2:加班 3:出差 4:报销
            String  apply_type = mapply_type;//申请类型
            String  start_time = mstart_time;//开始时间(报销时无需填写)
            String  end_time = mend_time;//结束时间(报销时无需填写)
            String  number = mnumber+"";//时长(报销时代表金额)
            String  content = mcontent;//事由
            String  way = mway;//加班核算方式(仅限加班时属于必填)
            String  payment = mpayment;//费用类型(cost_type),发生时间(start_time),费用金额(money),费用说明(content)(仅限报销时需要填写)

            String  approve_user = personIds(mapprove_user);//审批人员(用户id用逗号隔开)
            String  copy_user = personIds(mcopy_user);//抄送人员(用户id用逗号隔开)
            String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+type);
            Map<String, String> map = new HashMap<String, String>();

            map.put("user_id",user_id);
            map.put("apply_type",apply_type);
            map.put("type",type);
            map.put("start_time",start_time);
            map.put("end_time",end_time);
            map.put("number",number);
            map.put("content",content);
            map.put("way",way);
            map.put("payment",payment);
            map.put("approve_user",approve_user);
            map.put("copy_user",copy_user);

            map.put("F",Constant.F);
            map.put("V",Constant.V);
            map.put("RandStr",RandStr);
            map.put("Sign",sign);
            url = RequestUtils.getRequestUrl(url, map);
            Logger.e("OA部分的申请url",url);
            Logger.e("oa_apply",url);
            activity.OkgoRequest(url,callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String personIds(List<CompanyContacts> contactses){
        String ids = "";
        for (int i=0;i<contactses.size();i++){
            if (contactses.get(i).getId()!=null&&!"".equals(contactses.get(i).getId())){
                if (i==0){
                    ids = ids+contactses.get(i).getId();
                }else {
                    ids = ids+","+contactses.get(i).getId();
                }
            }
        }
        return ids;
    }
}
