package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CrmCustomSeaModule;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.widget.CustomCrmItem;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author juxinhua
 * 潜在客户公海-客户公海详情
 */
public class PotentialOpenSeaDetailActivity extends BaseActivity {

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    //客户名称
    @BindView(R.id.open_sea_detail_name)
    CustomCrmItem open_sea_detail_name;

    //联系方式
    @BindView(R.id.open_sea_detail_phone)
    CustomCrmItem open_sea_detail_phone;

    //跟进时间
    @BindView(R.id.open_sea_detail_time)
    CustomCrmItem open_sea_detail_time;

    //跟进情况
    @BindView(R.id.open_sea_detail_case)
    CustomCrmItem open_sea_detail_case;

    //跟进人
    @BindView(R.id.open_sea_detail_person)
    CustomCrmItem open_sea_detail_person;

    //跟进方式
    @BindView(R.id.open_sea_detail_way)
    CustomCrmItem open_sea_detail_way;
    private CrmCustomSeaModule.Data.CustomSea mCustom;//客户module


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_open_sea_detail);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    //获取携带数据
    private void getExtras(){
        mCustom = (CrmCustomSeaModule.Data.CustomSea) getIntent().getSerializableExtra("custom");
        if(mCustom==null){
            MsgUtil.shortToastInCenter(this,"请重试!");
            onBackPressed();
        }
    }

    //初始化界面数据
    private void initView(){
        tv_edit.setVisibility(View.VISIBLE);
        tv_edit.setText("转存");
        tv_title.setText("客户公海详情");
        setTitle();
        setSingleLine();
        setAllKeyDescVisible();
        setRightVisible();
        setEditTextVisible();
        setTextViewVisible();
        initData();//将客户数据展现出来
    }

    private void initData(){
        try {
            setText(mCustom.getName(),open_sea_detail_name);//客户名称
            setText(mCustom.getPhone(),open_sea_detail_phone);//联系方式
            setText(mCustom.getFollow_time(),open_sea_detail_time);//跟进时间
            setText(mCustom.getFollow_situation(),open_sea_detail_case);//跟进情况
            setText(mCustom.getFollow_user(),open_sea_detail_person);//跟进人
            setText(getResources().getStringArray(R.array.crm_custom_follow_way)[Integer.parseInt(mCustom.getFollow_type())],open_sea_detail_way);//跟进方式
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setText(String text,CustomCrmItem item){
        if(!TextUtils.isEmpty(text)){
            item.setValue(text);
        }else{
            item.setHintValue("未填写");
        }
    }

    //设置每一项的标题
    private void setTitle(){
        open_sea_detail_name.setTitle("客户名称");
        open_sea_detail_phone.setTitle("联系方式");
        open_sea_detail_time.setTitle("跟进时间");
        open_sea_detail_case.setTitle("跟进情况");
        open_sea_detail_person.setTitle("跟进人");
        open_sea_detail_way.setTitle("跟进方式");
    }

    private void setSingleLine(){
        open_sea_detail_name.setEditSingleLine();
        open_sea_detail_phone.setEditSingleLine();
        open_sea_detail_time.setEditSingleLine();
        open_sea_detail_case.setEditSingleLine();
        open_sea_detail_person.setEditSingleLine();
        open_sea_detail_way.setEditSingleLine();

        open_sea_detail_name.setValueSingle();
        open_sea_detail_phone.setValueSingle();
        open_sea_detail_time.setValueSingle();
        open_sea_detail_case.setValueSingle();
        open_sea_detail_person.setValueSingle();
        open_sea_detail_way.setValueSingle();
        CustomCrmItem[] itemArr = {open_sea_detail_name,open_sea_detail_phone,open_sea_detail_time,
                open_sea_detail_case,open_sea_detail_person,open_sea_detail_way};
        for(int i = 0;i<itemArr.length;i++){
            setItemSingleLine(itemArr[i]);
        }
        itemArr = null;
    }

    private void setItemSingleLine(CustomCrmItem item){
        item.setEditSingleLine();
        item.setValueSingle();
    }

    //设置标题后的小红点全部不显示
    private void setAllKeyDescVisible(){
        open_sea_detail_name.setKeyDescVisible(false);
        open_sea_detail_phone.setKeyDescVisible(false);
        open_sea_detail_time.setKeyDescVisible(false);
        open_sea_detail_case.setKeyDescVisible(false);
        open_sea_detail_person.setKeyDescVisible(false);
        open_sea_detail_way.setKeyDescVisible(false);
    }

    //设置右边的箭头全部为不可见
    private void setRightVisible(){
        open_sea_detail_name.setRightVisible(false);
        open_sea_detail_phone.setRightVisible(false);
        open_sea_detail_time.setRightVisible(false);
        open_sea_detail_case.setRightVisible(false);
        open_sea_detail_person.setRightVisible(false);
        open_sea_detail_way.setRightVisible(false);
    }

    //设置右边编辑框全部为不可见
    private void setEditTextVisible(){
        open_sea_detail_name.setEditVisible(false);
        open_sea_detail_phone.setEditVisible(false);
        open_sea_detail_time.setEditVisible(false);
        open_sea_detail_case.setEditVisible(false);
        open_sea_detail_person.setEditVisible(false);
        open_sea_detail_way.setEditVisible(false);
    }

    //设置右边ETextView全部为可见
    private void setTextViewVisible(){
        open_sea_detail_name.setValueVisible(true);
        open_sea_detail_phone.setValueVisible(true);
        open_sea_detail_time.setValueVisible(true);
        open_sea_detail_case.setValueVisible(true);
        open_sea_detail_person.setValueVisible(true);
        open_sea_detail_way.setValueVisible(true);
    }

    @OnClick({R.id.tv_edit,R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.tv_edit:
                //转存
                Intent i = new Intent(this,CrmCustomuploadingActivity.class);
                i.putExtra("id",mCustom.getId());
                startActivityForResult(i,101);
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == 101){
            onBackPressed();
        }
    }
}
