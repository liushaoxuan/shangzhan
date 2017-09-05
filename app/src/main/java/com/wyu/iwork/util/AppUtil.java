package com.wyu.iwork.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.wyu.iwork.model.Contact;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class AppUtil {
    private static final String TAG = "AppUtil";

    public static String getVersionName(Context ctxt) {
        String versionName;
        PackageManager pm = ctxt.getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(ctxt.getPackageName(), 0);
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "";
        }
        return versionName;
    }

    public static String getCurProcessName(Context ctxt) {
        final int pid = android.os.Process.myPid();
        android.app.ActivityManager activityManager = (android.app.ActivityManager) ctxt
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static Map<String, Integer> getAlphaMap(List<Contact> contacts) {
        Map<String, Integer> alphaMap = new LinkedHashMap<>();
        for (int i = 0; i < contacts.size(); i++) {
            final String currAlpha = contacts.get(i).getAlpha();
            final String beforeAlpha = i != 0 ? contacts.get(i - 1).getAlpha() : null;
            if (!TextUtils.equals(currAlpha, beforeAlpha)) {
                alphaMap.put(currAlpha, i);
            }
        }
        Logger.e(TAG,"getAlphaMap===>"+alphaMap.toString());
        return alphaMap;
    }


}
