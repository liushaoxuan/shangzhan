package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CargoLocationManagerAdapter;
import com.wyu.iwork.adapter.GoodsManagerAdapter;
import com.wyu.iwork.adapter.SelectCustmerAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CargoModel;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.model.CustomerModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 选择客户
 */
public class SelectCustmerActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{


    @BindView(R.id.activity_select_custmer_refreshview)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_select_custmer_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.action_title)
    TextView title;

    private SelectCustmerAdapter adapter;

    public String flag = "";
    //页码，获取当前页内容，默认传1
    private int index = 1;

    //请求地址
    private String url = Constant.URL_GET_CUSTOMER_LIST;

    private ArrayList<CustomerModel> list = new ArrayList<>();

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_custmer);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("选择客户");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectCustmerAdapter(this,list);
        recyclerView.setAdapter(adapter);
        setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
        initflag();
        initParama();
    }

    private void initflag(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            flag = bundle.getString("flag");
        }
    }

    @OnClick({R.id.action_back,R.id.action_add})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.action_add://添加
                Intent intent = new Intent(this,CrmCustomDetailActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        list.clear();
        OkgoRequest(url,callback());
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    //初始化请求参数
    private void initParama() {
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String page = index+"";
        String type = "1";//0:潜在客户  1:客户
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+type);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("page", page);
        map.put("type", type);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取客户的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (bgaRefreshLayout!=null){
                    bgaRefreshLayout.endLoadingMore();
                    bgaRefreshLayout.endRefreshing();
                }
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    if (bgaRefreshLayout!=null){
                        bgaRefreshLayout.endLoadingMore();
                        bgaRefreshLayout.endRefreshing();
                    }
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONArray data = object.optJSONArray("data");
                        List<CustomerModel> templist = JSON.parseArray(data.toString(),CustomerModel.class);
                        list.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                    }else {
                        Toast.makeText(SelectCustmerActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                ;
    }
}
