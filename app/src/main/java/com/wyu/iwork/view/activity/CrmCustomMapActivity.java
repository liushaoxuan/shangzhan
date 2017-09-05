package com.wyu.iwork.view.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CrmCustomListViewAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MapCustomModule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.mipmap.location;

/**
 * 客户地图
 */
public class CrmCustomMapActivity extends AppCompatActivity {

    private static final String TAG = CrmCustomMapActivity.class.getSimpleName();
    //返回按钮
    @BindView(R.id.crm_custom_map_back)
    ImageView crm_custom_map_back;

    //搜索框
    @BindView(R.id.crm_custom_map_search)
    AutoLinearLayout crm_custom_map_search;

    //定位到当前位置
    @BindView(R.id.crm_custom_map_location)
    ImageView crm_custom_map_location;

    //方圆5公里
    @BindView(R.id.crm_custom_map_five_meter)
    RadioButton crm_custom_map_five_meter;

    //方圆十公里
    @BindView(R.id.crm_custom_map_ten_meter)
    RadioButton crm_custom_map_ten_meter;

    //地图图层
    @BindView(R.id.crm_custom_map_mapview)
    MapView crm_custom_map_mapview;

    @BindView(R.id.crm_custom_map_listview)
    ListView crm_custom_map_listview;

    //暂无的布局
    @BindView(R.id.crm_custom_zanwu)
    AutoLinearLayout crm_custom_zanwu;

    private CrmCustomListViewAdapter adapter;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();//定位监听器

    private double longitude = 1000;//经度
    private double latitude = 1000;//纬度
    private Gson gson;
    private static final String ROUND_FIVE_METER = "1";//方圆5公里
    private static final String ROUND_TEN_METER = "2";//方圆十公里
    private String currentRound = ROUND_FIVE_METER;//当前半径
    private ArrayList<MapCustomModule.Custom> list;//存放附近客户的集合
    private CircleOptions mOptions;
    private View pop;
    private TextView tv_message;
    private TextView tv_address;

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 0){
                showPopView();
            }
        }
    };
    private String mAddress;
    private String mBuildingName;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crm_custom_map);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);

        // 自定义颜色
        tintManager.setTintColor(getResources().getColor(R.color.transparent));
        ButterKnife.bind(this);
        list = new ArrayList<>();
        initView();
    }

    private void initView(){
        //初始化定位数据配置
        //获取地图控制器
        mBaiduMap = crm_custom_map_mapview.getMap();
        //声明locationClient类
        crm_custom_map_mapview.showZoomControls(false);//隐藏缩放按钮
        crm_custom_map_mapview.showScaleControl(false);//隐藏比例尺
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        setMyLocationConfigeration(MyLocationConfiguration.LocationMode.FOLLOWING);
        //配置覆盖物基本属性
        mOptions =  new CircleOptions();	// 创建一个圆形覆盖物的参数
        mOptions.radius(2500)	// 半径
                .fillColor(0x5587CEFA);	// 圆的填充颜色
        mLocationClient.start();

    }

    /** 设置定位图层的配置 */
    private void setMyLocationConfigeration(
            MyLocationConfiguration.LocationMode mode) {
        boolean enableDirection = false;	//
        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(location);	// 自定义定位的图标
        MyLocationConfiguration config = new MyLocationConfiguration(mode, enableDirection, customMarker,R.color.transparent,R.color.transparent);
        mBaiduMap.setMyLocationConfigeration(config);
    }

    //初始化位置参数
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        //option.setScanSpan(0);
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

    //定位监听器
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location != null) {
                mBaiduMap.clear();
                MyLocationData.Builder builder = new MyLocationData.Builder();
                builder.accuracy(location.getRadius());		// 设置精度
                builder.direction(location.getDirection());	// 设置方向
                builder.latitude(location.getLatitude());	// 设置纬度
                builder.longitude(location.getLongitude());	// 设置经度
                MyLocationData locationData = builder.build();
                mBaiduMap.setMyLocationData(locationData);	// 把定位数据显示到地图上
                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                mOptions.center(latLng);
                mBaiduMap.addOverlay(mOptions);	// 添加一个覆盖物
                float f = mBaiduMap.getMaxZoomLevel();//19.0

                //float m = mBaiduMap.getMinZoomLevel();//3.0

                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, f-9);

                mBaiduMap.animateMapStatus(u,600);
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();//获取经度信息
            mAddress = location.getAddrStr();
            mBuildingName = location.getBuildingName();
            mHandler.sendEmptyMessage(0);

            //location.getRadius()    //获取定位精准度
            if (location.getLocType() == BDLocation.TypeServerError) {

                //sb.append("\ndescribe : ");
                //sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

               // sb.append("\ndescribe : ");
               // sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

               // sb.append("\ndescribe : ");
               // sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null && list.size()>0) {
                mTitle = list.get(0).getName();
            }
            Logger.i(TAG,"Latitude=="+latitude+"  longitude=="+longitude);
            mLocationClient.stop();
            getNearCustomFromServer();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void getNearCustomFromServer(){
        /**
         * user_id	是	int[11]用户ID
         lng	是	float[8]百度坐标 经度
         lat	是	float[8]百度坐标 纬度
         scope	是	int[2]范围  1：5000m  2：10000m
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.lng)
         */
        if(longitude != 1000 && latitude != 1000){
            String RandStr = CustomUtils.getRandStr();
            String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+longitude);
            OkGo.get(Constant.URL_CUSTOMER_MAP).tag(this)
                    .params("user_id", AppManager.getInstance(this).getUserInfo().getUser_id())
                    .params("lng",longitude)
                    .params("lat",latitude)
                    .params("scope",currentRound)
                    .params("F",Constant.F)
                    .params("V",Constant.V)
                    .params("RandStr",RandStr)
                    .params("Sign",Sign)
                    .execute(new DialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            super.onSuccess(s,call,response);
                            Logger.i(TAG,s);
                            parseCustomData(s);
                        }
                    });
        }else{
            MsgUtil.shortToastInCenter(this,"定位失败,请退出该界面并重试!");
        }
    }
    //解析客户数据
    private void parseCustomData(String s){
        if(gson == null){
            gson = new Gson();
        }
        MapCustomModule module = null;
        try {
            module = gson.fromJson(s,MapCustomModule.class);
            if("0".equals(module.getCode())){
                if(module.getData() != null && module.getData().size()>0){
                    list.clear();
                    list.addAll(module.getData());
                    showContent(true);
                    setAdapter(module.getData());
                }else{
                    //附近没有客户
                    showContent(false);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }finally {
            if(module != null){
                module = null;
            }
        }
    }

    //设置适配器
    private void setAdapter(ArrayList<MapCustomModule.Custom> list){
        adapter = new CrmCustomListViewAdapter(this,list,1);
        crm_custom_map_listview.setAdapter(adapter);
    }

    @OnClick({R.id.crm_custom_map_location,R.id.crm_custom_map_five_meter,R.id.crm_custom_map_ten_meter,
            R.id.crm_custom_map_back,R.id.crm_custom_map_search})
    void Click(View v){
        switch (v.getId()){
            case R.id.crm_custom_map_location:
                if(mLocationClient!= null){
                    mLocationClient.start();
                }else{

                }
                break;
            case R.id.crm_custom_map_five_meter:
                //5公里
                currentRound = ROUND_FIVE_METER;
                mBaiduMap.clear();
                mOptions.radius(2500)	// 半径
                        .fillColor(0x5587CEFA);
                LatLng latLng = new LatLng(latitude,longitude);
                mOptions.center(latLng);
                mBaiduMap.addOverlay(mOptions);	// 添加一个覆盖物
                getNearCustomFromServer();
                break;
            case R.id.crm_custom_map_ten_meter:
                //十公里
                currentRound = ROUND_TEN_METER;
                mBaiduMap.clear();
                mOptions.radius(5000)	// 半径
                        .fillColor(0x5587CEFA);
                LatLng latLng1 = new LatLng(latitude,longitude);
                mOptions.center(latLng1);
                mBaiduMap.addOverlay(mOptions);	// 添加一个覆盖物
                getNearCustomFromServer();
                break;
            case R.id.crm_custom_map_back:
                onBackPressed();
                break;
            case R.id.crm_custom_map_search:
                Intent intent = new Intent(this,CrmCustomMapSearchActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("LIST",list);
                intent.putExtras(bundle);
                startActivityForResult(intent,101);
                break;
        }
    }

    private void showContent(boolean flag){
        crm_custom_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        crm_custom_map_listview.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onDestroy() {
        try {
            if(mLocationClient != null){
                mLocationClient.stop();//在activity销毁的时候，停止定位
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == 101){
            getNearCustomFromServer();
        }
    }

    //创建popview布局
    /**
     * 创建一个布局参数
     * @param position
     * @return
     */
    private MapViewLayoutParams createLayoutParams(LatLng position) {
        MapViewLayoutParams.Builder buidler = new MapViewLayoutParams.Builder();
        buidler.layoutMode(MapViewLayoutParams.ELayoutMode.mapMode);	// 指定坐标类型为经纬度
        buidler.position(position);		// 设置标志的位置
        buidler.yOffset(-50);			// 设置View往上偏移
        MapViewLayoutParams params = buidler.build();
        return params;
    }

    private void showPopView(){
        // 显示一个泡泡
        LatLng l = new LatLng(latitude,longitude);
        if (pop == null) {
            pop = View.inflate(CrmCustomMapActivity.this, R.layout.layout_pop_map_view, null);
            tv_message = (TextView) pop.findViewById(R.id.tv_message);
            tv_address = (TextView) pop.findViewById(R.id.tv_address);
            crm_custom_map_mapview.addView(pop, createLayoutParams(l));
        } else {
            crm_custom_map_mapview.updateViewLayout(pop, createLayoutParams(l));
        }
        if(!TextUtils.isEmpty(mTitle)){
            tv_message.setText(mTitle+"( 附近 )");
        }
        tv_address.setText(mAddress);
    }
}
