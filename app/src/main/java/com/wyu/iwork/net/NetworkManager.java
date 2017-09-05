package com.wyu.iwork.net;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.wyu.iwork.interfaces.RequestCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class NetworkManager {
    public static final String TAG = NetworkManager.class.getSimpleName();
    private static NetworkManager sNetworkManager;
    private Context mContext;

    /**
     * 网络请求容器类
     */
    private RequestQueue mRequestQueue;
    /**
     * 图片加载类
     */
    private ImageLoader mImageLoader;

    private NetworkManager(){}

    private NetworkManager(Context context) {
        mContext = context.getApplicationContext();
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (sNetworkManager == null) {
            sNetworkManager = new NetworkManager(context);
        }
        return sNetworkManager;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public <T> void addRequest(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(getRequestQueue(), new LruBitmapCache(mContext));
        }
        return mImageLoader;
    }

    //------------------------Volley 网络请求框架本地封装-----------------------------------\\

    /**
     * 获取网络请求的异常信息
     * @return
     */
    private static String getErrorInfo(VolleyError error) {
        String errorInfo = error.toString();
        if (error instanceof NetworkError) {
            errorInfo = "网络错误，请检查网络设置!";
        } else if (error instanceof ServerError) {
            errorInfo = "服务无响应!";
        } else if (error instanceof AuthFailureError) {
        } else if (error instanceof TimeoutError) {
            errorInfo = "连接超时!";
        }
        return errorInfo;
    }

    public static String mergeParams(String url, JSONObject params) {
        if (params == null) return url;
        Iterator<String> paramNames = params.keys();
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        while (paramNames.hasNext()) {
            final String key = paramNames.next();
            final Object value = params.optString(key);
            sb.append(key).append("=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        String paramUrl = sb.toString();
        Logger.e(TAG, "mergedUrl=>" + paramUrl);
        return paramUrl;
    }

    public static String mergeParams(String url, Map<String,String> params) {
        if (params == null) return url;
        Iterator<Map.Entry<String, String>> paramEntries = params.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        while (paramEntries.hasNext()) {
            final Map.Entry<String,String> paramEntry = paramEntries.next();
            final String key = paramEntry.getKey();
            final String value = paramEntry.getValue();
            sb.append(key).append("=").append(value).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        String paramUrl = sb.toString();
        Logger.e(TAG, "mergedUrl=>" + paramUrl);
        return paramUrl;
    }


    public void jsonGetRequest(String url, Map<String, String> params, Object tag,
                               final RequestCallback callback) {
        jsonGetRequest(mergeParams(url, params), null, null, tag, callback);
    }

    public void jsonPostRequest(String url, Map<String, String> params, Object tag,
                                final RequestCallback callback) {
        jsonPostRequest(url, params, null, tag, callback);
    }

    public void jsonGetRequest(String url, Map<String, String> params, Map<String, String> headers,
                               Object tag, final RequestCallback callback)
    {
        jsonRequest(Request.Method.GET, mergeParams(url, params), headers, null, tag, callback);
    }

    public void jsonPostRequest(String url, Map<String, String> params, Map<String, String> headers,
                                Object tag, final RequestCallback callback)
    {
        jsonRequest(Request.Method.POST, url, params, headers, tag, callback);
    }

    public void jsonRequest(int method, String url, Map<String, String> params, final Object tag,
                            final RequestCallback callback)
    {
        jsonRequest(method, url, params, null, tag, callback);
    }

    public void jsonRequest(int method, String url, Map<String, String> params,
                            Map<String, String> headers, final Object tag,
                            final RequestCallback callback)
    {
        JSONObjectRequest request = new JSONObjectRequest(method, url, params, headers,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onErrorResponse(RequestCallback.ERROR_RESPONSE_LOCAL, getErrorInfo(error));
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        addRequest(request);
    }

    public void setNetImage(String url, ImageView view) {
        if (url == null) return;
        if (!url.startsWith("http") && !url.startsWith("https")) {
            if (url.startsWith("/")) {
                url = Constant.URL + url;
            } else {
                url = Constant.URL + "/" + url;
            }
        }
        getImageLoader().get(url,
                ImageLoader.getImageListener(view, 0, 0))
                .getBitmap();
    }

    /**
     * 取消所有标记过的请求
     *
     * @param tag 请求标记
     */
    public void cancelRequest(Object tag) {
        getRequestQueue().cancelAll(tag);
    }

}
