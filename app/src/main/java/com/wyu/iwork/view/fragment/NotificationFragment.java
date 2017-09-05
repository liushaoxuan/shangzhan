package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.NotificationAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Notification;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;

import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class NotificationFragment extends BaseFragment {
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;
    private int page = 1;
    private View mRootView;
    private Gson gson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_notification, container, false);
        return content;
    }


    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
        initData();
        this.mRootView = rootView;
        /**
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mAdapter = new NotificationAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);*/
    }

    public void initData(){
        /**
         * page F V
         */
        HashMap<String,String> data = new HashMap<>();
        String F = Constant.F;
        String V = Constant.V;
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        Logger.i(TAG,RandStr);
        String Sign = Md5Util.getSign(F+V+RandStr+page);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SYSTEM_NOTICE, data);
        OkGo.get(murl).tag(this)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseData(s);
                    }
                });
    }

    public void parseData(String s){
        try {
            if(gson == null){
                gson = new Gson();
            }
            Notification notification = gson.fromJson(s,Notification.class);
            if(notification.getCode() == 0){
                if(notification.getData().getList().size()>0){
                    mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
                    mAdapter = new NotificationAdapter(getActivity(),notification.getData().getList());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }else{
                MsgUtil.shortToastInCenter(getActivity(),notification.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.close, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
