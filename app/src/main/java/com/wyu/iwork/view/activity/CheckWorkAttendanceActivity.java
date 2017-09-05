package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.baidu.mapapi.search.core.PoiInfo;
import com.wyu.iwork.view.fragment.CheckWorkAttendanceFragment;

/**
 * 考勤
 */
public class CheckWorkAttendanceActivity extends BaseActivity {

    private static final String TAG = CheckWorkAttendanceActivity.class.getSimpleName();
    private CheckWorkAttendanceFragment chechWorkFragment;
    private PoiInfo mPoiInfo;

    @Nullable
    @Override
    public Fragment getFragment() {
        chechWorkFragment = new CheckWorkAttendanceFragment();
        return chechWorkFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setBackNaviAction();
        //((TextView)setCustomViewForToolbar(R.layout.title_toolbar)).setText("手机考勤");
        hideToolbar();
    }

    public CheckWorkAttendanceFragment getCheckWorkAttendanceFragment(){
        if(chechWorkFragment != null){
            return chechWorkFragment;
        }
        return null;
    }

}
