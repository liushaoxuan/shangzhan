package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.RouteItemAdapter;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CRM - 查看路线
 */

public class CrmCheckRouteActivity extends AppCompatActivity implements OnGetRoutePlanResultListener{

    private static final String TAG = CrmCheckRouteActivity.class.getSimpleName();

    //我的位置
    @BindView(R.id.route_mylocation)
    TextView route_mylocation;

    //刷新位置
    @BindView(R.id.route_refresh)
    ImageView route_refresh;

    //下侧刷新位置
    @BindView(R.id.route_refresh_bottom)
    ImageView route_refresh_bottom;

    //目标位置
    @BindView(R.id.route_target_location)
    TextView route_target_location;

    //交换位置
    @BindView(R.id.route_exchange_location)
    ImageView route_exchange_location;

    //开车
    @BindView(R.id.crm_check_route_driving)
    AutoLinearLayout crm_check_route_driving;

    //换乘
    @BindView(R.id.crm_check_route_trans)
    AutoLinearLayout crm_check_route_trans;

    //步行
    @BindView(R.id.crm_check_route_walking)
    AutoLinearLayout crm_check_route_walking;

    //放置路线的容器
    @BindView(R.id.crm_check_route_recycleview)
    RecyclerView crm_check_route_recycleview;

    //暂无
    @BindView(R.id.check_route_zanwu)
    AutoLinearLayout check_route_zanwu;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private ArrayList<AutoLinearLayout> layoutList;
    private ArrayList<Fragment> fragments;
    private double longitude;//目标经度
    private double latitude;//目标纬度
    private double myLongitude;//我的位置的经度
    private double myLatitude;//我的位置的纬度
    private static final int TYPE_DRIVING = 1;//开车路线
    private static final int TYPE_TRANS = 2;//换乘路线
    private static final int TYPE_WALKING = 3;//走路路线
    private ArrayList<DrivingRouteLine> routes;
    private LocationClient mLocationClient;
    private BDLocationListener myListener = new MyLocationListener();
    private RoutePlanSearch routePlanSearch;//路线搜索
    private LatLng targetLatLng;//目标位置
    private List<DrivingRouteLine> drivingRouteLines;//驾车路线
    private List<WalkingRouteLine> walkingRouteLines;
    private List<TransitRouteLine> transRouteLines;
    private RouteItemAdapter adapter;
    private int currentType = 0;
    private LatLng mLatLng;
    private String mCity;
    private static final int MINE_TO_TARGET = 1;//我的位置--->目标地址
    private static final int TARGET_TO_MINE = 2;//目标地址--->我的位置
    private int currentRoute = MINE_TO_TARGET;//当前路线类型
    private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_check_route);
        ButterKnife.bind(this);
        getExtras();
        initLocation();
        fragments = new ArrayList<>();
        layoutList = new ArrayList<>();
        drivingRouteLines = new ArrayList<>();
        walkingRouteLines = new ArrayList<>();
        transRouteLines = new ArrayList<>();
        tv_title.setText("查看路线");
        route_target_location.setText(address);
        //targetLatLng = new LatLng(latitude,longitude);
        targetLatLng = new LatLng(latitude,longitude);
        initLayoutList();
        setLayoutSelector(0);
    }

    //配置并开启定位功能
    private void initLocation(){
        //配置定位
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );
        //注册监听函数
        initLocationconfiguration();

        //配置路线查询的属性
        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(this);

        mLocationClient.start();
    }

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

    //获取经纬度信息
    private void getExtras(){
        try {
            latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
            longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));
            address = getIntent().getStringExtra("address");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initLayoutList(){
        layoutList.add(crm_check_route_driving);//驾车
        layoutList.add(crm_check_route_trans);//换乘
        layoutList.add(crm_check_route_walking);//步行
    }


    private void setLayoutSelector(int position){
        for(int i = 0;i<layoutList.size();i++){
            if(i == position){
                layoutList.get(i).setSelected(true);
            }else{
                layoutList.get(i).setSelected(false);
            }
        }
        showRoutes(position);//根据当前所处的选项显示相应的路线
        currentType = position;
    }

    private void showRoutes(int position){
        if(position == 0){
            //驾车
            if(drivingRouteLines != null && drivingRouteLines.size()>0){
                showContent(true);
                adapter = new RouteItemAdapter(this,drivingRouteLines,1);
                crm_check_route_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                crm_check_route_recycleview.setAdapter(adapter);
            }else{
                if(currentRoute == MINE_TO_TARGET){
                    routePlanSearch.drivingSearch(getDrivingParams(mLatLng,targetLatLng));
                }else{
                    routePlanSearch.drivingSearch(getDrivingParams(targetLatLng,mLatLng));
                }
            }
        }else if(position == 1){
            //换乘

            if(transRouteLines != null && transRouteLines.size()>0){
                showContent(true);
                adapter = new RouteItemAdapter(transRouteLines,2,this);
                crm_check_route_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                crm_check_route_recycleview.setAdapter(adapter);
            }else{
                if(currentRoute == MINE_TO_TARGET){
                    routePlanSearch.transitSearch(getTransParams(mLatLng,targetLatLng));
                }else{
                    routePlanSearch.transitSearch(getTransParams(targetLatLng,mLatLng));
                }
            }
        }else if(position == 2){
            //步行

            if(walkingRouteLines != null && walkingRouteLines.size()>0){
                showContent(true);
                adapter = new RouteItemAdapter(walkingRouteLines,this,3);
                crm_check_route_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
                crm_check_route_recycleview.setAdapter(adapter);
            }else{
                if(currentRoute == MINE_TO_TARGET){
                    routePlanSearch.walkingSearch(getWalkingParams(mLatLng,targetLatLng));
                }else{
                    routePlanSearch.walkingSearch(getWalkingParams(targetLatLng,mLatLng));
                }

            }
        }
    }

    private void showContent(boolean flag){
        check_route_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        crm_check_route_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    @OnClick({R.id.crm_check_route_driving,R.id.crm_check_route_trans,R.id.crm_check_route_walking,R.id.ll_back,
                R.id.route_refresh_bottom,R.id.route_refresh,R.id.route_exchange_location})
    void Click(View v){
        switch (v.getId()){
            case R.id.crm_check_route_driving:
                //开车路线
                setLayoutSelector(0);
                break;
            case R.id.crm_check_route_trans:
                //换乘路线
                setLayoutSelector(1);
                break;
            case R.id.crm_check_route_walking:
                //步行路线
                setLayoutSelector(2);
                break;
            case R.id.route_refresh:
            case R.id.route_refresh_bottom:
                //刷新位置
                if(drivingRouteLines != null && drivingRouteLines.size()>0){
                    drivingRouteLines.clear();
                }
                if(transRouteLines != null && transRouteLines.size()>0){
                    transRouteLines.clear();
                }
                if(walkingRouteLines != null && walkingRouteLines.size()>0){
                    walkingRouteLines.clear();
                }
                if(mLocationClient != null){
                    mLocationClient.start();
                }
                break;
            case R.id.route_exchange_location:
                //调换位置，并发起路线规划请求
                exchangeLocation();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    private void exchangeLocation(){
        if(currentRoute == MINE_TO_TARGET){
            route_mylocation.setText(route_target_location.getText().toString());
            route_mylocation.setTextColor(getResources().getColor(R.color.colorGray999999));
            route_target_location.setText("我的位置");
            route_target_location.setTextColor(getResources().getColor(R.color.colorGray404040));
            route_refresh.setVisibility(View.GONE);
            route_refresh_bottom.setVisibility(View.VISIBLE);
            if(drivingRouteLines != null && drivingRouteLines.size()>0){
                drivingRouteLines.clear();
            }
            if(transRouteLines != null && transRouteLines.size()>0){
                transRouteLines.clear();
            }
            if(walkingRouteLines != null && walkingRouteLines.size()>0){
                walkingRouteLines.clear();
            }
            if(currentType == 0){
                routePlanSearch.drivingSearch(getDrivingParams(targetLatLng,mLatLng));
            }else if(currentType == 1){
                routePlanSearch.transitSearch(getTransParams(targetLatLng,mLatLng));
            }else if(currentType == 2){
                routePlanSearch.walkingSearch(getWalkingParams(targetLatLng,mLatLng));
            }
            currentRoute = TARGET_TO_MINE;
        }else if(currentRoute == TARGET_TO_MINE){
            route_target_location.setText(route_mylocation.getText().toString());
            route_target_location.setTextColor(getResources().getColor(R.color.colorGray999999));
            route_mylocation.setText("我的位置");
            route_mylocation.setTextColor(getResources().getColor(R.color.colorGray404040));
            route_refresh.setVisibility(View.VISIBLE);
            route_refresh_bottom.setVisibility(View.GONE);
            if(drivingRouteLines != null && drivingRouteLines.size()>0){
                drivingRouteLines.clear();
            }
            if(transRouteLines != null && transRouteLines.size()>0){
                transRouteLines.clear();
            }
            if(walkingRouteLines != null && walkingRouteLines.size()>0){
                walkingRouteLines.clear();
            }
            if(currentType == 0){
                routePlanSearch.drivingSearch(getDrivingParams(mLatLng,targetLatLng));
            }else if(currentType == 1){
                routePlanSearch.transitSearch(getTransParams(mLatLng,targetLatLng));
            }else if(currentType == 2){
                routePlanSearch.walkingSearch(getWalkingParams(mLatLng,targetLatLng));
            }
            currentRoute = MINE_TO_TARGET;
        }
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
                    MsgUtil.shortToastInCenter(CrmCheckRouteActivity.this,"请检查手机是否处于飞行模式!");
                    break;
                case BDLocation.TypeCriteriaException:
                    //无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                    Logger.i(TAG,"无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    MsgUtil.shortToastInCenter(CrmCheckRouteActivity.this,"请检查手机是否处于飞行模式!");
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
        myLongitude = location.getLongitude();
        mCity = location.getCity();
        myLatitude = location.getLatitude();
        String ss = location.getAddress().address;
        mLatLng = new LatLng(myLatitude,myLongitude);

        if(currentType == 0){
            if(currentRoute == MINE_TO_TARGET){
                routePlanSearch.drivingSearch(getDrivingParams(mLatLng,targetLatLng));
            }else{
                routePlanSearch.drivingSearch(getDrivingParams(targetLatLng,mLatLng));
            }
        }else if(currentType == 1){
            if(currentRoute == MINE_TO_TARGET){
                routePlanSearch.transitSearch(getTransParams(mLatLng,targetLatLng));
            }else{
                routePlanSearch.transitSearch(getTransParams(targetLatLng,mLatLng));
            }

        }else if(currentType == 2){
            if(currentRoute == MINE_TO_TARGET){
                routePlanSearch.walkingSearch(getWalkingParams(mLatLng,targetLatLng));
            }else{
                routePlanSearch.walkingSearch(getWalkingParams(targetLatLng,mLatLng));
            }

        }
        int nu = location.getSatelliteNumber();
        Logger.i(TAG,"longitude:"+longitude+"   latitude:"+latitude+"   ss="+ss+" nu="+nu);
        mLocationClient.stop();
    }

    private DrivingRoutePlanOption getDrivingParams(LatLng from,LatLng to){
        DrivingRoutePlanOption option = new DrivingRoutePlanOption();
        option.policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_AVOID_JAM);//策略 ： 躲避拥堵
        option.from(PlanNode.withLocation(from));
        option.to(PlanNode.withLocation(to));
        return option;
    }

    private TransitRoutePlanOption getTransParams(LatLng from,LatLng to){
        TransitRoutePlanOption option = new TransitRoutePlanOption();
        option.city(mCity);
        option.policy(TransitRoutePlanOption.TransitPolicy.EBUS_TIME_FIRST);//策略 ： 时间最短优先
        option.from(PlanNode.withLocation(from));
        option.to(PlanNode.withLocation(to));
        return option;
    }

    private WalkingRoutePlanOption getWalkingParams(LatLng from,LatLng to){
        WalkingRoutePlanOption option = new WalkingRoutePlanOption();
        option.from(PlanNode.withLocation(from));
        option.to(PlanNode.withLocation(to));
        return option;
    }

    /**
     * 以下为路线搜索回调
     * @param walkingRouteResult
     */
    //步行路线搜索回调
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        // 获取到所有的搜索路线，最优化的路线会在集合的前面
        walkingRouteLines = walkingRouteResult.getRouteLines();
        if(currentType == 2 && walkingRouteLines != null && walkingRouteLines.size()>0){
            showContent(true);
            adapter = new RouteItemAdapter(walkingRouteLines,this,3);
            crm_check_route_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            crm_check_route_recycleview.setAdapter(adapter);
        }else{
            showContent(false);
        }
    }

    //换乘路线搜索回调
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        // 获取到所有的搜索路线，最优化的路线会在集合的前面
        transRouteLines = transitRouteResult.getRouteLines();
        if(currentType == 1 && transRouteLines != null && transRouteLines.size()>0){
            showContent(true);
            adapter = new RouteItemAdapter(transRouteLines,2,this);
            crm_check_route_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            crm_check_route_recycleview.setAdapter(adapter);
        }else {
            showContent(false);
        }
    }

    //跨城公共交通路线结果回调 * @param result 跨城公共交通路线结果
    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    //驾车路线搜索回调
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        drivingRouteLines = drivingRouteResult.getRouteLines();
        if(currentType == 0 && drivingRouteLines != null && drivingRouteLines.size()>0){
            showContent(true);
            adapter = new RouteItemAdapter(this,drivingRouteLines,1);
            crm_check_route_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            crm_check_route_recycleview.setAdapter(adapter);
        }else{
            showContent(false);
        }
    }

    //室内路线规划回调
    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    //骑行路线结果回调
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
