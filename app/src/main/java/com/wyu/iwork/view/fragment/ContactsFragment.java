package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AZAdapter;
import com.wyu.iwork.adapter.ContactsListviewAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RecyclerviewScollUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.InvitaFriendsActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.OrganizationalStructureActivity;
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
 * Created by sxliu on 2017/4/11.
 * 通讯录
 */

public class ContactsFragment extends Fragment implements AdapterView.OnItemClickListener, BGARefreshLayout.BGARefreshLayoutDelegate,
        TextWatcher,View.OnTouchListener{
    /**
     * 搜索框
     */
    @BindView(R.id.home_contacts_search)
    EditText search;

    /**
     * 搜索框fuqin
     */
    @BindView(R.id.home_contacts_search_linearlayout)
    LinearLayout linearLayout;
    /**
     * Scroolview
     */
    @BindView(R.id.home_contacts_scrollview)
    ScrollView scrollView;

    /**
     * 邀请好友
     */
    @BindView(R.id.home_contacts_invite)
    RelativeLayout invite;

    /**
     * 联系人列表
     */
    @BindView(R.id.home_contacts_mylistview)
    MyListView contactrecyclerview;
    /**
     * 字母列表
     */
    @BindView(R.id.home_contacts_listview)
    ListView listview;
    /**
     * 刷新控件
     */
    @BindView(R.id.home_contacts_refreshview)
    BGARefreshLayout refreshLayout;
    /**
     * 无数据
     */
    @BindView(R.id.home_contacts_nodata)
    LinearLayout nodata_layout;

    //组织架构  管理员权限显示
    @BindView(R.id.home_contacts_orgnize)
    RelativeLayout orhnize;
    //字母适配器
    private AZAdapter azAdapter;
    //联系人适配器
    private ContactsListviewAdapter adapter;
    private List<ContactModel> datas;
    private List<ContactModel> contactlist;
    private String AzString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private List<String> Azlist;
    private UserInfo userInfo;

    private MainActivity activity;
    private RecyclerviewScollUtil recyclerviewScollUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactlist = new ArrayList<>();
        datas = new ArrayList<>();
        userInfo = MyApplication.userInfo;
        activity = (MainActivity) getActivity();
        recyclerviewScollUtil = new RecyclerviewScollUtil(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_contacts, container, false);
        ButterKnife.bind(this, view);
        initAzlist();
        azAdapter = new AZAdapter(getActivity(), Azlist);
        listview.setAdapter(azAdapter);
        listview.setOnItemClickListener(this);
        adapter = new ContactsListviewAdapter(getActivity(), contactlist);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        contactrecyclerview.setAdapter(adapter);
        activity.setRefresh(refreshLayout);
        refreshLayout.setDelegate(this);
        search.addTextChangedListener(this);
        return view;
    }

    @OnClick({R.id.home_contacts_invite,R.id.home_contacts_orgnize})
    void Click(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.home_contacts_invite://邀请好友
                intent = new Intent(getActivity(), InvitaFriendsActivity.class);
                startActivity(intent);
                break;

            case R.id.home_contacts_orgnize://组织架构
                intent = new Intent(getActivity(), OrganizationalStructureActivity.class);
                startActivity(intent);
                break;
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void initAzlist() {
        Azlist = new ArrayList<>();
        for (int i = 0; i < AzString.length(); i++) {
            Azlist.add(AzString.charAt(i) + "");
        }
    }

    //获取联系人列表
    private void getContacts() {
//        loadingDialog.show(this.getFragmentManager(), "ContactsFragment");
        String url = Constant.URL_CONTACTS_LIST2;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
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

        Logger.e("murl",murl);
        OkGo.get(murl)
                .tag("ContactsFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        refreshLayout.endRefreshing();
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            refreshLayout.endRefreshing();//停止刷新
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            Logger.e("constacts",s);
                            if ("0".equals(code)) {
                                contactlist.clear();
                                datas.clear();
                                JSONArray data = object.optJSONArray("data");
                                String Py = "***";
                                ContactModel model = null;
                                for (int i = 0;i<data.length();i++){
                                    JSONObject Contact = data.getJSONObject(i);
                                    String user_id = Contact.optString("user_id");
                                    String real_name = Contact.optString("real_name");
                                    String phone = Contact.optString("phone");
                                    String face_img = Contact.optString("face_img");
                                    String pinyin = Contact.optString("pinyin");
                                    model = new ContactModel(user_id,real_name,phone,face_img,pinyin);
                                    if (!Py.equals(pinyin)){
                                        model.setFirstPY(pinyin);
                                    }
                                    Py = pinyin;
                                    contactlist.add(model);
                                    datas.add(model);
                                    model = null;
                                }
                                adapter.notifyDataSetChanged();
                                if (contactlist==null||contactlist.size()==0){
                                    nodata_layout.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
                                if (contactlist==null||contactlist.size()==0){
                                    nodata_layout.setVisibility(View.VISIBLE);
                                }
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
        getContacts();
    }

    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        search.setText("");
        getContacts();
    }

    //加载更多
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()==0){
            contactlist.clear();
            contactlist.addAll(datas);
            adapter.notifyDataSetChanged();
        }
        if (s.length()>0){
            contactlist.clear();
            for(int i = 0;i<datas.size();i++){
                //进行遍历   若有相同的  则提取到搜索列表中
                if(datas.get(i).getReal_name().indexOf(search.getText().toString()) != -1 ||
                        datas.get(i).getPhone().indexOf(search.getText().toString()) != -1){
                    contactlist.add(datas.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        activity.Hideinputwindown(v);
        return false;
    }

}
