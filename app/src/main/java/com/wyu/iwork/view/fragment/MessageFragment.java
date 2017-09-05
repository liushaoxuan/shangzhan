package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.RongImListAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.model.UserModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.DynamicActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.MettingActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;

/**
 * Created by sxliu on 2017/4/7.
 * 消息
 */

public class MessageFragment extends Fragment{

    @BindView(R.id.fragment_home_message_dynamic_metting_layout)
    LinearLayout linearLayout;
    @BindView(R.id.rong_container)
    FrameLayout frameLayout;
    /**
     * 我的动态
     */
    @BindView(R.id.fragment_home_message_my_dynamic)
    RelativeLayout my_dynamic;
    /**
     * 我的头像
     */
    @BindView(R.id.home_message_fragment_my_head)
    ImageView my_head;

    /**
     * 我的动态_红点
     */
    @BindView(R.id.fragment_home_message_has_dynamic)
    ImageView has_dynamic;

    /**
     * 会议通知
     */
    @BindView(R.id.fragment_home_message_meetting_note)
    RelativeLayout meetting_note;

    private MainActivity activity;
    private UserInfo userInfo;

    private List<UserModel> users;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        userInfo = MyApplication.userInfo;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_message, container, false);
        ButterKnife.bind(this, view);
        Glide.with(getActivity()).load(userInfo.getUser_face_img()).transform(new CenterCrop(getActivity()), new GlideRoundTransform(getActivity(), 5)).placeholder(R.mipmap.head_icon_nodata).into(my_head);
        return view;
    }

    private void ConverList() {
        //会话列表
        ConversationListFragment conversationListFragment = new ConversationListFragment();
        conversationListFragment.setAdapter(new RongImListAdapter(getActivity(),users));
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist").appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//设置系统会话，该会话非聚合显示
                .build();
        conversationListFragment.setUri(uri);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.rong_container, conversationListFragment);
        transaction.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        getConverlist();
    }

    private void getConverlist() {
        List<Conversation> list = RongIM.getInstance().getConversationList();
        if (list == null || list.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
        } else {
            List<String> userslist = new ArrayList<>();
            linearLayout.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
            for (int i = 0;i<list.size();i++){
                userslist.add(list.get(i).getTargetId());
                list.get(i).getLatestMessage().getSearchableWord().get(0);
                list.get(i).getSentTime();
                RongIMClient.getInstance().getConversation(Conversation.ConversationType.PRIVATE, list.get(i).getTargetId(), new RongIMClient.ResultCallback<Conversation>() {
                    @Override
                    public void onSuccess(Conversation conversation) {
                        Logger.e("------msg-----",conversation.getLatestMessage().getSearchableWord().get(0)) ;

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
            getUsers(JSON.toJSONString(userslist));
//            RongIMClient.getInstance().searchConversations();

        }


    }

    @OnClick({R.id.fragment_home_message_my_dynamic, R.id.fragment_home_message_meetting_note})
    void Click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.fragment_home_message_my_dynamic://我的动态
                intent = new Intent(getActivity(), DynamicActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_home_message_meetting_note://会议通知
                intent = new Intent(getActivity(), MettingActivity.class);
                 startActivity(intent);
                break;
        }
    }


    //获取用户头像信息接口
    private void getUsers(String users_json){
        String url = Constant.URL_GET_USER_HEAD_NAME;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id );
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("users", users_json);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.get(murl)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONArray data = object.optJSONArray("data");
                                users = JSON.parseArray(data.toString(),UserModel.class);
                                ConverList();

                            } else {
                                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.e("response------------>", s);
                    }
                });
    }
}
