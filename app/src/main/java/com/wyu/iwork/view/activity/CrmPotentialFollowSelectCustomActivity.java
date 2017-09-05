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
import com.wyu.iwork.adapter.PotentialCustomerSelectAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import okhttp3.Response;

public class CrmPotentialFollowSelectCustomActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = CrmPotentialFollowSelectCustomActivity.class.getSimpleName();
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.select_custom_zanwu)
    AutoRelativeLayout select_custom_zanwu;

    @BindView(R.id.select_custom_recycleview)
    RecyclerView select_custom_recycleview;

    @BindView(R.id.select_custom_refreshlayout)
    BGARefreshLayout select_custom_refreshlayout;

    private int page = 1;//分页加载的页码
    private boolean isLoadingMore = false;//加载更多状态
    private boolean isPullToRefresh = false;//下拉刷新状态
    private ArrayList<CrmCustom.Custom> customList;
    //private LoadingDialog mLoadingDialog;//加载过程中的进度条

    private PotentialCustomerSelectAdapter adapter;
    private Gson gson;
    private CrmCustom.Custom customer;
    private String type;//type 为 0 代表潜在客户   type 为 1 代表正式客户


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_potential_follow_select_custom);
        hideToolbar();
        ButterKnife.bind(this);
        customList = new ArrayList<>();
        //mLoadingDialog = new LoadingDialog();
        initRefreshLayout();//配置RefreshLayout
        getExtras();
    }

    //获取上个界面传递过来的数据
    private void getExtras(){
        customer = (CrmCustom.Custom) getIntent().getSerializableExtra("custom");
        type = getIntent().getStringExtra("type");
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        customList.clear();
        getDataFromServer();
    }

    private void initRefreshLayout(){
        select_custom_refreshlayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        select_custom_refreshlayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initView(){
        tv_title.setText("选择客户");
        iv_add.setVisibility(View.VISIBLE);
        /**
        adapter = new PotentialCustomerSelectAdapter(this);
        select_custom_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        select_custom_recycleview.setAdapter(adapter);*/
    }

    private void showContent(boolean flag){
        select_custom_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        select_custom_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
        select_custom_refreshlayout.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    @OnClick({R.id.iv_add, R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this,CrmCustomDetailActivity.class);
                if("0".equals(type)){
                    intent.putExtra("type","POTENTIAL_NEW");
                }else if("1".equals(type)){
                    intent.putExtra("type","NEW");
                }
                startActivity(intent);
                break;
        }
    }

    private void getDataFromServer(){
        /**
         * user_id	是	int[11]用户ID
         type	是	int[2]0:潜在客户  1:客户
         page	是	int[2]页码，获取当前页内容， 默认1
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.type)
          */
        //mLoadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        String RandStr =  CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+type);
        OkGo.get(Constant.URL_GET_CUSTOMER_LIST).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("type",type)
                .params("F",Constant.F)
                .params("page",page)
                .params("V",Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseData(s);
                        //mLoadingDialog.dismiss();
                    }
                });
    }

    //解析数据
    private void parseData(String s){
        if(isPullToRefresh){
            //下拉刷新状态  则完成下拉刷新
            select_custom_refreshlayout.endRefreshing();
            isPullToRefresh = false;
            Logger.i(TAG,"下拉刷新");
        }
        if(isLoadingMore){
            //加载更多状态 则完成加载更多
            select_custom_refreshlayout.endLoadingMore();
            isLoadingMore = false;
            Logger.i(TAG,"加载更多");
        }
        if(gson == null){
            gson = new Gson();
        }
        try {
            CrmCustom custom = gson.fromJson(s,CrmCustom.class);
            if(custom.getCode().equals("0")){
                showContent(true);
                if(customList.size() == 0){
                   //第一次加载数据
                    if(custom.getData() != null && custom.getData().size()>0){
                        showContent(true);
                        page++;
                        customList.addAll(custom.getData());
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }else{
                    //加载更多
                    if(custom.getData() != null && custom.getData().size()>0){
                        customList.addAll(custom.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已经加载全部数据");
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,custom.getMsg());
                showContent(false);
            }
            for(int i = 0;i<customList.size();i++){
                if(customer!= null){
                    if(customList.get(i).getId().equals(customer.getId())){
                        customList.get(i).setSelect(true);
                        setAdapter();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new PotentialCustomerSelectAdapter(this,customList);
            select_custom_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            select_custom_recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isPullToRefresh = true;
        customList.clear();
        page = 1;
        getDataFromServer();
    }

    //加载更多
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getDataFromServer();
        return true;
    }
}
