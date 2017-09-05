package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.SalesOrderAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.SaleOrderModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONArray;
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
 * 销售订单
 */
public class SalesOrderActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static String TAG = SalesOrderActivity.class.getSimpleName();

    @BindView(R.id.activity_sales_order_refreshview)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_sales_order_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.nodata_text)
    TextView nodata_text;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;

    private SalesOrderAdapter adapter;
    public static String flag = "";

    //请求地址
    private String url = "";

    //页码，获取当前页内容，默认传1
    private int index = 1;

    //获取所有订单传0,获取出库销售订单选择列表2
    private String status = "0";

    //销售订单集合
    private List<SaleOrderModel> list = new ArrayList<>();
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("销售订单");
        nodata_text.setText("暂无销售订单");
        initflag();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SalesOrderAdapter(this,list);
        recyclerView.setAdapter(adapter);
        setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
    }

    private void initflag(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            flag = bundle.getString("flag");
            if (flag.isEmpty()){
                status = "0";
            }else {
                status = "2";
            }
        }
    }

    @OnClick({R.id.action_back,R.id.action_add})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.action_add://添加
                Intent intent = new Intent(this,BuildSalesOrderActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void initParama() {
        url = Constant.URL_SALE_ORDER_LIST;
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String page = index+"";
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+page);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("page", page+"");
        map.put("status", status);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        Logger.e(TAG,url);
        OkgoRequest(url,callback());
    }

    //获取供货商的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                if (bgaRefreshLayout != null) {
                    bgaRefreshLayout.endLoadingMore();
                    bgaRefreshLayout.endRefreshing();
                }
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                try {
                    if (bgaRefreshLayout != null) {
                        bgaRefreshLayout.endLoadingMore();
                        bgaRefreshLayout.endRefreshing();
                    }
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONArray data = object.optJSONArray("data");
                        List<SaleOrderModel> templist = JSON.parseArray(data.toString(), SaleOrderModel.class);
                        list.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                        if (list.size()>0){
                            nodata_layout.setVisibility(View.GONE);
                        }else {
                            nodata_layout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(SalesOrderActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                ;
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

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        index = 1;
        initParama();
    }
}
