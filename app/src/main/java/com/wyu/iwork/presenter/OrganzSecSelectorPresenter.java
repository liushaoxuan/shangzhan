package com.wyu.iwork.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.model.UserOrganzParent;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.NetUtil;

import java.util.List;
import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class OrganzSecSelectorPresenter extends NetPresenter<List<UserOrganzParent>> {
    private static final String TAG = OrganzSecSelectorPresenter.class.getSimpleName();

    public OrganzSecSelectorPresenter(Context ctxt) {
        super(ctxt, new TypeToken<List<UserOrganzParent>>() {
        }.getType());
    }

    @Override
    public void onLoadData() {
        if (NetUtil.checkLoginStatus(ctxt)) {
            UserInfo userInfo = AppManager.getInstance(ctxt).getUserInfo();
            Map<String, String> params = new NetParams.Builder()
                    .buildParams("company_id", userInfo.getCompany_id())
                    .buildParams("user_id", userInfo.getUser_id()).build();
            doGetRequest(true, Constant.URL_ORGANZ_MANAGE, params, null, TAG);
        }
    }
}
