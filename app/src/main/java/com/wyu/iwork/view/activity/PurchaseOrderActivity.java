package com.wyu.iwork.view.activity;

import android.content.Intent;
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
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.PurchaseOrderAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Contact;
import com.wyu.iwork.model.PurchaseModel;
import com.wyu.iwork.model.SurpplierManagerModel;
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
 * @author  sxliu
 * 采购订单
 */
public class PurchaseOrderActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.activity_purchase_order_refreshview)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_purchase_order_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.nodata_text)
    TextView nodata_text;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;

    private PurchaseOrderAdapter adapter;
    public String flag = "";

    private String url = "";

    // 页码，获取当前页内容，默认传1
    private int index = 1;

    //订单数据集合
    private List<PurchaseModel> list;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        getSupportActionBar().hide();
        title.setText("采购订单");
        nodata_text.setText("暂无采购订单");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PurchaseOrderAdapter(this,list);
        recyclerView.setAdapter(adapter);
        bgaRefreshLayout.setDelegate(this);
        setRefresh(bgaRefreshLayout);
        getextras();
    }

    private void getextras(){
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
                Intent intent = new Intent(this,AddOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        list.clear();
        index = 1;
      initParama();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        index++;
        initParama();
        return true;
    }

    private void initParama() {
        url = Constant.URL_PURCHASE_LIST;
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String page = index+"";
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+page);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("page", page);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        index = 1;
        adapter.notifyDataSetChanged();
        initParama();

    }

    //获取供货商的请求回调
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
                        List<PurchaseModel> templist = JSON.parseArray(data.toString(),PurchaseModel.class);
                        list.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                        if (list.size()>0){
                            nodata_layout.setVisibility(View.GONE);
                        }else {
                            nodata_layout.setVisibility(View.VISIBLE);
                        }
                    }else {
                        Toast.makeText(PurchaseOrderActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                ;
    }
}
