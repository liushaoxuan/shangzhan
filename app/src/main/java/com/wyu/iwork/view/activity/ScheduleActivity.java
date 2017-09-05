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
import com.wyu.iwork.view.fragment.PersonScheduleFragment;
import com.wyu.iwork.view.fragment.WorkScheduleFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 日程列表
 */
public class ScheduleActivity extends BaseActivity {

    private static final String TAG = ScheduleActivity.class.getSimpleName();
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

    @BindView(R.id.schedule_viewpager)
    ViewPager viewpager;


    private static final int TYPE_WORK = 1;
    private static final int TYPE_PERSON = 2;
    private int CURRENTTYPE = TYPE_WORK;

    private ArrayList<BaseFragment> fragments ;
    private WorkScheduleFragment workFragment;
    private PersonScheduleFragment personFragment;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ButterKnife.bind(this);
        hideToolbar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private void initView(){
        tv_title.setText("日程列表");
        tv_edit.setText("新建");
        tv_edit.setVisibility(View.VISIBLE);
        setSelectLayout(TYPE_WORK);
        fragments = new ArrayList<>();
        workFragment = new WorkScheduleFragment();
        personFragment = new PersonScheduleFragment();
        fragments.add(workFragment);
        fragments.add(personFragment);
        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                setSelectLayout(TYPE_WORK);
                break;
            case R.id.tv_person:
                setSelectLayout(TYPE_PERSON);
                break;
            case R.id.tv_edit:
                Intent intent = new Intent(this,CreateScheduleActivity.class);
                startActivity(intent);
                break;
        }
    }



    private void setSelectLayout(int type){
        if(type == TYPE_WORK){
            //当前为工作事务
            setSelect(true);
            CURRENTTYPE = 1;
            viewpager.setCurrentItem(0);
        }else if(type == TYPE_PERSON){
            //当前为个人事务
            setSelect(false);
            CURRENTTYPE = 2;
            viewpager.setCurrentItem(1);
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

    class ViewPagerAdapter extends FragmentPagerAdapter{

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
            if(workFragment != null){
                return workFragment;
            }
        }
        if(type == 2){
            //个人事务
            if(personFragment != null){
                return personFragment;
            }
        }
        return  null;
    }

}
