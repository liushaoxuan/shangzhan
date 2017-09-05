package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.BusinessOpportunityAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmOpportunity;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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

/**
 * @author  juxinhua
 * crm - 商机
 */
public class CrmBusinessOpportunityActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = CrmBusinessOpportunityActivity.class.getSimpleName();

    @BindView(R.id.opportunity_recycleview)
    RecyclerView opportunity_recycleview;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.crm_opportunity_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.crm_opportunity_zanwu)
    AutoRelativeLayout crm_opportunity_zanwu;

    @BindView(R.id.zanwu_textone)
    TextView zanwu_textone;

    @BindView(R.id.zanwu_textthree)
    TextView zanwu_textthree;

    private int page = 1;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;

    private ArrayList<CrmOpportunity.Opportunity> opportunityList;
    private Gson gson;
    private BusinessOpportunityAdapter adapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_business_opportunity);
        hideToolbar();
        ButterKnife.bind(this);
        initRefreshLayout();
        initView();
        opportunityList = new ArrayList<>();
        showContent(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getOpportunityFromServer();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initView(){
        tv_title.setText("商机");
        iv_add.setVisibility(View.VISIBLE);
        zanwu_textone.setText("您还没有商机，请在右上方“");
        zanwu_textthree.setText("”添加商机");
    }

    @OnClick({R.id.ll_back,R.id.iv_add})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this,CrmOpportunityDetailActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
                break;
        }
    }

    private void getOpportunityFromServer(){
        /**URL_GET_CHANCE_LIST
         *
         * user_id	是	int[11]
         用户ID
         page	是	int[11]
         页码，获取当前页内容，默认传1
         F	是	string[18]
         请求来源：IOS/ANDROID/WEB
         V	是	string[20]
         版本号如：1.0.1
         RandStr	是	string[50]
         请求加密随机数 time().|.rand()
         Sign	是	string[400]
         请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+page);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("page",page+"");
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CHANCE_LIST,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        if(page == 1){
                            opportunityList.clear();
                        }
                        parseData(s);
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    //解析数据
    private void parseData(String data){
        if(isLoadingMore){
            //加载更多
            isLoadingMore = false;
            mRefreshLayout.endLoadingMore();
            Logger.i(TAG,"加载更多");
        }else if(isRefreshing){
            //下拉刷新
            isRefreshing = false;
            mRefreshLayout.endRefreshing();
            Logger.i(TAG,"下拉刷新");
        }
        try {
            if(gson == null){
                gson = new Gson();
            }
            CrmOpportunity crmOpportunity = gson.fromJson(data,CrmOpportunity.class);
            if("0".equals(crmOpportunity.getCode())){
                if(opportunityList.size()>0){
                    if(crmOpportunity.getData() != null && crmOpportunity.getData().size()>0){
                        page++;
                        showContent(true);
                        opportunityList.addAll(crmOpportunity.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已加载全部数据!");
                    }
                }else{
                    if(crmOpportunity.getData() != null && crmOpportunity.getData().size()>0){
                        page++;
                        showContent(true);
                        opportunityList.addAll(crmOpportunity.getData());
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,crmOpportunity.getMsg());
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }

    }

    private void showContent(boolean flag){
        crm_opportunity_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        opportunity_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new BusinessOpportunityAdapter(this,opportunityList);
            opportunity_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            opportunity_recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        getOpportunityFromServer();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getOpportunityFromServer();
        return true;
    }
}
