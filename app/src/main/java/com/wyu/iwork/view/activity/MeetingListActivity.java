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

import com.google.gson.Gson;
import com.wyu.iwork.R;
import com.wyu.iwork.model.MeetingListMoudle;
import com.wyu.iwork.view.fragment.BaseFragment;
import com.wyu.iwork.view.fragment.ReceiveMeetingFragment;
import com.wyu.iwork.view.fragment.SendMeetingFragment;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingListActivity extends BaseActivity {

    private static final String TAG = MeetingListActivity.class.getSimpleName();

//    @BindView(R.id.meeting_recycleview)
//    RecyclerView recycleview;
//
//    @BindView(R.id.meeting_not_available)
//    LinearLayout notavailable;
//
//    @BindView(R.id.tv_notavailable)
//    TextView tv_notavailable;

    @BindView(R.id.meeting_list_viewpager)
    ViewPager viewpager;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_edit)
    TextView edit;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.tv_work)
    TextView tv_work;

    @BindView(R.id.tv_person)
    TextView tv_person;

    private Gson gson;
    private MeetingListMoudle meeting;
    private ArrayList<BaseFragment> fragments;
    private ReceiveMeetingFragment receiveFragment;
    private SendMeetingFragment sendFragment;

    private static final int TYPE_RECEIVE = 1;
    private static final int TYPE_SEND = 2;
    private int CURRENTTYPE = TYPE_RECEIVE;
    private static final int CODE_REQUEST = 200;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_list);
        hideToolbar();
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getMeetingList();
    }

    private void initView(){
        title.setText("会议通知");
        edit.setText("新建");
        tv_work.setText("我收到的");
        tv_person.setText("我发出的");
        setSelectLayout(TYPE_RECEIVE);
        edit.setVisibility(View.VISIBLE);
        fragments = new ArrayList<>();
        receiveFragment = new ReceiveMeetingFragment();
        sendFragment = new SendMeetingFragment();
        fragments.add(receiveFragment);
        fragments.add(sendFragment);
        //tv_notavailable.setText("暂无会议通知");
        viewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    setSelectLayout(TYPE_RECEIVE);
                    receiveFragment.initData();
                }else{
                    setSelectLayout(TYPE_SEND);
                    sendFragment.initData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.ll_back,R.id.tv_edit,R.id.tv_work,R.id.tv_person})
    void Click(View view){
        switch (view.getId()){
            case R.id.tv_work:
                setSelectLayout(TYPE_RECEIVE);
                break;
            case R.id.tv_person:
                setSelectLayout(TYPE_SEND);
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                startActivityForResult(new Intent(this,MeetingMessageActivity.class),CODE_REQUEST);
                break;
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

    private void setSelectLayout(int type){
        if(type == TYPE_RECEIVE){
            //当前为我收到的会议
            setSelect(true);
            CURRENTTYPE = 1;
            viewpager.setCurrentItem(0);
        }else if(type == TYPE_SEND){
            //当前为我发送的会议
            setSelect(false);
            CURRENTTYPE = 2;
            viewpager.setCurrentItem(1);
        }

    }

    private void setSelect(boolean flag){
        if(flag){
            //收到的会议
            tv_work.setSelected(true);
            tv_person.setSelected(false);
        }else{
            //发送的会议
            tv_person.setSelected(true);
            tv_work.setSelected(false);
        }
    }

    private BaseFragment getFragment(int type){
        if(type == TYPE_RECEIVE){
            return fragments.get(0);
        }else{
            return fragments.get(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            receiveFragment.initData();
            sendFragment.initData();
        }
    }
}
