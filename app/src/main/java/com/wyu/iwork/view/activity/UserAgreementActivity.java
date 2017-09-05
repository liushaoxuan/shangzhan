package com.wyu.iwork.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wyu.iwork.R;
import com.wyu.iwork.util.Constant;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户协议
 */
public class UserAgreementActivity extends AutoLayoutActivity {

    @BindView(R.id.activity_about_us_webview)
    WebView webView;
    private String url ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
        ButterKnife.bind(this);
        url = Constant.URL_USER_AGREEMENT;
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick(R.id.activity_user_agreement_back)
    void Click(){
        onBackPressed();
    }
}
