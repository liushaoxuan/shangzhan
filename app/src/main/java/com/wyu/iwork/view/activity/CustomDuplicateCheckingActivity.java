package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.view.fragment.CompanyDuplicateFragment;
import com.wyu.iwork.view.fragment.PersonDuplicateFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author juxinhua
 * CRM - 客户查重
 */
public class CustomDuplicateCheckingActivity extends BaseActivity {

    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //个人查重
    @BindView(R.id.checking_person)
    AutoLinearLayout checking_person;

    //公司查重
    @BindView(R.id.checking_company)
    AutoLinearLayout checking_company;

    //ViewPager
    @BindView(R.id.custom_duplicate_checking_viewpager)
    ViewPager viewPager;

    private ArrayList<Fragment> fragments;//存储fragment

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_duplicate_checking);
        hideToolbar();
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        tv_title.setText("客户查重");
        fragments = new ArrayList<>();
        fragments.add(new PersonDuplicateFragment());
        fragments.add(new CompanyDuplicateFragment());
        viewPager.setAdapter(new ViewpagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0,true);
        checking_person.setSelected(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    checking_person.setSelected(true);
                    checking_company.setSelected(false);
                }else if(position == 1){
                    checking_person.setSelected(false);
                    checking_company.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.checking_person,R.id.checking_company,R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.checking_person:
                personDuplicateSelect();
                break;
            case R.id.checking_company:
                companyDuplicateSelect();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    //当前为个人查重
    private void personDuplicateSelect(){
        viewPager.setCurrentItem(0,true);
        checking_person.setSelected(true);
        checking_company.setSelected(false);
    }

    //当前为公司查重
    private void companyDuplicateSelect(){
        viewPager.setCurrentItem(1,true);
        checking_person.setSelected(false);
        checking_company.setSelected(true);
    }

    //ViewPager的适配器
    class ViewpagerAdapter extends FragmentPagerAdapter{

        public ViewpagerAdapter(FragmentManager fm) {
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
}
