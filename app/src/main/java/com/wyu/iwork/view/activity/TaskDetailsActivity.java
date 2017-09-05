package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ChildTaskArrangeAdapter;
import com.wyu.iwork.adapter.JoinerAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.ChildTaskModel;
import com.wyu.iwork.model.PersonListModel;
import com.wyu.iwork.model.TaskInfoModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.widget.HorizontialListView;
import com.wyu.iwork.widget.MyDialog;
import com.wyu.iwork.widget.MyListView;
import com.wyu.iwork.widget.MyProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

public class TaskDetailsActivity extends BaseNetActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG="TaskDetailsActivity";
    private ProgressBar pb_TaskDetails_Time;
    private Button btn_CompletedTack;
    private TextView tv_TaskDetails_ChildText,tv_TaskDetails_Start,tv_TaskDetails_Stop,tv_TaskDetails_Charge,tv_TaskDetails_Create,tv_TaskDetails_State,tv_TaskDetails_Theme;
    private MyListView lv_TaskDetails_ChildTask,lv_TaskDetails_Communicate;
    private HorizontialListView hlv_TaskDetails_Join;
    private MyProgressBar pb_TaskDetails;
    private LinearLayout ll_ChildTask;
    private ImageView img_TaskDetails_ChildDown,img_TaskDetails_addChild;
    private CircleImageView img_TaskDetails_head;
    private int ChildFLAG=0;
    private Bitmap bitmap;
    private BitmapDrawable bd;
    private JoinerAdapter joinAdapter;
    private ChildTaskArrangeAdapter childTaskAdapter;
    private String task_id,headerName="",level="一般",fatherID="0",user_id;
    //private LoadingDialog loading=new LoadingDialog();
    private TaskInfoModel taskInfoModel;
    private List<PersonListModel> headerList=new ArrayList<>();
    private List<PersonListModel> joinerList=new ArrayList<>();
    private List<ChildTaskModel> juniorList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        setBackNaviAction();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("任务详情");
        user_id=AppManager.getInstance(this).getUserInfo().getUser_id();
        init();
        getExtera();
        initListener();
        getTaskDetailsRequest();
    }

    private void getExtera() {
        task_id=getIntent().getStringExtra("task_id");
        if(getIntent().getIntExtra("task_child",0)==1){
            ll_ChildTask.setVisibility(View.GONE);
            fatherID=getIntent().getStringExtra("fatherid");
            ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("子任务详情");
        }else{
            fatherID=task_id;
        }
    }


    private void init() {
        ll_ChildTask=(LinearLayout)findViewById(R.id.ll_ChildTask);
        tv_TaskDetails_Start=(TextView)findViewById(R.id.tv_TaskDetails_Start) ;
        tv_TaskDetails_Stop=(TextView)findViewById(R.id.tv_TaskDetails_Stop) ;
        tv_TaskDetails_Charge=(TextView)findViewById(R.id.tv_TaskDetails_Charge) ;
        tv_TaskDetails_Create=(TextView)findViewById(R.id.tv_TaskDetails_Create) ;
        tv_TaskDetails_State=(TextView)findViewById(R.id.tv_TaskDetails_State) ;
        tv_TaskDetails_Theme=(TextView)findViewById(R.id.tv_TaskDetails_Theme) ;
        tv_TaskDetails_ChildText=(TextView)findViewById(R.id.tv_TaskDetails_ChildText);

        pb_TaskDetails_Time=(ProgressBar)findViewById(R.id.pb_TaskDetails_Time);
        lv_TaskDetails_ChildTask=(MyListView)findViewById(R.id.lv_TaskDetails_ChildTask);
        lv_TaskDetails_Communicate=(MyListView)findViewById(R.id.lv_TaskDetails_Communicate);
        img_TaskDetails_ChildDown=(ImageView)findViewById(R.id.img_TaskDetails_ChildDown) ;
        img_TaskDetails_head=(CircleImageView) findViewById(R.id.img_TaskDetails_head) ;
        img_TaskDetails_addChild=(ImageView)findViewById(R.id.img_TaskDetails_addChild) ;

        hlv_TaskDetails_Join=(HorizontialListView)findViewById(R.id.hlv_TaskDetails_Join);

        pb_TaskDetails=(MyProgressBar)findViewById(R.id.pb_TaskDetails);

        btn_CompletedTack=(Button)findViewById(R.id.btn_CompletedTack);

    }

    private void initListener() {
        img_TaskDetails_ChildDown.setOnClickListener(this);
        img_TaskDetails_addChild.setOnClickListener(this);
        lv_TaskDetails_ChildTask.setOnItemClickListener(this);
        btn_CompletedTack.setOnClickListener(this);
        tv_TaskDetails_ChildText.setOnClickListener(this);
    }

    private void initData() {
        try {
            Glide.with(this).load(headerList.get(0).getFace_img()).dontAnimate().placeholder(R.mipmap.ic_noimage).into(img_TaskDetails_head);
        }catch (Exception e){

        }
        tv_TaskDetails_Create.setText("创建人："+taskInfoModel.getCreater());
        headerName="";
        for(int i=0;i<headerList.size();i++){
            if(i==headerList.size()-1){
                headerName+=headerList.get(i).getName();
            }else {
                headerName += headerList.get(i).getName() + "、";
            }
            if(user_id.equals(headerList.get(i).getId())){
                btn_CompletedTack.setVisibility(View.VISIBLE);
            }
        }
        tv_TaskDetails_Charge.setText("负责人："+headerName);
        tv_TaskDetails_Theme.setText("任务主题："+taskInfoModel.getTitle());
        tv_TaskDetails_Start.setText(taskInfoModel.getBegin_time());
        tv_TaskDetails_Stop.setText(taskInfoModel.getEnd_time());
        tv_TaskDetails_State.setText(getLevel(taskInfoModel.getLevel()));
        joinAdapter=new JoinerAdapter(this,joinerList);
        hlv_TaskDetails_Join.setAdapter(joinAdapter);
        childTaskAdapter=new ChildTaskArrangeAdapter(this,juniorList);
        lv_TaskDetails_ChildTask.setAdapter(childTaskAdapter);
        tv_TaskDetails_ChildText.setText("子任务（"+juniorList.size()+"）");
        setProgerss();
    }

    private void setProgerss() {
        SimpleDateFormat Times= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();// 当前时间
        String CurrentTime=Times.format(currentTime);
        String StartTime=tv_TaskDetails_Start.getText().toString();
        String StopTime=tv_TaskDetails_Stop.getText().toString();
        try {
            Date current=Times.parse(CurrentTime);
            Date start=Times.parse(StartTime);
            Date stop=Times.parse(StopTime);
            long a=(stop.getTime()-start.getTime())/86400000;
            long b=(current.getTime()-start.getTime())/86400000;
            if(a<1){
                a=1;
            }
            if(b>=a){
                pb_TaskDetails_Time.setMax(100);
                pb_TaskDetails_Time.setProgress(100);
                pb_TaskDetails.setProgress(100);
            }else {
                pb_TaskDetails_Time.setMax((int) a);
                pb_TaskDetails_Time.setProgress((int) b);
                pb_TaskDetails.setProgress((int) ((Float.valueOf(b)/Float.valueOf(a))*100));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete,menu);
        getMenuInflater().inflate(R.menu.edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        switch (item.getItemId()){
            case R.id.delete:
                new MyDialog(3, this, R.style.MyDialog, "确定", "取消", "您确定要删除本任务吗？", new MyDialog.DialogClickListener() {
                    @Override
                    public void oneClick(Dialog dialog) {
                        deleteTaskRequest();
                        dialog.dismiss();
                    }

                    @Override
                    public void twoClick(Dialog dialog) {
                        dialog.dismiss();

                    }

                    @Override
                    public void threeClick(Dialog dialog) {

                    }
                }).show();
                break;
            case R.id.edit:
                it=new Intent(this,EditTaskActivity.class);
                    it.putExtra("task_id",task_id);
                    it.putExtra("task_superior",fatherID);
                startActivityForResult(it,0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_CompletedTack:
                new MyDialog(3, this, R.style.MyDialog, "确定", "取消", "您是否已经完成本任务?", new MyDialog.DialogClickListener() {
                    @Override
                    public void oneClick(Dialog dialog) {
                        completedTask();
                        dialog.dismiss();
                    }

                    @Override
                    public void twoClick(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void threeClick(Dialog dialog) {}
                }).show();
                break;
            case R.id.tv_TaskDetails_ChildText:
            case R.id.img_TaskDetails_ChildDown:
                if(ChildFLAG==0){
                    bd= (BitmapDrawable) getResources().getDrawable(R.mipmap.ic_down);
                    bitmap= bd.getBitmap();
                    bitmap= CustomUtils.toturn(bitmap);
                    img_TaskDetails_ChildDown.setImageBitmap(bitmap);
                    lv_TaskDetails_ChildTask.setVisibility(View.GONE);
                    ChildFLAG=1;
                }else{
                    img_TaskDetails_ChildDown.setImageResource(R.mipmap.ic_down);
                    lv_TaskDetails_ChildTask.setVisibility(View.VISIBLE);
                    ChildFLAG=0;
                }
                break;

            case R.id.img_TaskDetails_addChild:
                Intent it=new Intent(this,EditTaskActivity.class);
                it.putExtra("task_superior",task_id);
                it.putExtra("task_id","0");
                if(getIntent().getIntExtra("task_child",0)!=0){
                }
                startActivityForResult(it,2);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.lv_TaskDetails_ChildTask:
                Intent it=new Intent(this,getClass());
                it.putExtra("task_child",1);
                it.putExtra("fatherid",fatherID);
                it.putExtra("task_id",juniorList.get(i).getTask_id());
                startActivityForResult(it,5);
                break;
        }
    }
    private void getTaskDetailsRequest(){
        //loading.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
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
                        headerList.clear();
                        joinerList.clear();
                        juniorList.clear();
                        JSONObject data = new JSONObject(object.optString("data"));
                        taskInfoModel = JSON.parseObject(data.toString(),TaskInfoModel.class);
                        JSONArray header=new JSONArray(data.optString("header"));
                        headerList=JSON.parseArray(header.toString(),PersonListModel.class);
                        JSONArray joiner=new JSONArray(data.optString("joiner"));
                        joinerList=JSON.parseArray(joiner.toString(),PersonListModel.class);
                        JSONArray junior=new JSONArray(data.optString("junior"));
                        juniorList=JSON.parseArray(junior.toString(),ChildTaskModel.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                initData();
            }
            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);
                //loading.dismiss();
            }
        });

    }
    public String getLevel(String level){
        if("3".equals(level)){
            level="非常紧急";
        }else if("2".equals(level)){
            level="紧急";
        }else{
            level="一般";
        }
        return  level;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case 101:
                getTaskDetailsRequest();
                childTaskAdapter.notifyDataSetChanged();
                break;
            case 105:
                getTaskDetailsRequest();
                setResult(101,getIntent());
                break;
        }
    }

    private void deleteTaskRequest(){
        Log.i("=====task_id",task_id);
        //loading.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_TASKDELETE;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+user_id+task_id);
        OkGo.get(url)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .params("user_id",user_id)
                .params("task_id",task_id)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",Sign).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    if("0".equals(object.optString("code"))){
                        setResult(101,getIntent());
                        finish();
                    }
                    MsgUtil.shortToast(TaskDetailsActivity.this,object.optString("msg"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);
                //loading.dismiss();
            }
        });
    }

    private void completedTask() {
        //loading.show(this.getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_TASKFINSH;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+user_id+task_id);
        OkGo.get(url)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .params("user_id",user_id)
                .params("task_id",task_id)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",Sign).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    if("0".equals(object.optString("code"))){
                        setResult(101,getIntent());
                    }
                    MsgUtil.shortToast(TaskDetailsActivity.this,object.optString("msg"));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);
                //loading.dismiss();
            }
        });

    }

}
