package com.wyu.iwork.view.activity;

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
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.MineAdvertiseAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MineAdvertiseModel;
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
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * 广告联盟 -- 我的任务
 */
public class MineAdvertisingTaskActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = MineAdvertisingTaskActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.bgarefresh_layout)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.mine_adver_recycleview)
    RecyclerView recycleview;

    @BindView(R.id.not_avaliable)
    LinearLayout not_avaliable;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavailable;

    private int page = 1;
    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;
    private Gson gson;
    private ArrayList<MineAdvertiseModel.AdvertiseMessage.AdvertiseTask> list = new ArrayList<>();
    private MineAdvertiseAdapter adapter;
    private MineAdvertiseModel.AdvertiseMessage.Mall mall;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_advertising_task);
        hideToolbar();
        ButterKnife.bind(this);
        initView();
        initDelegate();
    }

    private void initDelegate(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initView(){
        title.setText("我的任务");
        tv_notavailable.setText("您暂无领取的广告任务");
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getMineTaskData();
    }

    @OnClick({R.id.ll_back})
    void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    private void getMineTaskData(){
        /**
         * user_id	    是       	int[11]           用户ID
         page	          否       	int[11]           页码
         F	                是       	string[18]        请求来源：IOS/ANDROID/WEB
         V	                是       	string[20]        版本号如：1.0.1
         RandStr	          是       	string[50]        请求加密随机数 time().|.rand()
         Sign	          是       	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        UserInfo userInfo = AppManager.getInstance(this).getUserInfo();
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+userInfo.getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",userInfo.getUser_id());
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_MY_TASK_LIST,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        if(page == 1){
                            list.clear();
                        }
                        parseData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        endRefreshing();
                    }
                });
    }

    private void parseData(String data){
        endRefreshing();
        if(gson == null){
            gson = new Gson();
        }
        try {
            MineAdvertiseModel model = gson.fromJson(data,MineAdvertiseModel.class);
            if("0".equals(model.getCode())){
                if(list.size()>0){
                    //设置list
                    if(model.getData() != null &&model.getData().getTask_list() != null && model.getData().getTask_list().size()>0){
                        page++;
                        list.addAll(model.getData().getTask_list());
//                        setAdapter(model.getData().getMall());
                    }else{
                        MsgUtil.shortToast(this,"已加载全部广告数据!");
                    }
                }else{
                    if(model.getData() != null && model.getData().getTask_list() != null && model.getData().getTask_list().size()>0){
                        page++;
                        list.addAll(model.getData().getTask_list());
//                        setAdapter(model.getData().getMall());
                    }
                }
                if(model.getData() != null && model.getData().getMall() != null){
                    mall = model.getData().getMall();
                    setAdapter(mall);
                }

            }else{
                MsgUtil.shortToast(this,model.getMsg());
                showContent(false,recycleview,not_avaliable);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false,recycleview,not_avaliable);
        }
    }

    private void setAdapter(MineAdvertiseModel.AdvertiseMessage.Mall mall){
        if(adapter == null){
            adapter = new MineAdvertiseAdapter(this,mall,list);
            recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isRefreshing = true;
        page = 1;
        getMineTaskData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getMineTaskData();
        return false;
    }
}
