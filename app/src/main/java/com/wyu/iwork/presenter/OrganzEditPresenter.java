package com.wyu.iwork.presenter;

import android.content.Context;
import android.os.Bundle;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.OrganzInfo;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.NetUtil;

import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzEditPresenter extends NetPresenter<OrganzInfo> {
    private static final String TAG = OrganzEditPresenter.class.getSimpleName();
    private OrganzInfo organzInfo;

    public OrganzEditPresenter(Context ctxt, Bundle args) {
        super(ctxt, args != null ? OrganzInfo.class : null);
        organzInfo = args != null ? (OrganzInfo) args.getSerializable(Constant.KEY_ENTITY) : null;
    }

    public OrganzInfo getOrganzInfo() {
        return organzInfo;
    }

    @Override
    public void onLoadData() {
        if (organzInfo != null) {// 编辑操作，请求数据
            Map<String, String> params = new NetParams.Builder()
                    .buildParams("department_id", organzInfo.getDepartment_id())
                    .build();
            doGetRequest(true, Constant.URL_ORGANZ_INFO, params, null, TAG);
        }
    }

    public void updateInfo(OrganzInfo organzInfo) {
        if (organzInfo == null) return;
        if (NetUtil.checkLoginStatus(ctxt)) {
            UserInfo userInfo = AppManager.getInstance(ctxt).getUserInfo();
            Map<String, String> signParams = new NetParams.Builder()
                    .buildParams("user_id", userInfo.getUser_id())
                    .buildParams("department_id", organzInfo.getDepartment_id())
                    .buildParams("department_superior",organzInfo.getDepartment_superior_id())
                    .buildParams("department",organzInfo.getDepartment())
                    .build();
            Map<String,String> params=new NetParams.Builder()
                    .buildParams("tel",organzInfo.getTel())
                    .buildParams("fax",organzInfo.getFax())
                    .buildParams("address",organzInfo.getAddress())
                    .build();
            params.putAll(signParams);
            doGetRequest(true, Constant.URL_ORGANZ_UPDATE, params, signParams, TAG);
        }
    }

}
