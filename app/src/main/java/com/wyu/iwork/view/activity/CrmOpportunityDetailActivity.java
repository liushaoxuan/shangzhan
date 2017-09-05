package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.CrmOpportunityDetailFragment;

/**
 * @author juxinhua
 * crm - 新建商机   编辑商机   商机详情
 */
public class CrmOpportunityDetailActivity extends BaseActivity {

    private static final String TAG = CrmOpportunityDetailActivity.class.getSimpleName();

    private String currentType;
    private String chance_id;
    @Nullable
    @Override
    public Fragment getFragment() {
        getExtras();
        if("BROWSER".equals(currentType)){
            return new CrmOpportunityDetailFragment(currentType,chance_id);
        }else{
            return  new CrmOpportunityDetailFragment(currentType);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_opportunity_detail);
        hideToolbar();

    }

    private void getExtras(){
        Intent intent = getIntent();
        currentType = intent.getStringExtra("type");
        if(currentType.equals("BROWSER")){
            chance_id = intent.getStringExtra("chance_id");
        }
    }

}
