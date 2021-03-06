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
import com.wyu.iwork.adapter.MeetingAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MeetingListMoudle;
import com.wyu.iwork.model.UserInfo;
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
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * Created by lx on 2017/8/21.
 */

public class SendMeetingFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = SendMeetingFragment.class.getSimpleName();

    @BindView(R.id.daily_receive_notavaliable)
    AutoLinearLayout notavaliable;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavaliable;

    @BindView(R.id.daily_receive_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.daily_receive_recycleview)
    RecyclerView recycleView;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;
    private int page = 1;
    private ArrayList<MeetingListMoudle.Meet> meetList = new ArrayList<>();
    private Gson gson;
    private MeetingListMoudle moudle;
    private MeetingAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_receive_daily_report,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView(){
        tv_notavaliable.setText("暂无会议通知");
        initRefreshLayout();
        initData();
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

    }

    public void initData(){
        page = 1;
        meetList.clear();
        getSendData();
    }

    private void getSendData(){
        /**
         *  user_id	        是	    int[11]             用户ID
            F	              是	    string[18]          请求来源：IOS/ANDROID/WEB
            V	              是	    string[20]          版本号如：1.0.1
            RandStr	        是	    string[50]          请求加密随机数 time().|.rand()
            Sign	              是	    string[400]         请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        UserInfo info = AppManager.getInstance(getActivity()).getUserInfo();
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+info.getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",info.getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("page",page+"");
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_MEETING_MYSEND_LIST,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        endRefresh();
                        parseMeetingData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        endRefresh();
                        showContent(false,recycleView,notavaliable);
                    }
                });
    }

    private void parseMeetingData(String data){
        if(gson == null){
            gson = new Gson();
        }
        try {
            moudle = gson.fromJson(data,MeetingListMoudle.class);
            if("0".equals(moudle.getCode())){
                if(meetList.size()>0){
                    //不是首次加载
                    if(moudle.getData() != null && moudle.getData().size()>0){
                        page++;
                        showContent(true,recycleView,notavaliable);
                        meetList.addAll(moudle.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(getActivity(),"已加载全部会议通知!");
                    }
                }else{
                    //首次加载
                    if(moudle.getData() != null && moudle.getData().size()>0){
                        page++;
                        showContent(true,recycleView,notavaliable);
                        meetList.addAll(moudle.getData());
                        setAdapter();
                    }else{
                        showContent(false,recycleView,notavaliable);
                    }
                }
            }else{
                showContent(false,recycleView,notavaliable);
                MsgUtil.shortToastInCenter(getActivity(),moudle.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false,recycleView,notavaliable);
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

    private void setAdapter(){
        if(adapter == null){
            adapter = new MeetingAdapter(getActivity(),meetList);
            recycleView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            recycleView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isRefreshing = true;
        initData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getSendData();
        return false;
    }
}
