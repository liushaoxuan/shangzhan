package com.wyu.iwork.view.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.Poi;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UnixStamp;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.CheckWorkAttendanceActivity;
import com.wyu.iwork.view.activity.GoOutAttendanceActivity;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * Created by lx on 2017/3/11.
 * 考勤 外出
 */

public class ClockGoOutFragment extends Fragment {

    private static final String TAG = ClockGoOutFragment.class.getSimpleName();

    //建筑名称
    @BindView(R.id.location_building)
    TextView location_building;

    //地址
    @BindView(R.id.location)
    TextView curretnLocation;

    //重新定位
    @BindView(R.id.relocation)
    TextView relocation;

    //当前时间
    @BindView(R.id.current_time)
    TextView current_time;

    //星期
    @BindView(R.id.time_weekend)
    TextView time_weekend;

    //外出考勤
    @BindView(R.id.go_out_clock)
    TextView go_out_clock;

    private String longitude;
    private String latitude;
    private String address;
    private static final int CODE_REQUEST = 120;
    private String time;
    private static final int CODE_LOCATION = 200;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_in_go_out, null);
        ButterKnife.bind(this, view);
        //initView();
        requestLocationPermission();
        return view;
    }

    @Override
    public void onResume() {
        go_out_clock.setVisibility(View.VISIBLE);
        getCurrentTime();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int isPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(isPermission != PackageManager.PERMISSION_GRANTED){
                Logger.i(TAG,"请求权限");
                ClockGoOutFragment.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_LOCATION);
            }else{
                Logger.i(TAG,"已有权限");
                startLocationService();
            }
        }else{
            startLocationService();
        }
    }


    private void startLocationService(){
        ((CheckWorkAttendanceActivity)getActivity()).getCheckWorkAttendanceFragment()
                .startLocationClient(new CheckWorkAttendanceFragment.Locationservice() {
                    @Override
                    public void getLocation(BDLocation location) {
                        longitude = location.getLongitude()+"";
                        latitude = location.getLatitude()+"";
                        address = location.getAddrStr();
                        String ss = location.getAddress().address;
                        final List<Poi> list = location.getPoiList();
                        Logger.i(TAG,longitude+" hu "+address+"   mmm");
                        //curretnLocation.setText(address);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData(list);
                            }
                        });

                    }
                });
    }

    private void setData(List<Poi> list){
        curretnLocation.setText(address);
        if(list != null && list.size()>0){
            location_building.setText(list.get(0).getName());
        }
    }

    @OnClick({R.id.relocation,R.id.go_out_clock})
    void Click(View v){
        switch (v.getId()){
            case R.id.relocation:
                //Intent intent1 = new Intent(getActivity(), LocationActivity.class);
                //intent1.putExtra("TYPE","1");
                //startActivityForResult(intent1,CODE_REQUEST);
                requestLocationPermission();
                break;
            case R.id.go_out_clock:
                Intent intent = new Intent(getActivity(),GoOutAttendanceActivity.class);
                intent.putExtra("longitude",longitude);
                intent.putExtra("latitude",latitude);
                intent.putExtra("address",address);
                getActivity().startActivity(intent);
                break;
        }
    }


    private void getCurrentTime(){
        /**
         * URL_NOW_UNIX_TIME
         * user_id	是	int[180]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
          */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_NOW_UNIX_TIME,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Gson gson = null;
                        try {
                            gson = new Gson();
                            UnixStamp stamp = gson.fromJson(s,UnixStamp.class);
                            if("0".equals(stamp.getCode())){
                                time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(stamp.getData().getUnix_time() * 1000));
                                setWeekDay(time);
                            }else {
                                MsgUtil.shortToastInCenter(getActivity(),stamp.getMsg());
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        location_building.setText("定位失败");
                        time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
                        setWeekDay(time);
                    }
                });
    }

    private void setWeekDay(String time){
        String[] weekDay = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        ParsePosition pos = new ParsePosition(0);
        String[] yTime = time.split(" ");
        Date strtodate = format.parse(yTime[0], pos);
        calendar.setTime(strtodate);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        String week = weekDay[weekday-1];
        time_weekend.setText(yTime[0]+" "+week);
        current_time.setText(yTime[1]);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_LOCATION){
            if(grantResults != null && grantResults.length>0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationService();
                }else{
                    MsgUtil.shortToastInCenter(getActivity(),"请前往手机设置打开手机定位权限!");
                }
            }
        }
    }
}
