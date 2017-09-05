package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.EmployeeListAdapter;
import com.wyu.iwork.adapter.PersonAZAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Person;
import com.wyu.iwork.model.TaskContactsModule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PersonListActivity extends BaseActivity implements TextWatcher{

    private static final String TAG = PersonListActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.custom_toolbar)
    AutoLinearLayout custom_toolbar;

    @BindView(R.id.home_contacts_listview)
    ListView home_contacts_listview;

    //@BindView(R.id.task_listview)
    //MyListView task_listview;

    @BindView(R.id.task_recycleview)
    RecyclerView task_recycleview;

    @BindView(R.id.ll_select_all)
    AutoLinearLayout ll_select_all;

    @BindView(R.id.tv_select_all)
    TextView tv_select_all;

    @BindView(R.id.activity_person_list_scrollview)
    ScrollView activity_person_list_scrollview;

    @BindView(R.id.activity_person_list_select)
    AutoLinearLayout activity_person_list_select;

    @BindView(R.id.activity_person_list_et_select)
    EditText activity_person_list_et_select;

    @BindView(R.id.person_finish)
    TextView person_finish;

    @BindView(R.id.contract_zanwu)
    LinearLayout contract_zanwu;

    //private LoadingDialog loadingDialog = new LoadingDialog();

    private ArrayList<Person.PersonMessage> contactlist = new ArrayList<>();
    private ArrayList<Person.PersonMessage> searchList = new ArrayList<>();
    private String AzString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private List<String> Azlist ;
    private Gson gson;
    private int listTag = 1;
    //字母适配器
    private PersonAZAdapter azAdapter;
    private TaskContactsModule mTaskContactsModule;
    //private TaskPersonListAdapter adapter;
    private ArrayList<Person.PersonMessage> headerList = new ArrayList<>();
    private ArrayList<Person.PersonMessage> joinerList = new ArrayList<>();
    private String type;
    private TextView tv_select;
    private EmployeeListAdapter mAdapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
        ButterKnife.bind(this);
        hideToolbar();
        getExtras();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_person_list_scrollview.smoothScrollTo(0,0);
    }

    private void getExtras(){
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(type.equals("header")){
            headerList = (ArrayList<Person.PersonMessage>) intent.getSerializableExtra("HEADER");
            Logger.i(TAG,headerList.toString()+"");
        }else{
            joinerList = (ArrayList<Person.PersonMessage>) intent.getSerializableExtra("JOINER");
            Logger.i(TAG,joinerList.toString()+"");
        }
    }

    private void initView(){
        tv_title.setText("人员列表");
        getContacts();
        initAzlist();
        azAdapter = new PersonAZAdapter(this,Azlist);
        home_contacts_listview.setAdapter(azAdapter);
        home_contacts_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Logger.i(TAG,Azlist.get(position));
                measureHeight(Azlist.get(position));
            }
        });
        activity_person_list_et_select.addTextChangedListener(this);
    }

    private void initAzlist(){
        Azlist = new ArrayList<>();
        for (int i = 0;i<AzString.length();i++){
            Azlist.add(AzString.charAt(i)+"");
        }
    }

    //获取联系人列表
    private void getContacts(){
        /**
         * company_id	是	int[11]
         公司ID
         RandStr	是	string[50]
         请求加密随机数 time().|.rand()
         Sign	是	string[400]
         请求加密值 F_moffice_encode(F.V.RandStr.company_id)
         */
        //loadingDialog.show(getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        String url = Constant.URL_GET_COMPANYUSERLIST;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + AppManager.getInstance(this).getUserInfo().getCompany_id());
        Map<String, String> map = new HashMap<String, String>();
        map.put("company_id", AppManager.getInstance(this).getUserInfo().getCompany_id());
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        Logger.i(TAG,murl);

        OkGo.get(murl)
                .tag("ContactsFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        //loadingDialog.dismiss();
                        Logger.i(TAG,s);
                        parseData(s);
                    }
                });
    }

    private void parseData(String data){
        try{
            if(gson == null){
                gson = new Gson();
            }
            Person person = gson.fromJson(data,Person.class);
            if(person.getCode() == 0){
                if(person.getData().size()>0){
                    showContent(true);
                    contactlist = person.getData();
                    if("header".equals(type)){
                        if(headerList.size() == contactlist.size()){
                            ll_select_all.setSelected(true);
                        }else{
                            ll_select_all.setSelected(false);
                        }
                    }else{
                        if(joinerList.size() == contactlist.size()){
                            ll_select_all.setSelected(true);
                        }else{
                            ll_select_all.setSelected(false);
                        }
                    }
                    setAdapter(contactlist);
                }else{
                    showContent(false);
                }
            }else{
                showContent(false);
                MsgUtil.shortToastInCenter(this,mTaskContactsModule.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void showContent(boolean flag){
        task_recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
        contract_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        person_finish.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    private void setAdapter(ArrayList<Person.PersonMessage> list){
        //adapter = new TaskPersonListAdapter(this,list);
        //task_listview.setAdapter(adapter);

        if("header".equals(type)){
            if(headerList.size()>0){
                for(int i = 0;i<headerList.size();i++){
                    for(int j = 0;j<list.size();j++){
                        if(headerList.get(i).getId().equals(list.get(j).getId())){
                            Logger.i(TAG,"HEADERID:"+headerList.get(i).getId()+"  LISTID:"+list.get(j).getId());
                            list.get(j).setSelected(true);
                        }
                    }
                }
            }
        }else{
            if(joinerList.size()>0){
                for(int i = 0;i<joinerList.size();i++){
                    for(int j = 0;j<list.size();j++){
                        if(joinerList.get(i).getId().equals(list.get(j).getId())){
                            Logger.i(TAG,"JOINER:"+joinerList.get(i).getId()+"  LISTID:"+list.get(j).getId());
                            list.get(j).setSelected(true);
                        }
                    }
                }
            }
        }
        mAdapter = new EmployeeListAdapter(this,list);
        task_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        task_recycleview.setAdapter(mAdapter);
        //mAdapter.notifyDataSetChanged();
    }

    public void addOrRemoveList(Person.PersonMessage personMessage){
        boolean flag = false;
        if("header".equals(type)){
            //
            if(headerList.size()>0){
                for (int i = 0;i<headerList.size();i++){
                    if(headerList.get(i).getId().equals(personMessage.getId())){
                        headerList.remove(i);
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    headerList.add(personMessage);
                }
            }else{
                headerList.add(personMessage);
            }
        }else{
            if(joinerList.size()>0){
                for (int i = 0;i<joinerList.size();i++){
                    if(joinerList.get(i).getId().equals(personMessage.getId())){
                        joinerList.remove(i);
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    joinerList.add(personMessage);
                }
            }else{
                joinerList.add(personMessage);
            }
        }
    }

    @OnClick({R.id.person_finish,R.id.ll_select_all,R.id.ll_back})
    void Click(View view){
        switch (view.getId()){
            case R.id.person_finish:
                Hideinputwindown(person_finish);
                jumpToTaskActivity();
                break;
            case R.id.ll_select_all:
                selectAll();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    private void selectAll(){
        tv_select_all.setSelected(!tv_select_all.isSelected());
        if(contactlist != null && contactlist.size()>0){
            if(tv_select_all.isSelected()){
                for(int i = 0;i<contactlist.size();i++){
                    contactlist.get(i).setSelected(true);
                }
                if(type.equals("header")){
                    headerList.clear();
                    headerList.addAll(contactlist);
                }else{
                    joinerList.clear();
                    joinerList.addAll(contactlist);
                }
            }else{
                for(int i = 0;i<contactlist.size();i++){
                    contactlist.get(i).setSelected(false);
                }
                if(type.equals("header")){
                    headerList.clear();
                }else{
                    joinerList.clear();
                }

            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private void jumpToTaskActivity(){
        Intent it=getIntent();
        Bundle bundle=new Bundle();
        if(type.equals("header")){
            /**
            headerList.clear();
            for(int i = 0;i<contactlist.size();i++){
                if(contactlist.get(i).isSelected()){
                    headerList.add(contactlist.get(i));
                }
            }*/
            bundle.putSerializable("HEADER",headerList);
            it.putExtras(bundle);
            setResult(101,it);
        }else{
            /**
            joinerList.clear();
            for(int i = 0;i<contactlist.size();i++){
                if(contactlist.get(i).isSelected()){
                    joinerList.add(contactlist.get(i));
                }
            }*/
            bundle.putSerializable("JOINER",joinerList);
            it.putExtras(bundle);
            setResult(102,it);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        jumpToTaskActivity();
        super.onBackPressed();
    }

    //计算高度
    private void measureHeight(String letter){
        int totalHeight = 0;
        custom_toolbar.measure(0,0);
        totalHeight += custom_toolbar.getMeasuredHeight();
        Logger.i(TAG,"custom_toolbar"+totalHeight);
        ll_select_all.measure(0,0);
        totalHeight += ll_select_all.getMeasuredHeight();
        Logger.i(TAG,"ll_select_all"+totalHeight);
        activity_person_list_select.measure(0,0);
        totalHeight += activity_person_list_select.getMeasuredHeight();
        Logger.i(TAG,"activity_person_list_select"+totalHeight);
        //首先找出list中第一个与letter相同的项
        int position = 0;
        if(contactlist != null && contactlist.size()>0){
            for(int i = 0;i<contactlist.size();i++){
                if(contactlist.get(i).getPinyin().equals(letter)){
                    Logger.i(TAG,"PINYIN="+contactlist.get(i).getPinyin());
                    position = i;
                    break;
                }
            }
        }

        activity_person_list_scrollview.smoothScrollTo(0,totalHeight);
        Logger.i(TAG,"totalHeight"+totalHeight);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(activity_person_list_et_select.getText().toString().isEmpty()){
            listTag = 1;
            setAdapter(contactlist);
        }else{
            listTag = 2;
            searchList.clear();
            for(int i = 0;i<contactlist.size();i++){
                //进行遍历   若有相同的  则提取到搜索列表中
                if(!TextUtils.isEmpty(contactlist.get(i).getUser_name()) && !TextUtils.isEmpty(contactlist.get(i).getReal_name())){
                    if(contactlist.get(i).getReal_name().indexOf(activity_person_list_et_select.getText().toString()) != -1 ||
                            contactlist.get(i).getUser_name().indexOf(activity_person_list_et_select.getText().toString()) != -1){
                        searchList.add(contactlist.get(i));
                    }
                }else if(!TextUtils.isEmpty(contactlist.get(i).getUser_name()) && TextUtils.isEmpty(contactlist.get(i).getReal_name())){
                    if(contactlist.get(i).getUser_name().indexOf(activity_person_list_et_select.getText().toString()) != -1){
                        searchList.add(contactlist.get(i));
                    }
                }else if(TextUtils.isEmpty(contactlist.get(i).getUser_name()) && !TextUtils.isEmpty(contactlist.get(i).getReal_name())){
                    if(contactlist.get(i).getReal_name().indexOf(activity_person_list_et_select.getText().toString()) != -1){
                        searchList.add(contactlist.get(i));
                    }
                }

            }
            setAdapter(searchList);
        }
    }
}
