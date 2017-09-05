package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.PersonAdapter;
import com.wyu.iwork.model.PersonModel;
import com.wyu.iwork.presenter.CompanyUserListPresenter;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.widget.MyListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedTaskActivity extends BaseNetActivity<List<PersonModel>> implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private static final String TAG = CompletedTaskActivity.class.getSimpleName();
    private EditText ed_searchView;
    private TextView tv_searchView;
    private MyListView lv_searchTask;
    private PersonAdapter personAdapter;
    private String tag;
    private int listTag=1;
    private ImageView img_selected;
    private ArrayList<PersonModel> list=new ArrayList<>();
    private PersonModel personModel;
    private CompanyUserListPresenter userListPresenter;
    private List<PersonModel> personList=new ArrayList<>();
    private List <PersonModel> searchList=new ArrayList<>();
    private ArrayList idList=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
        setBackNaviAction();
        getExter();
        init();
        ((TextView) setCustomViewForToolbar(R.layout.title_toolbar)).setText("所有人员");
        requestUserList();

    }


    private void requestUserList() {
        String companyID= AppManager.getInstance(this).getUserInfo().getCompany_id();
        setNetPresenter(userListPresenter=new CompanyUserListPresenter(this),TAG);
        userListPresenter=new CompanyUserListPresenter(this);
        userListPresenter.attachView(this);
        userListPresenter.doSignIn(companyID);
    }


    private void init() {
        tv_searchView=(TextView)findViewById(R.id.tv_searchView);
        ed_searchView=(EditText) findViewById(R.id.ed_searchView);
        lv_searchTask=(MyListView)findViewById(R.id.lv_searchTask);
        initListener();
    }

    private void initListener() {
        tv_searchView.setOnClickListener(this);
        ed_searchView.addTextChangedListener(this);
        lv_searchTask.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_searchView:
                initList();
                ed_searchView.setFocusableInTouchMode(true);
                ed_searchView.requestFocus();
                tv_searchView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(ed_searchView.getText().toString().isEmpty()){
            listTag=1;
            personAdapter=new PersonAdapter(personList,this);
            lv_searchTask.setAdapter(personAdapter);
            personAdapter.notifyDataSetChanged();
        }else{
            listTag=2;
            searchList.clear();
            for(int i=0;i<personList.size();i++){
                if(personList.get(i).getReal_name().indexOf(ed_searchView.getText().toString())!=-1||
                        personList.get(i).getDepartment().indexOf(ed_searchView.getText().toString())!=-1){
                    searchList.add(personList.get(i));
                }
            }
            Log.i(TAG,searchList.toString());
            personAdapter=new PersonAdapter(searchList,this);
            lv_searchTask.setAdapter(personAdapter);
            personAdapter.notifyDataSetChanged();
        }

    }

    public void getExter() {
        Intent it=getIntent();
        tag=it.getStringExtra("TAG");
        idList= it.getExtras().getStringArrayList("idList");
         Log.i("===idList===",idList.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if("EditTaskActivity".equals(tag)){
            img_selected=(ImageView)view.findViewById(R.id.item_selected);
            if (img_selected.isSelected()){
                img_selected.setSelected(false);
                for(int j=0;j<idList.size();j++) {
                    if (listTag == 1) {
                        if (personList.get(i).getId().equals(list.get(j).getId())) {
                            list.remove(j);
                        }
                        Map m = (Map) idList.get(j);
                        if (personList.get(i).getId().equals(m.get("id"))) {
                            idList.remove(j);
                        }
                        personList.get(i).setSelected(false);
                    }else {
                        if (searchList.get(i).getId().equals(list.get(j).getId())) {
                            list.remove(j);
                        }
                        Map m = (Map) idList.get(j);
                        if (searchList.get(i).getId().equals(m.get("id"))) {
                            idList.remove(j);
                        }
                        for(int k=0;k<personList.size();k++){
                            if(personList.get(k).getId().equals(searchList.get(i).getId())){
                                personList.get(k).setSelected(false);
                            }
                        }
                    }
                }
            }else{
                img_selected.setSelected(true);
                Map<String,String> map=new HashMap<>();
                if(listTag==1) {
                    personModel = new PersonModel(personList.get(i).getReal_name(), personList.get(i).getFace_img(),personList.get(i).getId());
                    map.put("id", personList.get(i).getId());
                    personList.get(i).setSelected(true);
                }else{
                    personModel = new PersonModel(searchList.get(i).getReal_name(), searchList.get(i).getFace_img(),personList.get(i).getId());
                    map.put("id", searchList.get(i).getId());
                    for(int j=0;j<personList.size();j++){
                        if(personList.get(j).getId().equals(searchList.get(i).getId())){
                            personList.get(j).setSelected(true);
                            Logger.e("--personList--",personList.get(i).getSelected()+"");
                        }
                    }
                }
                idList.add(map);
                list.add(personModel);
            }
            Logger.e("==idList==",idList.toString());
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        if("EditTaskActivity".equals(tag)){
            getMenuInflater().inflate(R.menu.complete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it=getIntent();
        Bundle b=new Bundle();
        b.putSerializable("personList",list);
        b.putStringArrayList("idList",idList);
        it.putExtras(b);
        setResult(101,it);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(List<PersonModel> data, JSONObject origin) {
        super.onSuccess(data, origin);
        personList=data;
        initList();
    }

    private void initList() {
        if(!idList.isEmpty()) {
            for (int i = 0; i < personList.size(); i++) {
                for (int j = 0; j < idList.size(); j++) {
                    Map<String, String> map = (Map<String, String>) idList.get(j);
                    if (map.get("id").equals(personList.get(i).getId())) {
                        Log.i("personList.get(i)",personList.get(i).getId());
                        personList.get(i).setSelected(true);
                        personModel = new PersonModel(personList.get(i).getReal_name(), personList.get(i).getFace_img(),personList.get(i).getId());
                        list.add(personModel);
                    }
                }
            }
        }
        personAdapter=new PersonAdapter(personList,this);
        lv_searchTask.setAdapter(personAdapter);
    }

}
