package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.TaskerPersonListAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.DetailTaskModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.TaskItem;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * @author juxinhua
 * 任务详情
 */
public class DetailsTaskActivity extends BaseActivity {

    private static final String TAG =  DetailsTaskActivity.class.getSimpleName();
    private String task_id;

    @BindView(R.id.task_theme)
    TextView task_theme;

    @BindView(R.id.task_desc)
    TextView task_desc;

    @BindView(R.id.emergency_degree)
    TaskItem emergency_degree;

    @BindView(R.id.start_time)
    TaskItem start_time;

    @BindView(R.id.end_time)
    TaskItem end_time;

    //负责人
    @BindView(R.id.rv_add_person)
    RecyclerView rv_add_person;

    //参与人
    @BindView(R.id.rv_add_pic)
    RecyclerView rv_add_pic;

    @BindView(R.id.reedit_task)
    TextView reedit_task;

    @BindView(R.id.cancel_task)
    TextView cancel_task;

    @BindView(R.id.finish_task)
    TextView finish_task;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.creater)
    TextView creater;

    @BindView(R.id.ll_operation)
    AutoLinearLayout ll_operation;

    private Gson gson;
    private TaskerPersonListAdapter mJoinerAdapter;
    private LinearLayoutManager joinerManager;
    private LinearLayoutManager headerManager;
    private TaskerPersonListAdapter mHeaderAdapter;
    private DetailTaskModel model;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_task);
        ButterKnife.bind(this);
        hideToolbar();
        getExtras();
        initView();
    }

    private void initView(){
        tv_title.setText("任务详情");
        initItem();
    }

    private void initItem(){
        /**
         *
        TaskItem emergency_degree;
         TaskItem start_time;
         TaskItem end_time;
         TaskItem creater;
         */
        emergency_degree.setTitle("紧急程度");
        start_time.setTitle("开始时间");
        end_time.setTitle("结束时间");

        start_time.setTopLineVisible(false);
        end_time.setTopLineVisible(false);

        emergency_degree.setHintValue("未填写");
        start_time.setHintValue("未填写");
        end_time.setHintValue("未填写");
    }

    private void getExtras(){
        task_id = getIntent().getStringExtra("task_id");
        getTaskDetailsRequest();
    }

    private void getTaskDetailsRequest(){
        /**
         * task_id	是	int[11]任务ID
         user_id	是	int[11]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.task_id.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+task_id+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("task_id",task_id);
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_TASKINFO_V2,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this)
                .execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.e(TAG,s);
                parseData(s);
            }
            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);
            }
        });

    }

    private void parseData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }

            model = gson.fromJson(data,DetailTaskModel.class);
            if("0".equals(model.getCode())){
                checkStr(model.getData().getTitle(),task_theme);
                checkStr(model.getData().getIntro(),task_desc);
                if(!TextUtils.isEmpty(model.getData().getLevel())){
                    setEmergency(model.getData().getLevel());
                }
                checkStr(splitTime(model.getData().getBegin_time()),start_time.getTextView());
                checkStr(splitTime(model.getData().getEnd_time()),end_time.getTextView());
                checkStr(model.getData().getCreater(),creater);

                if(model.getData().getJoiner() != null && model.getData().getJoiner().size()>0){
                    mJoinerAdapter = new TaskerPersonListAdapter(this, model.getData().getJoiner());
                    joinerManager = new LinearLayoutManager(this);
                    joinerManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rv_add_pic.setLayoutManager(joinerManager);
                    rv_add_pic.setAdapter(mJoinerAdapter);
                }
                if(model.getData().getHeader() != null && model.getData().getHeader().size()>0){
                    Logger.i(TAG, model.getData().toString());
                    mHeaderAdapter = new TaskerPersonListAdapter(this, model.getData().getHeader());
                    headerManager = new LinearLayoutManager(this);
                    headerManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rv_add_person.setLayoutManager(headerManager);
                    rv_add_person.setAdapter(mHeaderAdapter);
                }
                //设置操作
                setOperation(model.getData().getRole(), model.getData().getStatus());
            }else{
                MsgUtil.shortToastInCenter(this, model.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setOperation(String role,String status){
        /**
         * [role] => 角色名，用“|”隔开，如：|creater|header|joiner|，分别为创建人、责任人、参与人。
         [status] => 任务状态（2：执行中、100：已完成、3：已取消、4：已过期。）

         1:既是创建人也是责任人的情况下   执行中任务：可以取消任务  完成任务   已取消任务:编辑任务
         2：只是创建人: 执行中任务 ：可以取消任务  已取消任务:编辑任务
         3：只是责任人：执行中任务：完成任务
         */
        ll_operation.setVisibility(View.GONE);
        reedit_task.setVisibility(View.GONE);
        if(role.contains("creater") && role.contains("header")){
            //情况1
            if("2".equals(status) || "1".equals(status)){
                ll_operation.setVisibility(View.VISIBLE);
                reedit_task.setVisibility(View.GONE);
            }else if("3".equals(status)){
                ll_operation.setVisibility(View.GONE);
                reedit_task.setVisibility(View.VISIBLE);
                reedit_task.setText("重新编辑");
            }
        }else if(role.contains("creater")){
            //情况2
            if("2".equals(status)|| "1".equals(status)){
                ll_operation.setVisibility(View.GONE);
                reedit_task.setVisibility(View.VISIBLE);
                reedit_task.setText("取消任务");
            }else if("3".equals(status)){
                ll_operation.setVisibility(View.GONE);
                reedit_task.setVisibility(View.VISIBLE);
                reedit_task.setText("重新编辑");
            }
        }else if(role.contains("header")){
            if("2".equals(status)|| "1".equals(status)){
                ll_operation.setVisibility(View.GONE);
                reedit_task.setVisibility(View.VISIBLE);
                reedit_task.setText("完成");
            }
        }
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    private String splitTime(String data){
        /**
         * 2017-01-04 16:10:00
         */
        String[] time = data.split(" ");
        String[] monthTime = time[0].split("-");
        String[] minutesTime = time[1].split(":");
        return monthTime[1]+"月"+monthTime[2]+"日 "+minutesTime[0]+":"+minutesTime[1];
    }

    private void setEmergency(String level){
        /**
         * 1.普通 2.紧急 3.非常紧急
         */
        if(level.equals("1")){
            emergency_degree.setValue("普通");
        }else if(level.equals("2")){
            emergency_degree.setValue("紧急");
        }else if(level.equals("3")){
            emergency_degree.setValue("非常紧急");
        }
    }

    @OnClick({R.id.ll_back/**, iv_task_builder*/,R.id.reedit_task,R.id.cancel_task,R.id.finish_task})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;

            case R.id.reedit_task:
                selectOperation();
                break;
            case R.id.cancel_task:
                showCancelDialog();
                break;
            case R.id.finish_task:
                showFinishDialog();
                break;
/**
            case iv_task_builder://呼叫、会话、名片
                if (detailTaskModel!=null){
                    new MyAlertDialog(this,detailTaskModel.getData().getCreater_id(),detailTaskModel.getData().getPhone(),detailTaskModel.getData().getCreater()).show();
                }
                break;*/
        }
    }

    private void selectOperation(){
        String role = model.getData().getRole();
        String status = model.getData().getStatus();
        if(role.contains("creater") && role.contains("header")){
            //情况1
            if("3".equals(status)){
                //重新编辑
                Logger.i(TAG,"重新编辑");
                jumpToEdit();
            }
        }else if(role.contains("creater")){
            //情况2
            if("2".equals(status)|| "1".equals(status)){
                showCancelDialog();
            }else if("3".equals(status)){
                //重新编辑
                Logger.i(TAG,"重新编辑");
                jumpToEdit();
            }
        }else if(role.contains("header")){
            if("2".equals(status)|| "1".equals(status)){
                showFinishDialog();
            }
        }
    }

    private void jumpToEdit(){
        Intent intent = new Intent(this,CreateNewTaskActivity.class);
        intent.putExtra("task_id",task_id);
        startActivity(intent);
        onBackPressed();
    }

    /**
     *
     */
    //取消任务提醒弹窗
    private void showCancelDialog(){
        new MyCustomDialogDialog(6, this, R.style.MyDialog, "确定要取消该任务吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                cancelTask();
                dialog.dismiss();
            }
        }).show();
    }

    //完成任务提醒弹窗
    private void showFinishDialog(){
        new MyCustomDialogDialog(6, this, R.style.MyDialog, "确定已完成该任务吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                finishTask();
                dialog.dismiss();
            }
        }).show();
    }


    private void cancelTask(){
        /**
         * URL_TASK_CANCEL
         * task_id	是	int[11]       任务ID
         F	            是	string[18]    请求来源：IOS/ANDROID/WEB
         V	            是	string[20]    版本号如：1.0.1
         RandStr	      是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.task_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("task_id",task_id);
        data.put("F",F);
        data.put("V",V);
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+task_id);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_TASK_CANCEL,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                //重新刷新界面
                                getTaskDetailsRequest();
                            }else{
                                MsgUtil.shortToastInCenter(DetailsTaskActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    private void finishTask(){
        /**
         * user_id	是	int[18]           当前用户ID,负责人才可以完成任务
         task_id	      是	int[11]           任务ID
         F	            是	string[18]        请求来源：IOS/ANDROID/WEB
         V	            是	string[20]        版本号如：1.0.1
         RandStr	      是	string[50]        请求加密随机数 time().|.rand()
         Sign	      是	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.user_id.task_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("task_id",task_id);
        data.put("F",F);
        data.put("V",V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+task_id);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_TASKFINSH,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                //重新刷新界面
                                getTaskDetailsRequest();
                            }else{
                                MsgUtil.shortToastInCenter(DetailsTaskActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }
}
