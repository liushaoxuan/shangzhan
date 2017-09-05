package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class NotificationDetailActivity extends BaseActivity {
    private static final String TAG = NotificationDetailActivity.class.getSimpleName();

    @BindView(R.id.wv_webview)
    WebView webView;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notification_detail);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText(getString(R.string.notification));
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("URL");
        initData();
    }

    public void initData(){
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public Fragment getFragment() {
        return null;
    }
}
