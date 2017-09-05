package com.wyu.iwork.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/11/2.
 */
public class Debug {
    private static final String TAG = "Debug";

    public static String printPrettyJson(JSONObject jsonObject) {
        try {
            return jsonObject.toString(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
