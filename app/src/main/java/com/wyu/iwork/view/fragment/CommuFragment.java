package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.view.activity.MainActivity;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class CommuFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "CommuFragment";
    private static final int TAB_COUNT = 3;
    private RadioGroup mTabGroup;
    private ViewPager mViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup content= (ViewGroup) inflater.inflate(R.layout.fragment_commu,container,false);
        return super.onCreateView(inflater, content, savedInstanceState);
    }


    @Override
    public void onInitView(View rootView) {
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        mViewPager.addOnPageChangeListener(mPageListener);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            final int position = mTabGroup.indexOfChild(buttonView);
            mViewPager.setCurrentItem(position, false);
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CommuChatFragment();
                case 1:
                    return new CommuOrgnzFragmentNew();
                case 2:
                    return new CommuContactFragment();
            }
            return new CommuChatFragment();
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    private ViewPager.SimpleOnPageChangeListener mPageListener =
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (mTabGroup == null) return;
                    ((RadioButton) mTabGroup.getChildAt(position)).toggle();
                }
            };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        initTabLayout();
    }

    private void initTabLayout() {
        View tabLayout = ((MainActivity) getActivity()).getCustomViewByLayoutId(
                R.layout.tab_layout_toolbar_commu);
        if (tabLayout instanceof RadioGroup) {
            mTabGroup = (RadioGroup) tabLayout;
            for (int i = 0; i < mTabGroup.getChildCount(); i++) {
                ((RadioButton) mTabGroup.getChildAt(i)).setOnCheckedChangeListener(this);
            }
        }
    }
}
