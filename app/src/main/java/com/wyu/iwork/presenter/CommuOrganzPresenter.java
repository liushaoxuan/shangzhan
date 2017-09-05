package com.wyu.iwork.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.OrgnzParent;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.NetUtil;

import java.util.List;
import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/9.
 */
public class CommuOrganzPresenter extends NetPresenter<List<OrgnzParent>> {
    private static final String TAG = CommuOrganzPresenter.class.getSimpleName();

    public CommuOrganzPresenter(Context ctxt) {
        super(ctxt, new TypeToken<List<OrgnzParent>>() {
        }.getType());
    }

    @Override
    public void onLoadData() {
        if (NetUtil.checkLoginStatus(ctxt)) {
            UserInfo userInfo = AppManager.getInstance(ctxt).getUserInfo();
            Map<String, String> params = new NetParams.Builder()
                    .buildParams("company_id", userInfo.getCompany_id()).
                            build();
            doGetRequest(true, Constant.URL_COMMU_ORGANZ, params, null, TAG);
        }
    }


}
