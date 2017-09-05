package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.oaMyAprovalAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.oaAprovalSendLauchModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.oaApplyDetailActivity;
import com.wyu.iwork.view.activity.oaApplyForLeaveDetailActivity;
import com.wyu.iwork.view.activity.oaBusinessTravelDetailActivity;
import com.wyu.iwork.view.activity.oaCopySendToMeActivity;
import com.wyu.iwork.view.activity.oaMyAprovalActivity;
import com.wyu.iwork.view.activity.oaMyLauchedActivity;
import com.wyu.iwork.view.activity.oaReimbursementDetailActivity;
import com.wyu.iwork.view.activity.oaWorkOverTimeDetailActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/6/6.15:29
 * 邮箱：2587294424@qq.com
 * 我已审批的
 */

public class oaMeAprovaledFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate,onItemClickListener {

    @BindView(R.id.nodata_layout)
    RelativeLayout nodatalayout;

    @BindView(R.id.nodata_text)
    TextView nodatatext;


    @BindView(R.id.fragment_aproval_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_aproval_bga)
    BGARefreshLayout bgaRefreshLayout;

    private oaMyAprovalActivity activity;

    private oaMyAprovalAdapter adapter;

    private List<oaAprovalSendLauchModel> list = new ArrayList<>();

    private String url = "";

    private int page_index = 1;

    private boolean hasmore = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (oaMyAprovalActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_aproval_aproval,null);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    //初始化
    private void init(){ 
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new oaMyAprovalAdapter(activity,list);
        recyclerView.setAdapter(adapter);
        nodatatext.setText("暂无内容");
        bgaRefreshLayout.setDelegate(this);
        adapter.setOnItemClickListener(this);
        activity.setRefresh(bgaRefreshLayout);
    }


    private void initParama(){
        url = Constant.URL_MY_APROVAL_APPLY;
        String user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
        String approve_type =  "1";//审批类型(1为已审核,2为待审核)
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+approve_type);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("approve_type",approve_type);
        map.put("page",page_index+"");
        map.put("F",Constant.F);
        map.put("V",Constant.V);
        map.put("RandStr",RandStr);
        map.put("Sign",sign);
         url = RequestUtils.getRequestUrl( url, map);
        Logger.e("OA——已审批的url",url);
        activity.OkgoRequest( url,callback());
    }

    private DialogCallback callback(){
        return new DialogCallback(activity) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                Logger.e("OA——已审批的异常了",e.getMessage());
                bgaRefreshLayout.endRefreshing();
                bgaRefreshLayout.endLoadingMore();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    bgaRefreshLayout.endRefreshing();
                    bgaRefreshLayout.endLoadingMore();
                    JSONObject object = new JSONObject(s);
                    Logger.e("OA——已审批的",s);
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
                        Toast.makeText(activity, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    if (list==null||list.size()==0){
                        nodatalayout.setVisibility(View.VISIBLE);
                    }else {
                        nodatalayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
            Toast.makeText(activity, "已加载完毕", Toast.LENGTH_SHORT).show();
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
                intent = new Intent(activity,oaApplyForLeaveDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("flag",0);
                startActivity(intent);
                break;

            case "2"://加班
                intent = new Intent(activity,oaWorkOverTimeDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title","加班");
                intent.putExtra("flag",0);
                startActivity(intent);
                break;

            case "3"://出差
                intent = new Intent(activity,oaBusinessTravelDetailActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("title","出差");
                intent.putExtra("flag",0);
                startActivity(intent);
                break;

            case "4"://报销
                intent = new Intent(activity,oaReimbursementDetailActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
                intent.putExtra("flag",0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initParama();
        list.clear();
    }
}
