package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;

    private static final String NOTICE_URL = "NOTICE_URL";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        setBackNaviAction();
        ((TextView)setCustomViewForToolbar(R.layout.title_toolbar)).setText("公告");
        ButterKnife.bind(this);
        initView();
    }

    protected void initView(){
        String url = getIntent().getStringExtra(NOTICE_URL);

    }
}
