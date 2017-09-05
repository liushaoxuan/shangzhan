package com.wyu.iwork.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CalendarAdapter;
import com.wyu.iwork.adapter.CheckInLateAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CheckInStatisticsModule;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.AttendanceStatisticsActivity;
import com.wyu.iwork.widget.MyGridView;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 打卡统计
 */
public class ClockStatisticsFragment extends Fragment {

    private static final String TAG = ClockStatisticsFragment.class.getSimpleName();

    @BindView(R.id.check_in_calendar)
    MyGridView check_in_calendar;

    @BindView(R.id.check_in_late_listview)
    ListView ckeck_in_late_listview;

    @BindView(R.id.check_in_early_listview)
    ListView check_in_early_listview;

    @BindView(R.id.tv_late_times)
    TextView tv_late_times;

    @BindView(R.id.tv_out_times)
    TextView tv_out_times;

    @BindView(R.id.tv_leave_times)
    TextView tv_leave_times;

    @BindView(R.id.tv_vacate_times)
    TextView tv_vacate_times;

    @BindView(R.id.tv_over_times)
    TextView tv_over_times;

    @BindView(R.id.check_report)
    TextView check_report;

    //头像
    @BindView(R.id.static_avatar)
    CircleImageView static_avatar;

    //姓名
    @BindView(R.id.static_name)
    TextView static_name;

    //职位
    @BindView(R.id.static_position)
    TextView static_position;

    //统计时间
    @BindView(R.id.static_time)
    TextView static_time;



    //状态码
    //其他(正常)
    private static final int CODE_OTHER = -1;
    //迟到
    private static final int CODE_LATE = 0;
    //出差
    private static final int CODE_BUSINESS = 1;
    //早退
    private static final int CODE_LEAVE_EARLY = 2;
    //请假
    private static final int CODE_VACATE = 3;
    //加班
    private static final int CODE_OVERTIME = 4;

    private ArrayList<Integer> calendarList;
    private HashMap<String,Integer> stateList;
    private UserInfo user;
    private Gson gson;
    private CheckInLateAdapter checkInLateAdapter;
    private CheckInLateAdapter leaveAdapter;
    private static final int TYPE_LATE = 1;
    private static final int TYPE_LEAVE_EARLY = 2;
    private int mWeekday;
    //private LoadingDialog loadingDialog = new LoadingDialog();
    private SharedPreferences sp;
    private String[] time;
    private String year = "";
    private String month = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock_statistics,null);
        ButterKnife.bind(this,view);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        time = sp.getString("CURRENT_TIME",format.format(date)).split("-");
        year = time[0];
        month = time[1];
        if(month.length() == 1){
            month = "0"+month;
        }
        getDataFromServer();
        initData();
        return view;
    }

    private void initData(){

        user = MyApplication.userInfo;
        Glide.with(getActivity()).load(user.getUser_face_img()).placeholder(R.mipmap.def_img_rect).into(static_avatar);

        if("1".equals(MyApplication.userInfo.getIs_admin())){
            check_report.setVisibility(View.VISIBLE);
        }else{
            check_report.setVisibility(View.GONE);
        }

        //设置日历

        setCalendar();

        //设置缺勤信息
    }

    private void setCalendar(){
        /**
         * 1：获取当月第一天是周几
         * 2：获取当月有几天
         *
         */
        Calendar calendar = Calendar.getInstance();
        //int year = calendar.get(Calendar.YEAR);
        //int month = calendar.get(Calendar.MONTH)+1;
        //设置当前的年月
        //check_in_header_time.setText(year+"年"+month+"月");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = format.parse(year+"-"+month+"-01", pos);
        calendar.setTime(strtodate);
        mWeekday = calendar.get(Calendar.DAY_OF_WEEK);
        Logger.i(TAG,"YEAR:"+Integer.parseInt(year)+"MONTH="+Integer.parseInt(month));
        int days = calDays(Integer.parseInt(year),Integer.parseInt(month));
        calendarList = new ArrayList<>();

        //stateList = new ArrayList<>();
        for(int i = 0;i<days+mWeekday-1;i++){
            if(i < mWeekday -1){
                calendarList.add(-1);
            }else{
                calendarList.add(i-mWeekday+2);

            }
        }

    }

    private void getDataFromServer(){
        /**
         * user_id	是	int[11]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        //loadingDialog.show(getFragmentManager(),Constant.DIALOG_TAG_LOADING);
        String times = time[0]+time[1];
        Logger.i(TAG,"TIME="+times);
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id()+times);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("date",time[0]+time[1]);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_STATISTICAL_MY,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseData(s);
                        //loadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        UserInfo info = AppManager.getInstance(getActivity()).getUserInfo();
                        checkStr(info.getUser_name(),static_name);
                        checkStr(info.getJob(),static_position);
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(new Date());
                        checkStr(spliteTimeYearMonth(time),static_time);

                        setCalendarMessage(null);
                    }
                });
    }

    private void parseData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            CheckInStatisticsModule module = gson.fromJson(data,CheckInStatisticsModule.class);
            if(module.getCode() == 0){
                //设置日历信息
                setCalendarMessage(module);
                //设置缺勤信息
                setCheckOnMessage(module);
                //设置迟到信息
                //设置早退信息
                setLateMessage(module);
                checkStr(module.getData().getUser_name(),static_name);
                checkStr(module.getData().getDepartment(),static_position);
                checkStr(spliteTimeYearMonth(module.getData().getNow_time()),static_time);
            }else{
                MsgUtil.shortToastInCenter(getActivity(),module.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    private String spliteTimeYearMonth(String str){
        //2017-01-04 12:05:00
        try {
            String[] times = str.split(" ");
            String[] detailTimes = times[0].split("-");
            return detailTimes[0]+"年"+detailTimes[1]+"月";
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //设置日历信息
    private void setCalendarMessage(CheckInStatisticsModule module){
        //初始化每天的状态信息
        stateList = new HashMap<>();
        if(module != null){
            if(module.getData().getMonth_sign() !=null && module.getData().getMonth_sign().size()>0){
                for(int i = 0;i<module.getData().getMonth_sign().size();i++){
                    stateList.put(module.getData().getMonth_sign().get(i).getDay(),module.getData().getMonth_sign().get(i).getStatus());
                }
            }
        }
        CalendarAdapter adapter = new CalendarAdapter(getActivity(), mWeekday,calendarList,stateList);
        check_in_calendar.setAdapter(adapter);
    }

    //设置缺勤信息
    private void setCheckOnMessage(CheckInStatisticsModule module){
        if(!TextUtils.isEmpty(module.getData().getOver_time())){
            //加班
            tv_over_times.setText(module.getData().getOver_time());
        }else{
            tv_over_times.setText("0小时");
        }
        if(!TextUtils.isEmpty(module.getData().getOut_time())){
            //出差
            tv_out_times.setText(module.getData().getOut_time());
        }else{
            tv_out_times.setText("0小时");
        }
        if(!TextUtils.isEmpty(module.getData().getLeave_time())){
            //请假
            tv_vacate_times.setText(module.getData().getLeave_time());
        }else{
            tv_vacate_times.setText("0小时");
        }
        if(!TextUtils.isEmpty(module.getData().getBe_late_num())){
            //迟到
            tv_late_times.setText(module.getData().getBe_late_num()+"次");
        }else {
            tv_late_times.setText("0次");
        }
        if(!TextUtils.isEmpty(module.getData().getLeave_early_num())){
            //早退
            tv_leave_times.setText(module.getData().getLeave_early_num()+"次");
        }else{
            tv_leave_times.setText("0次");
        }
    }

    //设置迟到信息
    //设置早退信息
    private void setLateMessage(CheckInStatisticsModule module){
        if(module.getData() != null){
            if(module.getData().getLate_msg() != null && module.getData().getLate_msg().size()>0){
                checkInLateAdapter = new CheckInLateAdapter(getActivity(),module.getData().getLate_msg(),TYPE_LATE);
                //设置迟到记录
                ckeck_in_late_listview.setAdapter(checkInLateAdapter);
                setListViewHeightBasedOnChildren(ckeck_in_late_listview);
            }
            if(module.getData().getLeave_early_msg() != null && module.getData().getLeave_early_msg().size()>0){
                leaveAdapter = new CheckInLateAdapter(getActivity(),module.getData().getLeave_early_msg(),TYPE_LEAVE_EARLY);
                check_in_early_listview.setAdapter(leaveAdapter);
                setListViewHeightBasedOnChildren(check_in_early_listview);
            }

        }

        //设置早退记录

    }

    public int calDays(int year, int month) {
        boolean leayyear = false;
        int day = 0;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        }else if(year % 400 == 0){
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    day = 30;
                    break;
            }
        }
        return day;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @OnClick({R.id.check_report})
    void Click(View view){
        switch (view.getId()){
            case R.id.check_report:
                startActivity(new Intent(getActivity(), AttendanceStatisticsActivity.class));
                break;
        }
    }


}
