package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AddDailyReportPersonAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Person;
import com.wyu.iwork.model.PersonModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 添加日报人员
 */
public class AddDailyReportPersonActivity extends BaseActivity implements AdapterView.OnItemClickListener,TextWatcher,View.OnClickListener {
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    private static final String TAG = AddDailyReportPersonActivity.class.getSimpleName();
    private ArrayList idList ;

    @BindView(R.id.et_search)
    EditText search;

    @BindView(R.id.lv_person)
    ListView personListView;

    @BindView(R.id.tv_cancel)
    TextView cancel;

    @BindView(R.id.tv_commit)
    TextView commit;

    @BindView(R.id.activity_add_daily_report_person)
    AutoLinearLayout activity_add_daily_report_person;

    private TextView tv_sure;
    private TextView tv_cancel;

    private AddDailyReportPersonAdapter adapter;
    private ArrayList<Person.PersonMessage> dataList ;
    private ArrayList<PersonModel> list=new ArrayList<>();
    private PersonModel personModel;
    private int listTag = 1;
    private ArrayList<Person.PersonMessage> searchList=new ArrayList<>();
    private HashMap<String,String> data;
    private int type = 1;//1 代表当前为添加日报人员   2 代表当前为添加会议人员

    private ImageView iv_select;
    private Dialog mDialog;
    private TextView tv_title;
    private TextView tv_time;
    private static final int CODE_RESULT = 110;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_report_person);
        ButterKnife.bind(this);
        getExter();
        hideToolbar();
        initData();
    }

    private void initData(){
        UserInfo user = AppManager.getInstance(this).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis()+"|"+ Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+user.getCompany_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("company_id",user.getCompany_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_COMPANYUSERLIST,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parsePersonData(s);
                    }
                });
        initClick();
    }

    private void initClick(){
        personListView.setOnItemClickListener(this);
        search.addTextChangedListener(this);
        activity_add_daily_report_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hideinputwindown(activity_add_daily_report_person);
            }
        });
    }
    private void parsePersonData(String data){
        try{
            if(gson == null){
                gson = new Gson();
            }
            Person person = gson.fromJson(data,Person.class);
            if(person.getCode() == 0){
                //请求成功
                if(person.getData().size()>0){
                    dataList = person.getData();
                    initList(dataList);
                }
            }else{
                MsgUtil.shortToastInCenter(this,person.getMsg());
            }
        }catch (Exception e){

        }
    }

    private void initList(ArrayList<Person.PersonMessage> personList){
        if(!idList.isEmpty()){
            for(int i = 0;i<personList.size();i++){
                for(int j = 0;j<idList.size();j++){
                    Map<String,String> map = (Map<String, String>) idList.get(j);
                    if(map.get("id").equals(personList.get(i).getId())){
                        personList.get(i).setSelected(true);
                        personModel = new PersonModel(personList.get(i).getReal_name(),personList.get(i).getFace_img(),personList.get(i).getId());
                        list.add(personModel);
                    }
                }
            }
        }
        adapter = new AddDailyReportPersonAdapter(this,personList);
        personListView.setAdapter(adapter);
    }


    public void getExter(){
        type = getIntent().getIntExtra("type",1);
        if(type == 1){
            idList = getIntent().getExtras().getStringArrayList("idList");
        }else if(type == 2){
            idList = new ArrayList();
            data = (HashMap<String, String>) getIntent().getSerializableExtra("data");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        iv_select = (ImageView) view.findViewById(R.id.iv_select);
        if(/**dataList.get(position).isSelect()*/iv_select.isSelected()){
            iv_select.setSelected(false);
            dataList.get(position).setSelected(false);
            for(int i = 0;i < idList.size();i++){
                if(listTag == 1){
                    if(dataList.get(position).getId().equals(list.get(i).getId())){
                        list.remove(i);
                    }
                    Map m = (Map) idList.get(i);
                    if (dataList.get(position).getId().equals(m.get("id"))) {
                        idList.remove(i);
                    }
                }else if(listTag == 2){
                    if (searchList.get(position).getId().equals(list.get(i).getId())) {
                        list.remove(i);
                    }
                    Map m = (Map) idList.get(i);
                    if (searchList.get(position).getId().equals(m.get("id"))) {
                        idList.remove(i);
                    }
                    for(int j=0;j < dataList.size();j++){
                        if(dataList.get(j).getId().equals(searchList.get(position).getId())){
                            dataList.get(j).setSelected(false);
                        }
                    }
                }
            }
        }else{
            iv_select.setSelected(true);
            dataList.get(position).setSelected(true);
            Map<String,String> map=new HashMap<>();
            if(listTag==1) {
                personModel = new PersonModel(dataList.get(position).getReal_name(), dataList.get(position).getFace_img(),dataList.get(position).getId());
                map.put("id", dataList.get(position).getId());
                dataList.get(position).setSelected(true);
            }else if(listTag == 2){
                personModel = new PersonModel(searchList.get(position).getReal_name(), searchList.get(position).getFace_img(),searchList.get(position).getId());
                map.put("id", searchList.get(position).getId());
                for(int i = 0;i < dataList.size();i++){
                    if(dataList.get(i).getId().equals(searchList.get(position).getId())){
                        dataList.get(i).setSelected(true);
                    }
                }
            }
            idList.add(map);
            list.add(personModel);
        }
        //adapter.notifyDataSetChanged();
        //添加人员到发送列表中
    }

    @OnClick({R.id.tv_cancel,R.id.tv_commit})
    void CLICK(View v){
        switch (v.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.tv_commit:
                if(type == 1){
                    jumpToDailyReport();
                }else if(type == 2){
                    if(idList != null && idList.size()>0){
                        showDialog();
                    }else{
                        MsgUtil.shortToastInCenter(this,"请选择会议人员!");
                    }
                }
                break;
        }
    }

    private void jumpToDailyReport(){
        Intent it=getIntent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("personList",list);
        bundle.putStringArrayList("idList",idList);
        it.putExtras(bundle);
        setResult(101,it);
        finish();
    }

    /**
     * user_id	是	int[11]会议通知发布者用户ID
     send_users	是	string[150]被通知者用户ID( 多个用户用 | 进行连接,如|232|445| )
     content	是	string[200]会议概要的内容和需要准备的资料
     address	是	string[150]会议地址
     start_time	是	string[18]会议开始的时间, 格式如：2017-03-03 9:00:51
     F	是	string[18]请求来源：IOS/ANDROID/WEB
     V	是	string[20]版本号如：1.0.1
     RandStr	是	string[50]请求加密随机数 time().|.rand()
     Sign       是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.send_users)
     */

    private void showDialog(){
        Logger.i(TAG,AppManager.getInstance(this).getUserInfo().toString());
        MyCustomDialogDialog dialog = new MyCustomDialogDialog(1,this, R.style.MyDialog,AppManager.getInstance(this).getUserInfo().getReal_name()+" 发起了一个会议","会议将于"+data.get("start_time")+"在"+data.get("address")+"开始！", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                commitMeeting();
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void commitMeeting(){
        //循环idList
        String idStr = "";
        for(int i = 0;i<idList.size();i++){
            Map<String,String> map = (Map<String, String>) idList.get(i);
            if(i == 0){
                idStr = "|"+idStr+map.get("id")+"|";
            }else{
                idStr = idStr+map.get("id")+"|";
            }
        }
        /**
         * user_id	是	int[11]会议通知发布者用户ID
         send_users	是	string[150]被通知者用户ID( 多个用户用 | 进行连接,如|232|445| )
         content	是	string[200]会议概要的内容和需要准备的资料
         address	是	string[150]会议地址
         start_time	是	string[18]会议开始的时间, 格式如：2017-03-03 9:00:51
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.send_users)
         */
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+ RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+idStr);
        data.put("send_users",idStr);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        Logger.i(TAG,idStr);
        String murl = RequestUtils.getRequestUrl(Constant.URL_SEND_METTING,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseMeetingData(s);
                    }
                });

    }

    private void parseMeetingData(String data){
        JSONObject object = null;
        try {
            object = new JSONObject(data);
            if(object.getString("code").equals("0")){
                setResult(CODE_RESULT);
                finish();
            }
            MsgUtil.shortToastInCenter(this,object.getString("msg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //搜索栏文本修改前状态
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //搜索栏文本修改中状态
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //搜索栏文本修改后状态
    @Override
    public void afterTextChanged(Editable s) {
        if(search.getText().toString().isEmpty()){
            listTag = 1;
            adapter = new AddDailyReportPersonAdapter(this,dataList);
            personListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{
            listTag = 2;
            //先清空搜索列表 再进行搜索  添加到搜索列表
            searchList.clear();
            if(dataList != null && dataList.size()>0){
                for(int i = 0;i<dataList.size();i++){
                    //进行遍历   若有相同的  则提取到搜索列表中
                    if(!TextUtils.isEmpty(dataList.get(i).getDepartment()) && !TextUtils.isEmpty(dataList.get(i).getReal_name())){
                        if(dataList.get(i).getReal_name().indexOf(search.getText().toString()) != -1 ||
                                dataList.get(i).getDepartment().indexOf(search.getText().toString()) != -1){
                            searchList.add(dataList.get(i));
                        }
                    }else if(!TextUtils.isEmpty(dataList.get(i).getDepartment()) && TextUtils.isEmpty(dataList.get(i).getReal_name())){
                        if(dataList.get(i).getDepartment().indexOf(search.getText().toString()) != -1){
                            searchList.add(dataList.get(i));
                        }
                    }else if(TextUtils.isEmpty(dataList.get(i).getDepartment()) && !TextUtils.isEmpty(dataList.get(i).getReal_name())){
                        if(dataList.get(i).getReal_name().indexOf(search.getText().toString()) != -1){
                            searchList.add(dataList.get(i));
                        }
                    }

                }
                adapter = new AddDailyReportPersonAdapter(this,searchList);
                personListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
                commitMeeting();
                mDialog.dismiss();
                break;
            case R.id.tv_cancel:
                mDialog.dismiss();
                break;
        }
    }

}
