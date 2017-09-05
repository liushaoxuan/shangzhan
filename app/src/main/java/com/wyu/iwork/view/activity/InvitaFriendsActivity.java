package com.wyu.iwork.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AZAdapter;
import com.wyu.iwork.adapter.ContactsFriendsAdapter;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.model.ContactsModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.GetPhoneNumberFromMobile;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.namee.permissiongen.PermissionGen;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 *         邀请好友
 */

public class InvitaFriendsActivity extends BaseActivity implements TextWatcher {

    private static String TAG = InvitaFriendsActivity.class.getSimpleName();

    @BindView(R.id.activity_invite_friends)
    LinearLayout parent;
    /**
     * 搜索
     */
    @BindView(R.id.activity_invite_friends_search)
    LinearLayout search_layout;
    /**
     * 搜索框
     */
    @BindView(R.id.activity_invite_friends_editext)
    EditText search;

    /**
     * 手机联系人
     */
    @BindView(R.id.activity_invite_friends_mylistview)
    ListView contacts;
    /**
     * 字母导航
     */
    @BindView(R.id.activity_invite_friends_listview)
    ListView listview;

    //字母适配器
    private AZAdapter azAdapter;
    //手机通讯录适配器
    private ContactsFriendsAdapter adapter;
    private List<ContactsModel> list;
    private List<ContactsModel> datas;
    private String AzString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private List<String> Azlist ;
    private int index = 0;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invita_friends);
        ButterKnife.bind(this);
        // 6.0 以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           getPermission();
        }
        list = new ArrayList<>();
        datas = new ArrayList<>();
        initAzlist();
        getSupportActionBar().hide();
        azAdapter = new AZAdapter(this,Azlist);
        listview.setAdapter(azAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String Py = AzString.charAt(position) + "";
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getFirst_py().equals(Py)) {
                        index = i;
                        contacts.setSelection(i);
                        break;
                    }
                }

            }
        });
        contacts.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Hideinputwindown(view);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        search.addTextChangedListener(this);
        new getcontacts().execute();
    }

    @OnClick({R.id.activity_invite_friends_back,R.id.activity_invite_friends})
    void Click(View v) {
        switch (v.getId()){
            case R.id.activity_invite_friends_back:
                onBackPressed();
                break;

            case R.id.activity_invite_friends:
                Hideinputwindown(v);
                break;
        }

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
            list.clear();
            list.addAll(datas);
            adapter.notifyDataSetChanged();
        }
        if (s.length()>0){
            list.clear();
            for(int i = 0;i<datas.size();i++){
                //进行遍历   若有相同的  则提取到搜索列表中
                if(datas.get(i).getName().indexOf(search.getText().toString()) != -1 ||
                        datas.get(i).getPhone().indexOf(search.getText().toString()) != -1){
                    list.add(datas.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }

    }


    private class getcontacts extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... params) {
            try {
                list = GetPhoneNumberFromMobile.instance().getContactFromMobile(InvitaFriendsActivity.this);
//                List<ContactsModel> temp = new ArrayList<>();
//                temp.add(list.get(4));
//                temp.add(list.get(5));
//                String address_list = JSON.toJSONString(temp);
                String address_list = JSON.toJSONString(list);
                Logger.e(TAG,address_list);
                post_constact(address_list);
            } catch (Exception e) {
                e.printStackTrace();
                return 1;
            }
            datas.clear();
            datas.addAll(list);
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                adapter = new ContactsFriendsAdapter(InvitaFriendsActivity.this, list);
                contacts.setAdapter(adapter);
            }else if (integer==1){
                Toast.makeText(InvitaFriendsActivity.this,"权限不足，无法获取联系人",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initAzlist(){
        Azlist = new ArrayList<>();
        for (int i = 0;i<AzString.length();i++){
            Azlist.add(AzString.charAt(i)+"");
        }
    }

    /**
     * 6.0动态申请权限
     */
    private void getPermission(){
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.SEND_SMS)
                .request();

        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_CONTACTS", "com.wyu.iwork"));
        if (permission) {

        }else {

        }
    }

    /**
     * 上传通讯录
     */
    private void post_constact(String address_list){
        String url = Constant.URL_POST_CONSTACTS;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map map = new HashMap<String,String>();
        map.put("user_id", user_id);
        map.put("phone", userInfo==null?"":userInfo.getUser_phone());
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url,map);
        Logger.e(TAG,murl);
        HttpParams params = new HttpParams();
        params.put("address_list",address_list==null?"":address_list);
        OkGo.post(murl)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.e(TAG,s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.e(TAG,e.getMessage());
                    }
                });
    }

}
