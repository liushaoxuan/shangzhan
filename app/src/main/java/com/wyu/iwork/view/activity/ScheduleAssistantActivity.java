package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ScheduleAssistantAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MessageNoticeAssistanBean;
import com.wyu.iwork.model.ScheduleAssistentModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 日程通知
 */
public class ScheduleAssistantActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static String TAG = ScheduleAssistantActivity.class.getSimpleName();
    @BindView(R.id.activity_task_assistant_title)
    TextView title;

    @BindView(R.id.activity_task_assistant_del_layout)
    RelativeLayout del_layout;

    @BindView(R.id.activity_task_assistant_bga)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_task_assistant_recyclerview)
    RecyclerView recyclerView;

    private ScheduleAssistantAdapter adapter;

    private List<ScheduleAssistentModel> list  = new ArrayList<>();
    //请求地址
    private String url =  "";

    private int pageindex = 1;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assistant);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
        init();
        initparama();
    }

    //初始化
    private void init(){
        title.setText("日程通知");
       adapter = new ScheduleAssistantAdapter(this,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.activity_task_assistant_back,R.id.activity_task_assistant_del})
    void Click(View v){
        switch (v.getId()){
            case R.id.activity_task_assistant_back:
                onBackPressed();
                break;

            case R.id.activity_task_assistant_del:
                del_layout.setVisibility(View.GONE);
                break;
        }
    }

    //初始化请求参数
    private void initparama(){
        url = Constant.URL_SCHEDULE_NOTICE;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        HttpParams params = new HttpParams();
        params.put("user_id", user_id);
        params.put("page", pageindex+"");
        params.put("F", F);
        params.put("V", V);
        params.put("RandStr", RandStr);
        params.put("Sign", sign);
        OkGo.get(url)
                .params(params)
                .execute(callback());
    }

    /**
     *  请求回调
     */
    private DialogCallback callback(){
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                bgaRefreshLayout.endRefreshing();
                bgaRefreshLayout.endLoadingMore();
                Logger.e(TAG,s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    JSONArray data = object.optJSONArray("data");
                    if ("0".equals(code)){
                        List<ScheduleAssistentModel> templist = new ArrayList<>();
                        templist = JSON.parseArray(data.toString(),ScheduleAssistentModel.class);
                        list.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                    }else {
                        Toast.makeText(ScheduleAssistantActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                bgaRefreshLayout.endRefreshing();
                bgaRefreshLayout.endLoadingMore();
            }
        };
    }
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        list.clear();
        pageindex = 1;
        initparama();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pageindex++;
        initparama();
        return true;
    }
}
