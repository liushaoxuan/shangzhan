package com.wyu.iwork.reveiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.AssistantModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.util.AppProcessInfo;
import com.wyu.iwork.util.Exit_app;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.CheckWorkAttendanceActivity;
import com.wyu.iwork.view.activity.DetailsTaskActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.MettingDetailActivity;
import com.wyu.iwork.view.activity.SigninActivity;
import com.wyu.iwork.view.activity.WebActivity;
import com.wyu.iwork.view.activity.oaApplyDetailActivity;
import com.wyu.iwork.view.activity.oaApplyForLeaveDetailActivity;
import com.wyu.iwork.view.activity.oaBusinessTravelDetailActivity;
import com.wyu.iwork.view.activity.oaReimbursementDetailActivity;
import com.wyu.iwork.view.activity.oaWorkOverTimeDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者： sxliu on 2017/6/21.13:55
 * 邮箱：2587294424@qq.com
 * 极光推送receiver
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {
         if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Logger.e(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Logger.e(TAG, "接受到推送下来的自定义消息");
            receivingNotification(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Logger.e(TAG, "接受到推送下来的通知");

            receivingNotification(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Logger.e(TAG, "用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            Logger.e(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Logger.d(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Logger.d(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

        try {
            JSONObject extrasJson = new JSONObject(extras);
            String key = extrasJson.optString("server");

            switch (key){//0：考勤 1：任务 2：会议 3：公告 4：审批  99：其他用户登录
                case "0"://考勤

                    break;

                case "1"://任务
                    AssistantModel model = DataSupport.find(AssistantModel.class,0x00001);
                    model.setNotes(model.getNotes()+1);
                    model.setContent(message==null?"":message);
                    model.setTime(System.currentTimeMillis());
                    model.update(0x00001);
                    break;

                case "2"://会议
                    AssistantModel model2 = DataSupport.find(AssistantModel.class,0x00002);
                    model2.setNotes(model2.getNotes()+1);
                    model2.setTime(System.currentTimeMillis());
                    model2.setContent(message==null?"":message);
                    model2.update(0x00002);
                    break;

                case "3"://公告
                    AssistantModel model3 = DataSupport.find(AssistantModel.class,0x00003);
                    model3.setNotes(model3.getNotes()+1);
                    model3.setContent(message==null?"":message);
                    model3.setTime(System.currentTimeMillis());
                    model3.update(0x00003);
                    break;

                case "4"://审批
                    AssistantModel model4 = DataSupport.find(AssistantModel.class,0x00004);
                    model4.setNotes(model4.getNotes()+1);
                    model4.setContent(message==null?"":message);
                    model4.setTime(System.currentTimeMillis());
                    model4.update(0x00004);
                    break;

                case "99"://其他用户登录
                    DataSupport.deleteAll(UserInfo.class);
                    AppManager.getInstance(context).setUserInfo(null);

                    boolean isalive = AppProcessInfo.isBackground(context);
                    //当前应用处于前台状态
                    if (!isalive) {
                        ActivityManager.getInstance().finishAllActivity();
                        Intent intent = new Intent(context, SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Toast.makeText(context, "您的账号已在其他设备登录", Toast.LENGTH_SHORT).show();
                        Exit_app.Exit(MyApplication.userInfo);
                    }
                    //当前应用处于后台状态
                    else {
                        ActivityManager.getInstance().finishAllActivity();
                    }
                    break;
            }

            Logger.e("JPush_", extrasJson.toString());

        } catch (Exception e) {
            Logger.w(TAG, "Unexpected: extras is not a valid json", e);
            return;
        }
        Logger.d(TAG, "extras : " + extras);
    }

    private void openNotification(Context context, Bundle bundle) {


        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Logger.e(TAG, "extras_ : " + extras);
        String myValue = "";

        try {
            JSONObject extrasJson = new JSONObject(extras);
            String key = extrasJson.optString("server");
            String data = extrasJson.optString("data");
            Logger.e("data",data);
            myValue = extrasJson.optString("myKey");
            Bundle bundle1 = new Bundle();
            Intent intent = null;
            switch (key) { //   0：考勤 1：任务 2：会议 3：公告 4：审批 99：其他用户登录

                case "99"://其他用户登录
                    ActivityManager.getInstance().finishAllActivity();
                    intent = new Intent(context, SigninActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "0"://考勤
                    intent = new Intent(context, CheckWorkAttendanceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case "1"://任务
                    AssistantModel model = DataSupport.find(AssistantModel.class,0x00001);
                    model.setNotes(model.getNotes()-1);
                    model.setTime(System.currentTimeMillis());
                    model.saveOrUpdate("name=?",model.getName());
                    intent = new Intent(context, DetailsTaskActivity.class);
                    JSONObject jsondata = new JSONObject(data);
                    intent.putExtra("task_id",jsondata.optString("task_id"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

                case "2"://会议
                    AssistantModel model2 = DataSupport.find(AssistantModel.class,0x00002);
                    model2.setNotes(model2.getNotes()-1);
                    model2.setTime(System.currentTimeMillis());
                    model2.saveOrUpdate("name=?",model2.getName());
                    intent = new Intent(context, MettingDetailActivity.class);
                    intent.putExtra("metting_id",data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

                case "3"://公告
                    AssistantModel model3 = DataSupport.find(AssistantModel.class,0x00003);
                    model3.setNotes(model3.getNotes()-1);
                    model3.setTime(System.currentTimeMillis());
                    model3.saveOrUpdate("name=?",model3.getName());

                    intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url",data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;

                case "4"://审批
                    AssistantModel model4 = DataSupport.find(AssistantModel.class,0x00004);
                    model4.setNotes(model4.getNotes()-1);
                    model4.setTime(System.currentTimeMillis());
                    model4.saveOrUpdate("name=?",model4.getName());
                    JSONObject dataobject = new JSONObject(data);
                    int apply_type = dataobject.optInt("apply_type");
                    switch (apply_type){
                        case 1://请假
                            intent = new Intent(context, oaApplyForLeaveDetailActivity.class);
                            break;

                        case 2://加班
                            intent = new Intent(context, oaWorkOverTimeDetailActivity.class);
                            break;

                        case 3://出差
                            intent = new Intent(context, oaBusinessTravelDetailActivity.class);
                            break;

                        case 4://报销
                            intent = new Intent(context, oaReimbursementDetailActivity.class);
                            break;
                    }
                    String contact_id = dataobject.optString("contact_id");
                    intent.putExtra("id", contact_id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
