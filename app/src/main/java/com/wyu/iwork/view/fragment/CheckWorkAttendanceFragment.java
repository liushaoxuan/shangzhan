package com.wyu.iwork.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.activity.CheckInSettingActivity;
import com.wyu.iwork.view.activity.OutRecordActivity;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lx on 2017/3/11.
 * 手机考勤
 */

public class CheckWorkAttendanceFragment extends BaseFragment{

    private static final String TAG = CheckWorkAttendanceFragment.class.getSimpleName();

    //打卡
    @BindView(R.id.check_work_attendance_check)
    AutoRelativeLayout checkLayout;

    //外出
    @BindView(R.id.check_work_attendance_out)
    AutoRelativeLayout outLayout;

    //统计
    @BindView(R.id.check_work_attendance_statistics)
    AutoRelativeLayout tongjiLayout;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    private static final int TYPE_SING = 1;//打卡
    private static final int TYPE_OUT_SING = 2;//外出考勤
    private static final int TYPE_STATIC = 3;//统计
    private int currentType = TYPE_SING;

    private String address = "定位失败";
    private String longitude = "361";
    private String latitude = "361";
    private Dialog dialog;

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    private List<Fragment> fragments;
    private List<AutoRelativeLayout> layoutList;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 1){
                stopLocation();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragments = new ArrayList<>();
        layoutList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_check_work_attendance,null);
        ButterKnife.bind(this,view);
        tv_title.setText("手机考勤");
        tv_edit.setText("设置");
        showEditButton();
        initDialog();
        initLocation();
        initFragments();
        setselectedLayout(0);
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Logger.i(TAG,keyCode+"");
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    Logger.i(TAG,"CheckWorkAttendance onBack!");
                    stopLocation();
                }
                return false;
            }
        });
        return view;
    }

    private void showEditButton(){
        if("1".equals(MyApplication.userInfo.getIs_admin())){
            tv_edit.setVisibility(View.VISIBLE);
        }else{
            tv_edit.setVisibility(View.GONE);
        }
    }

    //初始化定位过程中的dialog
    private void initDialog() {
        dialog = new Dialog(getActivity(), R.style.progress_dialog);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setOnKeyListener(onKeyListener);
        TextView msg = (TextView) dialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("定位中...");
    }

    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                stopLocation();
            }
            return false;
        }
    };

    @OnClick({R.id.check_work_attendance_check,R.id.check_work_attendance_out,R.id.check_work_attendance_statistics,R.id.ll_back,R.id.tv_edit})
    void CLick(View v){
        switch (v.getId()){
            case R.id.check_work_attendance_check://打卡
                showEditButton();
                setselectedLayout(0);
                tv_edit.setText("设置");
                tv_title.setText("手机考勤");
                currentType = TYPE_SING;
                break;

            case R.id.check_work_attendance_out://外出
                setselectedLayout(1);
                tv_edit.setText("外出记录");
                tv_title.setText("外出考勤");
                currentType = TYPE_OUT_SING;
                tv_edit.setVisibility(View.VISIBLE);
                break;

            case R.id.check_work_attendance_statistics://统计
                showEditButton();
                setselectedLayout(2);
                tv_edit.setVisibility(View.GONE);
                tv_edit.setText("考勤统计");
                tv_title.setText("我的考勤");
                currentType = TYPE_STATIC;
                break;

            case R.id.tv_edit:
                //编辑
                if(currentType == TYPE_OUT_SING){
                    //外出记录
                    startActivity(new Intent(getActivity(), OutRecordActivity.class));
                }else if(currentType == TYPE_SING) {
                    //打卡设置
                    startActivity(new Intent(getActivity(), CheckInSettingActivity.class));
                }
                break;
            case R.id.ll_back:
                //返回
                getActivity().finish();
                break;
        }
    }

    /**
     * 设置fragment
     */
    private void setFragment(Fragment fragment){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.check_work_attendance_framlayout,fragment);
        transaction.commit();
    }

    //
    private void initFragments(){
        fragments.clear();
        layoutList.clear();
        fragments.add(new ClockInFragment());
        fragments.add(new ClockGoOutFragment());
        fragments.add(new ClockStatisticsFragment());
        layoutList.add(checkLayout);
        layoutList.add(outLayout);
        layoutList.add(tongjiLayout);
    }

    private void setselectedLayout(int num){
        for (int i = 0;i<layoutList.size();i++){
            if (num==i){
                layoutList.get(i).setSelected(true);
            }else {
                layoutList.get(i).setSelected(false);
            }
        }
        setFragment(fragments.get(num));
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            switch (location.getLocType()){
                case BDLocation.TypeGpsLocation://GPS定位
                case BDLocation.TypeNetWorkLocation://网络定位
                case BDLocation.TypeOffLineLocation://离线定位
                    //均为有效定位
                    getAddressMessage(location);
                    break;
                case BDLocation.TypeServerError:
                    //服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                    Logger.i(TAG,"服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    stopLocation();
                    break;
                case BDLocation.TypeNetWorkException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    stopLocation();
                    break;
                case BDLocation.TypeCriteriaException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    MsgUtil.shortToastInCenter(getActivity(),"请检查手机是否处于飞行模式!");
                    stopLocation();
                    break;
                default:
                    Logger.i(TAG,"定位失败");
                    stopLocation();
                    break;
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void getAddressMessage(BDLocation location){
        longitude = location.getLongitude()+"";
        latitude = location.getLatitude()+"";
        address = location.getAddrStr();
        String ss = location.getAddress().address;
        int nu = location.getSatelliteNumber();
        Logger.i(TAG,"longitude:"+longitude+"   latitude:"+latitude+"    address:"+address+"   ss="+ss+" nu="+nu);
        stopLocation();
        locationService.getLocation(location);
    }

    public void stopLocation(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
        if(mLocationClient != null){
            mLocationClient.stop();
        }
    }

    Locationservice locationService;

    public interface Locationservice{
        void getLocation(BDLocation location);
    }

    public void initLocation(){
        //配置定位
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span=0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    public void startLocationClient(Locationservice locationservice){
        this.locationService = locationservice;
        if(locationservice != null){
            mLocationClient.start();
            mHandler.sendEmptyMessageDelayed(1,5000);
            if(dialog != null && !dialog.isShowing()){
                dialog.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        if(mLocationClient != null){
            mLocationClient.stop();
            mLocationClient = null;
        }
        super.onDestroy();
    }
}
