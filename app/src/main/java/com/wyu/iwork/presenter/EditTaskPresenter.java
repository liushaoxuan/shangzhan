package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.Map;

/**
 * Created by lx on 2017/1/4.
 */

public class EditTaskPresenter extends NetPresenter {
    private static final String TAG = EditTaskPresenter.class.getSimpleName();
    public EditTaskPresenter(Context ctxt) {
        super(ctxt);
    }
    public void doSignIn(String user_id,String task_superior,String task_id,String title,String header,
                         String joiner,String begin_time,String end_time,String level,String intro) {
        Map<String, String> params = new NetParams.Builder()
                .buildParams("user_id", user_id)
                .buildParams("task_superior", task_superior)
                .buildParams("task_id", task_id)
                .buildParams("title", title)
                .buildParams("header", header)
                .buildParams("joiner", joiner)
                .buildParams("begin_time", begin_time)
                .buildParams("end_time", end_time)
                .buildParams("level", level)
                .buildParams("intro", intro)
                .build();
        Map<String, String> signParams = new NetParams.Builder()
                .buildParams("user_id", user_id)
                .buildParams("task_superior", task_superior)
                .buildParams("task_id", task_id)
                .build();
        doGetRequest(true, Constant.URL_GET_UPDATETASK, signParams,params,TAG);
    }
}
