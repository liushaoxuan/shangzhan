package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.FragmentPageadapter;
import com.wyu.iwork.adapter.oaMyLauchedAdapter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.view.fragment.oaMeAprovaledFragment;
import com.wyu.iwork.view.fragment.oaWaiteMeAprovalFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author  sxliu
 * OA——我审批的
 */
public class oaMyAprovalActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 待审批
     */
    @BindView(R.id.activity_oa_my_aproval_waite_me_aproval)
    TextView wait_aproval;

    /**
     * 已审批
     */
    @BindView(R.id.activity_oa_my_aproval_me_aprovaled)
    TextView aprovaled;

    /**
     *  viewpager
     */
    @BindView(R.id.activity_oa_my_aproval_viewpager)
    ViewPager viewPager;

    private FragmentPageadapter adapter;

    private List<Fragment> fragments = new ArrayList<>();
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_my_aproval);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
    }

    //初始化
    private void init(){
        title.setText(R.string.my_aproval);
        edit.setVisibility(View.GONE);
        wait_aproval.setSelected(true);
        fragments.add(new oaWaiteMeAprovalFragment());
        fragments.add(new oaMeAprovaledFragment());
        adapter = new FragmentPageadapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @OnClick({R.id.action_back,R.id.activity_oa_my_aproval_waite_me_aproval,R.id.activity_oa_my_aproval_me_aprovaled})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.activity_oa_my_aproval_waite_me_aproval://待审批
                wait_aproval.setSelected(true);
                aprovaled.setSelected(false);
                viewPager.setCurrentItem(0);
                break;

            case R.id.activity_oa_my_aproval_me_aprovaled://已经审批
                wait_aproval.setSelected(false);
                aprovaled.setSelected(true);
                viewPager.setCurrentItem(1);
                break;
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                wait_aproval.setSelected(true);
                aprovaled.setSelected(false);
                break;
            case 1:
                wait_aproval.setSelected(false);
                aprovaled.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
