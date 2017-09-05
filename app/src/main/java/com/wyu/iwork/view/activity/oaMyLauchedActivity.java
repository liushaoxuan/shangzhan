package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.oaMyLauchedAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.oaAprovalSendLauchModel;
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
 * @author  sxliu
 * oa_我发起的
 */

public class oaMyLauchedActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate,onItemClickListener {

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    @BindView(R.id.activity_oa_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.oa_bag)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;

    @BindView(R.id.nodata_text)
    TextView nodatatext;

    private oaMyLauchedAdapter adapter;

    private List<oaAprovalSendLauchModel> list = new ArrayList<>();

    private String url = "";
    private int page_index = 1;

    private boolean hasmore = true;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_my_lauched);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
    }

    //初始化
    private void init(){
        title.setText(R.string.my_lauched);
        edit.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new oaMyLauchedAdapter(this,list);
        recyclerView.setAdapter(adapter);
        setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
        adapter.setOnItemClickListener(this);
    }

    @OnClick(R.id.action_back)
    void Click(){
        onBackPressed();
    }


    private void initParama(){
        url = Constant.URL_OA_MY_LAUCH;
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("page",page_index+"");
        map.put("F",Constant.F);
        map.put("V",Constant.V);
        map.put("RandStr",RandStr);
        map.put("Sign",sign);
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    private DialogCallback callback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                bgaRefreshLayout.endRefreshing();
                bgaRefreshLayout.endLoadingMore();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    bgaRefreshLayout.endRefreshing();
                    bgaRefreshLayout.endLoadingMore();
                    JSONObject object = new JSONObject(s);
                    Logger.e("调试",s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONArray array = object.optJSONArray("data");
                        List<oaAprovalSendLauchModel> templist = JSON.parseArray(array.toString(),oaAprovalSendLauchModel.class);
                        list.addAll(templist);
                        if (templist!=null&&templist.size()>0){
                            hasmore = true;
                        }else {
                            hasmore = false;
                        }
                        templist.clear();
                        templist = null;
                        adapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(oaMyLauchedActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                    if (list==null||list.size()==0){
                        nodata_layout.setVisibility(View.VISIBLE);
                        nodatatext.setText("暂无内容");
                    }else {
                        nodata_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        adapter.notifyDataSetChanged();
        initParama();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        list.clear();
        page_index = 1;
        initParama();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (hasmore){
            page_index ++;
            initParama();
            return true;
        }
        else{
            Toast.makeText(oaMyLauchedActivity.this, "已加载完毕", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
       String type =  list.get(position).getType();//类型(请假,加班,出差,报销)
        String id = list.get(position).getId();
        Intent intent = null;
        switch (type){
            case "1"://请假
                intent = new Intent(oaMyLauchedActivity.this,oaApplyForLeaveDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                break;

            case "2"://加班
                intent = new Intent(oaMyLauchedActivity.this,oaWorkOverTimeDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title","加班");
                intent.putExtra("flag",0);
                startActivity(intent);
                break;

            case "3"://出差
                intent = new Intent(oaMyLauchedActivity.this,oaBusinessTravelDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title","出差");
                intent.putExtra("flag",0);
                startActivity(intent);
                break;

            case "4"://报销
                intent = new Intent(oaMyLauchedActivity.this,oaReimbursementDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("flag",0);
                startActivity(intent);
                break;
        }
    }
}
