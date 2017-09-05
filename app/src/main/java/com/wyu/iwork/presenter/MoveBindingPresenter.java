package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.Map;

/**
 * Created by lx on 2016/12/28.
 */

public class MoveBindingPresenter extends NetPresenter {
    private static final String TAG = MoveBindingPresenter.class.getSimpleName();

    public MoveBindingPresenter(Context ctxt) {
        super(ctxt);
    }
    public void doSignIn(String userid) {
        Map<String, String> params = new NetParams.Builder()
                .buildParams("user_id", userid)
                .build();
        doGetRequest(true, Constant.URL_UPDATE_USERPMOVEBINDING, params,null, TAG);
    }
}
