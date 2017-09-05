package com.wyu.iwork.stor;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wyu.iwork.util.Logger;

/**
 * Created by jhj_Plus on 2016/11/2.
 */
public class Prefs {
    private static final String USER_INFO = "userInfo";

    public static SharedPreferences getDefaultPrefs(Context ctxt) {
        return PreferenceManager.getDefaultSharedPreferences(ctxt);
    }

    public static String getUserInfo(Context ctxt) {
        return getDefaultPrefs(ctxt).getString(USER_INFO, "");
    }

//    public static void setUserInfo(Context ctxt, String userInfo) {
//        getDefaultPrefs(ctxt).edit().putString(USER_INFO, userInfo).apply();
//    }

    /**
     * 删除用户本地存储的信息
     * @param context
     */
    public static void delUserInfo(Context context){
        SharedPreferences preferences = getDefaultPrefs(context);
        preferences.edit().clear().commit();
        String ee = getUserInfo(context);
        Logger.e("USER_INFO------",ee);
    }
}
