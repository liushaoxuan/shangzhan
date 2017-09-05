package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import com.wyu.iwork.view.fragment.MessageDynamicFragment;
/**
 * @author sxliu
 * 动态
 */
public class DynamicActivity extends BaseActivity {
    @Nullable
    @Override
    public Fragment getFragment() {
        return new MessageDynamicFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

}
