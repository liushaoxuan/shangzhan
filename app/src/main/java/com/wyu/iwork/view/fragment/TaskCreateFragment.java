package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.TaskAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Task;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * Created by lx on 2017/7/24.
 * 我创建的任务
 */

public class TaskCreateFragment extends BaseFragment {

    private static final String TAG = TaskCreateFragment.class.getSimpleName();

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavailable;

    @BindView(R.id.notavaliable)
    AutoLinearLayout notavaliable;

    private TaskAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_task,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        notavaliable.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView(){
        getCreateTask();
        tv_notavailable.setText("暂无任务通知");
    }

    private void getCreateTask(){
        /**
         * user_id	是	int[11]       用户ID
         company_id	是	int[18]       公司ID
         status	      是	int[1]        任务列表状态（1:我创建的；2：待执行；3：已完成）
         F	            是	string[18]    请求来源：IOS/ANDROID/WEB
         V	            是	string[20]    版本号如：1.0.1
         RandStr	      是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.company_id
         */
        UserInfo info = MyApplication.userInfo;
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", info.getUser_id());
        data.put("company_id",info.getCompany_id());
        data.put("status","1");
        data.put("F",F);
        data.put("V",V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+info.getUser_id()+info.getCompany_id()+"1");
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_TASK_V2,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseTaskData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        showContent(false);
                    }
                });
    }

    private void parseTaskData(String data){
        Gson gson =  new Gson();
        try {
            Task task = gson.fromJson(data,Task.class);
            if("0".equals(task.getCode())){
                if(task.getData() != null && task.getData().size()>0){
                    showContent(true);
                    setAdapter(task);
                }else{
                    showContent(false);
                }
            }else{
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void setAdapter(Task task){
        adapter = new TaskAdapter(getActivity(),task.getData());
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false));
        recycleview.setAdapter(adapter);
    }

    private void showContent(boolean flag){
        notavaliable.setVisibility(flag?View.GONE:View.VISIBLE);
        recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
    }
}
