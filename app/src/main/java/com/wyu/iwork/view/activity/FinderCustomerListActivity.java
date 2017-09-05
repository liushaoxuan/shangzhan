package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.FinderCustomAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.FinderCustomer;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

public class FinderCustomerListActivity extends BaseActivity {

    private static final String TAG = FinderCustomerListActivity.class.getSimpleName();

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.crm_custom_zanwu)
    AutoLinearLayout crm_custom_zanwu;


    private int type;//类型 代表为个人查重还是公司查重 1 为公司查重 0 为个人查重
    private String name;//姓名

    private String number;//电话
    private FinderCustomAdapter adapter;


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finder_customer_list);
        ButterKnife.bind(this);
        hideToolbar();
        initView();
        getExtra();
    }

    private void initView(){
        tv_title.setText("查询结果");
    }

    @OnClick({R.id.ll_back})
    void Click(View v){
        if(v.getId() == R.id.ll_back){
            onBackPressed();
        }
    }

    private void getExtra(){
        Intent intent = getIntent();
        type = intent.getIntExtra("type",0);
        name = intent.getStringExtra("name");
        if(type == 0){
            number = intent.getStringExtra("phone");
        }
        finderCustomer();
    }

    private void finderCustomer(){
        /**
         * URL_FIND_CUSTOMER
         */
        /**
         *  user_id	是	int[11]       用户ID
         name	      是	int[11]       客户/潜在客户姓名  公司名称
         phone	      是	int[11]       客户/潜在客户联系方式
         F	      是	string[18]    请求来源：IOS/ANDROID/WEB
         V	      是	string[20]    版本号如：1.0.1
         RandStr	是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("name",name);
        if(type == 0 && !TextUtils.isEmpty(number)){
            data.put("phone",number);
        }
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_FIND_CUSTOMER,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        parseData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    private void parseData(String data){
        Gson gson =  new Gson();
        try {
            FinderCustomer customer = gson.fromJson(data,FinderCustomer.class);
            if("0".equals(customer.getCode())){
                if(customer.getData() != null && customer.getData().size()>0){
                    showContent(true);
                    adapter = new FinderCustomAdapter(this,customer.getData());
                    recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                    recycleview.setAdapter(adapter);
                }else{
                    showContent(false);
                }
            }else{
                MsgUtil.shortToastInCenter(this,customer.getMsg());
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void showContent(boolean flag){
        crm_custom_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
    }
}
