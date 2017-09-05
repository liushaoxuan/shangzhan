package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.ScheduleMoudle;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.dialog.LoadingDialog;
import com.wyu.iwork.wheeldate.ChangeDateDialog;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class EditScheduleActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_officeWork,tv_personWork,tv_Start_Date,tv_Stop_Date;
    private EditText ed_scheduleContent;
    private String type="2",begin_time,end_time,text,
            user_id= AppManager.getInstance(this).getUserInfo().getUser_id(),schedule_id="0";
    private LoadingDialog loadingDialog=new LoadingDialog();
    private ScheduleMoudle scheduleMoudle;
    private Bundle b;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);
        setBackNaviAction();
        getExter();
        init();
        if(b!=null) {
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("日程详情");
            initEdit();
        }else{
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("添加日程");
            initListener();
            tv_officeWork.setSelected(true);
        }
    }

    private void initEdit() {
        tv_Start_Date.setText(scheduleMoudle.getBegin_time());
        tv_Stop_Date.setText(scheduleMoudle.getEnd_time());
        ed_scheduleContent.setText(scheduleMoudle.getText());
        ed_scheduleContent.setEnabled(false);
        if("2".equals(scheduleMoudle.getType())){
            tv_officeWork.setSelected(false);
            tv_personWork.setSelected(true);
            tv_officeWork.setTextColor(getResources().getColor(R.color.blue));
            tv_personWork.setTextColor(getResources().getColor(R.color.white));
            type="2";
        }else{
            tv_officeWork.setSelected(true);
            tv_personWork.setSelected(false);
            tv_officeWork.setTextColor(getResources().getColor(R.color.white));
            tv_personWork.setTextColor(getResources().getColor(R.color.blue));
            type="1";
        }
    }

    private void getExter() {
        b=getIntent().getBundleExtra("moudle");
        if(b!=null){
            scheduleMoudle= (ScheduleMoudle) b.getSerializable("scheduleMoudle");
            schedule_id = scheduleMoudle.getId();
        }else {
            schedule_id="0";
        }
    }

    private void init() {
        tv_officeWork=(TextView)findViewById(R.id.tv_officeWork);
        tv_personWork=(TextView)findViewById(R.id.tv_personWork);
        tv_Start_Date=(TextView)findViewById(R.id.tv_Start_Date);
        tv_Stop_Date=(TextView)findViewById(R.id.tv_Stop_Date);
        ed_scheduleContent=(EditText)findViewById(R.id.ed_scheduleContent);
    }

    private void initListener() {
        tv_officeWork.setOnClickListener(this);
        tv_personWork.setOnClickListener(this);
        tv_Start_Date.setOnClickListener(this);
        tv_Stop_Date.setOnClickListener(this);
    }

    private Menu mMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(b!=null){
            getMenuInflater().inflate(R.menu.edit, menu);
            mMenu=menu;
        }else {
            getMenuInflater().inflate(R.menu.complete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                item.setVisible(false);
                ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("修改日程");
                getMenuInflater().inflate(R.menu.complete, mMenu);
                ed_scheduleContent.setEnabled(true);
                initListener();
                break;
            case R.id.complete:
                begin_time=tv_Start_Date.getText().toString();
                end_time=tv_Stop_Date.getText().toString();
                Log.i("begin_time",begin_time+":"+end_time);
                text=ed_scheduleContent.getText().toString();
                if(begin_time.isEmpty()){
                    MsgUtil.shortToast(this,"请选择开始时间");
                }else if(end_time.isEmpty()){
                    MsgUtil.shortToast(this,"请选择结束时间");
                }else if(text.isEmpty()){
                    MsgUtil.shortToast(this,"请填写日程内容");
                }else {
                    editScheduleRequest();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_officeWork:
                tv_officeWork.setSelected(true);
                tv_personWork.setSelected(false);
                tv_officeWork.setTextColor(getResources().getColor(R.color.white));
                tv_personWork.setTextColor(getResources().getColor(R.color.blue));
                type="1";
                break;
            case R.id.tv_personWork:
                tv_officeWork.setSelected(false);
                tv_personWork.setSelected(true);
                tv_officeWork.setTextColor(getResources().getColor(R.color.blue));
                tv_personWork.setTextColor(getResources().getColor(R.color.white));
                type="2";
                break;
            case R.id.tv_Start_Date:
                openDateDialog("选择开始时间",1);
                break;
            case R.id.tv_Stop_Date:
                openDateDialog("选择结束时间",2);
                break;
        }

    }

    private void openDateDialog(String str, final int FLAG){
        ChangeDateDialog mChangeDateDialog = new ChangeDateDialog(this);
        SimpleDateFormat Times= new SimpleDateFormat("yyyy/MM/dd/HH/mm");
        String []date=Times.format(new Date()).split("/");
        mChangeDateDialog.setDate(Integer.valueOf(date[0]), Integer.valueOf(date[1]),Integer.valueOf(date[2]),Integer.valueOf(date[3]),Integer.valueOf(date[4]));
        mChangeDateDialog.show();
        mChangeDateDialog.setTitle(str);
        mChangeDateDialog.setBirthdayListener(new ChangeDateDialog.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day, String hour, String Mmin) {
                if(month.length()==1){
                    month="0"+month;
                }
                if(day.length()==1){
                    day="0"+day;
                }
                if(hour.length()==1){

                    hour="0"+hour;
                }
                if(Mmin.length()==1){
                    Mmin="0"+Mmin;
                }
                if(FLAG==1){
                    tv_Start_Date.setText(year + "-" + month + "-" + day+" "+hour+":"+Mmin);
                }else{
                    tv_Stop_Date.setText(year + "-" + month + "-" + day+" "+hour+":"+Mmin);
                }
            }
        });
    }

    private void editScheduleRequest(){
        Log.i("schedule_id",schedule_id);
        loadingDialog.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_UPDATESCHEDULE;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+user_id+schedule_id+type);
        OkGo.get(url)
                .params("user_id", user_id)
                .params("schedule_id", schedule_id)
                .params("type", type)
                .params("begin_time", begin_time)
                .params("end_time", end_time)
                .params("text", text)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            Logger.e("object",object.optString("code"));
                            if("0".equals(object.optString("code"))){
                                setResult(201,getIntent());
                                finish();
                            }
                            MsgUtil.shortToast(EditScheduleActivity.this,object.optString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }
}
