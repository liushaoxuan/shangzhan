package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.TaskArrangeAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.TaskModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.view.dialog.LoadingDialog;
import com.wyu.iwork.widget.MyListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class FinishTaskActivity extends BaseNetActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private static final String TAG = FinishTaskActivity.class.getSimpleName();
    private EditText ed_searchView;
    private TextView tv_searchView;
    private MyListView lv_searchTask;
    private TaskArrangeAdapter taskArrangeAdapter;
    private List<TaskModel> taskList=new ArrayList<>();
    private List<TaskModel> newtaskList=new ArrayList<>();
    private LoadingDialog loading=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_task);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("已完成任务");
        init();
        getCompletedTask();
    }
    private void init() {
        tv_searchView=(TextView)findViewById(R.id.tv_searchView);
        ed_searchView=(EditText) findViewById(R.id.ed_searchView);
        lv_searchTask=(MyListView)findViewById(R.id.lv_searchTask);
        initListener();
    }

    private void initListener() {
        tv_searchView.setOnClickListener(this);
        ed_searchView.addTextChangedListener(this);
        lv_searchTask.setOnItemClickListener(this);
    }



    private void getCompletedTask() {
        String company_id= AppManager.getInstance(this).getUserInfo().getCompany_id();
        loading=new LoadingDialog();
        loading.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_TASKFINSHLIST;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+company_id);
        OkGo.get(url)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .params("company_id",company_id)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",Sign).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                try {
                    JSONObject object = new JSONObject(s);
                    String code=object.optString("code");
                    if("0".equals(code)) {
                        JSONArray array = new JSONArray(object.optString("data"));
                        taskList= JSON.parseArray(array.toString(),TaskModel.class);
                        Collections.reverse(taskList);
                        taskArrangeAdapter=new TaskArrangeAdapter(FinishTaskActivity.this,taskList);
                        lv_searchTask.setAdapter(taskArrangeAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);
                loading.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_searchView:
                ed_searchView.setFocusableInTouchMode(true);
                ed_searchView.requestFocus();
                tv_searchView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(ed_searchView.getText().toString().isEmpty()){
            taskArrangeAdapter=new TaskArrangeAdapter(this,taskList);
            lv_searchTask.setAdapter(taskArrangeAdapter);
            taskArrangeAdapter.notifyDataSetChanged();
        }else{
            newtaskList.clear();
            for(int i=0;i<taskList.size();i++){
                if(-1!=taskList.get(i).getTask().indexOf(ed_searchView.getText().toString())){
                    newtaskList.add(taskList.get(i));
                }
            }
            taskArrangeAdapter=new TaskArrangeAdapter(this,newtaskList);
            lv_searchTask.setAdapter(taskArrangeAdapter);
            taskArrangeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
