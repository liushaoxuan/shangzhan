package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CustomDetail;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.view.dialog.LoadingDialog;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.wheeldate.CustomTextPicker;
import com.wyu.iwork.widget.CustomCrmItem;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 潜在客户公海转存潜在客户
 */

public class CrmCustomuploadingActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    private static final String TAG = CrmCustomuploadingActivity.class.getSimpleName();
    //客户名称
    @BindView(R.id.crm_custom_name)
    CustomCrmItem crm_custom_name;

    //客户来源
    @BindView(R.id.crm_custom_resource)
    CustomCrmItem crm_custom_resource;

    //客户级别
    @BindView(R.id.crm_custom_level)
    CustomCrmItem crm_custom_level;

    //客户状态
    @BindView(R.id.crm_custom_state)
    CustomCrmItem crm_custom_state;

    //联系电话
    @BindView(R.id.crm_custom_phone)
    CustomCrmItem crm_custom_phone;

    //地址
    @BindView(R.id.crm_custom_address)
    CustomCrmItem crm_custom_address;

    //详细地址
    @BindView(R.id.crm_custom_detail_address)
    CustomCrmItem crm_custom_detail_address;

    //传真
    @BindView(R.id.crm_custom_fax)
    CustomCrmItem crm_custom_fax;

    //邮箱
    @BindView(R.id.crm_custom_mail)
    CustomCrmItem crm_custom_mail;

    //网址
    @BindView(R.id.crm_custom_website)
    CustomCrmItem crm_custom_website;

    //预计成交金额
    @BindView(R.id.crm_custom_money)
    CustomCrmItem crm_custom_money;

    //预计成交日期
    @BindView(R.id.crm_custom_date)
    CustomCrmItem crm_custom_date;

    //跟进人
    @BindView(R.id.crm_custom_followup)
    CustomCrmItem crm_custom_followup;

    //备注
    @BindView(R.id.crm_custom_remark)
    CustomCrmItem crm_custom_remark;

    //编辑
    @BindView(R.id.crm_custom_edit)
    TextView crm_custom_edit;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.activity_crm_custom_detail_scrollview)
    ScrollView activity_crm_custom_detail_scrollview;

    private String id;

    private LoadingDialog mLoadingDialog;
    private PickerViewDialog addrPicker;//地址弹窗

    private String province ;//省
    private String city ;//市
    private String country;//区
    private String predict_date;
    private CustomTextPicker mPicker;
    private Gson gson;
    private CustomDetail detail;
    private int type;


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_custom_detail);
        ButterKnife.bind(this);
        hideToolbar();
        getExtra();
    }

    //获取传递的信息  包括type 以及浏览详情是传递过来的对象
    private void getExtra(){
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",0);
        if(101 == type){
            crm_custom_edit.setVisibility(View.GONE);
            crm_custom_state.setVisibility(View.GONE);
        }
        getCustomDataFromServer(id);
        mLoadingDialog = new LoadingDialog();
        initView();
    }

    private void getCustomDataFromServer(String id){
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+id);
        OkGo.get(Constant.URL_GET_CUSTOMER_DETAIL).tag(this)
                .cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("customer_id",id)
                .params("F",Constant.F)
                .params("V",Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseCustomData(s);
                    }
                });
    }

    private void parseCustomData(String s){
        if(gson == null){
            gson = new Gson();
        }
        try {
            detail = gson.fromJson(s,CustomDetail.class);
            if("0".equals(detail.getCode())){
                //设置数据
                initData(detail.getData());
            }else{
                MsgUtil.shortToastInCenter(CrmCustomuploadingActivity.this,"请重试!");
                onBackPressed();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //初始化界面
    private void initView(){

        setTypeView();//根据当前的类型设置界面上的一些不同点  如标题

        setItemTitle();//设置每个item的标题

        setRightMustGone();//设置如下这些item的右边的小箭头一定不显示

        setKeyMustGone();//设置如下这些item的必填选项提示一定不显示

        setItemBottemLineMustGone();//设置部分item的底部横线一定不显示

        setSingleLine();

        ll_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        crm_custom_edit.setOnClickListener(this);

        //浏览状态
        setAllKeyDescVisible(false);
        setAllRightVisible(false);
        setAllTextViewValueVisible(true);

        activity_crm_custom_detail_scrollview.setOnTouchListener(this);
    }

    //根据当前的类型设置界面上的一些不同点  如标题
    private void setTypeView(){
        //新建客户状态
        tv_title.setText("潜在客户详情");
        tv_edit.setVisibility(View.GONE);
        crm_custom_edit.setText("保存");
    }

    //设置每个item的标题
    private void setItemTitle(){
        crm_custom_name.setTitle("客户名称");
        crm_custom_resource.setTitle("客户来源");
        crm_custom_level.setTitle("客户级别");
        crm_custom_state.setTitle("客户状态");
        crm_custom_phone.setTitle("联系电话");
        crm_custom_address.setTitle("地址");
        crm_custom_detail_address.setTitle("详细地址");
        crm_custom_fax.setTitle("传真");
        crm_custom_mail.setTitle("邮箱");
        crm_custom_website.setTitle("网址");
        crm_custom_money.setTitle("预计成交金额");
        crm_custom_date.setTitle("预计成交日期");
        crm_custom_followup.setTitle("跟进人");
        crm_custom_remark.setTitle("备注");
    }

    private void setSingleLine(){

        //设置网址项的最大行数为2行
        crm_custom_website.setEditMaxLine(2);
        crm_custom_website.setValueMaxLine(2);

        CustomCrmItem[] itemArr = {crm_custom_name,crm_custom_resource,crm_custom_level,crm_custom_state,
                crm_custom_phone,crm_custom_address,crm_custom_detail_address,crm_custom_fax,
                crm_custom_mail,crm_custom_website,crm_custom_money,crm_custom_date,crm_custom_followup,
                crm_custom_remark};
        for(int i = 0;i<itemArr.length;i++){
            setItemSingleLine(itemArr[i]);
        }
        itemArr = null;
    }

    private void setItemSingleLine(CustomCrmItem item){
        item.setValueSingle();
        item.setEditSingleLine();
    }

    //设置部分item的右边小箭头一定不显示
    private void setRightMustGone(){
        crm_custom_name.setRightVisible(false);
        crm_custom_phone.setRightVisible(false);
        crm_custom_detail_address.setRightVisible(false);
        crm_custom_fax.setRightVisible(false);
        crm_custom_mail.setRightVisible(false);
        crm_custom_website.setRightVisible(false);
        crm_custom_money.setRightVisible(false);
        crm_custom_followup.setRightVisible(false);
        crm_custom_remark.setRightVisible(false);
    }

    //设置部分item的不填选项提示一定不显示
    private void setKeyMustGone(){
        //设置如下这些item的必填选项提示一定不显示
        crm_custom_fax.setKeyDescVisible(false);
        crm_custom_mail.setKeyDescVisible(false);
        crm_custom_website.setKeyDescVisible(false);
        crm_custom_money.setKeyDescVisible(false);
        crm_custom_date.setKeyDescVisible(false);
        crm_custom_followup.setKeyDescVisible(false);
        crm_custom_remark.setKeyDescVisible(false);
    }

    //设置部分item的底部横线一定不显示
    private void setItemBottemLineMustGone(){
        //设置item的底部横线是否显示
        crm_custom_state.setBottomLineVisible(false);
        crm_custom_website.setBottomLineVisible(false);
        crm_custom_date.setBottomLineVisible(false);
        crm_custom_remark.setBottomLineVisible(false);
    }

    //初始化界面数据
    private void initData(CustomDetail.Detail custom){
        province = custom.getProvince();
        city = custom.getCity();
        country = custom.getDistrict();
        predict_date = custom.getPredict_date();
        CustomCrmItem[] items = {crm_custom_name,crm_custom_resource,crm_custom_level,crm_custom_state,crm_custom_phone,crm_custom_address,
                crm_custom_detail_address,crm_custom_fax,crm_custom_mail,crm_custom_website,crm_custom_money,crm_custom_date,crm_custom_followup,crm_custom_remark};
        String[] resourceArr = getResources().getStringArray(R.array.crm_custom_resource);
        String[] levelArr = getResources().getStringArray(R.array.crm_custom_level);
        String[] stateArr = getResources().getStringArray(R.array.crm_custom_state);
        setText(custom.getName(),items[0]);
        if(!TextUtils.isEmpty(custom.getSource())){
            setText(resourceArr[Integer.parseInt(custom.getSource())],items[1]);
        }
        if(!TextUtils.isEmpty(custom.getGrade())){
            setText(levelArr[Integer.parseInt(custom.getGrade())-1],items[2]);
        }
        if(type != 101){
            if(!TextUtils.isEmpty(custom.getStatus())){
                setText(stateArr[Integer.parseInt(custom.getStatus())-1],items[3]);
            }
        }
        setText(custom.getPhone(),items[4]);
        setText(province+city+country,items[5]);
        setText(custom.getAddress(),items[6]);
        setText(custom.getFax(),items[7]);
        setText(custom.getMail(),items[8]);
        setText(custom.getUrl(),items[9]);
        setText(custom.getPredict_money(),items[10]);
        setText(custom.getPredict_date(),items[11]);
        setText(custom.getFollow_user(),items[12]);
        setText(custom.getRemark(),items[13]);

        //手动释放资源
        items = null;
    }

    private void setText(String text,CustomCrmItem item){
        if(!TextUtils.isEmpty(text)){
            item.setValue(text);
            item.setEditText(text);
        }else{
            item.setHintValue("未填写");
            item.setHintText("请输入");
        }
    }


    //当状态为新建或编辑时   需要显示这些必填提示   当状态为浏览时，则隐藏
    private void setAllKeyDescVisible(boolean flag){
        crm_custom_name.setKeyDescVisible(flag);
        crm_custom_resource.setKeyDescVisible(flag);
        crm_custom_level.setKeyDescVisible(flag);
        crm_custom_state.setKeyDescVisible(flag);
        crm_custom_phone.setKeyDescVisible(flag);
        crm_custom_address.setKeyDescVisible(flag);
        crm_custom_detail_address.setKeyDescVisible(flag);
    }

    //当状态为新建或编辑是  则显示右边的箭头   当状态为浏览信息是，则隐藏
    private void setAllRightVisible(boolean flag){
        crm_custom_resource.setRightVisible(flag);
        crm_custom_level.setRightVisible(flag);
        crm_custom_state.setRightVisible(flag);
        crm_custom_address.setRightVisible(flag);
        crm_custom_date.setRightVisible(flag);
    }

    private void setAllTextViewValueVisible(boolean flag){

        //1 浏览   全部显示textview   2：新建或编辑状态：部分显示
        crm_custom_name.setValueVisible(flag);
        crm_custom_resource.setValueVisible(true);
        crm_custom_level.setValueVisible(true);
        crm_custom_state.setValueVisible(true);
        crm_custom_phone.setValueVisible(flag);
        crm_custom_address.setValueVisible(true);
        crm_custom_detail_address.setValueVisible(flag);
        crm_custom_fax.setValueVisible(flag);
        crm_custom_mail.setValueVisible(flag);
        crm_custom_website.setValueVisible(flag);
        crm_custom_money.setValueVisible(flag);
        crm_custom_date.setValueVisible(true);
        crm_custom_followup.setValueVisible(flag);
        crm_custom_remark.setValueVisible(flag);

        crm_custom_name.setEditVisible(!flag);
        crm_custom_resource.setEditVisible(false);
        crm_custom_level.setEditVisible(false);
        crm_custom_state.setEditVisible(false);
        crm_custom_phone.setEditVisible(!flag);
        crm_custom_address.setEditVisible(false);
        crm_custom_detail_address.setEditVisible(!flag);
        crm_custom_fax.setEditVisible(!flag);
        crm_custom_mail.setEditVisible(!flag);
        crm_custom_website.setEditVisible(!flag);
        crm_custom_money.setEditVisible(!flag);
        crm_custom_date.setEditVisible(false);
        crm_custom_followup.setEditVisible(!flag);
        crm_custom_remark.setEditVisible(!flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crm_custom_edit:
                transferCustom();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }

    }

    private void transferCustom(){
        /**
         * URL_CUSTOMER_TRANSFER
         * user_id	是	int[11]用户ID
         customer_id	是	int[2]潜在客户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+detail.getData().getId());
        OkGo.get(Constant.URL_CUSTOMER_TRANSFER).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("customer_id",detail.getData().getId())
                .params("F",Constant.F)
                .params("V",Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                Intent intent = new Intent(CrmCustomuploadingActivity.this,PotentialCustomerManagerActivity.class);
                                intent.putExtra("type","POTENTIAL");
                                startActivity(intent);
                                setResult(101);
                                onBackPressed();
                            }else{
                                MsgUtil.shortToastInCenter(CrmCustomuploadingActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    //使得在触摸到滑动区域时，键盘隐藏
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_crm_custom_detail_scrollview);
        return false;
    }
}
