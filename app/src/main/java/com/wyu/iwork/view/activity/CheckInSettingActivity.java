package com.wyu.iwork.view.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AddressAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.SignConf;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.wheeldate.CustomTimePicker;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.TaskItem;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;
import static com.wyu.iwork.util.Md5Util.getSign;


public class CheckInSettingActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = CheckInSettingActivity.class.getSimpleName();


    private CustomTimePicker customTimePicker;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.task_sing_in_time)
    TaskItem task_sing_in_time;

    @BindView(R.id.task_sing_out_time)
    TaskItem task_sing_out_time;

    @BindView(R.id.task_sing_in_date)
    TaskItem task_sing_in_date;

    @BindView(R.id.sing_setting_recycleview)
    RecyclerView sing_setting_recycleview;

    @BindView(R.id.add_address)
    AutoRelativeLayout add_address;

    @BindView(R.id.activity_check_in_setting_scrollview)
    ScrollView activity_check_in_setting_scrollview;

    private String signInTime = "";
    private String signOutTime = "";
    private static final int REQUEST_CODE = 105;
    private ArrayList<Integer> weekList = new ArrayList<>();
    private Gson gson;
    private AddressAdapter adapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_setting);
        hideToolbar();
        ButterKnife.bind(this);
        initView();

        //initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSignConf();
    }

    private void initData(){
        /**
         * user_id	是	int[11]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SIGN_CONF,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        //parseSignData(s);
                    }
                });

    }
/**
    private void parseSignData(String data){
        Gson gson = new Gson();
        SignInSettingModule signInSettingModule = gson.fromJson(data,SignInSettingModule.class);
        if(signInSettingModule.getCode() == 0){
            if(signInSettingModule.getData() != null && !TextUtils.isEmpty(signInSettingModule.getData().getSign_time())){
                activity_check_in_setting_sign_time.setText(signInSettingModule.getData().getSign_time());
                signInTime = signInSettingModule.getData().getSign_time();
            }
            if(signInSettingModule.getData() != null && !TextUtils.isEmpty(signInSettingModule.getData().getSignout_time())){
                activity_check_in_setting_sign_out_time.setText(signInSettingModule.getData().getSignout_time());
                signOutTime = signInSettingModule.getData().getSignout_time();
            }
            if(signInSettingModule.getData() != null && !TextUtils.isEmpty(signInSettingModule.getData().getAddress())){
                schedule_detail_title.setText(signInSettingModule.getData().getAddress());
            }



        }else{
            MsgUtil.shortToastInCenter(this,signInSettingModule.getMsg());
        }
    }
*/
    private void initView(){
        tv_title.setText("设置");
        tv_edit.setText("完成");
        tv_edit.setVisibility(View.VISIBLE);
        initItem();
        activity_check_in_setting_scrollview.setOnTouchListener(this);
    }

    private void initItem(){
        task_sing_in_time.setTitle("上班时间");
        task_sing_out_time.setTitle("下班时间");
        task_sing_in_date.setTitle("考勤日期");

        task_sing_in_time.setBottomLineVisible(false);
        task_sing_out_time.setBottomLineVisible(false);

        task_sing_in_time.setHintValue("请选择");
        task_sing_out_time.setHintValue("请选择");
        task_sing_in_date.setHintValue("请选择");
    }

    @OnClick({R.id.task_sing_in_time,R.id.task_sing_out_time,R.id.ll_back,R.id.tv_edit,R.id.task_sing_in_date,R.id.add_address})
    void Click(View view){
        switch (view.getId()){
            case R.id.task_sing_in_time:
                showDatePickerDialog(true);
                break;
            case R.id.task_sing_out_time:
                showDatePickerDialog(false);
                break;
            case R.id.task_sing_in_date:
                Intent intent = new Intent(this,SingSettingDateActivity.class);
                Bundle bundle = new Bundle();
                for(int i = 0;i<weekList.size();i++){
                    Logger.i(TAG,"week transfer ； "+weekList.get(i));
                }
                Logger.i(TAG,"task_sing_in_date="+weekList.size());
                bundle.putSerializable("week",weekList);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.tv_edit:
                updateSignTime();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.add_address:
                startActivity(new Intent(this,LocationActivity.class));
                break;
        }
    }
/**
    private void postSettingData(){
        /**
         * user_id	是	int[11]用户ID
         sign_time	是	time上班签到时间,格式：09:00:00
         signout_time	是	time下班签到时间,格式：18:00:00
         address	是	string[50]公司地址
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.sign_time.signout_time)
         *//**
        String address = schedule_detail_title.getText().toString();
        if(TextUtils.isEmpty(signInTime)){
            MsgUtil.shortToastInCenter(this,"请选择上班签到时间!");
        }else if(TextUtils.isEmpty(signOutTime)){
            MsgUtil.shortToastInCenter(this,"请选择下班签到退时间!");
        }else if(TextUtils.isEmpty(address)){
            MsgUtil.shortToastInCenter(this,"请填写签到地址!");
        }else{
            String RandStr = CustomUtils.getRandStr();
            String Sign = Md5Util.getSign(F+V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+signInTime+signOutTime);
            HashMap<String,String> data = new HashMap<>();
            data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
            data.put("sign_time",signInTime);
            data.put("signout_time",signOutTime);
            data.put("address",address);
            data.put("F",F);
            data.put("V",V);
            data.put("RandStr",RandStr);
            data.put("Sign",Sign);
            String murl = RequestUtils.getRequestUrl(Constant.URL_EDIT_SING_CONF,data);
            OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                    .execute(new DialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Logger.i(TAG,s);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(s);
                                if(object.getString("code").equals("0")){
                                    finish();
                                }
                                MsgUtil.shortToastInCenter(CheckInSettingActivity.this,object.getString("msg"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
*/

    //修改签到时间 星期
    private void updateSignTime(){
        /**
         * user_id	    是   	int[11]         用户ID
         sign_time	    是   	time            上班签到时间,格式：09:00:00
         signout_time	    是   	time            下班签到时间,格式：18:00:00
         work_day	          是   	string[50]      工作日期,格式：[0,1,2,3,4,5,6],0 代表周日
         F	                是   	string[18]      请求来源：IOS/ANDROID/WEB
         V	                是   	string[20]      版本号如：1.0.1
         RandStr	          是   	string[50]      请求加密随机数 time().|.rand()
         Sign	          是   	string[400]     请求加密值 F_moffice_encode(F.V.RandStr.user_id.sign_time.signout_time)
         */
        String sign_time = task_sing_in_time.getValue();
        String signout_time = task_sing_out_time.getValue();
        String work_day = getWeekValue();
        Logger.i(TAG,"work_day="+work_day+"   sign_time="+sign_time+"   signout_time="+signout_time);
        if(TextUtils.isEmpty(sign_time) || TextUtils.isEmpty(signout_time) || TextUtils.isEmpty(work_day)){
            showRedmineDialog();
            return;
        }
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("sign_time",sign_time);
        data.put("signout_time",signout_time);
        data.put("work_day",work_day);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_SIGN_CONF_TIME,data);
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
                            MsgUtil.shortToast(CheckInSettingActivity.this,object.getString("msg"));
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

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, "您的签到信息未填写完整\n请完成填写并提交", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    //获取签到设置信息
    private void getSignConf(){
        /**
         * user_id	是	int[11]           用户ID
         F	            是	string[18]        请求来源：IOS/ANDROID/WEB
         V	            是	string[20]        版本号如：1.0.1
         RandStr	      是	string[50]        请求加密随机数 time().|.rand()
         Sign	      是	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SIGN_CONF_V2, data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        parseSignConf(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    //解析签到设置信息
    private void parseSignConf(String data){
        if (gson == null){
            gson = new Gson();
        }
        try {
            SignConf signConf = gson.fromJson(data,SignConf.class);
            Logger.i(TAG,signConf.toString());
            //设置签到数据
            if("0".equals(signConf.getCode())){
                if(weekList.size()==0){
                    setSignData(signConf);
                }

                //设置签到地点
                if(signConf.getData().getAddress_list() != null && signConf.getData().getAddress_list().size()>0){
                    setSignAddress(signConf.getData().getAddress_list());
                }
            }else{
                MsgUtil.shortToast(this,signConf.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //设置签到数据
    private void setSignData(SignConf signConf){
        checkStr(signConf.getData().getSign_time(),task_sing_in_time);
        checkStr(signConf.getData().getSignout_time(),task_sing_out_time);
        setWeek(signConf.getData().getWork_day());
    }

    private void checkStr(String str,TaskItem item){
        if(!TextUtils.isEmpty(str)){
            item.setValue(str);
        }else{
            item.setHintValue("请选择");
        }
    }

    private void setWeek(String week){
        for(int i = 1;i<7;i++){
            if(week.contains(i+"")){
                weekList.add(1);
            }else{
                weekList.add(0);
            }
        }
        if(week.contains("0")){
            weekList.add(1);
        }else{
            weekList.add(0);
        }
        setDate();
    }

    private String getWeekValue(){
        if(TextUtils.isEmpty(task_sing_in_date.getValue())){
            return null;
        }else{
            if(weekList.size() == 7){
                String week = "[";
                boolean isFirst = true;
                for(int i = 0;i<weekList.size();i++){
                    if(weekList.get(i) == 1){
                        int t = i+1;
                        if(isFirst){

                            if(t<7){
                                week += t;
                            }else{
                                week += "0";
                            }
                            isFirst = false;
                        }else{
                            if(t<7){
                                week += ","+t;
                            }else{
                                week += ","+0;
                            }
                        }
                    }
                }
                week += "]";
                if(isFirst == false)
                return week;
            }
            return null;
        }
    }

    private void setSignAddress(ArrayList<SignConf.AddressConf> list){
        adapter = new AddressAdapter(this,list);
        sing_setting_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        sing_setting_recycleview.setAdapter(adapter);
    }

    //时间选择对话框
    private void showDatePickerDialog(final boolean flag){
        customTimePicker = new CustomTimePicker(this);
        SimpleDateFormat Times= new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        String []date=Times.format(new Date()).split("/");
        customTimePicker.setDate(Integer.valueOf(date[0]), Integer.valueOf(date[1]),Integer.valueOf(date[2]),Integer.valueOf(date[3]),Integer.valueOf(date[4]));
        customTimePicker.show();
        customTimePicker.setBirthdayListener(new CustomTimePicker.OnBirthListener() {
            @Override
            public void onClick(String year, String day, String hour, String Mmin) {
                if(hour.length()==1){
                    hour="0"+hour;
                }
                if(Mmin.length()==1){
                    Mmin="0"+Mmin;
                }
                if(flag){
                    signInTime = hour+":"+Mmin+":00";
                    task_sing_in_time.setValue(signInTime);
                }else{
                    signOutTime = hour+":"+Mmin+":00";
                    task_sing_out_time.setValue(signOutTime);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == 101){
            weekList.clear();
            for(int i = 0;i<weekList.size();i++){
                Logger.i(TAG,"week before ； "+weekList.get(i));
            }
            weekList = data.getIntegerArrayListExtra("week");
            for(int i = 0;i<weekList.size();i++){
                Logger.i(TAG,"week after : "+weekList.get(i));
            }
            setDate();
        }

    }

    private void setDate(){
        String week = "";
        boolean first = true;
        if(weekList.size() == 7){
            if(weekList.get(0) == 1 && weekList.get(6) == 1
                    && weekList.get(1) == 1 && weekList.get(2) == 1
                    && weekList.get(3) == 1 && weekList.get(4) == 1
                    && weekList.get(5) == 1){
                week = "每天";
            }else if(weekList.get(0) == 1 && weekList.get(6) == 0
                    && weekList.get(1) == 1 && weekList.get(2) == 1
                    && weekList.get(3) == 1 && weekList.get(4) == 1
                    && weekList.get(5) == 0 ){
                week = "工作日";
            }else if(weekList.get(0) == 0 && weekList.get(6) == 1
                    && weekList.get(1) == 0 && weekList.get(2) == 0
                    && weekList.get(3) == 0 && weekList.get(4) == 0
                    && weekList.get(5) == 1){
                week = "周末";
            }else{
                for(int i = 0;i < weekList.size();i++){
                    if(weekList.get(i) == 1){
                        if(first){
                            week = "星期"+getWeek(i);
                            first = false;
                        }else{
                            week += "、" + getWeek(i);
                        }
                    }
                }
            }
        }
        if(!TextUtils.isEmpty(week)){
            task_sing_in_date.setValue(week);
        }else{
            task_sing_in_date.setHintValue("请选择");
        }
        Logger.i(TAG,"setDate="+weekList.size());
    }

    private String getWeek(int day){
        if(day == 0){
            return "一";
        }else if(day == 1){
            return "二";
        }else if(day == 2){
            return "三";
        }else if(day == 3){
            return "四";
        }else if(day == 4){
            return "五";
        }else if(day == 5){
            return "六";
        }else if(day == 6){
            return "日";
        }
        return null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_check_in_setting_scrollview);
        return false;
    }
}
