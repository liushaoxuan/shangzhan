package com.wyu.iwork.view.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.OnBackPressedCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.NetUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static com.wyu.iwork.R.id.toolbar;

/**
 * Created by jhj_Plus on 2016/10/24.
 */
public abstract class BaseActivity extends AutoLayoutActivity {
    private static final String TAG = "BaseActivity";
    private OnBackPressedCallback mBackPressedCallback;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    public UserInfo userInfo;
    @Override
    public void putExtraData(ExtraData extraData) {
        super.putExtraData(extraData);
    }

    public abstract @Nullable Fragment getFragment();

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
        userInfo =  MyApplication.userInfo;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        ActivityManager.getInstance().addActivity(this);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        // 自定义颜色
        tintManager.setTintColor(getResources().getColor(R.color.blue));
        setSupportActionBar(getToolbar());
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment content = fragmentManager.findFragmentById(getFragmentContainerId());
        if (content == null) {
            content = getFragment();
            if (content != null) {
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, content)
                        .commit();
            }
        }
    }

    @Override
    public void setContentView(int layoutResId) {
        View content = getLayoutInflater().inflate(layoutResId, null);
        getFragmentContainer().removeAllViews();
        getFragmentContainer().addView(content,
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public AppBarLayout getAppBarLayout() {
        if (mAppBarLayout == null) {
            mAppBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        }
        return mAppBarLayout;
    }

    public Toolbar getToolbar() {
        if (mToolbar == null) {
            mToolbar = (Toolbar) findViewById(toolbar);
        }
        return mToolbar;
    }
    //隐藏toolbar
    public void hideToolbar(){
        Toolbar toolbar = getToolbar();
        if(toolbar != null){
            toolbar.setVisibility(View.GONE);
        }
    }

    public void setBackNaviAction() {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.mipmap.arrow_left);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
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
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        actionBar.setCustomView(view, lp);
    }

    public void setBackPressedCallback(OnBackPressedCallback backPressedCallback)
    {
        this.mBackPressedCallback = backPressedCallback;
    }


    @Override
    public void onBackPressed() {
        Logger.e(TAG,">>>>onBackPressed<<<");
        if (mBackPressedCallback == null || !mBackPressedCallback.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void doRequestget(String url, String cacheKey ,StringCallback callback){
        OkGo.get(url)
                .cacheKey(cacheKey)
                .execute(callback);
    }
    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    //下拉刷新控件的一些设置
    public void setRefresh(BGARefreshLayout mRefreshLayout){
        BGANormalRefreshViewHolder moocStyleRefreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);

        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
        // 设置正在加载更多时不显示加载更多控件
        // mRefreshLayout.setIsShowLoadingMoreView(false);
        // 设置正在加载更多时的文本
//        refreshViewHolder.setLoadingMoreText("正在加载");
        // 设置整个加载更多控件的背景颜色资源 id
//        refreshViewHolder.setLoadMoreBackgroundColorRes(loadMoreBackgroundColorRes);
        // 设置整个加载更多控件的背景 drawable 资源 id
//        refreshViewHolder.setLoadMoreBackgroundDrawableRes(loadMoreBackgroundDrawableRes);
        // 设置下拉刷新控件的背景颜色资源 id
//        refreshViewHolder.setRefreshViewBackgroundColorRes(refreshViewBackgroundColorRes);
        // 设置下拉刷新控件的背景 drawable 资源 id
//        refreshViewHolder.setRefreshViewBackgroundDrawableRes(refreshViewBackgroundDrawableRes);
        // 设置自定义头部视图（也可以不用设置）     参数1：自定义头部视图（例如广告位）， 参数2：上拉加载更多是否可用
//        mRefreshLayout.setCustomHeaderView(null, true);
        // 可选配置  -------------END
    }

    //强制隐藏软键盘
    public   void Hideinputwindown(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    //公告网络请求方法
    public void OkgoRequest(String url,DialogCallback dialogcallback){
        boolean isnet =  NetUtil.checkNetwork(this);
//        if (!isnet){
//            MsgUtil.shortToastInCenter(this,"网络错误,请稍后重试!");
//            return;
//        }
              OkGo.get(url)
                      .connTimeOut(5000)//设置超时时间 5秒
                     .execute(dialogcallback);

    }

    //解决嵌套滑动不流畅问题
    public void ScrollSmooth(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    //更新后诚信获取用户信息
    public void updateUserInfo(){
        userInfo =  MyApplication.userInfo;
    }

    //根据状态显示当前显示内容还是显示暂无状态
    public void showContent(boolean flag,View content,View notavaliable){
        content.setVisibility(flag?View.VISIBLE:View.GONE);
        notavaliable.setVisibility(flag?View.GONE:View.VISIBLE);
    }


    public void Refresh(){

    };
}
