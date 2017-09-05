package com.wyu.iwork.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.BuildDynamicfragment;
/**
 * 新建——动态
 */

public class BuildDynamicActivity extends BaseActivity {

    private static final String TAG = BuildDynamicActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public Fragment getFragment() {
        return new BuildDynamicfragment();
    }
}
