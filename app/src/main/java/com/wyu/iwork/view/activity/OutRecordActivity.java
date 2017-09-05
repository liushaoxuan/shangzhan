package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.RecordAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.OutSignRecord;
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
 * 外出考勤记录
 */
public class OutRecordActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = OutRecordActivity.class.getSimpleName();

    @BindView(R.id.record_recycleView)
    RecyclerView record_recycleView;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.record_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.record_notavaliable)
    AutoLinearLayout notavaliable;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavailable;

    private int page = 1;//页码

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;
    private ArrayList<OutSignRecord.Record> recordList = new ArrayList<>();
    private Gson gson;
    private LinearLayoutManager linearLayoutManager;
    private RecordAdapter adapter;


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_record);
        ButterKnife.bind(this);
        hideToolbar();
        initView();
        initRefreshLayout();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getRecordList();
    }

    private void initView(){
        title.setText("外出考勤记录");
        tv_notavailable.setText("暂无外出考勤记录");
    }

    @OnClick({R.id.ll_back})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    private void getRecordList(){
        /**
         * user_id	    是   	int[180]        用户ID
         page	          是   	int[180]        分页页码,递增
         F	                是   	string[18]      请求来源：IOS/ANDROID/WEB
         V	                是   	string[20]      版本号如：1.0.1
         RandStr	          是   	string[50]      请求加密随机数 time().|.rand()
         Sign	          是   	string[400]     请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_OUT_SING_LIST,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s, call, response);
                Logger.i(TAG,s);
                if(page == 1){
                    recordList.clear();
                }
                parseData(s);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                endRefresh();
            }
        });
    }

    private void endRefresh(){
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

    private void parseData(String data){
        endRefresh();
        try {
            if(gson == null){
                gson = new Gson();
            }
            OutSignRecord record = gson.fromJson(data,OutSignRecord.class);
            if("0".equals(record.getCode())){
                if(recordList.size()>0){
                    if(record.getData() != null && record.getData().size()>0){
                        page++;
                        showContent(true,record_recycleView,notavaliable);
                        recordList.addAll(record.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已加载全部外出考勤记录!");
                    }
                }else{
                    if(record.getData() != null && record.getData().size()>0){
                        page++;
                        showContent(true,record_recycleView,notavaliable);
                        recordList.addAll(record.getData());
                        setAdapter();
                    }else{
                        showContent(false,record_recycleView,notavaliable);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,record.getMsg());
                showContent(false,record_recycleView,notavaliable);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false,record_recycleView,notavaliable);
        }
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new RecordAdapter(this,recordList);
            linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            record_recycleView.setLayoutManager(linearLayoutManager);
            record_recycleView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        getRecordList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getRecordList();
        return true;
    }
}
