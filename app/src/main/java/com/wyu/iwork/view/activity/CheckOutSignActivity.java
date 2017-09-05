package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lzy.okgo.OkGo;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 外出打卡
 */
public class CheckOutSignActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = CheckOutSignActivity.class.getSimpleName();
    @BindView(R.id.check_out_sign_time)
    TextView check_out_sign_time;

    @BindView(R.id.check_out_sign_address)
    TextView check_out_sign_address;

    @BindView(R.id.check_out_sign_things)
    TextView check_out_sign_things;

    @BindView(R.id.check_out_sign_true)
    TextView check_out_sign_true;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.activity_check_out_sign_scrollview)
    ScrollView activity_check_out_sign_scrollview;

    private SimpleDateFormat format;
    private Handler handle;
    private String longitude = "361";
    private String latitude = "361";
    private String address = "定位失败";

    private LocationClient mLocationClient = null;
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_check_out_sign_scrollview);
        return false;
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
                    check_out_sign_address.setText(address);
                    break;
                case BDLocation.TypeNetWorkException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    check_out_sign_address.setText(address);
                    break;
                case BDLocation.TypeCriteriaException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    MsgUtil.shortToastInCenter(CheckOutSignActivity.this,"请检查手机是否处于飞行模式!");
                    check_out_sign_address.setText(address);
                    break;
                default:
                    Logger.i(TAG,"定位失败");
                    check_out_sign_address.setText(address);
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
        //设置地址

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Logger.i(TAG,address);
                check_out_sign_address.setText(address);
                Logger.i(TAG,address);
            }
        });
    }

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out_sign);
        ButterKnife.bind(this);
        initView();
        hideToolbar();
    }

    private void initView(){

        tv_title.setText("外出登记");
        //设置打卡时间
        setSignTime();
        //更新时间 10秒更新一次
        updateSignTime();
        //设置打卡地点
        setLocation();

        activity_check_out_sign_scrollview.setOnTouchListener(this);

    }

    private void setLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        //int span=1000;
        //option.setScanSpan(span);
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

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    @OnClick({R.id.check_out_sign_true,R.id.ll_back})
    void Click(View view){
        switch (view.getId()){
            case R.id.check_out_sign_true:
                //上传打卡数据
                commitSignData();
                break;
            case R.id.ll_back:
                finish();
        }
    }

    private void commitSignData(){
        //判断事由是有为空
        /**
         * user_id	是	int[11]用户id
         status	是	int[2]签到状态，0：外勤签到，1：正常签到
         longitude	是	decimal(10,6)所在地坐标经度,百度坐标，格式：22.123456 ，注意必须小于等于6位小数
         latitude	是	decimal(10,6)所在地坐标纬度,百度坐标，格式：33.123456 ，同上
         address	否	string[50]外勤具体地点,外勤时填写
         visit	否	string[50]外勤拜访人,外勤时填写
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(user_id.status.longitude.latitude.F.V.RandStr)
         */
        String visit = check_out_sign_things.getText().toString();
        if(TextUtils.isEmpty(visit)){
            MsgUtil.shortToastInCenter(this,"外出事由不能为空!");
            return;
        }
        String F = Constant.F;
        String V = Constant.V;
        //String longitude = "121.497854";
        //String latitude = "31.240722";
        //String address = "江西中路349号(近天津路)";
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+"0"+longitude+latitude);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("status","0");
        data.put("longitude",longitude);
        data.put("latitude",latitude);
        data.put("address",address);
        data.put("visit",visit);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SING,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.i(TAG,s);
                parseData(s);
            }
        });
    }

    private void parseData(String data){
        JSONObject object = null;
        try {
            object = new JSONObject(data);
            if("0".equals(object.getString("code"))){
                finish();
            }
            MsgUtil.shortToastInCenter(CheckOutSignActivity.this,object.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSignTime(){
        if(handle == null){
            handle = new Handler(){
                public void handleMessage(Message msg){
                    setSignTime();
                }
            };
        }
        handle.sendEmptyMessageDelayed(0,10000);//每隔10秒更新一次数据
    }

    private void setSignTime(){
        Date date = new Date();
        if(format == null){
            format = new SimpleDateFormat("yyyy年MM月dd日 HH:ss");
        }
        check_out_sign_time.setText(format.format(date));
    }
}
