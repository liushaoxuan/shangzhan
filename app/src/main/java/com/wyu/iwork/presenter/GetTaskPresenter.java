package com.wyu.iwork.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wyu.iwork.model.TaskModel;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.List;
import java.util.Map;

/**
 * Created by lx on 2017/1/5.
 */

public class GetTaskPresenter extends NetPresenter<List<TaskModel>> {
    private static final String TAG = GetTaskPresenter.class.getSimpleName();
    public GetTaskPresenter(Context ctxt) {
        super(ctxt,new TypeToken<List<TaskModel>>(){}.getType());
    }
    public void doSignIn(String user_id,String company_id) {
        Map<String, String> params = new NetParams.Builder()
                .buildParams("user_id", user_id)
                .buildParams("company_id", company_id)
                .build();
        doGetRequest(true, Constant.URL_GET_TASK, params,null,TAG);
    }
}
