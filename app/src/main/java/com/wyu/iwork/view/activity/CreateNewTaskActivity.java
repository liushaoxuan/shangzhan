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
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.PersonListAdapter;
import com.wyu.iwork.adapter.TaskerPersonListAdapter;
import com.wyu.iwork.adapter.spaceitemdecoration.SpaceItemDecoration;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.DetailTaskModel;
import com.wyu.iwork.model.Person;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.wheeldate.CustomTextPicker;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.TaskItem;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.type;
import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;
/**
 * 新建任务
 */
public class CreateNewTaskActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = CreateNewTaskActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.task_theme)
    EditText task_theme;

    @BindView(R.id.task_desc)
    EditText task_desc;

    @BindView(R.id.emergency_degree)
    TaskItem emergency_degree;

    @BindView(R.id.start_time)
    TaskItem start_time;

    @BindView(R.id.end_time)
    TaskItem end_time;

    //负责人
    @BindView(R.id.rv_add_person)
    RecyclerView rv_add_person;

    //添加负责人
    @BindView(R.id.add_image)
    AutoLinearLayout add_image;

    //参与人
    @BindView(R.id.rv_add_pic)
    RecyclerView rv_add_pic;

    //添加参与人
    @BindView(R.id.add_partic)
    AutoLinearLayout add_partic;

    @BindView(R.id.activity_create_new_task_scroolview)
    ScrollView activity_create_new_task_scroolview;

    private ArrayList<Person.PersonMessage> headerList = new ArrayList<>();
    private ArrayList<Person.PersonMessage> joinerList = new ArrayList<>();
    private static final int CODE_HEADER = 101;
    private static final int CODE_JOINER = 102;
    private LinearLayoutManager headerManager;
    private PersonListAdapter headerAdapter;
    private LinearLayoutManager joinerManager;
    private PersonListAdapter joinerAdapter;
    private Date startTime;
    private Date endTime;

    private CustomTextPicker mPicker;
    private SpaceItemDecoration spaceItemDecoration;
    private int spacingInPixels;
    private String task_id;
    private Gson gson;
    private TaskerPersonListAdapter mJoinerAdapter;
    private TaskerPersonListAdapter mHeaderAdapter;

    private ArrayList<DetailTaskModel.Data.PartIn> editHeadlist = new ArrayList<>();
    private ArrayList<DetailTaskModel.Data.PartIn> editJoinlist = new ArrayList<>();

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_task);
        ButterKnife.bind(this);
        hideToolbar();
        tv_title.setText("创建任务");
        getExtras();
        initView();
    }

    private void getExtras(){
        task_id = getIntent().getStringExtra("task_id");
        if(!TextUtils.isEmpty(task_id)){
            getTaskMessgae();
            tv_title.setText("编辑任务");
        }
    }

    private void getTaskMessgae(){
        /**
         * task_id	是	int[11]任务ID
         user_id	是	int[11]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.task_id.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+task_id+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("task_id",task_id);
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_TASKINFO_V2,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseData(s);
                    }
                    @Override
                    public void onAfter(@Nullable String s, @Nullable Exception e) {
                        super.onAfter(s, e);
                    }
                });

    }

    private void parseData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }

            DetailTaskModel model = gson.fromJson(data,DetailTaskModel.class);
            if("0".equals(model.getCode())){
                checkStr(model.getData().getTitle(),task_theme);
                checkStr(model.getData().getIntro(),task_desc);
                if(!TextUtils.isEmpty(model.getData().getLevel())){
                    setEmergency(model.getData().getLevel());
                }
                checkStr(splitTime(model.getData().getBegin_time()),start_time.getTextView());
                checkStr(splitTime(model.getData().getEnd_time()),end_time.getTextView());

                if(model.getData().getJoiner() != null && model.getData().getJoiner().size()>0){
                    editJoinlist.clear();
                    editJoinlist.addAll(model.getData().getJoiner());
                    rv_add_pic.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                    rv_add_pic.setAdapter(new TaskerPersonListAdapter(this, editJoinlist,2));
                }
                if(model.getData().getHeader() != null && model.getData().getHeader().size()>0){
                    editHeadlist.clear();
                    editHeadlist.addAll(model.getData().getHeader());
                    Logger.i(TAG, model.getData().toString());
                    rv_add_person.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                    rv_add_person.setAdapter(new TaskerPersonListAdapter(this, editHeadlist,1));
                }
                initParameter(model);
            }else{
                MsgUtil.shortToastInCenter(this, model.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void initParameter(DetailTaskModel model){
        //时间
        startTime = strToDate(model.getData().getBegin_time());
        endTime = strToDate(model.getData().getEnd_time());
    }

    public void setPersonList(int type){
        if(type == 1){
            if(editHeadlist != null && editHeadlist.size()>0){
                for(int i = 0;i<editHeadlist.size();i++){
                    Person p = new Person();
                    Person.PersonMessage message = p.new PersonMessage();
                    message.setReal_name(editHeadlist.get(i).getName());
                    message.setId(editHeadlist.get(i).getId());
                    headerList.add(message);
                }
            }
        }else if(type == 2){
            if(editJoinlist != null && editJoinlist.size()>0){
                for(int i = 0;i<editJoinlist.size();i++){
                    Person p = new Person();
                    Person.PersonMessage message = p.new PersonMessage();
                    message.setReal_name(editJoinlist.get(i).getName());
                    message.setId(editJoinlist.get(i).getId());
                    joinerList.add(message);
                }
            }
        }

    }

    public Date strToDate(String strDate) {//把格式是2012-12-02的日期串转为Date类型
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    private void setEmergency(String level){
        /**
         * 1.普通 2.紧急 3.非常紧急
         */
        if(level.equals("1")){
            emergency_degree.setValue("普通");
        }else if(level.equals("2")){
            emergency_degree.setValue("紧急");
        }else if(level.equals("3")){
            emergency_degree.setValue("非常紧急");
        }
    }

    private String splitTime(String data){
        /**
         * 2017-01-04 16:10:00
         */
        String[] time = data.split(" ");
        String[] monthTime = time[0].split("-");
        String[] minutesTime = time[1].split(":");
        return monthTime[1]+"月"+monthTime[2]+"日 "+minutesTime[0]+":"+minutesTime[1];
    }

    private void initView(){
        tv_edit.setVisibility(View.VISIBLE);
        tv_edit.setText("完成");
        //设置头像
        activity_create_new_task_scroolview.setOnTouchListener(this);

        initItem();
    }

    private void initItem(){

        emergency_degree.setBottomLineVisible(false);
        start_time.setBottomLineVisible(false);

        emergency_degree.setTitle("紧急程度");
        start_time.setTitle("开始时间");
        end_time.setTitle("结束时间");

        emergency_degree.setHintValue("请选择");
        start_time.setHintValue("请选择");
        end_time.setHintValue("请选择");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_create_new_task_scroolview);
        return false;
    }

    @OnClick({R.id.ll_back,R.id.start_time,R.id.end_time,R.id.emergency_degree,R.id.add_image,R.id.add_partic,R.id.tv_edit})
    void Click(View view){
        switch (view.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.start_time:
                showDatePickerDialog(start_time);
                break;
            case R.id.end_time:
                showDatePickerDialog(end_time);
                break;
            case R.id.emergency_degree:
                showCustomResourceDialog(getResources().getStringArray(R.array.task_emergency_evel),emergency_degree);
                break;
            case R.id.add_image:
                //添加负责人
                addPrincipal();
                break;
            case R.id.add_partic:
                //添加参与人
                addJoiner();
                break;
            case R.id.tv_edit:
                checkPostMessage();
                break;
        }
    }

    //显示时间弹窗选择日期
    private void showDatePickerDialog(final TaskItem taskItem){
        new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(taskItem == start_time){
                    startTime = date;
                }else if(taskItem == end_time){
                    endTime = date;
                }
                taskItem.setValue(getTime(date));
            }
        }," ").show_timepicker_h_m();
    }

    //显示客户来源选项弹框
    private void showCustomResourceDialog(String[] arr, final TaskItem item){
        mPicker = new CustomTextPicker(this,arr);
        mPicker.show();
        mPicker.setBirthdayListener(new CustomTextPicker.OnBirthListener() {
            @Override
            public void onClick(String Mmin) {
                Logger.i(TAG,"Min="+Mmin);
                item.setValue(Mmin);
            }
        });
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        return format.format(date);
    }

    private String getPostTime(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

    //添加负责人
    public void addPrincipal(){
        if(headerList != headerList && headerList.size()==0){
            setPersonList(1);
        }
        Intent headerIntent = new Intent(this, PersonListActivity.class);
        headerIntent.putExtra("type","header");
        Bundle bundle = new Bundle();
        bundle.putSerializable("HEADER",headerList);
        headerIntent.putExtras(bundle);
        startActivityForResult(headerIntent,CODE_HEADER);
    }

    //添加参与人
    public void addJoiner(){
        if(joinerList != null && joinerList.size() == 0){
            setPersonList(2);
        }
        Intent joinerIntent = new Intent(this, PersonListActivity.class);
        joinerIntent.putExtra("type","joiner");
        Bundle bundle1 = new Bundle();
        bundle1.putSerializable("JOINER",joinerList);
        joinerIntent.putExtras(bundle1);
        startActivityForResult(joinerIntent,CODE_JOINER);
    }


    private void checkPostMessage(){
        String header = getAllId(1);
        String joiner = getAllId(2);
        String title = task_theme.getText().toString();
        String intro = task_desc.getText().toString();
        //任务主题  任务描述  开始时间  结束时间  负责人
        //if(TextUtils.isEmpty(header) || TextUtils.isEmpty(joiner) || TextUtils.isEmpty(title)
          //      || TextUtils.isEmpty(intro) || TextUtils.isEmpty(start_time.getValue())
            //    || TextUtils.isEmpty(end_time.getValue()) || TextUtils.isEmpty(emergency_degree.getValue())){
            //showRedmineDialog("您的任务信息未填写完整\n请完成填写并提交");
        if(TextUtils.isEmpty(title)){
            MsgUtil.shortToastInCenter(this,"请输入任务主题!");
        }else if(TextUtils.isEmpty(intro)){
            MsgUtil.shortToastInCenter(this,"请输入任务描述!");
        }else if(TextUtils.isEmpty(start_time.getValue())){
            MsgUtil.shortToastInCenter(this,"请选择任务开始时间!");
        }else if(TextUtils.isEmpty(end_time.getValue())){
            MsgUtil.shortToastInCenter(this,"请选择任务结束时间!");
        }else if(TextUtils.isEmpty(header)){
            MsgUtil.shortToastInCenter(this,"请选择任务负责人!");
        }else if(startTime.getTime() >= endTime.getTime()){
            showRedmineDialog("任务结束时间必须大于开始时间,\n请重新设置再提交!");
        }else{
            HttpParams data = new HttpParams();
            String F = "";
            String V = "";
            String RandStr = CustomUtils.getRandStr();
            String Sign = "";//F.V.RandStr.user_id.task_superior.task_id
            data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
            data.put("task_superior","0");
            if(!TextUtils.isEmpty(task_id)){
                data.put("task_id",task_id);
                Sign = Md5Util.getSign(F+V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+"0"+task_id);
            }else{
                data.put("task_id","0");
                Sign = Md5Util.getSign(F+V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+"0"+"0");
            }
            data.put("title",title);
            data.put("header",header);
            if(!TextUtils.isEmpty(joiner)){
                data.put("joiner",joiner);
            }
            data.put("begin_time",getPostTime(startTime));
            data.put("end_time",getPostTime(endTime));
            if(!TextUtils.isEmpty(emergency_degree.getValue())){
                String type = "";
                if("普通".equals(emergency_degree.getValue())){
                    type = "1";
                }else if("紧急".equals(emergency_degree.getValue())){
                    type = "2";
                }else if("非常紧急".equals(emergency_degree.getValue())){
                    type = "3";
                }
                data.put("level",type);
            }
            data.put("intro",intro);
            data.put("F",F);
            data.put("V",V);
            data.put("RandStr",RandStr);
            data.put("Sign",Sign);
            OkGo.post(Constant.URL_GET_UPDATE_TASK_V2).tag(this).cacheMode(CacheMode.DEFAULT).params(data)
                    .execute(new DialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Logger.i(TAG,s);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(s);
                                if(object.getString("code").equals("0")){
                                    onBackPressed();
                                }
                                MsgUtil.shortToastInCenter(CreateNewTaskActivity.this,object.getString("msg"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    //提醒弹窗//"您的任务信息未填写完整\n请完成填写并提交"
    private void showRedmineDialog(String str){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, str, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    private String getAllId(int type){
        String idStr= "|";
        if(type == 1){
            if(headerList != null && headerList.size() == 0 && editHeadlist != null && editHeadlist.size()>0 ){
                setPersonList(1);
            }
            if(headerList.size() <= 0){
                //showRedmineDialog("请添加负责人!");
            }else{
                for(int i = 0;i<headerList.size();i++){
                    idStr += headerList.get(i).getId()+"|";
                }
                return idStr;
            }
        }else{

            if(joinerList != null && joinerList.size() == 0 && editJoinlist != null && editJoinlist.size()>0 ){
                setPersonList(2);
            }

            if(headerList.size() <= 0){
                //showRedmineDialog("请添加参与人!");
            }else{
                for(int i = 0;i<joinerList.size();i++){
                    idStr += joinerList.get(i).getId()+"|";
                }
                return idStr;
            }
        }
        return "";
    }

    public void removePerson(int type,int item){
        if(type == 1){
            editHeadlist.remove(item);
        }else if(type == 2){
            editJoinlist.remove(item);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_HEADER && resultCode == 101){
            headerList = (ArrayList<Person.PersonMessage>) data.getExtras().getSerializable("HEADER");
            if(headerManager == null){
                headerManager = new LinearLayoutManager(this);
                headerManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }

            if(spaceItemDecoration == null){
                spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
                spaceItemDecoration = new SpaceItemDecoration(spacingInPixels);
            }
            headerAdapter = new PersonListAdapter(this,headerList);
            rv_add_person.setLayoutManager(headerManager);
            rv_add_person.addItemDecoration(spaceItemDecoration);
            rv_add_person.setAdapter(headerAdapter);
            headerAdapter.notifyDataSetChanged();
        }else{
            joinerList = (ArrayList<Person.PersonMessage>) data.getExtras().getSerializable("JOINER");
            //设置图片
            if(joinerManager == null){
                joinerManager = new LinearLayoutManager(this);
                joinerManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            }
            if(spaceItemDecoration == null){
                spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space);
                spaceItemDecoration = new SpaceItemDecoration(spacingInPixels);
            }
            joinerAdapter = new PersonListAdapter(this,joinerList);
            rv_add_pic.setLayoutManager(joinerManager);
            rv_add_pic.addItemDecoration(spaceItemDecoration);
            rv_add_pic.setAdapter(joinerAdapter);
            joinerAdapter.notifyDataSetChanged();
        }
    }
}
