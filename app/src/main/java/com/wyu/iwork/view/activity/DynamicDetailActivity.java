package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.DynamicDetailFragment;

/**
 * 动态详情
 */
public class DynamicDetailActivity extends BaseActivity{

    private String Id;

    @Nullable
    @Override
    public Fragment getFragment() {
        return new DynamicDetailFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getId();
    }

    private void getId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Id = bundle.getString("dynamic_id");
        }
    }

    public String get_Id(){
        return Id;
    }


}
