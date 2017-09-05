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
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.DailyReportAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.DailyReport;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import okhttp3.Response;

public class ReceiveDailyReportFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = ReceiveDailyReportFragment.class.getSimpleName();

    private int receivePage = 1;//页码
    private Gson gson;

    private ArrayList<DailyReport.Data> receiveList = new ArrayList<>() ;

    private DailyReportAdapter receiveAdapter;

    @BindView(R.id.daily_receive_recycleview)
    RecyclerView recycleView;

    @BindView(R.id.daily_receive_notavaliable)
    AutoLinearLayout notavaliable;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavaliable;

    @BindView(R.id.daily_receive_refresh)
    BGARefreshLayout mRefreshLayout;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive_daily_report,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView(){
        tv_notavaliable.setText("暂无日报");
        initRefreshLayout();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity(),true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(receiveAdapter != null){
            receiveAdapter = null;
        }
        receivePage = 1;
        getDailyReportFromServer();
    }

    public void getDailyReportFromServer(){
        UserInfo user = MyApplication.userInfo;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        Logger.i(TAG,RandStr);
        HashMap<String,String> data = new HashMap<>();
        String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+receivePage);
        data.put("page",receivePage+"");
        data.put("user_id",user.getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_RECEIVE_DAILY, data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        if(receivePage == 1){
                            receiveList.clear();
                        }
                        parseDailyReport(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        endRefresh();
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
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

    public void parseDailyReport(String data){

        endRefresh();
        try{
            if(gson == null){
                gson = new Gson();
            }
            DailyReport dailyReport = gson.fromJson(data,DailyReport.class);

            if("0".equals(dailyReport.getCode())){
                if(receiveList.size()>0){
                    if(dailyReport.getData() != null && dailyReport.getData().size()>0){
                        receivePage++;
                        showContent(true,recycleView,notavaliable);
                        receiveList.addAll(dailyReport.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(getActivity(),"已加载全部日报数据!");
                    }
                }else{
                    if(dailyReport.getData() != null && dailyReport.getData().size()>0){
                        receivePage++;
                        showContent(true,recycleView,notavaliable);
                        receiveList.addAll(dailyReport.getData());
                        setAdapter();
                    }else{
                        showContent(false,recycleView,notavaliable);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(getActivity(),dailyReport.getMsg());
                showContent(false,recycleView,notavaliable);
            }

        }catch (Exception e){
            e.printStackTrace();
            showContent(false,recycleView,notavaliable);
        }
    }

    private void setAdapter(){
        if(receiveAdapter == null){
            receiveAdapter = new DailyReportAdapter(getActivity(),receiveList,1);
            recycleView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            recycleView.setAdapter(receiveAdapter);
        }else{
            receiveAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        receivePage = 1;
        isRefreshing = true;
        getDailyReportFromServer();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getDailyReportFromServer();
        return true;
    }

}
