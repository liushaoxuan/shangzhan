package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.OrganizationalStructureFragment;

/**
 * 组织架构
 */
public class OrganizationalStructureActivity extends BaseActivity {

    @Nullable
    @Override
    public Fragment getFragment() {
        return new OrganizationalStructureFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }
}
