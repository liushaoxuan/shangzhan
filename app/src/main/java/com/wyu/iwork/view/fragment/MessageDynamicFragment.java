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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.MessageDynamicAdapter;
import com.wyu.iwork.adapter.MylistviewAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RecyclerviewScollUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.BuildDynamicActivity;
import com.wyu.iwork.view.activity.DynamicActivity;
import com.wyu.iwork.view.dialog.LoadingDialog;
import com.wyu.iwork.widget.MyListView;

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
 * Created by lx on 2016/12/20.
 * 首页——动态
 */

public class MessageDynamicFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate{

    /**
     * 大背景图
     */
//    @BindView(R.id.activity_dynamic_big_image)
//    ImageView big_image;
    /**
     * 头像
     */
    @BindView(R.id.activity_dynamic_head)
    ImageView head;
    /**
     * 昵称
     */
    @BindView(R.id.activity_dynamic_nikename)
    TextView nikeName;
    /**
     * recyclerView
     */
    @BindView(R.id.activity_dynamic_recyclerview)
    ListView recyclerView;
    @BindView(R.id.activity_dynamic_refreshview)
    BGARefreshLayout refreshLayout;

    private MylistviewAdapter adapter;
    private List<DynamicBean> dynamicBeanList;
    private UserInfo userInfo;
    private DynamicActivity activity;

    private int page_index = 1;//页码
    private RecyclerviewScollUtil recyclerviewScollUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dynamicBeanList = new ArrayList<>();
        userInfo = MyApplication.userInfo;
        activity = (DynamicActivity) getActivity();
        recyclerviewScollUtil = new RecyclerviewScollUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_dynamic, null);
        ButterKnife.bind(this, rootView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        setListViewHeightBasedOnChildren(recyclerView);
        activity.setRefresh(refreshLayout);
        refreshLayout.setDelegate(this);
        bindDatas();
        return rootView;
    }

    /**
     * 绑定数据
     */
    private void bindDatas() {
//        Glide.with(this).load(userInfo == null ? "" : userInfo.getUser_top_img()).placeholder(R.mipmap.image).into(big_image);
        Glide.with(this).load(userInfo == null ? "" : userInfo.getUser_face_img()).placeholder(R.mipmap.head_icon_nodata).into(head);
        nikeName.setText(userInfo==null?"":userInfo.getReal_name());
    }

    @OnClick({R.id.activity_dynamic_back,R.id.activty_dynamic_release})
    void Click(View v){
        switch (v.getId()){
            case R.id.activity_dynamic_back:
                getActivity().onBackPressed();
                break;
            case R.id.activty_dynamic_release://发布动态
                Intent intent = new Intent(getActivity(),BuildDynamicActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取动态列表
     */
    private void getDynamic() {
        String url = Constant.URL_DYNAMIC_LIST;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String min_did = page_index+"";
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id + min_did);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("min_did", min_did);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.get(murl)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                            refreshLayout.endLoadingMore();
                            refreshLayout.endRefreshing();
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {

                                refreshLayout.endLoadingMore();
                                refreshLayout.endRefreshing();

                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONArray data = object.optJSONArray("data");
                                List<DynamicBean> templist = new ArrayList<DynamicBean>();
                                templist = JSON.parseArray(data.toString(), DynamicBean.class);
                                dynamicBeanList.addAll(templist);
                                adapter = new MylistviewAdapter(getActivity(), dynamicBeanList);
                                recyclerView.setAdapter(adapter);
                                setListViewHeightBasedOnChildren(recyclerView);
                                templist.clear();
                                templist = null;

                            } else {
                                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Logger.e("response------------>", s);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        dynamicBeanList.clear();
        page_index = 1;
        getDynamic();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        dynamicBeanList.clear();
        page_index = 1;
        getDynamic();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        page_index++;
        getDynamic();
        return false;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
