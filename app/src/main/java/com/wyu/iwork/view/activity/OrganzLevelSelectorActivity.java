package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.OrganzLevelSelectorFragment;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzLevelSelectorActivity extends BaseActivity {
    private static final String TAG = OrganzLevelSelectorActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText(getString(R.string.organz_selector));
    }

    @Nullable
    @Override
    public Fragment getFragment() {
        OrganzLevelSelectorFragment fragment = new OrganzLevelSelectorFragment();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }

}
