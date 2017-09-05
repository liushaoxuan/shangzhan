package com.wyu.iwork.net;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.wyu.iwork.util.Logger;

import java.lang.reflect.Type;

/**
 * Created by jhj_Plus on 2016/11/1.
 */
public class GsonManager {
    private static final String TAG = "GsonManager";
    private static GsonManager sGsonLab;
    private Context mCtxt;
    private Gson mGson;

    private GsonManager(){}

    public GsonManager(Context ctxt) {
        mCtxt = ctxt.getApplicationContext();
        mGson = buildGson();
    }

    public synchronized static GsonManager getInstance(Context ctxt) {
        if (sGsonLab == null) {
            sGsonLab = new GsonManager(ctxt);
        }
        return sGsonLab;
    }

    private Gson buildGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        T result = null;
        try {
            result = mGson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            Logger.e(TAG, "JsonSyntaxException=" + e.getMessage());
        } catch (JsonParseException e) {
            Logger.e(TAG, "JsonParseException=" + e.getMessage());
        }
        return result;
    }

    public <T> T fromJson(String json, Type typeOfT) {
        T result=null;
        try {
            result = mGson.fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            Logger.e(TAG, "JsonSyntaxException=" + e.getMessage());
        } catch (JsonParseException e) {
            Logger.e(TAG, "JsonParseException=" + e.getMessage());
        }
        return result;
    }

    public String toJson(Object src) {
        return mGson.toJson(src);
    }

    public String toJson(Object src, Type typeOfSrc) {
        return mGson.toJson(src, typeOfSrc);
    }
}
