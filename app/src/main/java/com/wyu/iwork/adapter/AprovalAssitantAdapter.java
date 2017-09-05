package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： sxliu on 2017/7/21.10:14
 * 邮箱：2587294424@qq.com
 */

public class AprovalAssitantAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private String tabTitles[] = new String[]{"全部","报销","请假","加班","出差"};
    private Context mcontext;
    public AprovalAssitantAdapter(FragmentManager fm, Context context,List<Fragment> list) {
        super(fm);
        mcontext = context;
        this.fragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment =  fragmentList.get(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
