package com.wyu.iwork.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： sxliu on 2017/5/23.15:09
 * 邮箱：2587294424@qq.com
 */

public class FragmentPageadapter extends FragmentPagerAdapter {

    private List<Fragment> list ;
    public FragmentPageadapter(FragmentManager fm) {
        super(fm);
    }

    public FragmentPageadapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
        if (this.list==null){
            this.list = new ArrayList<>();
        }
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
