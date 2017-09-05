package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.AdTaskShareModel;
import com.wyu.iwork.util.AdShareUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.widget.ShareDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author sxliu
 * 广告联盟 任务详情
 */
public class AdTaskDetailActivity extends BaseActivity {

    private static String TAG = AdTaskDetailActivity.class.getSimpleName();
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;

    /**
     * 分享
     */
    @BindView(R.id.action_edit)
    TextView share;

    /**
     * WebView
     */
    @BindView(R.id.activity_ad_task_detail_webview)
    WebView webView;
    /**
     * 进度条
     */
    @BindView(R.id.activity_ad_task_detail_progress)
    ProgressBar progressbar;

    //详情页url
    private String url  = "";

    //详情页id
    private String Id = "";

    private AdTaskShareModel shareModel;//分享的model
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_task_detail);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        getUrl();
        init();
    }

    /**
     * 获取详情页URl
     */
    private void getUrl(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            url = bundle.getString("url");
            Logger.e(TAG,url);
            Id = bundle.getString("id");
        }
    }
    /**
     * 初始化
     */
    private void init(){
        title.setText("任务详情");
        share.setText("分享");
        AdShareUtils.getShareData(Id==null?"":Id,callback());
    }

    /**
     * 加载详情页
     */
    private void initUrl(){
        if (url!=null&&!url.isEmpty()){
            webView.canGoBack();
            webView.loadUrl(url);
            WebSettings seting  = webView.getSettings();
            seting.setJavaScriptEnabled(true);//设置webview支持javascript脚本

            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                    view.loadUrl(url);
                    return true;
                }

            });
            webView.setWebChromeClient(new android.webkit.WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {

                    if (newProgress == 100) {
                        progressbar.setVisibility(GONE);
                    } else {
                        if (progressbar.getVisibility() == GONE)
                            progressbar.setVisibility(VISIBLE);
                        progressbar.setProgress(newProgress);
                    }
                    super.onProgressChanged(view, newProgress);
                }
            });
        }
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.action_back,R.id.action_edit})
    void click(View view){
        switch (view.getId()){
            case R.id.action_back://返回
                onBackPressed();
                break;

            case R.id.action_edit://分享
                if (shareModel!=null){
                    new ShareDialog(this,1,shareModel.getTitle(),shareModel.getMessage(),shareModel.getUrl(),shareModel.getImg_url()).show();
                }else {
                    Toast.makeText(AdTaskDetailActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 获取分享信息回调
     */
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.e(TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    String data = object.optString("data");
                    if ("0".equals(code)){
                        shareModel = JSON.parseObject(data.toString(),AdTaskShareModel.class);
                        initUrl();
                    }else {
                        Toast.makeText(AdTaskDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }
}
