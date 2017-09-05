package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.FindPassWordFragment;

public class FindPassWordActivity extends BaseActivity {

    @Nullable
    @Override
    public Fragment getFragment() {
        return new FindPassWordFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ((TextView)setCustomViewForToolbar(R.layout.title_toolbar)).setText("找回密码");
        setBackNaviAction();
    }
}
