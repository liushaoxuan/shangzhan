package com.wyu.iwork.presenter;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.reflect.TypeToken;
import com.wyu.iwork.model.OrganzSelectorItem;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.List;
import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzLevelSelectorPresenter extends NetPresenter<List<OrganzSelectorItem>> {
    private static final String TAG = OrganzLevelSelectorPresenter.class.getSimpleName();
    private String mCompanyId = "0";
    private String mOrganzId = "0";

    public OrganzLevelSelectorPresenter(Context ctxt, Bundle args) {
        super(ctxt, new TypeToken<List<OrganzSelectorItem>>() {
        }.getType());
        mCompanyId = args != null ? args.getString(Constant.KEY_ID, "0") : "0";
        mOrganzId = args != null ? args.getString(Constant.KEY_ID_2, "0") : "0";
    }

    @Override
    public void onLoadData() {
        Map<String, String> params = new NetParams.Builder().buildParams("company_id", mCompanyId)
                .buildParams("department_id", mOrganzId).build();
        doGetRequest(true, Constant.URL_ORGANZ_LIST, params, null, TAG);
    }
}
