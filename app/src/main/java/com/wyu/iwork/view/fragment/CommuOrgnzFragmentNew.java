package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CommuOrgnzAdapter;
import com.wyu.iwork.adapter.OrgizationAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.Organization;
import com.wyu.iwork.model.OrgnzParent;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.OrganzUserEditActivity;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class CommuOrgnzFragmentNew extends BaseFragment<List<OrgnzParent>> {
    private static final String TAG = "CommuOrgnzFragment";
    private RecyclerView mRv;
    private CommuOrgnzAdapter mAdapter;
    private ListView mFirstView;
    private ListView mSecondListView;
    private ExpandableListView mListview;
    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childTag = TAG;

    }

    public void getDataFromServer(){
        UserInfo user = MyApplication.userInfo;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        Logger.i(TAG,RandStr);
        String Sign = Md5Util.getSign(F+V+RandStr+user.getCompany_id());

        HashMap<String,String> data = new HashMap<>();
        data.put("company_id",user.getCompany_id());
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_COMMU_ORGANZ, data);
        OkGo.get(murl).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                parseData(s);
            }
        });
    }

    public void parseData(String s){
        try {
            if(gson == null){
                gson = new Gson();
            }
            Organization organization = gson.fromJson(s,Organization.class);
            OrgizationAdapter adapter = new OrgizationAdapter(getActivity(),organization.getData());
            mListview.setAdapter(adapter);
            mListview.setCacheColorHint(0);
            initClick();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initClick(){
        mListview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        mListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ImageView view = (ImageView) v.findViewById(R.id.indicator);
                if(view.getRotation() == -180){
                    view.setRotation(-90);
                }else{
                    view.setRotation(-180);
                }
                return false;
            }
        });

        mListview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                ImageView view = (ImageView) v.findViewById(R.id.indicator);
                if(view.getRotation() == 0){
                    view.setRotation(-90);
                }else{
                    view.setRotation(0);
                }
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup content = (ViewGroup) inflater
                .inflate(R.layout.fragment_commu_orgnz, container, false);

        return super.onCreateView(inflater, content, savedInstanceState);
    }

    @Override
    public void onInitView(View rootView) {
        mListview = (ExpandableListView)rootView.findViewById(R.id.el_expand);
        mListview.setGroupIndicator(null);
        UserInfo userInfo = MyApplication.userInfo;
        ((TextView) rootView.findViewById(R.id.name))
                .setText(userInfo != null ? userInfo.getCompany() : "");
        getDataFromServer();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent userEditIntent = new Intent(getActivity(), OrganzUserEditActivity.class);
                Bundle args = new Bundle();
                args.putString(Constant.KEY_ACTION_TYPE,Constant.VALUE_TYPE_C);
                userEditIntent.putExtras(args);
                startActivity(userEditIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
