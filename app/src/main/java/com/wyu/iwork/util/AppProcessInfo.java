package com.wyu.iwork.util;

import android.content.Context;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.util.Log;

import java.util.List;

/**
 * 作者： sxliu on 2017/6/29.08:59
 * 邮箱：2587294424@qq.com
 */

public class AppProcessInfo {

    public static boolean isBackground(Context context) {

       android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }else{
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


}
