package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CardBagAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CardModel;
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


public class CardBagActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate,TextWatcher{

    private static final String TAG = CardBagActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.bag_notavaliable)
    AutoLinearLayout notavaliable;

    @BindView(R.id.bag_recycleview)
    RecyclerView recycleView;

    @BindView(R.id.bag_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.bag_search)
    EditText bag_search;

    @BindView(R.id.tv_notavailable)
    TextView tv_notavailable;

    private int page = 1;
    private UserInfo mUser;
    //卡包数据
    private ArrayList<CardModel.Data.Card> datasList = new ArrayList<>();

    //卡包搜索数据
    private ArrayList<CardModel.Data.Card> searchList = new ArrayList<>();
    private CardBagAdapter searchAdapter ;
    private CardBagAdapter adapter;
    private Gson gson;
    private CardModel model;

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
        setContentView(R.layout.activity_card_bag);
        ButterKnife.bind(this);
        hideToolbar();
        initView();
    }

    private void initView(){
        title.setText("名片夹");
        tv_notavailable.setText("暂无名片");
        initRefreshLayout();
        bag_search.addTextChangedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        page = 1;
        initData();
    }

    public void initData(){
        getDataFromServer();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    private void getDataFromServer(){
        /**
         * user_id	是	int[11]用户自己的ID
         page	是	int[11]页码,每页显示8条信息
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.page)
         */
        if(mUser == null)
        mUser = AppManager.getInstance(this).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ mUser.getUser_id()+page);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",mUser.getUser_id());
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_CARD_LIST,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        if(page == 1){
                            datasList.clear();
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
            model = gson.fromJson(data,CardModel.class);
            if("0".equals(model.getCode())){
                if(datasList.size()>0){
                    if(model.getData() != null && model.getData().getList() != null && model.getData().getList().size()>0){
                        showContent(true,recycleView,notavaliable);
                        datasList.addAll(model.getData().getList());
                        setAdapter();
                        page++;
                    }else{
                        MsgUtil.shortToastInCenter(CardBagActivity.this,"已加载全部名片!");
                    }
                }else{
                    if(model.getData() != null && model.getData().getList() != null && model.getData().getList().size()>0){
                        showContent(true,recycleView,notavaliable);
                        datasList.addAll(model.getData().getList());
                        setAdapter();
                        page++;
                    }else{
                        showContent(false,recycleView,notavaliable);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,model.getMsg());
                showContent(false,recycleView,notavaliable);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false,recycleView,notavaliable);
        }
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new CardBagAdapter(this,datasList);
            recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recycleView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        initData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        initData();
        return true;
    }

    public void showContentImpl(boolean flag){
        showContent(flag,recycleView,notavaliable);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //进行搜索
        searchList.clear();
        showContentImpl(true);
        String searchStr = bag_search.getText().toString();
        if(!TextUtils.isEmpty(searchStr)){

            if(datasList != null && datasList.size() > 0){
                for(int i = 0;i<datasList.size();i++){
                    if(datasList.get(i).getName().contains(searchStr)){
                        searchList.add(datasList.get(i));
                    }
                }
            }
            if(searchList.size()>0){
                adapter = null;
            }
            if(searchList.size()>0){
                if(searchAdapter == null){
                    searchAdapter = new CardBagAdapter(this,searchList);
                    recycleView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                    recycleView.setAdapter(searchAdapter);
                }else{
                    searchAdapter.notifyDataSetChanged();
                }
            }else{
                showContentImpl(false);
            }


        }else{
            searchAdapter = null;
            showContentImpl(true);
            setAdapter();
        }
    }
}
