package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.Map;

/**
 * Created by lx on 2016/12/28.
 */

public class UpDateUserHeadPresenter extends NetPresenter {
    private static final String TAG = UpDateUserHeadPresenter.class.getSimpleName();

    public UpDateUserHeadPresenter(Context ctxt) {
        super(ctxt);
    }
    public void doSignIn(String userid,String face) {
        Map<String, String> signParams = new NetParams.Builder()
                .buildParams("user_id", userid)
                .build();
        Map<String, String> params = new NetParams.Builder()
                .buildParams("user_id", userid)
                .buildParams("face", face)
                .build();
        doPostRequest(true, Constant.URL_UPDATE_FACE, params,signParams, TAG);
    }
}
