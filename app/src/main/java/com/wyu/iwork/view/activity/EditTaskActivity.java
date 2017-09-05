package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ChargeAdapter;
import com.wyu.iwork.adapter.TaskTagAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.PersonListModel;
import com.wyu.iwork.model.PersonModel;
import com.wyu.iwork.model.TaskInfoModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.wheeldate.ChangeDateDialog;
import com.wyu.iwork.widget.HorizontialListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class EditTaskActivity extends BaseNetActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, View.OnKeyListener {
    private static final String TAG="EditTaskActivity";
    private TextView tv_Create_name,tv_Start_Date,tv_Stop_Date,tv_progress,tv_taskContext;
    private CircleImageView img_Create_head;
    private ImageView img_addCharge,img_addJoin,tv_LabelGeneral,tv_LabelUrgent,tv_LabelVeryUrgent;
    private LinearLayout ll_LabelGeneral,ll_LabelUrgent,ll_LabelVeryUrgent;
    private HorizontialListView hlv_Charge,hlv_Join,hlv_taskTag;
    private EditText ed_TaskTheme,ed_inputTag;
    private SeekBar sb_task;

    private ArrayList tagList=new ArrayList();
    private ArrayList<PersonModel> ChargeList=new ArrayList<PersonModel>();
    private ArrayList<PersonModel> JoinList=new ArrayList<PersonModel>();
    private List<PersonListModel> headerList=new ArrayList<>();
    private List<PersonListModel> joinerList=new ArrayList<>();
    private TaskInfoModel taskInfoModel;
    private ArrayList ChargeIDList=new ArrayList();
    private ArrayList JoinIDList=new ArrayList();
    private ChargeAdapter chargeAdapter;
    private ChargeAdapter joinAdapter;
    private TaskTagAdapter taskTagAdapter=null;
    private Intent it;
    //private LoadingDialog loadingDialog=new LoadingDialog();
    private String user_id,task_superior="0",task_id="0",title,header="|",joiner="|",begin_time,end_time,level="1",intro="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        setBackNaviAction();
        getExter();
        init();
        if(!"0".equals(task_id)&&task_id!=null) {
            getTaskDetails();
        }
        initData();
        initListener();
    }

    private void getExter() {
        task_superior=getIntent().getStringExtra("task_superior");
        task_id=getIntent().getStringExtra("task_id");
        if(task_id.equals(task_superior)){
            task_superior="0";
        }
        if("0".equals(task_superior)&&"0".equals(task_id)){
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("创建新任务");
        }else if("0".equals(task_superior)&&!"0".equals(task_id)){
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("修改任务");
        }else if(!"0".equals(task_superior)&&"0".equals(task_id)){
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("创建子任务");
        }else{
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("修改子任务");
        }
    }

    private void init() {
        tv_Create_name=(TextView)findViewById(R.id.tv_Create_name);
        tv_Start_Date=(TextView)findViewById(R.id.tv_Start_Date);
        tv_Stop_Date=(TextView)findViewById(R.id.tv_Stop_Date);
        tv_LabelGeneral=(ImageView)findViewById(R.id.tv_LabelGeneral);
        tv_LabelUrgent=(ImageView)findViewById(R.id.tv_LabelUrgent);
        tv_LabelVeryUrgent=(ImageView)findViewById(R.id.tv_LabelVeryUrgent);
        tv_progress=(TextView)findViewById(R.id.tv_progress);
        tv_taskContext=(TextView) findViewById(R.id.tv_taskContext);

        img_Create_head=(CircleImageView)findViewById(R.id.img_Create_head);
        img_addCharge=(ImageView)findViewById(R.id.img_addCharge);
        img_addJoin=(ImageView)findViewById(R.id.img_addJoin);

        ll_LabelGeneral=(LinearLayout) findViewById(R.id.ll_LabelGeneral);
        ll_LabelUrgent=(LinearLayout) findViewById(R.id.ll_LabelUrgent);
        ll_LabelVeryUrgent=(LinearLayout) findViewById(R.id.ll_LabelVeryUrgent);

        ed_TaskTheme=(EditText) findViewById(R.id.ed_TaskTheme);
        tv_taskContext=(TextView) findViewById(R.id.tv_taskContext);
        ed_inputTag=(EditText) findViewById(R.id.ed_inputTag);

        hlv_Charge=(HorizontialListView) findViewById(R.id.hlv_Charge);
        hlv_Join=(HorizontialListView) findViewById(R.id.hlv_Join);
        hlv_taskTag=(HorizontialListView) findViewById(R.id.hlv_taskTag);

        sb_task=(SeekBar)findViewById(R.id.sb_task);
        Glide.with(this).load(AppManager.getInstance(this).getUserInfo().getUser_face_img()).into(img_Create_head);

    }

    private void initData() {
        taskTagAdapter = new TaskTagAdapter(tagList,this);
        hlv_taskTag.setAdapter(taskTagAdapter);
        String dates=currentTime();
        tv_Start_Date.setText(dates);
    }

    private String currentTime() {
        SimpleDateFormat Times= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return Times.format(new Date());
    }

    private void initListener() {
        sb_task.setOnSeekBarChangeListener(this);
        tv_Start_Date.setOnClickListener(this);
        tv_Stop_Date.setOnClickListener(this);
        img_Create_head.setOnClickListener(this);
        img_addCharge.setOnClickListener(this);
        img_addJoin.setOnClickListener(this);
        ll_LabelGeneral.setOnClickListener(this);
        ll_LabelUrgent.setOnClickListener(this);
        ll_LabelVeryUrgent.setOnClickListener(this);
        tv_taskContext.setOnClickListener(this);
        ed_inputTag.setOnKeyListener(this);
        hlv_taskTag.setOnItemClickListener(this);
        hlv_Charge.setOnItemClickListener(this);
        hlv_Join.setOnItemClickListener(this);
        tv_LabelGeneral.setSelected(true);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        tv_progress.setText(i+"");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onClick(View view) {
        Bundle bundle=new Bundle();
        switch (view.getId()){
            case R.id.tv_Start_Date:
                openDateDialog("选择开始时间",1);
                break;
            case R.id.tv_Stop_Date:
                openDateDialog("选择结束时间",2);
                break;
            case R.id.img_Create_head:
                break;
            case R.id.img_addCharge:
                it=new Intent(this,CompletedTaskActivity.class);
                bundle.putStringArrayList("idList", ChargeIDList);
                it.putExtra("TAG",TAG);
                it.putExtra("tag","1");
                it.putExtras(bundle);
                startActivityForResult(it,0);
                break;
            case R.id.img_addJoin:
                it=new Intent(this,CompletedTaskActivity.class);
                bundle.putStringArrayList("idList", JoinIDList);
                it.putExtra("TAG",TAG);
                it.putExtra("tag","2");
                it.putExtras(bundle);
                startActivityForResult(it,1);
                break;
            case R.id.ll_LabelGeneral:
                level="1";
                tv_LabelGeneral.setSelected(true);
                tv_LabelUrgent.setSelected(false);
                tv_LabelVeryUrgent.setSelected(false);
                break;
            case R.id.ll_LabelUrgent:
                level="2";
                tv_LabelGeneral.setSelected(false);
                tv_LabelUrgent.setSelected(true);
                tv_LabelVeryUrgent.setSelected(false);
                break;
            case R.id.ll_LabelVeryUrgent:
                level="3";
                tv_LabelGeneral.setSelected(false);
                tv_LabelUrgent.setSelected(false);
                tv_LabelVeryUrgent.setSelected(true);
                break;
            case R.id.tv_taskContext:
                Intent it=new Intent(this,FeedBackActivity.class);
                it.putExtra("intro",intro);
                startActivityForResult(it,2);
                break;
        }

    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER&&keyEvent.getAction()==KeyEvent.ACTION_UP) {
            if(!ed_inputTag.getText().toString().isEmpty()){
                tagList.add(ed_inputTag.getText().toString());
                taskTagAdapter.notifyDataSetChanged();
                ed_inputTag.setText("");
            }
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.hlv_taskTag:
                tagList.remove(i);
                taskTagAdapter.notifyDataSetChanged();
                break;
            case R.id.hlv_Charge:
                ChargeList.remove(i);
                ChargeIDList.remove(i);
                header= CustomUtils.changList(ChargeIDList);
                chargeAdapter.notifyDataSetChanged();
                break;
            case R.id.hlv_Join:
                JoinList.remove(i);
                JoinIDList.remove(i);
                joiner=CustomUtils.changList(JoinIDList);
                joinAdapter.notifyDataSetChanged();
                break;
        }
        Log.i(header+"",joiner+"");
    }

    private void openDateDialog(String str, final int FLAG){
        ChangeDateDialog mChangeDateDialog = new ChangeDateDialog(EditTaskActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editTask();
        return super.onOptionsItemSelected(item);
    }

    private void editTask() {
        user_id= AppManager.getInstance(this).getUserInfo().getUser_id();
        title=ed_TaskTheme.getText().toString();
        begin_time=tv_Start_Date.getText().toString();
        end_time=tv_Stop_Date.getText().toString();
        if("|".equals(header)){
            MsgUtil.shortToast(EditTaskActivity.this,"请添加负责人");
        }else if("|".equals(joiner)){
            MsgUtil.shortToast(EditTaskActivity.this,"请添加参与人");
        }else if(title.isEmpty()){
            MsgUtil.shortToast(EditTaskActivity.this,"请添加任务主题");
        }else if(end_time.isEmpty()){
            MsgUtil.shortToast(EditTaskActivity.this,"请选择结束时间");
        }else if(begin_time.isEmpty()){
            MsgUtil.shortToast(EditTaskActivity.this,"请选择开始时间");
        }else if(!CustomUtils.matchTime(begin_time,end_time)){
            MsgUtil.shortToastInCenter(EditTaskActivity.this,"结束时间必须大于开始时间");
        }else {
            editTaskRequest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 101:
                Bundle bundle=data.getExtras();
                if(requestCode==0){
                    ChargeList= (ArrayList<PersonModel>) bundle.getSerializable("personList");
                    ChargeIDList=bundle.getStringArrayList("idList");
                    chargeAdapter=new ChargeAdapter(this,ChargeList,1);
                    hlv_Charge.setAdapter(chargeAdapter);
                    header= CustomUtils.changList(ChargeIDList);
                    Log.i("====header",header);
                }else if((requestCode==1)){
                    JoinList=(ArrayList<PersonModel>) bundle.getSerializable("personList");
                    JoinIDList=bundle.getStringArrayList("idList");
                    joinAdapter=new ChargeAdapter(this,JoinList,2);
                    hlv_Join.setAdapter(joinAdapter);
                    joiner=CustomUtils.changList(JoinIDList);
                    Log.i("====joiner",joiner);
                }
                break;
            case 102:
                Bundle b=data.getExtras();
                intro=b.getString("Context");
                Log.i(TAG,intro+"");
                break;
        }
    }
    private void editTaskRequest(){
        Log.i("=====task_id",task_id+":"+task_superior);
        //loadingDialog.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_UPDATETASK;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+user_id+task_superior+task_id);
        OkGo.get(url)
                .params("user_id", user_id)
                .params("task_superior", task_superior)
                .params("task_id", task_id)
                .params("title", title)
                .params("header", header)
                .params("joiner", joiner)
                .params("begin_time", begin_time)
                .params("end_time", end_time)
                .params("level", level)
                .params("intro", intro)
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
                                if(!"0".equals(task_superior)&&!"0".equals(task_id)){
                                    setResult(105, getIntent());
                                    finish();
                                }else {
                                    setResult(101, getIntent());
                                    finish();
                                }
                            }
                            MsgUtil.shortToast(EditTaskActivity.this,object.optString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //loadingDialog.dismiss();
                    }
                });
    }


    private void getTaskDetails() {
        //loadingDialog.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_TASKINFO;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+task_id);
        OkGo.get(url)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .params("task_id",task_id)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",Sign).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code=object.optString("code");
                    if("0".equals(code)) {
                        JSONObject data = new JSONObject(object.optString("data"));
                        taskInfoModel = JSON.parseObject(data.toString(),TaskInfoModel.class);
                        JSONArray header=new JSONArray(data.optString("header"));
                        headerList=JSON.parseArray(header.toString(),PersonListModel.class);
                        JSONArray joiner=new JSONArray(data.optString("joiner"));
                        joinerList=JSON.parseArray(joiner.toString(),PersonListModel.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initTaskDetails();
            }
            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);
                //loadingDialog.dismiss();
            }
        });
    }

    private void initTaskDetails() {
        Glide.with(this).load(headerList.get(0).getFace_img()).into(img_Create_head);
        ed_TaskTheme.setText(taskInfoModel.getTitle());
        tv_Start_Date.setText(taskInfoModel.getBegin_time());
        tv_Stop_Date.setText(taskInfoModel.getEnd_time());
        setTaskLevel(taskInfoModel.getLevel());
        intro=taskInfoModel.getIntro();
        for(int i=0;i<headerList.size();i++){
            PersonModel headerInfo=new PersonModel(headerList.get(i).getName(),headerList.get(i).getFace_img(),headerList.get(i).getId());
            ChargeList.add(headerInfo);
            Map<String,String> map=new HashMap<>();
            map.put("id",headerList.get(i).getId());
            ChargeIDList.add(map);
        }
        for(int i=0;i<joinerList.size();i++){
            PersonModel joinerInfo=new PersonModel(joinerList.get(i).getName(),joinerList.get(i).getFace_img(),joinerList.get(i).getId());
            JoinList.add(joinerInfo);
            Map<String,String> map=new HashMap<>();
            map.put("id",joinerList.get(i).getId());
//            Log.i("jid",joinerList.get(i).getId());
            JoinIDList.add(map);
        }

        Log.i(ChargeIDList.toString(),JoinIDList.toString());
        header= CustomUtils.changList(ChargeIDList);
        joiner=CustomUtils.changList(JoinIDList);
        Log.i(header,joiner);
        chargeAdapter=new ChargeAdapter(this,ChargeList,1);
        hlv_Charge.setAdapter(chargeAdapter);
        joinAdapter=new ChargeAdapter(this,JoinList,2);
        hlv_Join.setAdapter(joinAdapter);
    }

    public void setTaskLevel(String level) {
        if("3".equals(level)){
            tv_LabelGeneral.setSelected(false);
            tv_LabelUrgent.setSelected(false);
            tv_LabelVeryUrgent.setSelected(true);
        }else if("2".equals(level)){
            tv_LabelGeneral.setSelected(false);
            tv_LabelUrgent.setSelected(true);
            tv_LabelVeryUrgent.setSelected(false);
        }
    }
}
