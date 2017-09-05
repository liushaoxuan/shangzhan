package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.wyu.iwork.R;
import com.wyu.iwork.model.BusinessCardInfo;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.fragment.SalesLeadDetailFragment;

/**
 * @author juxinhua
 * CRM - 新建销售线索 查看销售线索详情  编辑销售线索
 */
public class SalesLeadDetailActivity extends BaseActivity {

    private static final String TAG = SalesLeadDetailActivity.class.getSimpleName();
    private String currentType;
    private String culeId;
    private BusinessCardInfo info;//拍照上传后返回的信息
    @Nullable
    @Override
    public Fragment getFragment() {
        getExtras();
        if("BROWSER".equals(currentType)){
            return new SalesLeadDetailFragment(currentType,culeId);
        }else if("NEW_RECT".equals(currentType)){
            currentType = "NEW";
            return new SalesLeadDetailFragment(currentType,info);
        }else{
            return new SalesLeadDetailFragment(currentType);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_lead_detail);
        hideToolbar();
    }

    private void getExtras(){
        Intent intent = getIntent();
        currentType = intent.getStringExtra("type");
        if("BROWSER".equals(currentType)){
            culeId = intent.getStringExtra("clue_id");
        }
        if("NEW_RECT".equals(currentType)){
            info = (BusinessCardInfo) intent.getSerializableExtra("info");
            Logger.i(TAG,info.toString());
        }
    }
}
