package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.BaseFragment;
import com.wyu.iwork.view.fragment.TaskCreateFragment;
import com.wyu.iwork.view.fragment.TaskFinishedFragment;
import com.wyu.iwork.view.fragment.TaskWaitExtcuteFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 任务列表
 */
public class TaskActivity extends BaseActivity {

    private static final String TAG = TaskActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.task_tab)
    TabLayout task_tab;

    @BindView(R.id.task_viewpager)
    ViewPager task_viewpager;

    private Gson gson;
    //private TaskAdapter adapter;
    private static final int CODE_NEW_TASK = 106;
    private String[] title = {"我创建的","待执行","已完成"};
    private ArrayList<BaseFragment> fragments;
    private TaskCreateFragment createFragment;
    private TaskWaitExtcuteFragment executeFragment;
    private TaskFinishedFragment finishFragment;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        hideToolbar();
        initView();
        initData();
    }

    private void initView(){
        tv_title.setText("任务列表");
        tv_edit.setVisibility(View.VISIBLE);
        tv_edit.setText("新建");
    }

    private void initData(){
        fragments = new ArrayList<>();
        createFragment = new TaskCreateFragment();
        executeFragment = new TaskWaitExtcuteFragment();
        finishFragment = new TaskFinishedFragment();
        fragments.add(createFragment);
        fragments.add(executeFragment);
        fragments.add(finishFragment);
        task_viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        task_tab.setupWithViewPager(task_viewpager);
    }

    @OnClick({R.id.ll_back,R.id.tv_edit})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_edit:
                Intent intent = new Intent(this,CreateNewTaskActivity.class);
                startActivityForResult(intent,CODE_NEW_TASK);
                break;

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

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }
    }
}
