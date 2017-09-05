package com.wyu.iwork.interfaces;

import org.json.JSONObject;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public interface NetIView<T> extends IView {

    int ERROR_VOLLEY = -400;

    int ERROR_NETWORK_CONNECTION = -500;

    void onRequestBefore();

    void onSuccess(T data, JSONObject origin);

    void onFailure(int errorCode, String errorMsg);

    void onUnLogin();

    void onRequestAfter();
}
