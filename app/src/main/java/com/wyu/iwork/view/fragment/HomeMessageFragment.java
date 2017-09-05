package com.wyu.iwork.view.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AssistantAdapter;
import com.wyu.iwork.adapter.MessageAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.AssistantModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.model.UserModel;
import com.wyu.iwork.util.CacheKey;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.NetUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sxliu on 2017/4/7.
 * 消息
 */

public class HomeMessageFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    /**
     * 通知助手recyclerview
     */
    @BindView(R.id.fragment_home_message_assistant)
    RecyclerView assistant_recyclerview;

    /**
     * 融云聊天recyclerview
     */
    @BindView(R.id.fragment_home_message_recyclerview)
    RecyclerView recyclerView;
    /**
     * 刷新控件
     */
    @BindView(R.id.fragment_home_message_refreshview)
    BGARefreshLayout refreshLayout;

    private MainActivity activity;
    private UserInfo userInfo;

    private List<UserModel> users;
    private List<Conversation> conversationList;
    private MessageAdapter messageAdapter;
    private AssistantAdapter assistantAdapter;

    private List<AssistantModel> assistantlist = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        userInfo =  MyApplication.userInfo;
        users = new ArrayList<>();
        conversationList = new ArrayList<>();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, view);
        activity.setRefresh(refreshLayout);
        refreshLayout.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        messageAdapter = new MessageAdapter(getActivity(), users, conversationList);
        recyclerView.setAdapter(messageAdapter);
        assistant_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void onResume() {
        super.onResume(); 
        assistantlist = DataSupport.order("time desc").find(AssistantModel.class);
        if (assistantlist==null ||assistantlist.size()==0){
            addAssistant();
        }
        assistantAdapter=null;
        assistantAdapter = new AssistantAdapter(this,assistantlist);
        assistant_recyclerview.setAdapter(assistantAdapter);
        getConverlist();
        /**
         * 所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
         */
        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                getConverlist();
                return false;
            }
        });
    }

    private void getConverlist() { 
        List<Conversation> list = RongIM.getInstance().getConversationList();
        if (list == null || list.size() == 0) {
            refreshLayout.endRefreshing();
        } else {
            List<String> userslist = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Message.SentStatus ststus =  list.get(i).getSentStatus();
                if (ststus== Message.SentStatus.SENT){

                }
                userslist.add(list.get(i).getTargetId());
            }
            conversationList.clear();
            conversationList.addAll(list == null ? new ArrayList<Conversation>() : list);
            getUsers(JSON.toJSONString(userslist));
        }


    }



    //获取用户头像信息接口
    private void getUsers(String users_json) {
        String url = Constant.URL_GET_USER_HEAD_NAME;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("users", users_json);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        activity.doRequestget(murl, CacheKey.HOMEMESSAGE, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                refreshLayout.endRefreshing();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                getdataSuccess(s);
            }

            @Override
            public void onCacheSuccess(String s, Call call) {
                getdataSuccess(s);
            }
        });


    }

    // 获取数据的结果
    private void getdataSuccess(String s){
        refreshLayout.endRefreshing();
        try {
            JSONObject object = new JSONObject(s);
            String code = object.optString("code");
            if ("0".equals(code)) {
                JSONArray data = object.optJSONArray("data");
                List<UserModel> mlist = new ArrayList<UserModel>();
                mlist = JSON.parseArray(data.toString(), UserModel.class);
                users.clear();
                users.addAll(mlist);
                messageAdapter.notifyDataSetChanged();
                mlist.clear();
                mlist = null;

            } else {
                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.e("response------------>", s);
    }

    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        boolean isconnetct = NetUtil.checkNetwork(getActivity());
        if (isconnetct) {
            getConverlist();
        } else {
           refreshLayout.endRefreshing();
        }

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    /**
     * 第一次 添加消息页面的助手信息
     */
    private void addAssistant(){
        if (assistantlist==null){
            assistantlist = new ArrayList<>();
        }
        assistantlist.add(new AssistantModel(0x00001,R.mipmap.icon_message_task,getActivity().getResources().getString(R.string.task_assistant),"暂无任务通知",System.currentTimeMillis(),0 ));
        assistantlist.add(new AssistantModel(0x00002,R.mipmap.icon_message_metting,getActivity().getResources().getString(R.string.metting_assistant),"暂无会议通知",System.currentTimeMillis(),0 ));
        assistantlist.add(new AssistantModel(0x00003,R.mipmap.icon_message_notice,getActivity().getResources().getString(R.string.notice_assistant),"暂无公告通知",System.currentTimeMillis(),0 ));
        assistantlist.add(new AssistantModel(0x00004,R.mipmap.icon_message_aproval,getActivity().getResources().getString(R.string.approval_assistant),"暂无审批信息",System.currentTimeMillis(),0 ));
        //        assistantlist.add(new AssistantModel(0x00005,R.mipmap.icon_message_schedule,getActivity().getResources().getString(R.string.schedule_assistant),"",System.currentTimeMillis(),0 ));
        DataSupport.saveAll(assistantlist);

    }

    /**
     * 更新助手的状态
     */
    public  void updateAssistant(int position){
        assistantlist.get(position).setNotes(0);
        ContentValues values = new ContentValues();
        values.put("notes",0);
        DataSupport.update(AssistantModel.class,values,assistantlist.get(position).getId());
    }
}
