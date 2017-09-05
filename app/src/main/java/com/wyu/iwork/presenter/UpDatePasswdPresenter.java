package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.Map;

/**
 * Created by lx on 2016/12/28.
 */

public class UpDatePasswdPresenter extends NetPresenter {
    private static final String TAG = UpDatePasswdPresenter.class.getSimpleName();

    public UpDatePasswdPresenter(Context ctxt) {
        super(ctxt);
    }
    public void doSignIn(String userid,String old_passwd,String new_passwd) {
        Map<String, String> params = new NetParams.Builder()
                .buildParams("user_id", userid)
                .buildParams("old_passwd", old_passwd)
                .buildParams("new_passwd",new_passwd)
                .build();
        doGetRequest(true, Constant.URL_UPDATE_USERPASSWORD, params,null, TAG);
    }
}
