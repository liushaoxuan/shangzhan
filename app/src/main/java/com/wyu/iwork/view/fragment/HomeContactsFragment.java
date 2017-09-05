package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ContactInviteAdapter;
import com.wyu.iwork.adapter.ContactsAdapter;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import io.rong.imkit.mention.SideBar;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/8/23.13:45
 * 邮箱：2587294424@qq.com
 * 首页——通讯录
 */

public class HomeContactsFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate ,SideBar.OnTouchingLetterChangedListener{
    private static String TAG = HomeContactsFragment.class.getSimpleName();

    /**
     * 刷新控件
     */
    @BindView(R.id.fragment_home_contact_refreshview)
    BGARefreshLayout refreshview;

    /**
     * recyclerview
     */
    @BindView(R.id.fragment_home_contact_recyclerview)
    RecyclerView recyclerView;
    /**
     * recyclerview
     */
    @BindView(R.id.fragment_home_contact_sidebar)
    SideBar sidebar;

    /**
     * 无数据
     */
    @BindView(R.id.home_contact_nodata)
    LinearLayout nodata_layout;

    private List<ContactModel> datas;
    private List<ContactModel> contactlist;
    private ContactInviteAdapter inviteAdapter;//邀请好友和组织架构的适配器
    private ContactsAdapter adapter;//正真联系人的适配器
    private DelegateAdapter delegateAdapter;//vlayout的adapter
    private MainActivity activity;
    private VirtualLayoutManager layoutManager; // 布局管理器
    private List<LayoutHelper> helpers = new ArrayList<>();
    private List<DelegateAdapter.Adapter> adapters = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        datas = new ArrayList<>();
        contactlist = new ArrayList<>();
        initAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_contact, container, false);
        ButterKnife.bind(this, view);
        layoutManager = new VirtualLayoutManager(activity); // 创建VirtualLayoutManager对象
        recyclerView.setLayoutManager(layoutManager);
        //绑定delegateAdapter
        delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapters(adapters);
        recyclerView.setAdapter(delegateAdapter);
        activity.setRefresh(refreshview);
        refreshview.setPullDownRefreshEnable(true);
        refreshview.setDelegate(this);
        getContacts();
        sidebar.setOnTouchingLetterChangedListener(this);
        return view;
    }

    /**
     * 初始化adapter
     */
    private void initAdapter() {
        //设置线性布局
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setItemCount(1);
        helpers.add(linearLayoutHelper);
        inviteAdapter = new ContactInviteAdapter(this, linearLayoutHelper);
        adapters.add(inviteAdapter);
        adapter = new ContactsAdapter(this, contactlist, linearLayoutHelper);
        adapters.add(adapter);
    }

    /**
     * 获取联系人信息
     */
    private void getContacts() {
        String url = Constant.URL_CONTACTS_LIST2;
        String user_id = null;
        user_id = activity.userInfo == null ? "" : activity.userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        Logger.e("murl", murl);

        OkGo.get(murl)
                .tag(TAG)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.e(TAG, e.getMessage());
                        refreshview.endRefreshing();
                        nodata_layout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            refreshview.endRefreshing();//停止刷新
                            JSONObject object = new JSONObject(s);
                            int code = object.optInt("code");
                            Logger.e(TAG, s);
                            if (code == 0) {
                                contactlist.clear();
                                datas.clear();
                                JSONArray data = object.optJSONArray("data");
                                String Py = "***";
                                ContactModel model = null;
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject Contact = data.getJSONObject(i);
                                    String user_id = Contact.optString("user_id");
                                    String real_name = Contact.optString("real_name");
                                    String phone = Contact.optString("phone");
                                    String face_img = Contact.optString("face_img");
                                    String pinyin = Contact.optString("pinyin");
                                    model = new ContactModel(user_id, real_name, phone, face_img, pinyin);
                                    if (!Py.equals(pinyin)) {
                                        model.setFirstPY(pinyin);
                                    }
                                    Py = pinyin;
                                    contactlist.add(model);
                                    datas.add(model);
                                    model = null;
                                }
                            }
                            adapter.notifyDataSetChanged();
                            if (contactlist != null && contactlist.size() > 0) {
                                nodata_layout.setVisibility(View.GONE);
                            }else {
                                nodata_layout.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            nodata_layout.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
       inviteAdapter.cleanSearch();
        getContacts();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    public void getSearch(Editable s,String content){
        if (s.length()==0){
            contactlist.clear();
            contactlist.addAll(datas);
            adapter.notifyDataSetChanged();
        }
        if (s.length()>0){
            contactlist.clear();
            for(int i = 0;i<datas.size();i++){
                //进行遍历   若有相同的  则提取到搜索列表中
                if(datas.get(i).getReal_name().indexOf(content) != -1 ||
                        datas.get(i).getPhone().indexOf(content) != -1){
                    contactlist.add(datas.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }

//        layoutManager.scrollToPositionWithOffset();
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        inviteAdapter.cleanSearch();
        for (int i=0;i<contactlist.size();i++){
            if (s.equals(contactlist.get(i).getFirstPY())){
                layoutManager.scrollToPositionWithOffset(i+1,0);
            }
        }
    }
}
