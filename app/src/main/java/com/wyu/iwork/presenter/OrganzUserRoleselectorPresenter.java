package com.wyu.iwork.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.OrganzUserRoleSelectorItem;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.NetUtil;

import java.util.List;
import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzUserRoleselectorPresenter extends NetPresenter<List<OrganzUserRoleSelectorItem>> {
    private static final String TAG = OrganzUserRoleselectorPresenter.class.getSimpleName();

    public OrganzUserRoleselectorPresenter(Context ctxt) {
        super(ctxt, new TypeToken<List<OrganzUserRoleSelectorItem>>() {
        }.getType());
    }

    @Override
    public void onLoadData() {
        if (NetUtil.checkLoginStatus(ctxt)) {
            UserInfo userInfo = AppManager.getInstance(ctxt).getUserInfo();
            Map<String, String> params = new NetParams.Builder()
                    .buildParams("company_id", userInfo.getCompany_id()).build();
            doGetRequest(true, Constant.URL_ORGANZ_ROLE_LIST, params, null, TAG);
        }
    }
}
