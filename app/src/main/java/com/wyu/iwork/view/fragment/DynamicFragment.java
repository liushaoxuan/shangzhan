package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.DynamicAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.BuildDynamicActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.dialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by lx on 2016/12/20.
 * 首页——动态
 */

public class DynamicFragment extends Fragment {

    /**
     * 大图
     */
    @BindView(R.id.frag_dynamic_image)
    ImageView bigImage;

    /**
     * 头像
     */
    @BindView(R.id.fragment_dynamic_person_icon)
    CircleImageView userHead;

    /**
     * 用户名
     */
    @BindView(R.id.fragment_dynamic_person_name)
    TextView userName;

    /**
     * RecyclerView
     */
    @BindView(R.id.fragment_dynamic_recycleview)
    RecyclerView recyclerView;

    private View rootView;
    private DynamicAdapter adapter;
    private MainActivity mainActivity;
    private List<DynamicBean> dynamicBeanList;

    //进度条
    LoadingDialog loadingDialog;

    private UserInfo userInfo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        dynamicBeanList = new ArrayList<>();
        loadingDialog = new LoadingDialog();
        userInfo = MyApplication.userInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dynamic, null);
            ButterKnife.bind(this, rootView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new DynamicAdapter(DynamicFragment.this, dynamicBeanList);
            recyclerView.setAdapter(adapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
        setHasOptionsMenu(true);
        bindDatas();
        return rootView;
    }

    /**
     * 绑定数据
     */
    private void bindDatas() {
        Glide.with(this).load(userInfo == null ? "" : userInfo.getUser_top_img()).placeholder(R.mipmap.image).into(bigImage);
        Glide.with(this).load(userInfo == null ? "" : userInfo.getUser_face_img()).placeholder(R.mipmap.head_icon_nodata).into(userHead);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.dynamic_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_dynamic:
                Intent intent = new Intent(getActivity(), BuildDynamicActivity.class);
                getActivity().startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 获取动态列表
     */
    private void getDynamic() {
        loadingDialog.show(this.getFragmentManager(), "Dynamicfragment");
        String url = Constant.URL_DYNAMIC_LIST;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String min_did = "0";
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
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONArray data = object.optJSONArray("data");
                                dynamicBeanList = JSON.parseArray(data.toString(), DynamicBean.class);
                                adapter.setDatas(dynamicBeanList);
                                adapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                        Logger.e("response------------>", s);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getDynamic();
    }

}
