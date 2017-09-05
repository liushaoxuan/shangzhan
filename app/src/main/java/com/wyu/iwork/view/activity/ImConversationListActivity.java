package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import io.rong.imkit.fragment.ConversationListFragment;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/16.
 */
public class ImConversationListActivity extends BaseActivity {
    private static final String TAG = ImConversationListActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBackNaviAction();
//        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText(getString(R.string.organz_edit));
    }

    @Nullable
    @Override
    public Fragment getFragment() {
        return new ConversationListFragment();
    }
}
