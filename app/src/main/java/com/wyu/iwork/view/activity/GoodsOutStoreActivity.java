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
import com.wyu.iwork.adapter.GoodsInStoreAdapter;
import com.wyu.iwork.adapter.GoodsOutStoreAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.GoodsModel;
import com.wyu.iwork.model.OutStoreModel;
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
 * 商品出库
 */
public class GoodsOutStoreActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{



    @BindView(R.id.activity_goods_in_store_refreshview)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_goods_in_store_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.nodata_text)
    TextView nodata_text;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;

    private GoodsOutStoreAdapter adapter;

    public String flag = "";

    //请求地址
    private String url = "";

    //出库集合
    private List<OutStoreModel> list = new ArrayList<>();

    //页码 默认1
    private int index = 1;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_in_store);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("商品出库");
        nodata_text.setText("暂无商品出库");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GoodsOutStoreAdapter(this,list);
        recyclerView.setAdapter(adapter);
        bgaRefreshLayout.setDelegate(this);
        setRefresh(bgaRefreshLayout);
        getflag();
    }

    private void getflag(){
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
                Intent intent = new Intent(this,BuildGoodsOutStoreActivity.class);
                startActivity(intent);
                break;
        }
    }



    //初始化请求参数
    private void initParama() {
        url = Constant.URL_OUTSTORE_LIST;
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String page = index+"";
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
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
                        List<OutStoreModel> templist = JSON.parseArray(data.toString(),OutStoreModel.class);
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
                        Toast.makeText(GoodsOutStoreActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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

        index =1;
        list.clear();
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

        index =1;
        list.clear();
        initParama();
    }
}
