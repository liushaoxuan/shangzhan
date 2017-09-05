package com.wyu.iwork.net;

import com.wyu.iwork.util.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * Created by jhj_Plus on 2016/11/2.
 */
public class JsonUtil {
    private static final String TAG = "JSONUtil";

    public static JSONObject get(String json) {
        try {
            return new JSONObject(json);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return null;
    }

    public static Object get(JSONObject jsonObject, String name) {
        try {
            return jsonObject.get(name);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return null;
    }

    public static void put(JSONObject jsonObject, String name, Object value) {
        try {
            jsonObject.put(name, value);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONObject(name);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return null;
    }

    public static JSONArray getJSONOArray(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getJSONArray(name);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return null;
    }

    public static JSONObject put(Map<String, Object> keyValueMap) {
        if (keyValueMap == null) return null;
        JSONObject jsonObject = new JSONObject();
        try {
            Set<Map.Entry<String, Object>> entries = keyValueMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return jsonObject;
    }

    public static String getString(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getString(name);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return "";
    }

    public static int getInt(JSONObject jsonObject, String name) {
        try {
            return jsonObject.getInt(name);
        } catch (Exception e) {
            Logger.e(TAG, "JSON Exception=>" + e.getMessage());
        }
        return -1;
    }
}
