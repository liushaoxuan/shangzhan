package com.wyu.iwork.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.util.CustomUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;

/**
 * 展示页
 */
public class ShowActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<ImageView> mImageViews;
    private TextView mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show);
        initView();
    }

    private void initView(){
        ViewPager viewPager = (ViewPager) findViewById(R.id.show_viewpager);
        mButton = (TextView) findViewById(R.id.show_button);
        mButton.setOnClickListener(this);
        final int[] drawables = {R.drawable.show_first,R.drawable.show_second,R.drawable.show_third,R.drawable.show_end};
        mImageViews = new ArrayList<>();
        for(int i = 0;i<drawables.length;i++){
            ImageView imageView = new ImageView(this);
            AutoRelativeLayout.LayoutParams params = new AutoRelativeLayout.LayoutParams(AutoRelativeLayout.LayoutParams.MATCH_PARENT,AutoRelativeLayout.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(drawables[i]);
            mImageViews.add(imageView);
        }
        viewPager.setAdapter(new ViewPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mButton.setVisibility(position == drawables.length - 1?View.VISIBLE:View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_button:
                //1:保存数据
                SharedPreferences.Editor editor = getSharedPreferences("SYSTEM_VERSION", Context.MODE_PRIVATE).edit();
                editor.putString("VERSION", CustomUtils.getVersionName(this));
                editor.commit();
                //2跳转到登陆页面
                startActivity(new Intent(this,SigninActivity.class));
                finish();
                break;
        }
    }

    class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }
    }
}
