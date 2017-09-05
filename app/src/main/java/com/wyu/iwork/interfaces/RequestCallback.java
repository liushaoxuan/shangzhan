package com.wyu.iwork.interfaces;

import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/11/1.
 */

public interface RequestCallback {

    int ERROR_RESPONSE_LOCAL = -400;

    void onResponse(JSONObject jsonObject);

    void onErrorResponse(int code, String msg);
}
