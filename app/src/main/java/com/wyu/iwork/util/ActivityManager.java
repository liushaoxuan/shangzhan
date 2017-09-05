package com.wyu.iwork.util;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Created by sxliu on 2017/1/3.
 * Activity管理类
 */

public class ActivityManager {
    private static Stack<Activity> activityStack;
    private static ActivityManager instance;
    static {
        instance = new ActivityManager();
    }
    //获取instance实例
    public static ActivityManager getInstance(){
        return instance;
    }

    /**
     * 添加指定Activity到堆栈
     */
    public void addActivity(Activity activity){
        if (activityStack==null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity(){
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity(){
        Activity activity = activityStack.lastElement();
        finishActivity(activity);

    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if (activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }

    }

    /**
     * 结束全部Activity
     */
    public void finishAllActivity(){
        if(activityStack!=null){

            for (int i = 0;i<activityStack.size();i++){
                if (null!=activityStack.get(i)){
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 退出应用
     */
    public void appExit(Context context){
        try {
            finishActivity();
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
