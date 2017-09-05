package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.wheeldate.SelectMeetingTime;
import com.wyu.iwork.widget.CrmCreateCustomDialog;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class CreateScheduleActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = CreateScheduleActivity.class.getSimpleName();


    @BindView(R.id.rl_choose_begin_time)
    AutoRelativeLayout rl_choose_begin_time;

    @BindView(R.id.rl_choose_end_time)
    AutoRelativeLayout rl_choose_end_time;

    @BindView(R.id.tv_begin_time)
    TextView tv_begin_time;

    @BindView(R.id.tv_end_time)
    TextView tv_end_time;

    @BindView(R.id.schedule_detail_title)
    EditText schedule_detail_title;

    @BindView(R.id.schedule_detail_content)
    EditText schedule_detail_content;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.schedule_type)
    AutoRelativeLayout schedule_type;

    @BindView(R.id.tv_type)
    TextView tv_type;

    @BindView(R.id.activity_create_schedule_scroolview)
    ScrollView activity_create_schedule_scroolview;

    private SelectMeetingTime mSelectMeetingTime;
    private String meetingTime = "";
    private Date startTime;
    private Date endTime;
    private String scheduleType;
    private static final int RESULT_CODE = 104;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        hideToolbar();
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        tv_title.setText("添加日程");
        tv_edit.setText("完成");
        tv_edit.setVisibility(View.VISIBLE);
        activity_create_schedule_scroolview.setOnTouchListener(this);
    }


    @OnClick({R.id.ll_back,R.id.tv_edit,R.id.rl_choose_begin_time,R.id.rl_choose_end_time,R.id.schedule_type})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                finish();
                break;
            case R.id.tv_edit:
                checkPostMessage();
                break;
            case R.id.rl_choose_begin_time:
                showDatePickerDialog(tv_begin_time);
                break;
            case R.id.rl_choose_end_time:
                showDatePickerDialog(tv_end_time);
                break;
            case R.id.schedule_type:
                //选择类型
                selectScheduleType();
                break;
        }
    }

    //显示选择日程类型弹窗
    private void selectScheduleType(){
        new CrmCreateCustomDialog(this, "个人事务", "工作事务", new CrmCreateCustomDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                scheduleType = "2";
                tv_type.setText("个人事务");
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                scheduleType = "1";
                tv_type.setText("工作事务");
                dialog.dismiss();
            }

            @Override
            public void threeClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    private void checkPostMessage(){
        //判断是否有信息没有填写
        //日程类型 开始时间  结束时间 日程标题为必选项
        //if(TextUtils.isEmpty(tv_begin_time.getText().toString()) || TextUtils.isEmpty(tv_end_time.getText().toString()) || TextUtils.isEmpty(schedule_detail_title.getText().toString())
          //      || TextUtils.isEmpty(schedule_detail_content.getText().toString()) || TextUtils.isEmpty(tv_type.getText().toString())){
            //showRedmineDialog();
        if(TextUtils.isEmpty(tv_type.getText().toString())){
            MsgUtil.shortToastInCenter(this,"请选择日程类型!");
        }else if(TextUtils.isEmpty(tv_begin_time.getText().toString())){
            MsgUtil.shortToastInCenter(this,"请选择日程开始时间!");
        }else if(TextUtils.isEmpty(tv_end_time.getText().toString())){
            MsgUtil.shortToastInCenter(this,"请选择日程结束时间!");
        }else if(TextUtils.isEmpty(schedule_detail_title.getText().toString())){
            MsgUtil.shortToastInCenter(this,"请输入日程标题!");
        }else if(startTime.getTime() >= endTime.getTime()){
            MsgUtil.shortToastInCenter(this,"结束时间必须大于开始时间");
        }else{
            postData();
        }

    }

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, "您的日程信息未填写完整\n请完成填写并提交", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    private void postData(){
        /**
         * user_id	是	int[18]用户ID
         schedule_id	是	int[18]日程ID,新增为0,修改填写修改日程ID
         type	是	int[1]日程类型 1：工作事务 2：个人事务
         begin_time	否	date日程开始时间  格式：2017-01-01 12:12
         end_time	否	date日程结束时间  格式：2017-01-01 12:12
         title	否	string日程具体事项
         text	否	string日程具体事项
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.schedule_id.type)
         */
        //loadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("schedule_id","0");//暂时只做新增事务
        String type = scheduleType;
        data.put("type",type);
        data.put("begin_time",formatData(startTime));
        data.put("end_time",formatData(endTime));
        data.put("title",schedule_detail_title.getText().toString());
        if(!TextUtils.isEmpty(schedule_detail_content.getText().toString())){
            data.put("text",schedule_detail_content.getText().toString());
        }
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+"0"+type);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_UPDATESCHEDULE,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //loadingDialog.dismiss();
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                setResult(RESULT_CODE);
                                finish();
                            }
                            MsgUtil.shortToastInCenter(CreateScheduleActivity.this,object.getString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //显示时间弹窗选择日期
    private void showDatePickerDialog(final TextView textView){
        new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(textView == tv_begin_time){
                    startTime = date;
                }else if(textView == tv_end_time){
                    endTime = date;
                }
                textView.setText(getTime(date));
            }
        }," ").show_timepicker_h_m();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        return format.format(date);
    }

    private String formatData(Date date){//2017-01-01 12:12
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_create_schedule_scroolview);
        return false;
    }
}
