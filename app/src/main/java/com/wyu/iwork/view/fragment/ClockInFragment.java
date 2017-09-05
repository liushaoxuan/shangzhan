package com.wyu.iwork.view.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CheckInSignModule;
import com.wyu.iwork.model.SignResult;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.CheckWorkAttendanceActivity;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.SignDialog;
import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.id.textview_op;

/**
 * Created by lx on 2017/3/11.
 * 考勤打卡
 */

public class ClockInFragment extends Fragment {

    private static final String TAG = ClockInFragment.class.getSimpleName();

    //头像
    @BindView(R.id.checking_in_avatar)
    CircleImageView checking_in_avatar;

    //写名字的头像
    @BindView(R.id.checking_in_text)
    TextView checking_in_text;

    //名字
    @BindView(R.id.tv_name)
    TextView tv_name;

    //职位
    @BindView(R.id.tv_position)
    TextView tv_position;

    //日期
    @BindView(R.id.check_in_header_time)
    TextView check_in_header_time;

    //上班打卡时间
    @BindView(R.id.checking_time)
    TextView checking_time;

    //未上班打卡前显示状态
    @BindView(R.id.by_bicycle)
    AutoRelativeLayout by_bicycle;

    //打卡显示
    @BindView(R.id.checking)
    AutoRelativeLayout checking;

    //下班打卡不显示
    @BindView(R.id.rl_class_over)
    AutoRelativeLayout rl_class_over;

    //上班打卡地址
    @BindView(R.id.checking_address)
    TextView checking_address;

    //上班打卡状态
    @BindView(R.id.checking_state)
    TextView checking_state;

    //下班打卡时间
    @BindView(R.id.checking_out_time)
    TextView checking_out_time;

    //下班打卡地址
    @BindView(R.id.checking_out_address)
    TextView checking_out_address;

    //下班打卡状态
    @BindView(R.id.checking_out_state)
    TextView checking_out_state;

    //打卡操作界面
    @BindView(R.id.clock_operation)
    AutoRelativeLayout clock_operation;

    //当前打卡时间
    @BindView(R.id.clock_in_time)
    TextView clock_in_time;

    //当前打卡地点
    @BindView(R.id.clock_in_address)
    TextView clock_in_address;

    //重新定位按钮
    @BindView(R.id.clock_in_location)
    TextView clock_in_location;

    //立即打卡按钮
    @BindView(R.id.clock_in_now)
    TextView clock_in_now;

    @BindView(R.id.tv_rest)
    TextView tv_rest;

    @BindView(R.id.fl_bottom_layout)
    AutoFrameLayout fl_bottom_layout;

    //无数据
    @BindView(R.id.clock_nodata)
    AutoLinearLayout clock_nodata;

    @BindView(R.id.clock_content_show)
    AutoRelativeLayout clock_content_show;

    @BindView(R.id.checking_out_time_1)
    TextView checking_out_time_1;

    @BindView(R.id.checking_time_1)
    TextView checking_time_1;

    //省略号
    @BindView(textview_op)
    TextView textview;


    private String times;

    private String address = "定位失败...";
    private String longitude = "361";
    private String latitude = "361";
    private Gson gson;
    private static final int TYPE_SIGNIN = 1;//打卡状态 ： 上班打卡
    private static final int TYPE_SIGNOUT = 2;//打卡状态:下班打卡
    private static final int TYPE_SIGNFINISH = 3;//已经打卡完毕
    private static final int TYPE_SIGNERROR = 4;//错误的签到状态 如上班未签到  但下班签到了
    private int SIGN_TYPE = TYPE_SIGNIN;//当前状态 默认为上班打卡
    private CheckInSignModule mCheckInSignModule;
    private String dateStr;//查询的日期
    private long unixTime;//保存当前Unix时间戳
    private String dateWeek;
    private int CODE_LOCATION = 200;
    private boolean isFirst = false;
    private String face_img ;

    //private UserInfo user;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    clock_in_address.setText(address);
                    if(!TextUtils.isEmpty(address) && address.length()>11){
                        textview.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_clock_in,null);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    private void initView(){
        //setDefaultShowContent();
        //加载后台数据
        getSignDataFromServer();//获取打卡数据

        requestLocationPermission();
        clock_nodata.setVisibility(View.VISIBLE);
        clock_content_show.setVisibility(View.GONE);
        fl_bottom_layout.setVisibility(View.GONE);
    }

    private void requestLocationPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int isPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
            if(isPermission != PackageManager.PERMISSION_GRANTED){
                Logger.i(TAG,"请求权限");
                ClockInFragment.this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_LOCATION);
            }else{
                Logger.i(TAG,"已有权限");
                startLocation();
            }
        }else{
            startLocation();
        }
    }

    private void startLocation(){
        ((CheckWorkAttendanceActivity)getActivity()).getCheckWorkAttendanceFragment()
                .startLocationClient(new CheckWorkAttendanceFragment.Locationservice() {
                    @Override
                    public void getLocation(BDLocation location) {
                        longitude = location.getLongitude()+"";
                        latitude = location.getLatitude()+"";
                        address = location.getAddrStr();

                        mHandler.sendEmptyMessage(1);
                    }

                });
    }

    private void getSignDataFromServer(){
        /**
         * user_id	是	int[180]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        /**
         * user_id	    是   	int[180]        用户ID
         date	          否   	int[180]        查询日期签到详情,不传默认为当天,查询日期应小于当前日期 格式：2017-07-24
         F	                是   	string[18]      请求来源：IOS/ANDROID/WEB
         V	                是   	string[20]      版本号如：1.0.1
         RandStr	          是   	string[50]      请求加密随机数 time().|.rand()
         Sign	          是   	string[400]     请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */

        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        if(!TextUtils.isEmpty(dateStr)){
            data.put("date",dateStr);
        }
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SIGN_DETAIL_V2,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseSignData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        setDefaultData();
                    }
                });
    }

    private void setDefaultData(){
        UserInfo info = AppManager.getInstance(getActivity()).getUserInfo();
        if(checkStr(info.getJob())){
            tv_position.setText(info.getJob());
        }
        if(!TextUtils.isEmpty(info.getUser_name())){
            tv_name.setText(info.getUser_name());
        }
        String month_day = getTime(new Date());
        check_in_header_time.setText(month_day.replaceAll("-",".")+" "+getWeek(month_day));

        fl_bottom_layout.setVisibility(View.GONE);
        clock_content_show.setVisibility(View.GONE);
    }

    private void parseSignData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            mCheckInSignModule = gson.fromJson(data,CheckInSignModule.class);
            if(mCheckInSignModule.getCode() == 0){
                //请求成功
                if(checkStr(mCheckInSignModule.getData().getDepartment())){
                    tv_position.setText(mCheckInSignModule.getData().getDepartment());
                }
                if(!TextUtils.isEmpty(mCheckInSignModule.getData().getUser_name())){
                    tv_name.setText(mCheckInSignModule.getData().getUser_name());
                }else{
                    tv_name.setText(MyApplication.userInfo.getUser_phone());
                }
                if(true){
                    checking_in_avatar.setVisibility(View.VISIBLE);
                    checking_in_text.setVisibility(View.GONE);
                }
                if(!isFirst){
                    unixTime = mCheckInSignModule.getData().getUnix_time()*1000;
                    isFirst = true;
                    face_img = mCheckInSignModule.getData().getFace_img();
                }
                if(!TextUtils.isEmpty(face_img)){
                    Glide.with(getActivity()).load(face_img).dontAnimate().placeholder(R.mipmap.def_img_rect).into(checking_in_avatar);
                }
                if(TextUtils.isEmpty(dateStr)){
                    setWeekDay(mCheckInSignModule);
                }else{
                    check_in_header_time.setText(dateWeek);
                }
                //判断当前是上班打卡还是下班打卡
                setShouldShowContent(mCheckInSignModule);
                //2017-02-22 16:46:46
                //存储当前时间
                SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
                sp.edit().putString("CURRENT_TIME",mCheckInSignModule.getData().getNow_time()).commit();
            }else{
                MsgUtil.shortToastInCenter(getActivity(),mCheckInSignModule.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.clock_in_now,R.id.check_in_header_time,R.id.clock_in_location})
    void Click(View view){
        switch (view.getId()){
            case R.id.clock_in_now:
                //showWarnDialog();
                commitSignData();
                break;
            case R.id.check_in_header_time:
                showTimeDialog();
                break;
            case R.id.clock_in_location:
                requestLocationPermission();
                break;
        }

    }

    //设置页面默认显示状态
    private void setShouldShowContent(CheckInSignModule checkInSignModule){
        /**
         [sign_type] => 签到状态，1：上午打卡 2：下午打卡 3：两次打卡完成 4：非工作日 98:未设置签到时间地点 99：无数据 100：查询
         */
        fl_bottom_layout.setVisibility(View.VISIBLE);
        clock_nodata.setVisibility(View.GONE);
        clock_content_show.setVisibility(View.VISIBLE);
        if(checkInSignModule.getData().getSign_type() == 1){
            //上午打卡
            fl_bottom_layout.setVisibility(View.VISIBLE);
            Logger.i(TAG,"Sign_type="+checkInSignModule.getData().getSign_type());
            checking.setVisibility(View.GONE);
            by_bicycle.setVisibility(View.VISIBLE);
            clock_in_now.setText("上班打卡");
        }else if(checkInSignModule.getData().getSign_type() == 2){
            //下午打卡
            clock_content_show.setVisibility(View.VISIBLE);

            fl_bottom_layout.setVisibility(View.VISIBLE);
            Logger.i(TAG,"Sign_type="+checkInSignModule.getData().getSign_type());
            by_bicycle.setVisibility(View.GONE);
            checking.setVisibility(View.VISIBLE);
            rl_class_over.setVisibility(View.GONE);
            clock_in_now.setText("下班打卡");
            //设置上班打卡信息
            setSignInData(checkInSignModule);
        }else if(checkInSignModule.getData().getSign_type() == 3){
            //完成两次打卡
            fl_bottom_layout.setVisibility(View.VISIBLE);
            Logger.i(TAG,"Sign_type="+checkInSignModule.getData().getSign_type());
            by_bicycle.setVisibility(View.GONE);
            checking.setVisibility(View.VISIBLE);
            rl_class_over.setVisibility(View.VISIBLE);
            tv_rest.setVisibility(View.VISIBLE);
            clock_operation.setVisibility(View.GONE);
            //设置上班打卡和下班打卡信息
            setSignInData(checkInSignModule);
            setSignOutData(checkInSignModule);
        }else if(checkInSignModule.getData().getSign_type() == 98){
            //未设置签到时间地点
            //无数据
            fl_bottom_layout.setVisibility(View.GONE);
            clock_nodata.setVisibility(View.VISIBLE);
            clock_content_show.setVisibility(View.GONE);
            showRedmineDialog("暂未设置签到地点，请前往\n设置签到地点!");
        }else if(checkInSignModule.getData().getSign_type() == 99){
            //无数据
            fl_bottom_layout.setVisibility(View.GONE);
            clock_nodata.setVisibility(View.VISIBLE);
            clock_content_show.setVisibility(View.GONE);
        }else if(checkInSignModule.getData().getSign_type() == 100){
            //查询
            clock_content_show.setVisibility(View.VISIBLE);
            by_bicycle.setVisibility(View.GONE);
            checking.setVisibility(View.VISIBLE);
            rl_class_over.setVisibility(View.VISIBLE);

            fl_bottom_layout.setVisibility(View.GONE);
            if(checkInSignModule.getData().getSign_in_msg() != null){
                setSignInData(checkInSignModule);
            }
            if(checkInSignModule.getData().getSign_out_msg() != null){
                setSignOutData(checkInSignModule);
            }
        }
    }

    /**
     * 0：正常签到  1：迟到  2：早退 3:缺勤 9:未签到 99：非工作日签到
     */
    private void getSignState(String code,int type){
        Logger.i(TAG,"CODE="+code+"type="+type);
        if(code.equals("0")){
            if(type == 1){
                checking_state.setText("正常");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }else if(type == 2){
                checking_out_state.setText("正常");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }
        }else if(code.equals("1")){
            if(type == 1){
                checking_state.setText("迟到");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_32cf74_30px));
            }else if(type == 2){
                checking_out_state.setText("迟到");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_32cf74_30px));
            }
        }else if(code.equals("2")){
            if(type == 1){
                checking_state.setText("早退");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ff704f_30px));
            }else if(type == 2){
                checking_out_state.setText("早退");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ff704f_30px));
            }
        }else if(code.equals("3")){
            if(type == 1){
                checking_state.setText("缺勤");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }else if(type == 2){
                checking_out_state.setText("缺勤");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }
        }else if(code.equals("9")){
            if(type == 1){
                checking_state.setText("未签到");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }else if(type == 2){
                checking_out_state.setText("未签到");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }
        }else if(code.equals("99")){
            if(type == 1){
                checking_state.setText("非工作日签到");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }else if(type == 2){
                checking_out_state.setText("非工作日签到");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }
        }else {
            if(type == 1){
                checking_state.setText("不正常");
                checking_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }else if(type == 2){
                checking_out_state.setText("不正常");
                checking_out_state.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_ffc74f_30px));
            }
        }

    }

    //设置上班打卡信息
    private void setSignInData(CheckInSignModule checkInSignModule){
        /**
         * 0：正常签到  1：迟到  2：早退 3:缺勤 9:未签到 99：非工作日签到
         */
        CheckInSignModule.Data.SignIn signIn = checkInSignModule.getData().getSign_in_msg();
        if("3".equals(signIn.getSign_status())){
            checking_time.setText("上班时间 "+spliteTimeHourMinutes(signIn.getTime()));
            checking_time_1.setVisibility(View.GONE);
        }else{
            checking_time.setText("打卡时间  "+spliteTimeHourMinutes(signIn.getSign_time().split(" ")[1]));
            checking_time_1.setVisibility(View.VISIBLE);
            checking_time_1.setText("  (上班时间 "+spliteTimeHourMinutes(signIn.getTime())+")");
        }
        if(checkStr(signIn.getSign_address())){
            checking_address.setText(signIn.getSign_address());
        }else{
            checking_address.setText("");
            checking_address.setHint("无打卡地点数据");
        }
        getSignState(signIn.getSign_status(),1);
    }

    //设置下班打卡信息
    private void setSignOutData(CheckInSignModule checkInSignModule){
        /**
         * 0：正常签到  1：迟到  2：早退 3:缺勤 9:未签到 99：非工作日签到
         */
        CheckInSignModule.Data.SignIn signOut = checkInSignModule.getData().getSign_out_msg();
        if("3".equals(signOut.getSign_status())){
            checking_out_time.setText("下班时间 "+spliteTimeHourMinutes(signOut.getTime()));
            checking_out_time_1.setVisibility(View.GONE);
        }else{
            checking_out_time.setText("打卡时间  "+spliteTimeHourMinutes(signOut.getSign_time().split(" ")[1]));
            checking_out_time_1.setText("  (下班时间 "+spliteTimeHourMinutes(signOut.getTime())+")");
            checking_out_time_1.setVisibility(View.VISIBLE);
        }
        if(checkStr(signOut.getSign_address())){
            checking_out_address.setText(signOut.getSign_address());
        }else{
            checking_out_address.setText("");
            checking_out_address.setHint("无打卡地点数据");
        }
        getSignState(signOut.getSign_status(),2);
    }

    /**
     * 从 18:50:30 ---> 18:50
     * @param time
     */
    private String spliteTimeHourMinutes(String time){
        try {
            String[] times = time.split(":");
            return times[0]+":"+times[1];
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkStr(String str){
        if(!TextUtils.isEmpty(str)){
            return true;
        }
        return false;
    }

    private void setWeekDay(CheckInSignModule checkInSignModule){

        /**
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        String week = weekDay[weekday-1];
        check_in_week_day.setText(year+"/"+(month<10?"0"+month:month)+"/"+(day<10?"0"+day:day)+" "+week);
*/

        String[] time = checkInSignModule.getData().getNow_time().split(" ");
        check_in_header_time.setText(time[0].replaceAll("-",".")+" "+getWeek(time[0]));
        clock_in_time.setText(time[1]);
    }

    private String getWeek(String str){
        String[] weekDay = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = format.parse(str, pos);
        calendar.setTime(strtodate);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay[weekday-1];
    }

    private void commitSignData(){
        String F = Constant.F;
        String V = Constant.V;
        //String longitude = "121.276935";
        //String latitude = "31.035132";
        //String address = "松江区江田东路185号9号楼209室 ";
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id()+"1"+longitude+latitude);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("status","1");
        data.put("longitude",longitude);
        data.put("latitude",latitude);
        data.put("address",address);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SING,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).execute(new DialogCallback(getActivity()) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                Logger.i(TAG,s);
                parseData(s);
            }
        });
    }

    private void parseData(String data){
        if(gson == null){
            gson = new Gson();
        }
        try {
            SignResult signResult = gson.fromJson(data,SignResult.class);
            if("0".equals(signResult.getCode())){
                getSignDataFromServer();
                showSignResultDialog(1,signResult.getData().getAdd_time());
            }else{
                showSignResultDialog(2,signResult.getData().getAdd_time());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //显示时间弹窗选择日期
    private void showTimeDialog(){
        new PickerViewDialog(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(date.getTime()>unixTime){
                    Logger.i(TAG,"UNIX_TIME="+mCheckInSignModule.getData().getUnix_time()+"\n date="+date.getTime());
                    showRedmineDialog("查询的时间不能大于当前时间\n请重新选择!");
                }else{
                    dateStr = getTime(date);
                    dateWeek = dateStr.replaceAll("-",".")+" "+getWeek(dateStr);
                    check_in_header_time.setText(dateWeek);
                    getSignDataFromServer();
                }
            }
        }).show_timepicker();
    }

    //提醒弹窗
    private void showRedmineDialog(String str){
        new MyCustomDialogDialog(5, getActivity(), R.style.MyDialog, str, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void showSignResultDialog(final int type,String time){
        new SignDialog(getActivity(), type,time, new SignDialog.ClickDialogListener() {
            @Override
            public void onClick(Dialog dialog) {
                if(type == 2){
                    commitSignData();
                }
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_LOCATION){
            if(grantResults != null && grantResults.length>0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocation();
                }else{
                    MsgUtil.shortToastInCenter(getActivity(),"请前往手机设置打开手机定位权限!");
                }
            }
        }
    }
}
