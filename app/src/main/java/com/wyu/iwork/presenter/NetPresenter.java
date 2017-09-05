package com.wyu.iwork.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.INetPresenter;
import com.wyu.iwork.interfaces.NetIView;
import com.wyu.iwork.interfaces.RequestCallback;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.net.JsonUtil;
import com.wyu.iwork.net.NetworkManager;
import com.wyu.iwork.util.Debug;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.NetUtil;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/26.
 */
public class NetPresenter<T> extends BasePresenter<NetIView<T>, T>
        implements INetPresenter, RequestCallback
{
    private static final String TAG = "NetPresenter";
    private Object mRequestTag;
    protected Context ctxt;
    private NetworkManager mNetManager;
    private Class<T> mClassOfT;
    private Type mTypeOfT;

    public NetPresenter(Context ctxt, Class<T> classOfT) {
        init(ctxt);
        mClassOfT = classOfT;
    }

    public NetPresenter(Context ctxt, Type typeOfT) {
        init(ctxt);
        mTypeOfT = typeOfT;
    }

    public NetPresenter(Context ctxt) {
        init(ctxt);
    }

    private void init(Context ctxt) {
        this.ctxt = ctxt;
        mNetManager = NetworkManager.getInstance(ctxt);
    }

    @Override
    public void onLoadData() {
        // do nothing
    }

    @Override
    public void doGetRequest(boolean requestToken, String url, Map<String, String> params,
                             Map<String,String> signParams,Object tag) {
        doGetRequest(requestToken, url, params, null, signParams, tag);
    }

    @Override
    public void doGetRequest(boolean requestToken, String url, Map<String, String> params,
                             Map<String, String> headers, Map<String, String> signParams,
                             Object tag)
    {
        //1.处理全局参数
        //2.手动合并请求参数至url后面
        params = NetUtil.mergeBaseParams(ctxt, params, signParams);
        doRequest(requestToken, Request.Method.GET, NetworkManager.mergeParams(url, params), null,
                headers, tag);
    }

    @Override
    public void doPostRequest(boolean requestToken, String url, Map<String, String> params,
                              Map<String,String> signParams,Object tag)
    {
        doPostRequest(requestToken, url, params, null, signParams, tag);
    }

    @Override
    public void doPostRequest(boolean requestToken, String url, Map<String, String> params,
                              Map<String, String> headers, Map<String, String> signParams,
                              Object tag)
    {
        // 处理全局参数
        params = NetUtil.mergeBaseParams(ctxt, params, signParams);
        doRequest(requestToken, Request.Method.POST, url, params, headers, tag);
    }

    @Override
    public void doRequest(boolean requestToken, int method, String url, Map<String, String> params,
                          Object tag)
    {
        doRequest(requestToken, method, url, params, null, tag);
    }

    @Override
    public void doRequest(boolean requestToken, int method, String url, Map<String, String> params,
                          Map<String, String> headers, Object tag)
    {
        if (!NetUtil.checkNetwork(ctxt)) {
            // 显示无数据空白页?
            MsgUtil.shortToast(ctxt, R.string.net_error);
            if (view != null) view.onFailure(NetIView.ERROR_NETWORK_CONNECTION,
                    ctxt.getString(R.string.net_error));
            return;
        }
        // 检查用户是否已登录
        //        if (requestToken && ){
        //
        //        }

        // 网络请求前的准备工作(显示 loading 界面等等)
        if (view != null) view.onRequestBefore();

        mRequestTag = tag;
        mNetManager.jsonRequest(method, url, params, headers, tag, this);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        int code = JsonUtil.getInt(jsonObject,"code");
        if (code == 0) {
            T result = null;
            Object data = jsonObject.opt("data");
            String json = data != null ? data.toString() : null;
            if (!TextUtils.isEmpty(json)) {
                if (!TextUtils.isEmpty(json)) {
                    if (mTypeOfT != null) {
                        Logger.e(TAG,"_________1________");
                        result = GsonManager.getInstance(ctxt).fromJson(json, mTypeOfT);
                    } else if (mClassOfT != null) {
                        Logger.e(TAG,"_________2_________");
                        result = GsonManager.getInstance(ctxt).fromJson(json, mClassOfT);
                    }
                }
            }
            Logger.e(TAG, "__________3__________");
            Logger.e(TAG, Debug.printPrettyJson(jsonObject));
            if (view != null) view.onSuccess(result, jsonObject);
            // 缓存填充加载后的数据的本地数据实体类
            cachedData = result;
        } else {
            String errorStr = JsonUtil.getString(jsonObject, "msg");
            if (view != null) view.onFailure(code, errorStr);
        }
        // 网络请求结束收尾工作 (取消 loading、上拉和下拉刷新相关界面等等)
        if (view != null) view.onRequestAfter();
    }

    @Override
    public void onErrorResponse(int code, String msg) {
        if (view != null) {
            view.onFailure(NetIView.ERROR_VOLLEY, msg);
            // 网络请求结束收尾工作 (取消 loading、上拉和下拉刷新相关界面等等)
            view.onRequestAfter();
        }
    }

    /**
     * 取消之前被标记为指定 tag 的请求
     */
    public void cancelRequest() {
        Logger.e(TAG, "<<<<<<<<<<<<<<<cancelRequest>>>>>>>>>>>>");
        if (mRequestTag != null) mNetManager.cancelRequest(mRequestTag);
    }
}
