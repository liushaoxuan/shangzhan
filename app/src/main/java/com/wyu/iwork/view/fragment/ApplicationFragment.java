package com.wyu.iwork.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wyu.iwork.R;
import com.wyu.iwork.model.QRCodeModule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.QRCodeContentActivity;
import com.wyu.iwork.view.activity.ScanCardResultActivity;
import com.wyu.iwork.view.activity.WebActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/1/17.
 * 应用
 */

public class ApplicationFragment extends BaseFragment {

    private static final String TAG = ApplicationFragment.class.getSimpleName();
    @BindView(R.id.fragment_application_viewpager)
    ViewPager viewPager;


    private static int REQUEST_CODE = 1001;
    private ImageView mZxing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadingDialog = new LoadingDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_application,null);
        ButterKnife.bind(this,container);
        setViewPager();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setViewPager(){
        viewPager.setAdapter(new ApplicationFragment.ViewPagerAdapter(getChildFragmentManager()));
        //viewPager.addOnPageChangeListener(mPageListener);
    }
    /**
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            final int position = mTabGroup.indexOfChild(buttonView);
            viewPager.setCurrentItem(position, false);
        }

    }*/

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //Fragment fragment = null ;
            /**
            switch (position) {
                case 0:
                    fragment =  new SelectedApplicatFragment(applicatModel.getExquisite());
                    break;
                case 1:
                    fragment =   new CommonApplicatFragment(applicatModel.getCommon());
                    break;
                case 2:
                    fragment = new NavigatApplicatFragment(applicatModel.getNavigation());
                    break;
            }*/

            return new SelectedApplicatFragment();
        }

        @Override
        public int getCount() {
            return 1;//
        }
    }
/**
    private ViewPager.SimpleOnPageChangeListener mPageListener =
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    if (mTabGroup == null) return;
                    ((RadioButton) mTabGroup.getChildAt(position)).toggle();
                }
            };*/

    private static final int CODE_CAMERA = 1;
    private void initTabLayout() {
        View tabLayout = ((MainActivity) getActivity()).getCustomViewByLayoutId(
                R.layout.layout_tab_toolbar_applicat_new);
            //mTabGroup = (RadioGroup) tabLayout.findViewById(R.id.layout_tab_toolbar_applicat_radiogroup);
            //for (int i = 0; i < mTabGroup.getChildCount(); i++) {
              //  ((RadioButton) mTabGroup.getChildAt(i)).setOnCheckedChangeListener(this);
        //}
        mZxing = (ImageView) tabLayout.findViewById(R.id.zxing_icon);
        mZxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        int isPermission = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA);
                        if(isPermission != PackageManager.PERMISSION_GRANTED){
                            Logger.i(TAG,"请求权限");
                            ApplicationFragment.this.requestPermissions(new String[]{Manifest.permission.CAMERA},CODE_CAMERA);
                        }else{
                            Logger.i(TAG,"已有权限");
                            scanCode();
                        }
                    }else{
                        scanCode();
                    }

                }catch (Exception e){
                    MsgUtil.shortToastInCenter(getActivity(),"请前往手机设置检查拍照权限是否打开!");
                }
            }
        });

    }

    private void scanCode(){
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        initTabLayout();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Logger.i(TAG,result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败，请重试!", Toast.LENGTH_LONG).show();

                }
            }
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_CAMERA){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Logger.i(TAG,"权限请求通过");
                scanCode();
            } else {
                // Permission Denied
                Logger.i(TAG,"权限请求未通过");
                MsgUtil.shortToastInCenter(getActivity(),"请前往手机设置检查拍照权限是否打开!");

            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
