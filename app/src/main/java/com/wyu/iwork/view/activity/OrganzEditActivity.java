package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.OrganzEditFragment;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzEditActivity extends BaseActivity {
    private static final String TAG = OrganzEditActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText(getString(R.string.organz_edit));
    }

    @Nullable
    @Override
    public Fragment getFragment() {
        OrganzEditFragment editFragment = new OrganzEditFragment();
        editFragment.setArguments(getIntent().getExtras());
        return editFragment;
    }
}
