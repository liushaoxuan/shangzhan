package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.NetIView;
import com.wyu.iwork.presenter.NetPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.dialog.LoadingDialog;

import org.json.JSONObject;

/**
 * Created by lx on 2016/12/28.
 */

public class BaseNetActivity<T> extends AppCompatActivity implements NetIView<T> {
    private static final String TAG = "BaseNetActivity";
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private View mError;

    private Object mRequestTag;

    /**
     * 组件 Presenter 基类
     */
    private NetPresenter mPresenter;
    private LoadingDialog mLoadingDialog;

    public void setNetPresenter(NetPresenter mPresenter,Object requestTag) {
        this.mPresenter = mPresenter;
        mRequestTag = requestTag;
    }

    public NetPresenter getNetPresenter() {
        return mPresenter;
    }


    public int getFragmentContainerId() {
        return R.id.fragmentContainer;
    }

    private FrameLayout getFragmentContainer() {
        return (FrameLayout) findViewById(getFragmentContainerId());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        setSupportActionBar(getToolbar());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment content = fragmentManager.findFragmentById(getFragmentContainerId());

            if (content != null) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, content).commit();
            }
    }

    @Override
    public void setContentView(int layoutResId) {
        View content = getLayoutInflater().inflate(layoutResId, null);
        getFragmentContainer().removeAllViews();
        getFragmentContainer().addView(content, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public AppBarLayout getAppBarLayout() {
        if (mAppBarLayout == null) {
            mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        }
        return mAppBarLayout;
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
        }
        return mToolbar;
    }

    public void hideToolBar() {
        getToolbar().setVisibility(View.GONE);
    }

    public void setBackNaviAction() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    public View setCustomViewForToolbar(int resId) {
        View view = getLayoutInflater().inflate(resId, null);
        setCustomViewForToolbar(view);
        return view;
    }

    public View getCustomViewForToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return null;
        return actionBar.getCustomView();
    }

    public void setCustomViewForToolbar(View view) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        actionBar.setCustomView(view, lp);
    }

    @Override
    public void onRequestBefore() {
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);

    }

    @Override
    public void onSuccess(T data, JSONObject origin) {
        Logger.e(TAG, "****onSuccess***");
    }

    @Override
    public void onFailure(int errorCode, String errorMsg) {
        Logger.e(TAG, "****onFailure****" + errorCode + "\n" + errorMsg);
    }

    @Override
    public void onUnLogin() {


    }

    @Override
    public void onRequestAfter() {
        DialogFragment loading = (DialogFragment) this.getSupportFragmentManager().findFragmentByTag(Constant.DIALOG_TAG_LOADING);
        if (loading != null) loading.dismiss();

    }
}
