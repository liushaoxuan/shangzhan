package com.wyu.iwork.interfaces;

import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public interface INetPresenter {

    void onLoadData();

    void doGetRequest(boolean requestToken, String url, Map<String, String> params,
                      Map<String, String> signParams, Object tag);

    void doGetRequest(boolean requestToken, String url, Map<String, String> params,
                      Map<String, String> headers, Map<String, String> signParams, Object tag);

    void doPostRequest(boolean requestToken, String url, Map<String, String> params,
                       Map<String, String> signParams, Object tag);

    void doPostRequest(boolean requestToken, String url, Map<String, String> params,
                       Map<String, String> headers, Map<String, String> signParams, Object tag);

    void doRequest(boolean requestToken, int method, String url, Map<String, String> params,
                   Object tag);

    void doRequest(boolean requestToken, int method, String url, Map<String, String> params,
                   Map<String, String> headers, Object tag);

    void cancelRequest();

}
