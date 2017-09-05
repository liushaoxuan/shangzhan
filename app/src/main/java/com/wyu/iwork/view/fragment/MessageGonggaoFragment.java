package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.MeetinggonggaoAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.DepartmentModel;
import com.wyu.iwork.model.MessageGonggaoModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.DepartmentManagerActivity;
import com.wyu.iwork.view.activity.MettingActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/5/23.15:30
 * 邮箱：2587294424@qq.com
 * 消息通知的公告fragment
 */

public class MessageGonggaoFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    private static String TAG = MessageGonggaoFragment.class.getSimpleName();

    @BindView(R.id.fragment_message_bga)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.fragment_message_recyclerview)
    ListView recyclerView;

    @BindView(R.id.fragment_message_zanwu)
    RelativeLayout meeting_zanwu;

    private MettingActivity activity;

    MeetinggonggaoAdapter adapter;
    private List<MessageGonggaoModel> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MettingActivity) getActivity();
        list = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message_, null);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        adapter = new MeetinggonggaoAdapter(this, list);
        recyclerView.setAdapter(adapter);
        activity.setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
    }

    private void initParama() {
        String url = Constant.URL_ALL_NOTICE;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F + V + RandStr + MyApplication.userInfo.getUser_id());
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("F", F);
        data.put("V", V);
        data.put("RandStr", RandStr);
        data.put("Sign", Sign);
        url = RequestUtils.getRequestUrl(url, data);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        bgaRefreshLayout.endRefreshing();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            bgaRefreshLayout.endRefreshing();
                            JSONObject object = new JSONObject(s);
                            Logger.e(TAG, s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONArray data = object.optJSONArray("data");
                                List<MessageGonggaoModel> templist = new ArrayList<MessageGonggaoModel>();
                                templist = JSON.parseArray(data.toString(), MessageGonggaoModel.class);
                                list.clear();
                                list.addAll(templist);
                                adapter.notifyDataSetChanged();
                                templist.clear();
                                if (list.size() > 0) {
                                    showContent(true);
                                }
                                templist = null;

                            } else {
                                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        list.clear();
        adapter.notifyDataSetChanged();
        initParama();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    public void showContent(boolean flag) {
        meeting_zanwu.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        adapter.notifyDataSetChanged();
        initParama();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter!=null){
            adapter.closeSwipe();
        }
    }
}
