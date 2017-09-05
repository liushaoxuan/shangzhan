package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.RouteMapAdapter;
import com.wyu.iwork.overlayUtil.DrivingRouteOverlay;
import com.wyu.iwork.overlayUtil.TransitRouteOverlay;
import com.wyu.iwork.overlayUtil.WalkingRouteOverlay;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CrmRouteMapActivity extends AppCompatActivity {

    private static final String TAG = CrmRouteMapActivity.class.getSimpleName();

    //地图控件
    @BindView(R.id.route_mapview)
    MapView route_mapview;

    //开关小按钮
    @BindView(R.id.iv_open_close)
    ImageView iv_open_close;

    //路线标题
    @BindView(R.id.route_map_title)
    TextView route_map_title;

    //路程耗时
    @BindView(R.id.route_map_time)
    TextView route_map_time;

    //路线所需距离
    @BindView(R.id.route_map_distance)
    TextView route_map_distance;

    //路线所需步行距离
    @BindView(R.id.route_map_walking)
    TextView route_map_walking;

    //具体路线
    @BindView(R.id.route_map_detail)
    AutoLinearLayout route_map_detail;

    //路线容器
    @BindView(R.id.route_map_recycleview)
    RecyclerView route_map_recycleview;

    //再次定位
    @BindView(R.id.crm_route_map_location)
    ImageView crm_route_map_location;

    //返回
    @BindView(R.id.crm_route_map_back)
    ImageView crm_route_map_back;

    @BindView(R.id.route_content)
    AutoLinearLayout route_content;

    @BindView(R.id.view)
    View view;

    private DrivingRouteLine drivingRouteLine;//驾车路线
    private TransitRouteLine transitRouteLine;//换乘路线
    private WalkingRouteLine walkingRouteLine;//步行路线
    private String Type;
    private BaiduMap baiduMap;
    private RouteMapAdapter adapter;
    private LocationClient mLocationClient;
    private double longitude;
    private double latitude;
    private BDLocationListener myListener = new MyLocationListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_route_map);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ButterKnife.bind(this);
        getExtras();
    }

    private void getExtras(){
        Intent intent = getIntent();
        Type = intent.getStringExtra("TYPE");
        if("1".equals(Type)){
            //驾车路线
            drivingRouteLine = (DrivingRouteLine) intent.getParcelableExtra("ROUTE_LINE");
            route_map_title.setText(drivingRouteLine.getAllStep().get(0).getEntranceInstructions());
            route_map_time.setText(drivingRouteLine.getDuration()/60+"分钟");
            route_map_distance.setText(drivingRouteLine.getDistance()<1000?drivingRouteLine.getDistance()+"米":drivingRouteLine.getDistance()/1000.0+"公里");
            route_map_walking.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }else if("2".equals(Type)){
            //换乘路线
            transitRouteLine = (TransitRouteLine) intent.getParcelableExtra("ROUTE_LINE");
            int index = 1;
            String trans = "";
            for(int i = 0;i<transitRouteLine.getAllStep().size();i++){
                if(transitRouteLine.getAllStep().get(i).getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.BUSLINE ||
                        transitRouteLine.getAllStep().get(i).getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.SUBWAY){
                    if(index >1){
                        trans += " - ";
                    }
                    trans += transitRouteLine.getAllStep().get(i).getVehicleInfo().getTitle();
                    index += 1;
                }
            }
            route_map_title.setText(trans);
            route_map_time.setText(transitRouteLine.getDuration()/60+"分钟");
            route_map_distance.setText(transitRouteLine.getDistance()<1000?transitRouteLine.getDistance()+"米":transitRouteLine.getDistance()/1000.0+"公里");
            int distance = 0;
            for(TransitRouteLine.TransitStep step:transitRouteLine.getAllStep()){
                if(step.getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING){
                    distance += step.getDistance();
                }
            }
            route_map_walking.setText(distance+"米");
        }else if("3".equals(Type)){
            //步行路线
            walkingRouteLine = (WalkingRouteLine) intent.getParcelableExtra("ROUTE_LINE");
            route_map_title.setText(walkingRouteLine.getAllStep().get(0).getInstructions());
            route_map_time.setText(walkingRouteLine.getDuration()/60+"分钟");
            route_map_distance.setText(walkingRouteLine.getDistance()<1000?walkingRouteLine.getDistance()+"米":walkingRouteLine.getDistance()/1000.0+"公里");
            route_map_walking.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    //初始化界面数据
    private void initView(){
        //配置定位功能
        //配置定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocationconfiguration();

        baiduMap = route_mapview.getMap();
        baiduMap.setMyLocationEnabled(true);	// 开启定位图层
        setMyLocationConfigeration(MyLocationConfiguration.LocationMode.FOLLOWING);
        mLocationClient.start();
        setMapOverlay();//设置路线覆盖物
        setAdapter();//设置具体路线
    }

    //设置路线
    private void setMapOverlay(){
        //获取所有规划路线方案
        if("1".equals(Type)){
            if(drivingRouteLine!=null){
                DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                overlay.setData(drivingRouteLine);	// 把搜索结果设置到覆盖物
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.addToMap();					// 把搜索结果添加到地图
                overlay.zoomToSpan();				// 把搜索结果在一个屏幕内显示完
            }
        }else if("2".equals(Type)){
            if(transitRouteLine != null){
                TransitRouteOverlay overlay = new TransitRouteOverlay(baiduMap);
                overlay.setData(transitRouteLine);	// 把搜索结果设置到覆盖物
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.addToMap();					// 把搜索结果添加到地图
                overlay.zoomToSpan();				// 把搜索结果在一个屏幕内显示完
            }
        }else if("3".equals(Type)){
            if(walkingRouteLine != null){
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
                overlay.setData(walkingRouteLine);	// 把搜索结果设置到覆盖物
                baiduMap.setOnMarkerClickListener(overlay);
                overlay.addToMap();					// 把搜索结果添加到地图
                overlay.zoomToSpan();				// 把搜索结果在一个屏幕内显示完
            }
        }
    }

    //设置具体路线
    private void setAdapter(){
        if("1".equals(Type)){
            //驾车路线
            adapter = new RouteMapAdapter(this,1,drivingRouteLine);
        }else if("2".equals(Type)){
            //换乘路线
            adapter = new RouteMapAdapter(this,2,transitRouteLine);
        }else if("3".equals(Type)){
            //步行路线
            adapter = new RouteMapAdapter(this,3,walkingRouteLine);
        }
        route_map_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        route_map_recycleview.setAdapter(adapter);
    }

    @OnClick({R.id.route_content,R.id.crm_route_map_location,R.id.crm_route_map_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.route_content:
                if(route_map_detail.getVisibility() == View.VISIBLE){
                    route_map_detail.setVisibility(View.GONE);
                    iv_open_close.setImageDrawable(getResources().getDrawable(R.mipmap.open));
                }else{
                    route_map_detail.setVisibility(View.VISIBLE);
                    iv_open_close.setImageDrawable(getResources().getDrawable(R.mipmap.close));
                }
                break;
            case R.id.crm_route_map_location:
                //再次定位
                if(mLocationClient != null){
                    mLocationClient.start();
                }
                break;
            case R.id.crm_route_map_back:
                finish();
                break;
        }
    }

    //初始化地图定位配置
    private void initLocationconfiguration(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setScanSpan(0);

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        //option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
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

                    break;
                case BDLocation.TypeNetWorkException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    MsgUtil.shortToastInCenter(CrmRouteMapActivity.this,"请检查手机是否处于飞行模式!");
                    break;
                case BDLocation.TypeCriteriaException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    MsgUtil.shortToastInCenter(CrmRouteMapActivity.this,"请检查手机是否处于飞行模式!");
                    break;
                default:
                    Logger.i(TAG,"定位失败");

                    break;
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void getAddressMessage(BDLocation location){
        if (location != null) {
            MyLocationData.Builder builder = new MyLocationData.Builder();
            builder.accuracy(location.getRadius());		// 设置精度
            builder.direction(location.getDirection());	// 设置方向
            builder.latitude(location.getLatitude());	// 设置纬度
            builder.longitude(location.getLongitude());	// 设置经度
            MyLocationData locationData = builder.build();
            baiduMap.setMyLocationData(locationData);	// 把定位数据显示到地图上
            /**
             LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
             mOptions.center(latLng);
             baiduMap.addOverlay(mOptions);	// 添加一个覆盖物
             routePlanSearch.drivingSearch(getSearchParams(latLng));*/

        }
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        String ss = location.getAddress().address;
        int nu = location.getSatelliteNumber();
        Logger.i(TAG,"longitude:"+ longitude +"   latitude:"+ latitude +"   ss="+ss+" nu="+nu);
        mLocationClient.stop();
    }

    /** 设置定位图层的配置 */
    private void setMyLocationConfigeration(
            MyLocationConfiguration.LocationMode mode) {
        boolean enableDirection = false;	//
        BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.mipmap.location);	// 自定义定位的图标
        MyLocationConfiguration config = new MyLocationConfiguration(mode, enableDirection, customMarker,R.color.transparent,R.color.transparent);
        baiduMap.setMyLocationConfigeration(config);
    }

}
