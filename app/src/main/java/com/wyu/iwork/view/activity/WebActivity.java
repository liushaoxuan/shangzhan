package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 加载url页面
 */
public class WebActivity extends BaseActivity {

    //webview
    @BindView(R.id.WebActivity_webview)
    WebView webView;

    //进度条
    @BindView(R.id.activity_web_progressbar)
    ProgressBar progressBar;

    private String url;
    private int mType;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        hideToolbar();
        getParama();
        loadurl();
    }

    //获取parama
    private void getParama(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            url = bundle.getString("url");
        }
        //获取当前公告类型为已读还是未读
        mType = getIntent().getIntExtra("type",-1);
    }

    private void loadurl(){
        if (url!=null){
            webView.loadUrl(url);
            WebSettings seting=webView.getSettings();
            seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本
            webView.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    if (newProgress==100){//进度条消失
                        progressBar.setVisibility(View.GONE);
                    }else {
                        progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        progressBar.setProgress(newProgress);//设置进度值
                    }
                }
            });

            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });

        }

        //webview 的按键监听
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN){
                    if (keyCode==KeyEvent.KEYCODE_BACK && webView.canGoBack()){//webView.canGoBack()标识webview有上一级可以返回
                        webView.goBack();//webview回退
                        //webview.goForward();//前进
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    //返回事件
    @OnClick(R.id.activity_web_back)
    void back(){
        /**
        Intent i = new Intent(this,NoticeActivity.class);*/
        setResult(RESULT_OK);
        finish();
        //onBackPressed();
    }
}
