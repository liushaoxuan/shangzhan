package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.LocationListAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.mipmap.location;
import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

public class LocationActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    private static final String TAG = LocationActivity.class.getSimpleName();
    //百度地图图层
    @BindView(R.id.mapview)
    MapView mapview;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.location_recycleview)
    RecyclerView location_recycleview;

    @BindView(R.id.relocation)
    ImageView relocation;

    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();//定位监听器
    private CircleOptions mOptions;
    private double longitude = 361;//经度
    private double latitude = 361;//纬度
    private String mAddress;
    private List<PoiInfo> mList;
    private GeoCoder mSearch = null;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location((LatLng) msg.obj));
                    break;
            }
            super.handleMessage(msg);
        }

    };
    private LocationListAdapter adapter;
    private String type;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
    }

    private void getExtras(){
        type = getIntent().getStringExtra("TYPE");
    }

    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }

    private void initView(){
        tv_title.setText("位置");
        tv_edit.setText("完成");
        tv_edit.setVisibility(View.VISIBLE);
        //初始化定位数据配置
        //获取地图控制器

        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mBaiduMap = mapview.getMap();
        //声明locationClient类
        mapview.showZoomControls(false);//隐藏缩放按钮
        mapview.showScaleControl(true);//显示比例尺
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mBaiduMap.setMyLocationEnabled(true);//开启定位图层
        setMyLocationConfigeration(MyLocationConfiguration.LocationMode.FOLLOWING);
        //配置覆盖物基本属性
        mOptions =  new CircleOptions();	// 创建一个圆形覆盖物的参数
        mOptions.radius(300)	// 半径
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

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Logger.i(TAG, "抱歉，未能找到结果");
            return;
        }
        Logger.i(TAG, "能找到结果");

        if (reverseGeoCodeResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            mList = reverseGeoCodeResult.getPoiList();
            for(PoiInfo p:mList){
                Logger.i(TAG,"地址："+p.address+"   建筑物："+p.name);
            }
            if(mList != null && mList.size()>0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRecycleView();
                    }
                });
            }

        }

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
                latitude = location.getLatitude();
                longitude = location.getLongitude();//获取经度信息
                //float m = mBaiduMap.getMinZoomLevel();//3.0

                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, f-7);

                mBaiduMap.animateMapStatus(u,600);
                Message message = new Message();
                message.obj = latLng;
                message.what = 1;
                handler.sendMessage(message);
            }

            mAddress = location.getAddrStr();

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
            Logger.i(TAG,"Latitude=="+latitude+"  longitude=="+longitude);
            mLocationClient.stop();
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    private void setRecycleView(){
        location_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new LocationListAdapter(this,mList);
        location_recycleview.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapview.onDestroy();
        mSearch.destroy();
    }

    @OnClick({R.id.tv_edit,R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.tv_edit:
                if("1".equals(type)){
                    //获取到地址
                    if(mList != null && mList.size()>0 && adapter != null){
                        Intent intent = getIntent();
                        PoiInfo poi = mList.get(adapter.getSelectItem());
                        Bundle bundle = new Bundle();
                        Logger.i(TAG,poi.name+ "       " +poi.address);
                        bundle.putParcelable("poi",poi);
                        intent.putExtras(bundle);
                        setResult(120,intent);
                        onBackPressed();
                    }else{
                        onBackPressed();
                    }
                }else{
                    //上传打卡地址
                    uploadSignAddress();
                }
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.relocation:
                if(mLocationClient != null){
                    mLocationClient.start();
                }
        }
    }

    private void uploadSignAddress(){
        /**
         * user_id	是	int[11]用户ID
         longitude	是	decimal(10,6)公司地址经度，格式：22.123456 ，注意必须小于等于6位小数
         latitude	是否	decimal(10,6)公司地址纬度 , 同上
         building	是	string[50]建筑物名称
         address	是	string[50]公司地址
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        PoiInfo poi = mList.get(adapter.getSelectItem());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("longitude",poi.location.longitude+"");
        data.put("latitude",poi.location.latitude+"");
        data.put("building",poi.name);
        data.put("address",poi.address);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_SIGN_CONF_ADDRESS,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                onBackPressed();
                            }
                            MsgUtil.shortToast(LocationActivity.this,object.getString("msg"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }
}
