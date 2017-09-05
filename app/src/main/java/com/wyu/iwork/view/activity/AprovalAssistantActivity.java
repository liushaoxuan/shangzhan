package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AprovalAssitantAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.fragment.AprovalAssistantAllFragment;
import com.wyu.iwork.view.fragment.AprovalAssistantLeaveFragment;
import com.wyu.iwork.view.fragment.AprovalAssistantOverTimeFragment;
import com.wyu.iwork.view.fragment.AprovalAssistantReimFragment;
import com.wyu.iwork.view.fragment.AprovalAssistantTravelFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @sxliu
 * 审批助手
 */
public class AprovalAssistantActivity extends BaseActivity {

    private static String TAG = AprovalAssistantActivity.class.getSimpleName();

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 指示器
     */
    @BindView(R.id.activity_aproval_assistant_tablayout)
    TabLayout tabLayout;

    /**
     * ViewPager
     */
    @BindView(R.id.activity_aproval_assistant_viewpager)
    ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<>();

    private AprovalAssitantAdapter adapter;

    private String url = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproval_assistant);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();

        adapter = new AprovalAssitantAdapter(getSupportFragmentManager(),this,fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    /**
     * 初始化
     */
    private void init(){

        title.setText("审批助手");
        edit.setVisibility(View.GONE);
        fragments.add(new AprovalAssistantAllFragment());
        fragments.add(new AprovalAssistantReimFragment());
        fragments.add(new AprovalAssistantLeaveFragment());
        fragments.add(new AprovalAssistantOverTimeFragment());
        fragments.add(new AprovalAssistantTravelFragment());
    }

    @OnClick(R.id.action_back)
    void click(){
        onBackPressed();
    }


    public void getData(int pageindex,String type ,DialogCallback callback){
        url = Constant.URL_TAPROVAL_ASSISTENT;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String,String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("type", type);
        params.put("page", pageindex+"");
        params.put("F", F);
        params.put("V", V);
        params.put("RandStr", RandStr);
        params.put("Sign", sign);
        url = RequestUtils.getRequestUrl(url,params);
        Logger.e(TAG,url);
        OkgoRequest(url,callback);
    }



}
