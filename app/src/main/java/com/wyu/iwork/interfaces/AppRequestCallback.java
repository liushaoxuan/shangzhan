package com.wyu.iwork.interfaces;

import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/11/1.
 */

public interface AppRequestCallback<T> {

    boolean requestBefore();

    void onSuccess(T t, JSONObject origin);

    void onFailure(int errorCode, String errorMsg);

    boolean requestAfter();
}
