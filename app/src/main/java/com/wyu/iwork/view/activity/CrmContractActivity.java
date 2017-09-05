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
import com.wyu.iwork.adapter.CrmContractAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmContract;
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

import static com.wyu.iwork.R.id.crm_contract_recycleview;
import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;


/**
 * @author juxinhua
 * CRM - 合同
 */
public class CrmContractActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = CrmContractActivity.class.getCanonicalName();

    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //添加
    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.crm_contracy_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.crm_contract_zanwu)
    AutoRelativeLayout crm_contract_zanwu;

    @BindView(crm_contract_recycleview)
    RecyclerView recycleView;

    @BindView(R.id.zanwu_textone)
    TextView zanwu_textone;

    @BindView(R.id.zanwu_textthree)
    TextView zanwu_textthree;

    private int page = 1;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;
    private Gson gson;

    private ArrayList<CrmContract.Contract> contractList;

    private CrmContractAdapter adapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_contract);
        hideToolbar();
        ButterKnife.bind(this);
        initRefreshLayout();
        contractList = new ArrayList<>();
        showContent(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        contractList.clear();
        initView();
    }

    private void initView(){
        getContractListFromServer();
        iv_add.setVisibility(View.VISIBLE);
        tv_title.setText("合同");
        zanwu_textone.setText("您还没有合同，请在右上方“");
        zanwu_textthree.setText("”添加合同");
    }
    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }


    //获取合同列表
    private void getContractListFromServer(){
        /**
         * URL_GET_CONTRACT_LIST
         * user_id	是	int[11]用户ID
         page	是	int[11]页码，获取当前页内容，默认传1
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.page)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+ V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+page);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CONTRACT_LIST,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                Logger.i(TAG,s);
                if(page == 1){
                    contractList.clear();
                }
                parseData(s);
            }
        });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void parseData(String data){
        if(isRefreshing){
            isRefreshing = false;
            mRefreshLayout.endRefreshing();
            Logger.i(TAG,"下拉刷新");
        }else if(isLoadingMore){
            isLoadingMore = false;
            mRefreshLayout.endLoadingMore();
            Logger.i(TAG,"加载更多");
        }
        try {
            if(gson == null){
                gson = new Gson();
            }
            CrmContract crmContract = gson.fromJson(data,CrmContract.class);
            if("0".equals(crmContract.getCode())){
                if(contractList.size()>0){
                    if(crmContract.getData() != null && crmContract.getData().size()>0){
                        page++;
                        showContent(true);
                        contractList.addAll(crmContract.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已加载全部数据!");
                    }
                }else{
                    if(crmContract.getData() != null && crmContract.getData().size()>0){
                        page++;
                        showContent(true);
                        contractList.addAll(crmContract.getData());
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,crmContract.getMsg());
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void showContent(boolean flag){
        recycleView.setVisibility(flag?View.VISIBLE:View.GONE);
        crm_contract_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new CrmContractAdapter(this,contractList);
            recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recycleView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }

    }


    @OnClick({R.id.ll_back,R.id.iv_add})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                Intent intent = new Intent(this,CrmContractDetailActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        getContractListFromServer();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getContractListFromServer();
        return true;
    }
}
