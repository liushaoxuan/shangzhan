package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AdvertiseAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.AdvertiseModel;
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
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * 广告联盟列表页
 */
public class AdvertiseActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = AdvertiseActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_edit)
    TextView edit;

    @BindView(R.id.refreshlayout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.advertise_recycleview)
    RecyclerView recycleview;

    //暂无广告任务
    @BindView(R.id.not_available)
    LinearLayout not_available;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavailable;

    private int page = 1;
    private boolean isRefreshing = false;
    private boolean isLoadingMore = false;
    private Gson gson;

    private ArrayList<AdvertiseModel.Advertise> list = new ArrayList<>();
    private AdvertiseAdapter adapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);
        hideToolbar();
        ButterKnife.bind(this);
        initDelegate();
        initView();
    }

    private void initDelegate(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    public void initView(){
        title.setText("广告联盟");
        edit.setText("我的任务");
        edit.setVisibility(View.VISIBLE);
        tv_notavailable.setText("暂无广告任务");
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getAdvTaskList();
    }

    @OnClick({R.id.ll_back,R.id.tv_edit})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                startActivity(new Intent(this,MineAdvertisingTaskActivity.class));
                break;
        }

    }

    private void getAdvTaskList(){
        /**
         * page	      是	int[11]           页面
         F	            是	string[18]        请求来源：IOS/ANDROID/WEB
         V	            是	string[20]        版本号如：1.0.1
         RandStr	      是	string[50]        请求加密随机数 time().|.rand()
         Sign	      是	string[400]       请求加密值 F_moffice_encode(F.V.RandStr)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr);
        HashMap<String,String> data = new HashMap<>();
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_ADTASK_LIST,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this)
                .cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s, call, response);
                Logger.i(TAG,s);
                if(page == 1){
                    list.clear();
                }
                parseAdvertise(s);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                endRefreshing();
            }
        });
    }

    private void endRefreshing(){
        if(isRefreshing){
            isRefreshing = false;
            mRefreshLayout.endRefreshing();
            Logger.i(TAG,"下拉刷新");
        }else if(isLoadingMore){
            isLoadingMore = false;
            mRefreshLayout.endLoadingMore();
            Logger.i(TAG,"加载更多");
        }
    }

    private void parseAdvertise(String data){
        endRefreshing();
        if(gson == null){
            gson = new Gson();
        }
        try {
            AdvertiseModel advertiseModel = gson.fromJson(data,AdvertiseModel.class);
            if("0".equals(advertiseModel.getCode())){
                if(list.size()>0){
                    if(advertiseModel.getData() != null && advertiseModel.getData().size()>0){
                        page++;
                        showContent(true,recycleview,not_available);
                        list.addAll(advertiseModel.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToast(this,"已加载全部广告数据!");
                    }
                }else{
                    if(advertiseModel.getData() != null && advertiseModel.getData().size()>0){
                        page++;
                        showContent(true,recycleview,not_available);
                        list.addAll(advertiseModel.getData());
                        setAdapter();
                    }else{
                        showContent(false,recycleview,not_available);
                    }
                }
            }else{
                MsgUtil.shortToast(this,advertiseModel.getMsg());
                showContent(false,recycleview,not_available);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false,recycleview,not_available);
        }
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new AdvertiseAdapter(this,list,1);
            recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        getAdvTaskList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getAdvTaskList();
        return true;
    }
}
