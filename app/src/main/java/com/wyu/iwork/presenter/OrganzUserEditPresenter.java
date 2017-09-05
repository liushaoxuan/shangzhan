package com.wyu.iwork.presenter;

import android.content.Context;
import android.os.Bundle;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.NetUtil;

import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/29.
 */
public class OrganzUserEditPresenter extends NetPresenter<UserInfo> {
    private static final String TAG = OrganzUserEditPresenter.class.getSimpleName();
    private Bundle mArgs;

    public OrganzUserEditPresenter(Context ctxt, Bundle args) {
        super(ctxt, UserInfo.class);
        mArgs = args;
    }

    @Override
    public void onLoadData() {
        final String actionType =
                mArgs != null ? mArgs.getString(Constant.KEY_ACTION_TYPE, "") : "";
        Logger.e(TAG, "onLoadData===>"+actionType);
        if (actionType.equals(Constant.VALUE_TYPE_R)) {
            final String userId = mArgs != null ? mArgs.getString(Constant.KEY_ID) : "";
            Map<String, String> params = new NetParams.Builder().buildParams("user_id", userId)
                    .build();

            doGetRequest(true, Constant.URL_GET_USERINFO, params, null, TAG);
        }
    }

    public void updateInfo(UserInfo userInfo) {
        if (NetUtil.checkLoginStatus(ctxt)) {
            final String localActionType =
                    mArgs != null ? mArgs.getString(Constant.KEY_ACTION_TYPE, "") : "";
            final String actionType =
                    localActionType.equals(Constant.VALUE_TYPE_R) ? userInfo.getUser_id() :
                    localActionType.equals(Constant.VALUE_TYPE_C) ? "0" : "";
            final UserInfo info = AppManager.getInstance(ctxt).getUserInfo();
            Map<String, String> params = new NetParams.Builder()
                    .buildParams("user_id",info.getUser_id())
                    .buildParams("update_user",actionType)
                    .buildParams("tel",userInfo.getUser_phone())
                    .buildParams("name",userInfo.getReal_name())
                    .buildParams("department_id",userInfo.getDepartment_id())
                    .buildParams("role_id",userInfo.getRole_id())
                    .buildParams("sex", userInfo.getSex())
                    .build();
            Map<String, String> signParams = new NetParams.Builder()
                    .buildParams("user_id", info.getUser_id())
                    .buildParams("update_user", actionType)
                    .buildParams("tel", userInfo.getUser_phone())
                    .build();
            doGetRequest(true, Constant.URL_COMMU_USER_CU, params, signParams, TAG);
        }
    }
}
