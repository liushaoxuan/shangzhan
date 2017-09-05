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
import com.wyu.iwork.adapter.MarketingSelectorAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Marketing;
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
 * @author juxinhua
 * CRM - 市场活动
 */
public class MarketingSelectorActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = MarketingSelectorActivity.class.getSimpleName();

    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //添加
    @BindView(R.id.iv_add)
    ImageView iv_add;

    //列表容器
    @BindView(R.id.market_recycleview)
    RecyclerView market_recycleview;

    @BindView(R.id.crm_activity_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.crm_activity_zanwu)
    AutoRelativeLayout crm_activity_zanwu;

    private int page = 1;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;
    private Gson gson;
    private ArrayList<Marketing.MarketingDetail> marketList;
    private MarketingSelectorAdapter adapter;
    private Marketing.MarketingDetail market;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing);
        hideToolbar();
        ButterKnife.bind(this);
        marketList = new ArrayList<>();
        showContent(false);
        getExtra();
        initView();
    }

    private void getExtra(){
        market = (Marketing.MarketingDetail) getIntent().getSerializableExtra("market");
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getMarketingActivityList();
    }

    private void initView(){
        iv_add.setVisibility(View.VISIBLE);
        tv_title.setText("市场活动");
        initRefreshLayout();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    @OnClick({R.id.ll_back,R.id.iv_add})
    void Click(View v){
        switch(v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                //新建
                Intent intent = new Intent(this,MarketingDetailActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
                break;
        }
    }

    private void getMarketingActivityList(){
        /**
         *  user_id	是	int[11]       用户ID
            page	      是	int[11]       页码，获取当前页内容，默认传1
            F	      是	string[18]    请求来源：IOS/ANDROID/WEB
            V	      是	string[20]    版本号如：1.0.1
            RandStr	是	string[50]    请求加密随机数 time().|.rand()
            Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.page)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+page);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("page",page+"");
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_ACTIVITY_LIST,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        if(page == 1){
                            marketList.clear();
                        }
                        parseData(s);
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

    //刷新完成之后，结束刷新事件
    private void endRefresh(){
        if(isLoadingMore){
            isLoadingMore = false;
            mRefreshLayout.endLoadingMore();
            Logger.i(TAG,"加载更多");
        }else if(isRefreshing){
            isRefreshing = false;
            mRefreshLayout.endRefreshing();
            Logger.i(TAG,"下拉刷新");
        }
    }

    //解析数据
    private void parseData(String data){
        endRefresh();
        try {
            if(gson == null){
                gson = new Gson();
            }
            Marketing marketing = gson.fromJson(data,Marketing.class);
            if("0".equals(marketing.getCode())){
                if(marketList.size()>0){
                    //加载更多
                    if(marketing.getData() != null && marketing.getData().size()>0){
                        page ++;
                        showContent(true);
                        marketList.addAll(marketing.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已加载全部数据");
                    }
                }else{
                    //首次加载
                    if(marketing.getData() != null && marketing.getData().size()>0){
                        page ++;
                        showContent(true);
                        marketList.addAll(marketing.getData());
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }
                for(int i = 0;i<marketList.size();i++){
                    if(market!= null){
                        if(marketList.get(i).getId().equals(market.getId())){
                            marketList.get(i).setSelect(true);
                            setAdapter();
                        }
                    }
                }
            }else{
                showContent(false);
                MsgUtil.shortToastInCenter(this,marketing.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new MarketingSelectorAdapter(this,marketList);
            market_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            market_recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    private void showContent(boolean flag){
        crm_activity_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        market_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        getMarketingActivityList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getMarketingActivityList();
        return true;
    }
}
