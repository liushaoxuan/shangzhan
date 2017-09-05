package com.wyu.iwork.view.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.huajianjiang.baserecyclerview.view.BaseRecyclerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.DynamicDetailAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.IDynamicDetailView;
import com.wyu.iwork.model.DynamicDetailBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.DynamicDetailPresenterCompl;
import com.wyu.iwork.presenter.IDynamicDetailPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.view.activity.DynamicDetailActivity;
import com.wyu.iwork.view.activity.PostingDetailsActivity;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/2/7.
 * 动态详情
 */

public class DynamicDetailFragment extends Fragment implements IDynamicDetailView {

    /**
     * 点赞标志(点赞数不为0时显示)
     */
    @BindView(R.id.activity_dynamic_detail_zan)
    ImageView haszan;

    /**
     * 点赞人昵称
     */
    @BindView(R.id.activity_dynamic_detail_zan_usernames)
    TextView likepersonsName;

    /**
     * recyclearview
     */
    @BindView(R.id.activity_dynamic_detail_listview)
    BaseRecyclerView listView;

    //头像
    @BindView(R.id.item_message_dynamic_head)
   ImageView headIcon;
    //名称
    @BindView(R.id.item_message_dynamic_releaser)
    TextView userName;
    //时间
    @BindView(R.id.item_message_dynamic_time)
    TextView time;
    //内容
    @BindView(R.id.item_message_dynamic_content)
    TextView content;
    //点赞数
    @BindView(R.id.message_dynamic_detail_praise_num)
    TextView goodNum;
    //评论数
    @BindView(R.id.message_dynamic_detail_comment_num)
    TextView commentNum;

    //点赞的父布局容器
    @BindView(R.id.activity_dynamic_detail_zan_layout)
    LinearLayout zan_layout;
    //点赞
    @BindView(R.id.message_dynamic_detail_praise)
    LinearLayout praise;
    //评论列表layout
    @BindView(R.id.activity_dynamic_detail_comments_layout)
    LinearLayout commentslayout;
    private DynamicDetailAdapter adapter;

    //动态ID
    private String Id;

    //动态详情数据
    private DynamicDetailBean detailbean;

    IDynamicDetailPresenter iDynamicDetailPresenter;
    UserInfo userInfo;

    private MyAlertDialog myDialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = MyApplication.userInfo;
        iDynamicDetailPresenter = new DynamicDetailPresenterCompl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dynamic_detail,null);
        ButterKnife.bind(this,view);
        Id = ((DynamicDetailActivity)getActivity()).get_Id();
        return view;
    }


    /**
     * 注入点击事件
     */
    @OnClick({ R.id.message_dynamic_detail_praise, R.id.message_dynamic_detail_comment,R.id.activity_dynamic_detail_back,R.id.item_message_dynamic_head})
    void Click(View view) {
        switch (view.getId()) {

            case R.id.activity_dynamic_detail_back:
                getActivity().onBackPressed();
                break;
            case R.id.message_dynamic_detail_praise://点赞
                iDynamicDetailPresenter.doPraise(Id);
                break;

            case R.id.message_dynamic_detail_comment://评论
                iDynamicDetailPresenter.doComment(Id);
                break;
            case R.id.item_message_dynamic_head://头像

                if (myDialog!=null){
                    myDialog.show();
                }
                break;
        }
    }

    @Override
    public void goback() {

    }

    //点赞
    @Override
    public void doPraise(String dynamic_id) {
        String url = Constant.URL_PRAISE;
        String F = Constant.F;
        String V = Constant.V;
        String user_id = userInfo==null?"":userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + dynamic_id + user_id);

        OkGo.get(url)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("dynamic_id", dynamic_id)
                .params("user_id", user_id)
                .params("F", F)
                .params("V", V)
                .params("RandStr", RandStr)
                .params("Sign", sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                getDynamicDetail();
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

    //评论
    @Override
    public void doComment(String dynamic_id) {
        Intent intent = new Intent(getActivity(), PostingDetailsActivity.class);
        intent.putExtra("dynamic_id", dynamic_id);
        startActivity(intent);
    }

    @Override
    public void deleteComment(String comment_id, int position) {
        String url = Constant.URL_DEL_DYNAMIC;
        String F = Constant.F;
        String V = Constant.V;
        String user_id = userInfo==null?"":userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + comment_id+ user_id);
        OkGo.get(url)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("comment_id", comment_id)
                .params("user_id", user_id)
                .params("F", F)
                .params("V", V)
                .params("RandStr", RandStr)
                .params("Sign", sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                getDynamicDetail();
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

    /**
     * 绑定数据
     */
    private void bindData() {
        Glide.with(this).load(detailbean.getUser_face_img()).transform(new CenterCrop(getActivity()), new GlideRoundTransform(getActivity(), 5)).placeholder(R.mipmap.head_icon_nodata).into(headIcon);
        userName.setText(detailbean.getNick_name());
        content.setText(detailbean.getText());
        time.setText(detailbean.getAdd_time());
        goodNum.setText(detailbean.getCount_praise());
        commentNum.setText(detailbean.getCount_comment());
        String names = "";
        for (int i = 0; i < detailbean.getPraise_list().size(); i++) {
            if (i == 0) {
                names = detailbean.getPraise_list().get(i).getNick_name();
            } else {
                names = names + "," + detailbean.getPraise_list().get(i).getNick_name();
            }
        }
        likepersonsName.setText(names);
        setAdapter();
        //是否有人点赞
        if (detailbean.getPraise_list() == null || detailbean.getPraise_list().size() == 0) {
            zan_layout.setVisibility(View.GONE);
        }else {
            zan_layout.setVisibility(View.VISIBLE);
        }

        //我是否点赞
        if ("1".equals(detailbean.getIs_praise())){
            praise.setSelected(true);
            goodNum.setTextColor(content.getResources().getColor(R.color.dynamic_releaser_color));
        }else {
            praise.setSelected(false);
            goodNum.setTextColor(content.getResources().getColor(R.color.blackb4));
        }
        //是否有评论
        if (detailbean.getComment_list()==null||detailbean.getComment_list().size()==0){
            commentslayout.setVisibility(View.GONE);
        }else {
            commentslayout.setVisibility(View.VISIBLE);
        }

        myDialog = new MyAlertDialog(getActivity(),detailbean.getUser_id(),detailbean.getUser_phone(),detailbean.getNick_name());
    }


    private void setAdapter() {
        adapter = new DynamicDetailAdapter(this, detailbean.getComment_list());
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listView.setAdapter(adapter);
    }


    //删除提醒
    public void DeleteComment(final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        View mpopView = getActivity().getLayoutInflater().inflate(R.layout.item_dynamic_detail_pop, null);
        TextView delete = (TextView) mpopView.findViewById(R.id.item_dynamic_detail_delete);
        TextView cancel = (TextView) mpopView.findViewById(R.id.item_dynamic_detail_cancel);
        alertDialog.setView(mpopView);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //删除评论  只能自己删除自己的，删除不聊别人的
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDynamicDetailPresenter.deleteComment(detailbean.getComment_list().get(position).getId(),position);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /**
     * 获取动态详情
     */
    private void getDynamicDetail() {
        String url = Constant.URL_DYNAMIC_DETAIL;
        String F = Constant.F;
        String V = Constant.V;
        String user_id =userInfo==null?"":userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + Id+user_id);

        OkGo.get(url)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("dynamic_id", Id)
                .params("user_id", user_id)
                .params("F", F)
                .params("V", V)
                .params("RandStr", RandStr)
                .params("Sign", sign)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONObject data = object.optJSONObject("data");
                                detailbean = JSON.parseObject(data.toString(), DynamicDetailBean.class);
                                bindData();
                                Logger.e("data------------>", data.toString());
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
        getDynamicDetail();
    }
}
