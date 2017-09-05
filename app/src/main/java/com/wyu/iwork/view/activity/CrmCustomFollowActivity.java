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
import com.wyu.iwork.adapter.PotentialCustomerFollowAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmCustomSeaModule;
import com.wyu.iwork.model.CrmFollowCustom;
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

/**
 * @author juxinhua
 * 潜在客户跟进/潜在客户公列表
 */
public class CrmCustomFollowActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = CrmCustomFollowActivity.class.getSimpleName();
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.crm_custom_follow_recycleview)
    RecyclerView crm_custom_follow_recycleview;

    @BindView(R.id.layout_crm_zanwu)
    AutoRelativeLayout layout_crm_zanwu;

    @BindView(R.id.crm_custom_follow_refreshlayout)
    BGARefreshLayout crm_custom_follow_refreshlayout;

    @BindView(R.id.zanwu_textone)
    TextView zanwu_textone;

    @BindView(R.id.zanwu_texttwo)
    TextView zanwu_texttwo;

    @BindView(R.id.zanwu_textthree)
    TextView zanwu_textthree;

    private PotentialCustomerFollowAdapter adapter ;
    private static final String TYPE_FOLLOW = "FOLLOW";//潜在客户跟进类型
    private static final String TYPE_OPENSEA = "OPENSEA";//客户公海类型
    private String type = "";
    private int page = 1;//请求页码
    private String F = Constant.F;
    private String V = Constant.V;
    private String RandStr;
    private String Sign;
    private boolean isPullToRefreshing = false; //是否处于下拉刷新状态
    private boolean isLoadingMore = false; //是否处于加载更多状态
    //private LoadingDialog mLoadingDialog;//加载过程中的loading状态条

    private ArrayList<CrmFollowCustom.FollowCustom> customList ;//客户跟进集合
    private ArrayList<CrmCustomSeaModule.Data.CustomSea> customSeaList;//客户公海集合

    private Gson gson;
    private String conf;//公海时限

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_custom_follow);
        ButterKnife.bind(this);
        hideToolbar();
        getExtras();
    }

    //获取携带的数据
    private void getExtras(){
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(TYPE_FOLLOW.equals(type)){
            initRefreshLayout();//配置BGARefreshLayout
        }
        customList = new ArrayList<>();//存放数据
        //mLoadingDialog = new LoadingDialog();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(TYPE_OPENSEA.equals(type)){
            zanwu_textone.setText("暂无设置时间内的潜在客户!");
            zanwu_texttwo.setVisibility(View.GONE);
            zanwu_textthree.setVisibility(View.GONE);
        }else{
            page = 1;
            getDataFromServer();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(TYPE_OPENSEA.equals(type)){
            getCustomHighSeasData();
        }
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        crm_custom_follow_refreshlayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        crm_custom_follow_refreshlayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void initView(){
        if(TYPE_OPENSEA.equals(type)){
            tv_edit.setText("设置");
            tv_edit.setVisibility(View.VISIBLE);
            tv_title.setText("潜在客户公海");
        }else{
            tv_title.setText("潜在客户跟进");
            iv_add.setVisibility(View.VISIBLE);
        }
        showContent(true);


    }

    //获取潜在客户公海数据
    private void getCustomHighSeasData(){
        /**
         * user_id	是	int[11]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        //mLoadingDialog.show(getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        OkGo.get(Constant.URL_GET_CUSTOMER_HIGH_SEAS).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("F",Constant.F)
                .params("V",Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseCustomSeaData(s);
                       // mLoadingDialog.dismiss();
                    }
                });
    }

    private void parseCustomSeaData(String s){
        if(gson == null){
            gson = new Gson();
        }
        try {
            CrmCustomSeaModule module = gson.fromJson(s,CrmCustomSeaModule.class);
            conf = module.getData().getConf();
            if(module.getCode().equals("0")){
                if(module.getData().getList() != null && module.getData().getList().size()>0){
                    showContent(true);
                    setHighSeaAdapter(module.getData().getList(),module.getData().getConf());
                }else{
                    showContent(false);
                }
            }else{
                showContent(false);
            }
            if(module != null){
                module = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //设置客户公海数据适配器
    private void setHighSeaAdapter(ArrayList<CrmCustomSeaModule.Data.CustomSea> list,String conf){
        if(adapter == null){
            adapter = new PotentialCustomerFollowAdapter(this,list,type);
            crm_custom_follow_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            crm_custom_follow_recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    //获取潜在客户跟进数据
    private void getDataFromServer(){
        /**
         * user_id	是	int[11]用户ID
         page	是	int[2]页码，获取当前页内容， 默认1
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.page)
         */
        RandStr = CustomUtils.getRandStr();
        Sign = Md5Util.getSign(F+V+ RandStr + AppManager.getInstance(this).getUserInfo().getUser_id()+page);
        OkGo.get(Constant.URL_GET_CUSTOMER_FOLLOW_LIST).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
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
                        parseDataForFollowCustom(s);
                    }
                });

    }

    //解析潜在客户跟进数据
    private void parseDataForFollowCustom(String s){
        if(isPullToRefreshing){
            //若当前为下拉刷新状态   则完成下拉刷新
            crm_custom_follow_refreshlayout.endRefreshing();
            isPullToRefreshing = false;
            Logger.i(TAG,"下拉刷新！");
        }else if(isLoadingMore){
            //若当前状态为加载更多状态  则完成加载更多状态
            crm_custom_follow_refreshlayout.endLoadingMore();
            isLoadingMore = false;
            Logger.i(TAG,"加载更多！");
        }
        try {
            if(gson == null){
                gson = new Gson();
            }
            CrmFollowCustom custom = gson.fromJson(s,CrmFollowCustom.class);
            if("0".equals(custom.getCode())){
                if(customList.size() == 0){
                    if(custom.getData() != null && custom.getData().size()>0){
                        page++;
                        showContent(true);
                        customList.addAll(custom.getData());
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }else{
                    if(custom.getData() != null && custom.getData().size()>0){
                        page++;
                        customList.addAll(custom.getData());
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已经加载全部数据!");
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,custom.getMsg());
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new PotentialCustomerFollowAdapter(this,type,customList);
            crm_custom_follow_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            crm_custom_follow_recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.iv_add, R.id.ll_back,R.id.tv_edit})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.iv_add:
                intent = new Intent(this,PotentialCustomDetailActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
                break;
            case R.id.tv_edit:
                intent = new Intent(this,CrmPotentialSettingActivity.class);
                intent.putExtra("conf",conf);
                startActivity(intent);
                break;
        }
    }

    private void showContent(boolean flag){
        layout_crm_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        crm_custom_follow_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
        crm_custom_follow_refreshlayout.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        isPullToRefreshing = true;
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
