package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.FragmentPageadapter;
import com.wyu.iwork.view.fragment.MessageGonggaoFragment;
import com.wyu.iwork.view.fragment.MessageNoticeFragment;
import com.wyu.iwork.widget.CrmCreateCustomDialog;
import com.wyu.iwork.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 会议通知
 */
public class MettingActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    //标题
    @BindView(R.id.action_title)
    TextView mtitle;  //标题
    //发布
    @BindView(R.id.action_edit)
    TextView release;

    /**
     * viewpager
     */
    @BindView(R.id.activity_metting_viewpager)
    NoScrollViewPager viewPager;

    //通知
    @BindView(R.id.activity_metting_notice)
    TextView notice;

    //公告
    @BindView(R.id.activity_metting_gonggao)
    TextView gonggao;

    CrmCreateCustomDialog dialog;

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
        setContentView(R.layout.activity_metting);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
        adapter = new FragmentPageadapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        setCurrent();
    }

    private void setCurrent(){
        int page = 0;
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            page = bundle.getInt("page");
        }
        viewPager.setCurrentItem(page);

        if (page==0){
            notice.setSelected(true);
            gonggao.setSelected(false);
        }else {
            notice.setSelected(false);
            gonggao.setSelected(true);
        }
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        dialog = new CrmCreateCustomDialog(this, "公告", "会议通知", new CrmCreateCustomDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                Intent intent = new Intent(MettingActivity.this,PublishNoticeActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                Intent intent = new Intent(MettingActivity.this,MeetingMessageActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void threeClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        notice.setSelected(true);
        mtitle.setText("通知消息");
        release.setText("发布");
        fragments.add(new MessageNoticeFragment());
        fragments.add(new MessageGonggaoFragment());

    }


    @OnClick({R.id.action_back,R.id.activity_metting_notice,R.id.activity_metting_gonggao,R.id.action_edit})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.action_edit://发布
                if (dialog!=null){
                    dialog.show();
                }
                break;

            case R.id.activity_metting_notice://通知
                notice.setSelected(true);
                gonggao.setSelected(false);
                viewPager.setCurrentItem(0);
                break;

            case R.id.activity_metting_gonggao://公告
                notice.setSelected(false);
                gonggao.setSelected(true);
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
                notice.setSelected(true);
                gonggao.setSelected(false);
                break;
            case 1:
                notice.setSelected(false);
                gonggao.setSelected(true);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
