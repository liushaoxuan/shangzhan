package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.BaseFragment;
import com.wyu.iwork.view.fragment.ReceiveDailyReportFragment;
import com.wyu.iwork.view.fragment.SendDailyReportFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 日报界面
 */
public class DailyReportActivity extends BaseActivity {

    private static final String TAG = DailyReportActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.tv_work)
    TextView tv_work;

    @BindView(R.id.tv_person)
    TextView tv_person;

    @BindView(R.id.daily_report_viewpager)
    ViewPager viewPager;

    private static final int TYPE_RECEIVE = 1;
    private static final int TYPE_SEND = 2;
    private int CURRENTTYPE = TYPE_RECEIVE;

    private ArrayList<BaseFragment> fragments ;
    private ReceiveDailyReportFragment receiveFragment;
    private SendDailyReportFragment sendFragment;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        ButterKnife.bind(this);
        /**
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("日报");*/
        hideToolbar();
        tv_title.setText("日报");
        tv_edit.setVisibility(View.VISIBLE);
        tv_edit.setText("发布");
        tv_work.setText("我收到的");
        tv_person.setText("我发出的");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView(){
        setSelectLayout(TYPE_RECEIVE);
        fragments = new ArrayList<>();
        receiveFragment = new ReceiveDailyReportFragment();
        sendFragment = new SendDailyReportFragment();
        fragments.add(receiveFragment);
        fragments.add(sendFragment);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    setSelect(true);
                }else{
                    setSelect(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ll_back,R.id.tv_work,R.id.tv_person,R.id.tv_edit})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_work:
                setSelectLayout(TYPE_RECEIVE);
                break;
            case R.id.tv_person:
                setSelectLayout(TYPE_SEND);
                break;
            case R.id.tv_edit:
                Intent intent = new Intent(this,AddDailyActivity.class);
                startActivity(intent);
                break;
        }
    }



    private void setSelectLayout(int type){
        if(type == TYPE_RECEIVE){
            //当前为工作事务
            setSelect(true);
            CURRENTTYPE = 1;
            viewPager.setCurrentItem(0);
        }else if(type == TYPE_SEND){
            //当前为个人事务
            setSelect(false);
            CURRENTTYPE = 2;
            viewPager.setCurrentItem(1);
        }

    }

    private void setSelect(boolean flag){
        if(flag){
            //工作事务
            tv_work.setSelected(true);
            tv_person.setSelected(false);
        }else{
            //个人事务
            tv_person.setSelected(true);
            tv_work.setSelected(false);
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public BaseFragment getFragment(int type){
        if(type == 1){
            //工作事务
            if(receiveFragment != null){
                return receiveFragment;
            }
        }
        if(type == 2){
            //个人事务
            if(sendFragment != null){
                return sendFragment;
            }
        }
        return  null;
    }
}
