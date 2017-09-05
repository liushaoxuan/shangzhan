package com.wyu.iwork.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ScheduleListAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Schedule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonScheduleFragment extends BaseFragment {

    private static final String TAG = PersonScheduleFragment.class.getSimpleName();
    private Gson gson;
    private Schedule schedule;

    @BindView(R.id.schedule_person_recycleview)
    RecyclerView recycleview;

    @BindView(R.id.schedule_notavaliable)
    AutoLinearLayout notavaliable;

    public PersonScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_schedule, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataFromServer();
    }

    private void getDataFromServer(){
        /**
         * user_id	是	int[18]用户ID
         type	是	int[18]日程类型 1：工作事务 2：个人事务
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.type)
         */
        String type = "2";
        String F = Constant.F;
        String V = Constant.V;
        String RadnStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RadnStr+ MyApplication.userInfo.getUser_id()+type);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("type",type);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RadnStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_SCHEDULE,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseData(s);
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void parseData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            schedule = gson.fromJson(data,Schedule.class);
            if(schedule.getCode() == 0){
                //请求成功
                if(schedule.getData() != null){
                    if(schedule.getData().size()>0){
                        showContent(true,recycleview,notavaliable);
                        setAdapter(schedule.getData());
                    }else{
                        showContent(false,recycleview,notavaliable);
                    }
                }else{
                    showContent(false,recycleview,notavaliable);
                }
            }else{
                MsgUtil.shortToastInCenter(getActivity(),schedule.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapter(ArrayList<Schedule.ScheduleBean> list){
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recycleview.setAdapter(new ScheduleListAdapter(getActivity(),list,2));
    }

    public void showContentforFragment(boolean flag){
        showContent(flag,recycleview,notavaliable);
    }
}
