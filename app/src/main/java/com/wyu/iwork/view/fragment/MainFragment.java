package com.wyu.iwork.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.TabCallback;
import com.wyu.iwork.util.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jhj_Plus on 2016/10/24.
 */
public class MainFragment extends Fragment  {
    private static final String TAG = "MainFragment";
    private static final int TAB_COUNT = 5;
    private TabCallback mCallback;
    private ViewPager mViewPager;
    private RadioGroup mTabGroup;

    @BindView(R.id.teb_app_layout)
    RelativeLayout app_layout;
    @BindView(R.id.teb_communicte_layout)
    RelativeLayout communicate_layout;
    @BindView(R.id.teb_work_layout)
    RelativeLayout work_layout;
    @BindView(R.id.teb_dynamic_layout)
    RelativeLayout dynamic_layout;
    @BindView(R.id.teb_mine_layout)
    RelativeLayout mine_layout;

    private List<RelativeLayout> relativeLayouts;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TabCallback) {
            mCallback = (TabCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //首次进入应用开始连接融云 IM 服务器
//        AppManager.requestImToken(getContext());
        AppManager.connectImServer(getContext());
        relativeLayouts = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view  = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        relativeLayouts.clear();
        relativeLayouts.add(app_layout);
        relativeLayouts.add(communicate_layout);
        relativeLayouts.add(work_layout);
        relativeLayouts.add(dynamic_layout);
        relativeLayouts.add(mine_layout);
        relativeLayouts.get(2).setSelected(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    public void initView(View rootView) {
        mTabGroup = (RadioGroup) rootView.findViewById(R.id.tabGroup);

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));
        setcheked(2);
    }


    @OnClick({R.id.teb_app_layout,R.id.teb_communicte_layout,R.id.teb_work_layout,
            R.id.teb_dynamic_layout,R.id.teb_mine_layout})
    void CLick(View v){
        switch (v.getId()){
            case R.id.teb_app_layout://应用
                setcheked(0);
                break;
            case R.id.teb_communicte_layout://联系人
                setcheked(1);
                break;
            case R.id.teb_work_layout://工作
                setcheked(2);
                break;
            case R.id.teb_dynamic_layout://消息
                setcheked(3);
                break;
            case R.id.teb_mine_layout://我的
                setcheked(4);
                break;
        }

    }
    public void setcheked(int position){
        if (mCallback != null) {
            Logger.e(TAG, "onCheckedChanged=" + position);
            mCallback.onTabSelected(position);
            for (int i = 0;i<relativeLayouts.size();i++){
                if (position==i){
                    relativeLayouts.get(i).setSelected(true);
                }else {
                    relativeLayouts.get(i).setSelected(false);
                }
            }
        }
        mViewPager.setCurrentItem(position, false);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ApplicationFragment();
                case 1:
//                    return new ContactsFragment();
                    return new HomeContactsFragment();

                case 2:
                    return new HomeWorkFragment();
                case 3:
//                    return new MessageFragment();
                    return new HomeMessageFragment();
                case 4:
                    return new Fragment_my_new();
            }
            throw new RuntimeException("error in switch tab");
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        int po = mViewPager.getCurrentItem();
        setcheked(po);
        Logger.e("position",po+"");
    }
}
