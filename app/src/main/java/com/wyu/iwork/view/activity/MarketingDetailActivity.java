package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.MarketingDetailFragment;

/**
 * @author juxinhua
 * CRM - 市场活动详情
 */
public class MarketingDetailActivity extends BaseActivity {

    private String type;
    private String activity_id;

    @Nullable
    @Override
    public Fragment getFragment() {
        getExtras();
        if("BROWSER".equals(type)){
            return new MarketingDetailFragment(type,activity_id);
        }else{
            return new MarketingDetailFragment(type);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_detail);
        hideToolbar();
    }

    private void getExtras(){
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if("BROWSER".equals(type)){
            activity_id = intent.getStringExtra("activity_id");
        }
    }
}
