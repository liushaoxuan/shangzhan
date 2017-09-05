package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.OrganzUserEditFragment;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/5.
 */
public class OrganzUserEditActivity extends BaseActivity {
    private static final String TAG = OrganzUserEditActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar))
                .setText(getString(R.string.organz_user_edit));
    }

    @Nullable
    @Override
    public Fragment getFragment() {
        OrganzUserEditFragment fragment = new OrganzUserEditFragment();
        fragment.setArguments(getIntent().getExtras());
        return fragment;
    }
}
