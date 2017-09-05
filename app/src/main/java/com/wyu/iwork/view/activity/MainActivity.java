package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.TabCallback;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateManager;
import com.wyu.iwork.view.fragment.MainFragment;

public class MainActivity extends BaseActivity implements TabCallback {
    private static final String TAG = "MainActivity";

    private int tab = 2;

    private MainFragment mainfragment;
    /**
     * 保存 Toolbar 中间的 customView
     */
    private SparseArray<View> mCustomViews = new SparseArray<>(3);

    private  UpdateManager mUpdateManager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onTabSelected(tab);
        getSupportActionBar().hide();
        if (MyApplication.userInfo.getIs_update()==1){//需要更新
            mUpdateManager = new UpdateManager(this,MyApplication.userInfo.getUrl());
            mUpdateManager.checkUpdateInfo();
        }

    }

    @Override
    public Fragment getFragment() {
        if (mainfragment==null){
            mainfragment = new MainFragment();
        }
        return  mainfragment;
    }


    @Override
    public void onTabSelected(int position) {
        int viewId = 0;
        String title = "";
        switch (position) {

            case 0:
                viewId = R.layout.layout_tab_toolbar_applicat_new;
                break;
            case 1:
                viewId = R.layout.tab_layout_toolbar_commu;
                break;
            case 2:
                viewId = R.layout.teb_layout_work;
                break;
            case 3:
                viewId = R.layout.teb_layout_message;
                break;
            case 4:
                viewId = R.layout.teb_layout_mine;
                break;
        }

        View customView = mCustomViews.get(viewId);
        if (customView == null) {
            customView = setCustomViewForToolbar(viewId);
            mCustomViews.put(viewId, customView);
            Logger.e(TAG,"onTabSelected///init!!"+customView);
        } else {
            Logger.e(TAG,"onTabSelected///reuse!!"+customView);
            setCustomViewForToolbar(customView);
        }

        if (customView instanceof TextView) {
            TextView tv_title = (TextView) customView;
            tv_title.setText(title);
        }
    }

    public View getCustomViewByLayoutId(int layoutResId) {
        return mCustomViews.get(layoutResId);
    }

/*
    //定义两次按键的时间
    private long  oldTime = 0;
    private long  nowTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {//返回键
            // 如何连续按两次，退出
            nowTime = System.currentTimeMillis();
            if ((nowTime - oldTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
                oldTime = nowTime;
            } else {
                ActivityManager.getInstance().finishActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
*/



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            tab =  bundle.getInt("tab");
            onTabSelected(tab);
            mainfragment.setcheked(tab);
        }
    }
}
