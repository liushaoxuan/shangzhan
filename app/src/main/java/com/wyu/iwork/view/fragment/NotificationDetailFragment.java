package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class NotificationDetailFragment extends BaseFragment {
    private static final String TAG = NotificationDetailFragment.class.getSimpleName();

    @BindView(R.id.wv_webview)
    WebView webView;
    private String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_notification_detail, container, false);
        ButterKnife.bind(this,content);
        initView();
        return content;
    }

    public void initView(){

    }


    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
    }
}
