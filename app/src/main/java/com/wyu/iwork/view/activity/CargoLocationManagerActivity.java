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
import com.wyu.iwork.adapter.CargoLocationManagerAdapter;
import com.wyu.iwork.adapter.StoresManagerAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CargoModel;
import com.wyu.iwork.model.StoreModel;
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
 * 货位管理
 */
public class CargoLocationManagerActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.activity_cargo_location_manager_refreshview)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_cargo_location_manager_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.nodata_text)
    TextView nodata_text;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;

    private CargoLocationManagerAdapter adapter;
    public static String flag = "";

    private String url = "";

    //页码，获取当前页内容，默认传1
    private int index = 1;

    private List<CargoModel> list = new ArrayList<>();
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_location_manager);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("货位管理");
        nodata_text.setText("暂无货位信息");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CargoLocationManagerAdapter(this,list);
        recyclerView.setAdapter(adapter);
        setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
        initflag();
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
                Intent intent = new Intent(this,BuildCargoLocationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
            list.clear();
       initParama();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        index++;
        initParama();
        return true;
    }

    //初始化请求参数
    private void initParama() {
        url = Constant.URL_CARGO_LIST;
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
                        List<CargoModel> templist = JSON.parseArray(data.toString(),CargoModel.class);
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
                        Toast.makeText(CargoLocationManagerActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        index = 1;
        initParama();
    }
}
