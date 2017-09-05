package com.wyu.iwork.view.activity;

import android.app.Dialog;
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
import com.wyu.iwork.adapter.PotentialCustomerManagerAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.widget.CrmCreateCustomDialog;
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

/**
 * @author juxinhua
 * 客户管理
 */
public class PotentialCustomerManagerActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = PotentialCustomerManagerActivity.class.getSimpleName();
    @BindView(R.id.potential_customer_manager_recycleview)
    RecyclerView potential_customer_manager_recycleview;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.potential_customer_manager_zanwu)
    AutoRelativeLayout potential_customer_manager_zanwu;

    @BindView(R.id.potential_customer_manager_refresh)
    BGARefreshLayout potential_customer_manager_refresh;

    private static final String TYPE_CUSTOM = "CUSTOM";//客户管理
    private static final String TYPE_POTENTIAL = "POTENTIAL";//潜在客户管理
    private String type;
    private String requestType;//请求类型  为客户还是潜在客户
    private int page = 1;//网络请求的页码
    private String F = Constant.F;
    private String V = Constant.V;
    private String RandStr;
    private String Sign;

    //private LoadingDialog mLoadingDialog = new LoadingDialog();
    private Gson gson;

    private ArrayList<CrmCustom.Custom> customList;
    private PotentialCustomerManagerAdapter adapter;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_customer_manager);
        ButterKnife.bind(this);
        hideToolbar();
        getExtras();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getDataFromServer();
    }

    private void getExtras(){
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        initRefreshLayout();//配置BGARefreshLayout
        initView();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        potential_customer_manager_refresh.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        potential_customer_manager_refresh.setRefreshViewHolder(refreshViewHolder);
    }

    //初始化界面状态
    private void initView(){
        iv_add.setVisibility(View.VISIBLE);
        if(TYPE_CUSTOM.equals(type)){
            tv_title.setText("客户管理");
        }else{
            tv_title.setText("潜在客户管理");
        }
        showContent(false);
        customList = new ArrayList<>();

    }

    @OnClick({R.id.ll_back,R.id.iv_add})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                showCreateDialog();
                break;
        }
    }

    //加载后台数据
    private void getDataFromServer(){
        /**
         * user_id	是	int[11] 用户ID
         type	是	int[2]0:潜在客户  1:客户
         page	是	int[2]页码，获取当前页内容， 默认1
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.type)
         */
        //mLoadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        if(TYPE_CUSTOM.equals(type)){
            requestType = "1";
        }else{
            requestType = "0";
        }
        RandStr = CustomUtils.getRandStr();
        Sign = Md5Util.getSign(F+V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+requestType);
        Logger.i("page",page+"");
        OkGo.get(Constant.URL_GET_CUSTOMER_LIST).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("type",requestType)
                .params("page",page)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        if(page == 1){
                            customList.clear();
                        }
                        parseData(s);
                        //mLoadingDialog.dismiss();

                    }
                });
    }

    private void parseData(String s){
        if(isRefreshing){
            isRefreshing = false;
            potential_customer_manager_refresh.endRefreshing();
            Logger.i(TAG,"下拉刷新");
        }else if(isLoadingMore){
            isLoadingMore = false;
            potential_customer_manager_refresh.endLoadingMore();
            Logger.i(TAG,"加载更多");
        }
        try {
            if(gson == null){
                gson = new Gson();
            }
            CrmCustom crmCustom = gson.fromJson(s,CrmCustom.class);
            if("0".equals(crmCustom.getCode())){
                //1:若当前为加载更多状态  则：case 1：已加载全部数据:则提示已加载全部数据 2:还有更多数据  则全部显示
                if(customList.size()>0){
                    //加载更多状态
                    if(crmCustom.getData() != null && crmCustom.getData().size()>0){
                        page ++;
                        showContent(true);
                        customList.addAll(crmCustom.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已加载全部数据!");
                    }
                }else{
                    if(crmCustom.getData() != null && crmCustom.getData().size()>0){
                        page ++;
                        showContent(true);
                        customList.addAll(crmCustom.getData());
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,crmCustom.getMsg());
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    //给recycleView设置数据
    private void setAdapter(){
        if(adapter == null){
            //第一次设置数据
            adapter = new PotentialCustomerManagerAdapter(this,type,customList);
            potential_customer_manager_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            potential_customer_manager_recycleview.setAdapter(adapter);
        }else{
            //刷新adapter
            adapter.notifyDataSetChanged();
        }
    }

    //显示选择弹窗
    private void showCreateDialog(){
        new CrmCreateCustomDialog(this, new CrmCreateCustomDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                Intent intent = new Intent(PotentialCustomerManagerActivity.this,CrmCustomDetailActivity.class);
                if(TYPE_CUSTOM.equals(type)){
                    intent.putExtra("type","NEW");
                }else{
                    intent.putExtra("type","POTENTIAL_NEW");
                }
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                Intent intent = new Intent(PotentialCustomerManagerActivity.this,RectCameraActivity.class);
                if(TYPE_CUSTOM.equals(type)){
                    intent.putExtra("type","NEW");
                }else{
                    intent.putExtra("type","POTENTIAL_NEW");
                }
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void threeClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    //当有数据时，显示数据，没有数据时，显示暂无数据的界面
    private void showContent(boolean flag){
        potential_customer_manager_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        potential_customer_manager_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
        potential_customer_manager_refresh.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
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
