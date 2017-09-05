package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.MeetingListAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.MeetingModule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MettingActivity;
import com.wyu.iwork.view.activity.WebActivity;
import com.wyu.iwork.view.activity.oaApplyDetailActivity;
import com.wyu.iwork.view.activity.oaReimbursementDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/5/23.15:30
 * 邮箱：2587294424@qq.com
 * 消息通知的通知fragment
 */

public class MessageNoticeFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate,onItemClickListener {

    private static String TAG = MessageNoticeFragment.class.getSimpleName();

    @BindView(R.id.fragment_message_bga)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.fragment_message_recyclerview)
    ListView recyclerView;

    @BindView(R.id.fragment_message_zanwu)
    RelativeLayout meeting_zanwu;

    private MettingActivity activity;

    private Gson gson;
    private MeetingModule mMeetingModule;
    private MeetingListAdapter adapter;
    private ArrayList<MeetingModule.Meeting> meetinglist = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MettingActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message_,null);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        adapter = new MeetingListAdapter(this,1,meetinglist);
        recyclerView.setAdapter(adapter);
        activity.setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
        adapter.setOnItemClickListener(this);
    }

    private void getMeetingDataFromServer(){
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_MEETING_LIST,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        bgaRefreshLayout.endRefreshing();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        bgaRefreshLayout.endRefreshing();
                        Logger.i(TAG,s);
                        parseData(s);
                    }
                });
    }

    private void parseData(String s){
        try {
            if(gson == null){
                gson = new Gson();
            }
            mMeetingModule = gson.fromJson(s,MeetingModule.class);
            if(mMeetingModule.getCode() == 0){
                if(mMeetingModule.getData() != null && mMeetingModule.getData().size() >= 0){
                    meetinglist.addAll(mMeetingModule.getData());
                    adapter.notifyDataSetChanged();
                    if (meetinglist.size()>0){
                        showContent(true);
                    }
                }else{
                }
            }else{
                MsgUtil.shortToastInCenter(getActivity(),mMeetingModule.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        meetinglist.clear();
        adapter.notifyDataSetChanged();
        getMeetingDataFromServer();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    public void showContent(boolean flag){
        meeting_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        meetinglist.clear();
        adapter.notifyDataSetChanged();
        getMeetingDataFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter!=null){
            adapter.closeSwipe();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        adapter. closeSwipe();
        Logger.e("onItemClickListener",position+"");
        MeetingModule.Meeting item = meetinglist.get(position);
        String apply_type = item.getApply_type();// 0:会议 1:请假 2:加班 3:出差 4:报销
        String type = item.getType();//  0:我发起的 1:待我审批的 2:我已经审批的 3:抄送我的
        String detail_url = item.getDetail_url();//  当apply_type为0时显示会议详情页面URL地址,否则显示关联id

        Intent intent = null;
        switch (apply_type){

            case "0":
                intent = new Intent(activity,WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",item.getDetail_url());
                intent.putExtras(bundle);
                break;
            case "1":
                intent = new Intent(activity, oaApplyDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", detail_url);
                intent.putExtra("title", "请假");
                switch (type){
                    case "0":
                        intent.putExtra("flag", 0);
                        break;

                    case "1":
                        intent.putExtra("flag", 1);
                        break;

                    case "2":
                        intent.putExtra("flag", 0);
                        break;
                    case "3":
                        intent.putExtra("flag", -1);
                        break;

                }

                intent.putExtras(intent);
                break;

            case "2":
                intent = new Intent(activity, oaApplyDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", detail_url);
                intent.putExtra("title", "加班");
                switch (type){
                    case "0":
                        intent.putExtra("flag", 0);
                        break;

                    case "1":
                        intent.putExtra("flag", 1);
                        break;

                    case "2":
                        intent.putExtra("flag", 0);
                        break;
                    case "3":
                        intent.putExtra("flag", -1);
                        break;

                }
                intent.putExtras(intent);
                break;

            case "3":
                intent = new Intent(activity, oaApplyDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", detail_url);
                intent.putExtra("title", "出差");
                switch (type){
                    case "0":
                        intent.putExtra("flag", 0);
                        break;

                    case "1":
                        intent.putExtra("flag", 1);
                        break;

                    case "2":
                        intent.putExtra("flag", 0);
                        break;
                    case "3":
                        intent.putExtra("flag", -1);
                        break;

                }
                intent.putExtras(intent);
                break;

            case "4":
                intent = new Intent(activity, oaReimbursementDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", detail_url);
                intent.putExtra("title", "报销");
                switch (type){
                    case "0":
                        intent.putExtra("flag", 0);
                        break;

                    case "1":
                        intent.putExtra("flag", 1);
                        break;

                    case "2":
                        intent.putExtra("flag", 0);
                        break;
                    case "3":
                        intent.putExtra("flag", -1);
                        break;

                }
                intent.putExtras(intent);
                break;
        }

        intent.putExtra("meetingId",item.getId());
        startActivity(intent);

    }
}
