package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CrmCustomListViewAdapter;
import com.wyu.iwork.model.MapCustomModule;
import com.wyu.iwork.util.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author juxinhua
 * 客户地图-搜索客户
 */
public class CrmCustomMapSearchActivity extends BaseActivity implements TextWatcher{

    private static final String TAG = CrmCustomMapSearchActivity.class.getSimpleName();
    //搜索栏
    @BindView(R.id.custom_map_search)
    EditText custom_map_search;

    //取消
    @BindView(R.id.custom_map_search_cancel)
    TextView custom_map_search_cancel;

    //listview
    @BindView(R.id.custom_map_search_listview)
    ListView custom_map_search_listview;

    //暂无布局
    @BindView(R.id.custom_map_search_zanwu)
    AutoLinearLayout custom_map_search_zanwu;
    private ArrayList<MapCustomModule.Custom> listAll;//待搜索的客户数据
    private ArrayList<MapCustomModule.Custom> list_search;//存放搜索到的客户数据
    private CrmCustomListViewAdapter adapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_custom_map_search);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        custom_map_search.addTextChangedListener(this);
        list_search = new ArrayList<>();
    }

    private void getExtras(){
        listAll = (ArrayList<MapCustomModule.Custom>) getIntent().getExtras().getSerializable("LIST");
        for(int i = 0; i< listAll.size(); i++){
            Logger.i(TAG, listAll.get(i).getName());
        }
    }

    //显示内容
    private void showContent(boolean flag){
        custom_map_search_listview.setVisibility(flag? View.VISIBLE:View.GONE);
        custom_map_search_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
    }

    //搜索栏修改前文本
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    //搜索栏问恩修改中状态
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    //搜索栏文本修改后状态
    @Override
    public void afterTextChanged(Editable s) {
        /**
         * 1:搜索栏无文本状态下  显示暂未搜索到任何信息
         * 2:搜索栏有搜索的文本 ：1：若匹配到相应的数据  则显示
         *                      2：若未匹配到数据  则显示暂未搜索到任何信息的布局
         */
        if(TextUtils.isEmpty(custom_map_search.getText().toString())){
            //搜索栏为空
            showContent(false);
        }else{
            //有搜索文本 遍历集合
            list_search.clear();
            if(listAll != null && listAll.size()>0){
                for(int i = 0;i<listAll.size();i++){
                    if(!TextUtils.isEmpty(listAll.get(i).getName())){
                        if(listAll.get(i).getName().indexOf(custom_map_search.getText().toString()) != -1){
                            list_search.add(listAll.get(i));
                        }
                    }
                }
                if(list_search.size()>0){
                    //有搜索结果
                    showContent(true);
                    setAdapter();
                }else{
                    //无搜索结果
                    showContent(false);
                }
            }else{
                showContent(false);
            }
        }
    }

    private void setAdapter(){
        if (adapter == null){
            adapter = new CrmCustomListViewAdapter(this,list_search,2);
            custom_map_search_listview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }

    }

    @OnClick({R.id.custom_map_search_cancel})
    void Click(View v){
        switch (v.getId()){
            case R.id.custom_map_search_cancel:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == 101){
            setResult(101);
            onBackPressed();
        }
    }
}
