package com.wyu.iwork.net;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.wyu.iwork.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/11/8.
 */
public class JSONObjectRequest extends Request<JSONObject> {
    private static final String TAG = "JSONObjectRequest";
    private Map<String,String> mHeaders;
    private Map<String, String> mParams;
    private Response.Listener<JSONObject> mListener;

    public JSONObjectRequest(String url, Map<String, String> params,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener)
    {
        this(url, params, null, listener, errorListener);
    }

    public JSONObjectRequest(String url, Map<String, String> params, Map<String, String> headers,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener)
    {
        this(Method.GET, url, params, headers, listener, errorListener);
    }

    public JSONObjectRequest(int method, String url, Map<String, String> params,
                             Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener)
    {
        this(method, url, params, null, listener, errorListener);
    }

    public JSONObjectRequest(int method, String url, Map<String, String> params,
                             Map<String, String> headers, Response.Listener<JSONObject> listener,
                             Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        Logger.i(TAG, "url====>" + url);
        mHeaders = headers;
        mParams = params;
        mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Logger.e(TAG, "getHeaders====>" + mHeaders);
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Logger.e(TAG, "getParams====>" + mParams);
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String json = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers, "UTF-8"));
            JSONObject jsonObject = new JSONObject(json);
            return Response.success(jsonObject,
                    HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException | JSONException e) {
            return Response.error(new ParseError(networkResponse));
        }
    }

    @Override
    protected void deliverResponse(JSONObject jsonObject) {
        mListener.onResponse(jsonObject);
    }
}
