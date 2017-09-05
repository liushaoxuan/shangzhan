package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CrmProcessAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.OpportunityDetail;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.ProcessSelectDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SalesProcessActivity extends BaseActivity {

    private static final String TAG = SalesProcessActivity.class.getSimpleName();
    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //流程变更
    @BindView(R.id.crm_process_change)
    TextView crm_process_change;

    //流程容器recycleview
    @BindView(R.id.crm_sales_process_recycleview)
    RecyclerView recycleview;

    private String currentProcess;//代表当前的流程状态
    private String finishOrder;//代表点击赢单或无效或保存的状态
    private Gson gson;
    private OpportunityDetail detail;
    private String chance_id;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_process);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    //获取商机ID
    private void getExtras(){
        chance_id = getIntent().getStringExtra("chance_id");
    }

    private void initView(){
        tv_title.setText("默认销售流程");
        getOpportunityDetail();
    }

    @OnClick({R.id.ll_back,R.id.crm_process_change})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.crm_process_change:
                //MsgUtil.shortToastInCenter(this,"流程变更");
                showSelectorDialog();
                break;
        }
    }

    //显示流程变更弹窗
    private void showSelectorDialog(){
        new ProcessSelectDialog(this, currentProcess, new ProcessSelectDialog.DialogListener() {
            @Override
            public void itemClick(String text) {
                Logger.i(TAG, text);
                if(!text.equals("赢单") && !text.equals("无效")){
                    updateChanceStatus(text);
                }
            }

            @Override
            public void showClickMessage(String text) {
                finishOrder = text;
                Logger.i(TAG, text);
                updateChanceStatus(finishOrder);
            }
        }).show();
    }

    //更新商机状态
    private void updateChanceStatus(String status){
        /**
         *  user_id	是	int[11]       用户ID
         chance_id	是	int[11]       商机ID
         status	是	int[11]       更改阶段ID 1：验证客户 2：需求确定 3：方案/报价 4：谈判审核 5：赢单 6：输单 7：无效
         fail_status	否	int[11]       只在更改状态为输单时填写  输单原因 1：客户不想购买了 2：未满足客户需求 3：被竞争对手抢单
         F	      是	string[18]    请求来源：IOS/ANDROID/WEB
         V	      是	string[20]    版本号如：1.0.1
         RandStr	是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.chance_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("chance_id",detail.getData().getId());
        //流程变更
        String[] processArr = getResources().getStringArray(R.array.crm_process_change);
        //输单原因
        String[] reasonArr = getResources().getStringArray(R.array.crm_lost_opportunity);
        if("确定商机赢单".equals(status) ){
            data.put("status",5+"");
        }else if("确定商机无效".equals(status)){
            data.put("status",7+"");
        }else{
            for(int i = 0;i<processArr.length;i++){
                if(processArr[i].equals(status)){
                    data.put("status",i+1+"");
                }
            }
            for(int i = 0;i<reasonArr.length;i++){
                if(reasonArr[i].equals(status)){
                    data.put("status","6");
                    data.put("fail_status",i+1+"");
                }
            }
        }
        data.put("F", Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+
                AppManager.getInstance(this).getUserInfo().getUser_id()+detail.getData().getId());
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CHANCE_STATUS,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                getOpportunityDetail();//获取商机详情
                            }else{
                                MsgUtil.shortToastInCenter(SalesProcessActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
        if(processArr != null){
            processArr = null;
        }
        if(reasonArr != null){
            reasonArr = null;
        }
    }

    //获取商机详情
    private void getOpportunityDetail(){
        /**
         *  user_id	是	int[11]       用户ID
         chance_id	是	int[11]       商机ID
         F	      是	string[18]    请求来源：IOS/ANDROID/WEB
         V	      是	string[20]    版本号如：1.0.1
         RandStr	是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.id.user_id.chance_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("chance_id",chance_id);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+chance_id);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CHANCE_DETAIL,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        try {
                            if(gson == null){
                                gson = new Gson();
                            }
                            detail = gson.fromJson(s,OpportunityDetail.class);
                            setData();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    //设置商机详情到相应位置
    private void setData(){
        if("5".equals(detail.getData().getStatus()) || "6".equals(detail.getData().getStatus()) || "7".equals(detail.getData().getStatus())){
            crm_process_change.setVisibility(View.GONE);
        }else{
            crm_process_change.setVisibility(View.VISIBLE);
        }
        currentProcess = getResources().getStringArray(R.array.crm_process_change)[Integer.parseInt(detail.getData().getStatus())-1];
        recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        Logger.i(TAG,detail.getData().getStatus());
        recycleview.setAdapter(new CrmProcessAdapter(this,detail.getData().getStatus()));
    }
}
