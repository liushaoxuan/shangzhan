package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.BusinessCardInfo;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.wheeldate.CustomTextPicker;
import com.wyu.iwork.widget.CustomCrmItem;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


public class CrmCustomDetailActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    private static final String TAG = CrmCustomDetailActivity.class.getSimpleName();
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

    private String type;

    private static final String TYPE_NEW = "NEW";//新建
    private static final String TYPE_EDIT = "EDIT";//编辑
    private static final String TYPE_BROWSE = "BROWSE";//浏览详情
    private static final String TYPE_NEW_RECT = "NEW_RECT";//该类型代表是从名片拍照页面传递过来的

    private static final String TYPE_NEW_POTENTIAL = "POTENTIAL_NEW";//潜在客户-新建
    private static final String TYPE_EDIT_POTENTIAL = "POTENTIAL_EDIT";//潜在客户-编辑
    private static final String TYPE_BROWSE_POTENTIAL = "POTENTIAL_BROWSE";//潜在客户-浏览详情
    private static final String TYPE_NEW_POTENTIAL_RECT = "POTENTIAL_NEW_RECT";//该类型代表是从名片拍照页面传递过来的

    private InputMethodManager imm;
    private CrmCustom.Custom custom;//客户实体类
    private BusinessCardInfo info;//拍照上传后返回的信息

    private static final String F = Constant.F;
    private static final String V = Constant.V;
    private String RandStr;
    private String Sign;

    //private LoadingDialog mLoadingDialog;
    private PickerViewDialog addrPicker;//地址弹窗

    private String province ;//省
    private String city ;//市
    private String country;//区
    private String predict_date;
    private CustomTextPicker mPicker;


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
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(TYPE_BROWSE.equals(type) || TYPE_BROWSE_POTENTIAL.equals(type)){
            custom = (CrmCustom.Custom) intent.getSerializableExtra("CUSTOM");
        }
        if(TYPE_NEW_POTENTIAL_RECT.equals(type) || TYPE_NEW_RECT.equals(type)){
            if(TYPE_NEW_POTENTIAL_RECT.equals(type)){
                type = TYPE_NEW_POTENTIAL;
            }else if(TYPE_NEW_RECT.equals(type)){
                type = TYPE_NEW;
            }
            info = (BusinessCardInfo) intent.getSerializableExtra("info");
            initInfoView();//将卡片扫描的数据填充到数据界面表单中去
        }
        //mLoadingDialog = new LoadingDialog();
        showCustomAddressDialog();//初始化地址弹窗
        initView();
    }

    private void initInfoView(){
        /**
         *  [user_name] => 姓名
         [phone] => 手机号
         [email] => 邮箱
         [wechat] => 微信号
         [position] => 职位
         [company_name] => 公司名称
         [address] => 地址
         [card_img] => 名片绝对地址
         */
        /**
         * crm_custom_name.setEditText(info.getData().getUser_name());
         crm_custom_phone.setEditText(info.getData().getPhone());
         crm_custom_mail.setEditText(info.getData().getEmail());
         */
        setText(info.getData().getUser_name(),crm_custom_name);
        setText(info.getData().getPhone(),crm_custom_phone);
        setText(info.getData().getEmail(),crm_custom_mail);
    }

    //初始化界面
    private void initView(){

        setTypeView();//根据当前的类型设置界面上的一些不同点  如标题

        setItemTitle();//设置每个item的标题

        setRightMustGone();//设置如下这些item的右边的小箭头一定不显示

        setKeyMustGone();//设置如下这些item的必填选项提示一定不显示

        setItemBottemLineMustGone();//设置部分item的底部横线一定不显示

        setInputType();//特殊条目设置输入类型

        //当前为客户类型时，隐藏客户状态  预计成交金额 预计成交日期三个条目
        if(TYPE_NEW.equals(type) || TYPE_EDIT.equals(type) || TYPE_BROWSE.equals(type)){
            crm_custom_state.setVisibility(View.GONE);
            crm_custom_money.setVisibility(View.GONE);
            crm_custom_date.setVisibility(View.GONE);
        }
        ll_back.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        crm_custom_edit.setOnClickListener(this);

        //设置网址项的最大行数为2行
        setMaxline();

        if(TYPE_BROWSE.equals(type) || TYPE_BROWSE_POTENTIAL.equals(type)){
            //浏览详情
            setAllKeyDescVisible(false);
            setAllRightVisible(false);
            setAllTextViewValueVisible(true);
            //捕获异常  防止数据错误导致程序崩溃
            try {
                initData();//初始化界面数据
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            //新建或编辑
            setAllKeyDescVisible(true);
            setAllRightVisible(true);
            setAllTextViewValueVisible(false);
            //设置特殊提示信息
            if(custom != null && TextUtils.isEmpty(custom.getPredict_date())){
                crm_custom_date.setHintValue("请选择");
            }
        }

        activity_crm_custom_detail_scrollview.setOnTouchListener(this);
    }

    private void setMaxline(){
        //设置网址项的最大行数为2行
        //crm_custom_website.setEditMaxLine(2);
        //crm_custom_website.setValueMaxLine(2);

        CustomCrmItem[] itemArr = {crm_custom_name,crm_custom_resource,crm_custom_level,
                crm_custom_state,crm_custom_phone,crm_custom_address,crm_custom_detail_address,
                crm_custom_fax,crm_custom_mail,crm_custom_money,crm_custom_date,crm_custom_website,crm_custom_followup,crm_custom_remark};
        for(int i = 0;i<itemArr.length;i++){
            setItemSingleLine(itemArr[i]);
        }
        itemArr = null;

    }

    private void setItemSingleLine(CustomCrmItem item){
        item.setValueSingle();
        item.setEditSingleLine();
    }

    private void setInputType(){
        crm_custom_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        crm_custom_fax.setDiaitals();
        crm_custom_mail.setDiaitals();
        crm_custom_website.setDiaitals();
    }

    //根据当前的类型设置界面上的一些不同点  如标题
    private void setTypeView(){
        if(TYPE_NEW.equals(type)){
            //新建客户状态
            tv_title.setText("新建客户");
            tv_edit.setVisibility(View.GONE);
            crm_custom_edit.setText("提交");
        }
        if(TYPE_NEW_POTENTIAL.equals(type)){
            //新建潜在客户状态
            tv_title.setText("新建潜在客户");
            tv_edit.setVisibility(View.GONE);
            crm_custom_edit.setText("提交");
        }
        if(TYPE_BROWSE.equals(type)){
            //浏览客户详情状态
            tv_title.setText("客户详情");
            tv_edit.setVisibility(View.VISIBLE);
            tv_edit.setText("删除");
            crm_custom_edit.setText("编辑");
        }
        if(TYPE_BROWSE_POTENTIAL.equals(type)){
            //浏览潜在客户详情状态
            tv_title.setText("潜在客户详情");
            tv_edit.setVisibility(View.VISIBLE);
            tv_edit.setText("删除");
            crm_custom_edit.setText("编辑");
        }
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
    private void initData(){
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
        if(!TextUtils.isEmpty(custom.getStatus())){
            setText(stateArr[Integer.parseInt(custom.getStatus())-1],items[3]);
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

    @OnClick({R.id.crm_custom_resource,R.id.crm_custom_level,
            R.id.crm_custom_state,R.id.crm_custom_address,R.id.crm_custom_date,
            R.id.crm_custom_edit})
    void Click(View v){
        //若当前为浏览状态  则不作点击处理
        if(TYPE_BROWSE.equals(type) || TYPE_BROWSE_POTENTIAL.equals(type)){
            return;
        }
        switch (v.getId()){
            case R.id.crm_custom_resource:
                //客户来源
                //从arrays.xml中获取数据
                Hideinputwindown(crm_custom_resource.getEditText());
                showCustomResourceDialog(getResources().getStringArray(R.array.crm_custom_resource),crm_custom_resource);
                break;
            case R.id.crm_custom_level:
                //客户等级
                Hideinputwindown(crm_custom_level.getEditText());
                showCustomResourceDialog(getResources().getStringArray(R.array.crm_custom_level),crm_custom_level);
                break;
            case R.id.crm_custom_state:
                //客户状态
                Hideinputwindown(crm_custom_state.getEditText());
                showCustomResourceDialog(getResources().getStringArray(R.array.crm_custom_state),crm_custom_state);
                break;
            case R.id.crm_custom_address:
                //地址
                Hideinputwindown(crm_custom_address.getEditText());
                if (addrPicker!=null){
                    addrPicker.show_cityPicker();
                }
                break;
            case R.id.crm_custom_date:
                //预计成交日期
                showCrmTimeDialog();
                break;
        }
    }

    //初始化地址弹窗
    private void showCustomAddressDialog(){

        addrPicker = new PickerViewDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                province =  addrPicker.options1Items.get(options1).getPickerViewText();
                city =  addrPicker.options2Items.get(options1).get(options2);
                country =  addrPicker.options3Items.get(options1).get(options2).get(options3);
                Logger.i(TAG,"OPTIONS1="+province+"options2="+city+"options3="+country);
                crm_custom_address.setValue(province+city+country);
            }
        });
    }

    //显示时间弹窗选择日期
    private void showCrmTimeDialog(){
        new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Logger.i(TAG,"DATE="+getTime(date));
                predict_date = getTime(date);
                crm_custom_date.setValue(getTime(date));
            }
        }).show_timepicker();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //显示客户来源选项弹框
    private void showCustomResourceDialog(String[] arr, final CustomCrmItem item){
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
                if(TYPE_EDIT_POTENTIAL.equals(type) || TYPE_EDIT.equals(type) || TYPE_NEW_POTENTIAL.equals(type) || TYPE_NEW.equals(type)){
                    postDatatoServer();
                }else{
                    if(TYPE_BROWSE.equals(type)){
                        type = TYPE_EDIT;
                    }else{
                        type = TYPE_EDIT_POTENTIAL;
                    }
                    crm_custom_edit.setText("提交");
                    initView();
                }
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                //删除该客户
                showDeleteCustomDialog();
                break;
        }

    }

    private void postDatatoServer(){

        //必填项为空时，则弹窗提醒
        if(TextUtils.isEmpty(crm_custom_name.getEditTextValue()) ||
                TextUtils.isEmpty(crm_custom_resource.getValue()) ||
                TextUtils.isEmpty(crm_custom_level.getValue()) ||
                TextUtils.isEmpty(crm_custom_phone.getEditTextValue()) ||
                TextUtils.isEmpty(crm_custom_address.getValue()) ||
                TextUtils.isEmpty(crm_custom_detail_address.getEditTextValue())){

            showRemindDialog();
            return;
        }

        //特殊状况    若当前为潜在客户状态时，还需要判断客户状态是否为空
        if(TYPE_EDIT_POTENTIAL.equals(type) || TYPE_NEW_POTENTIAL.equals(type)){
            if(TextUtils.isEmpty(crm_custom_state.getValue())){
                showRemindDialog();
                return;
            }
        }

        /**
         * user_id	是	int[11]用户ID
         name	是	string[50]客户名称
         type	是	string[50]0:潜在客户 1:客户
         customer_id	是	int[11]修改客户ID, 新增填0
         source	是	int[11]客户来源   0：其他 1：广告 2：推广 3：搜索引擎 4：转介绍 5：固有资源 6：线上注册 7：线上咨询 8：预约上门
         grade	是	int[11]客户等级  1：一般客户 2：普通客户 3：重要客户
         phone	是	int[11]联系电话
         address	是	string[50]详细地址 格式：上海市松江区江田东路185号
         fax	是	int[15]传真
         mail	是	string[50]邮箱
         url	是	string[50]网址
         remark	是	string[50]备注
         status	是	string[50]客户状态  潜在客户时填写 潜在客户时填写  客户状态 1：初步沟通 2：意向沟通 3：方案报价 4：签订成交 5：客户停滞
         predict_money	是	int[11]预计成交金额 单位：元  潜在客户时填写
         predict_date	是	datetime预计成交日期  格式：2017-02-19   潜在客户时填写
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.name.source)
         */

        //mLoadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        String customType = "";
        if(type.contains("POTENTIAL")){
            //潜在客户类型
            customType = "0";
        }else{
            //客户类型
            customType = "1";
        }
        String customer_id = "";
        if(type.contains("EDIT")){
            //编辑客户
            customer_id = custom.getId();
        }else{
            //新建客户
            customer_id = "0";
        }
        //循环取出item的值  除了客户来源 客户级别   客户状态  成交日期
        CustomCrmItem[] crmItems = {crm_custom_name,//客户名称
                crm_custom_phone,//联系方式
                crm_custom_detail_address,//详细地址
                crm_custom_fax,//传真
                crm_custom_mail,//邮箱
                crm_custom_website,//网站
                crm_custom_money,//预计成交金额
                crm_custom_followup,//跟进人
                crm_custom_remark};//备注
        ArrayList<String> valueList = new ArrayList<>();
        ArrayList<String> keyValue = new ArrayList<>();
        keyValue.add("name");
        keyValue.add("phone");
        keyValue.add("address");
        keyValue.add("fax");
        keyValue.add("mail");
        keyValue.add("url");
        keyValue.add("predict_money");
        keyValue.add("follow_user");
        keyValue.add("remark");
        for(int i = 0;i<crmItems.length;i++){
            if(TextUtils.isEmpty(crmItems[i].getEditTextValue())){
                valueList.add("");
            }else{
                valueList.add(crmItems[i].getEditTextValue());
            }
        }

        //处理特殊item 包括客户来源 客户级别 客户状态 预计成交日期

        //客户来源
        String source = "";
        String[] resourceArr = getResources().getStringArray(R.array.crm_custom_resource);
        for(int i = 0;i < resourceArr.length;i++){
            if(resourceArr[i].equals(crm_custom_resource.getValue())){
                source = i+"";
            }
        }
        //客户级别
        String grade = "";
        String[] levelArr = getResources().getStringArray(R.array.crm_custom_level);
        for(int i = 0;i < levelArr.length;i++){
            if(levelArr[i].equals(crm_custom_level.getValue())){
                grade = i+1+"";
            }
        }
        //客户状态
        String status = "";
        String[] stateArr = getResources().getStringArray(R.array.crm_custom_state);
        for(int i = 0;i < stateArr.length;i++){
            if(stateArr[i].equals(crm_custom_state.getValue())){
                status = i+1+"";
            }
        }
        //预计成交日期
        HashMap<String,String> data = new HashMap<>();
        /**
         * user_id	是	int[11]用户ID   1
         name	是	string[50]客户名称    1
         type	是	string[50]0:潜在客户 1:客户   1
         customer_id	是	int[11]修改客户ID, 新增填0   1
         source	是	int[11]客户来源   0：其他 1：广告 2：推广 3：搜索引擎 4：转介绍 5：固有资源 6：线上注册 7：线上咨询 8：预约上门   1
         grade	是	int[11]客户等级  1：一般客户 2：普通客户 3：重要客户   1
         phone	是	int[11]联系电话  1
         address	是	string[50]详细地址 格式：上海市松江区江田东路185号  1
         fax	是	int[15]传真  1
         mail	是	string[50]邮箱  1
         url	是	string[50]网址  1
         remark	是	string[50]备注  1
         status	是	string[50]客户状态  潜在客户时填写 潜在客户时填写  客户状态 1：初步沟通 2：意向沟通 3：方案报价 4：签订成交 5：客户停滞   1
         predict_money	是	int[11]预计成交金额 单位：元  潜在客户时填写  1
         predict_date	是	datetime预计成交日期  格式：2017-02-19   潜在客户时填写  1
         F	是	string[18]请求来源：IOS/ANDROID/WEB  1
         V	是	string[20]版本号如：1.0.1  1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign 请求加密值 F_moffice_encode(F.V.RandStr.user_id.name.source)
         */
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("type",customType);
        data.put("customer_id",customer_id);
        data.put("source",source);
        data.put("grade",grade);
        if(type.contains("POTENTIAL")){
            data.put("status",status);
        }
        for(int i = 0;i<crmItems.length;i++){
            if(!TextUtils.isEmpty(valueList.get(i))){
                data.put(keyValue.get(i),valueList.get(i));
            }
        }
        if(type.contains("POTENTIAL")){
            if(!TextUtils.isEmpty(predict_date)){
                data.put("predict_date",predict_date);
            }
        }
        data.put("F",F);
        data.put("V",V);
        RandStr = CustomUtils.getRandStr();
        Sign = Md5Util.getSign(F+V+ RandStr + AppManager.getInstance(this).getUserInfo().getUser_id()+valueList.get(0)+source);
        data.put("RandStr", RandStr);
        data.put("Sign", Sign);
        /**
         * province	是	string[50]省
         city	是	string[50]市
         district	是	string[50]区
         */
        data.put("province",province);
        data.put("city",city);
        data.put("district",country);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CUSTOMER,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        //mLoadingDialog.dismiss();
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                finish();
                            }else{
                                MsgUtil.shortToastInCenter(CrmCustomDetailActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        //释放资源
        if(crmItems != null){
            crmItems = null;
        }
        if(valueList != null){
            valueList.clear();
            valueList = null;
        }
        if(keyValue != null){
            keyValue.clear();
            keyValue = null;
        }
        if(data != null){
            data.clear();
            data = null;
        }
    }

    //显示消息没有填完整的提示框
    private void showRemindDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, null, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    //使得在触摸到滑动区域时，键盘隐藏
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_crm_custom_detail_scrollview);
        return false;
    }

    private void showDeleteCustomDialog(){
        new MyCustomDialogDialog(6, this, R.style.MyDialog, null, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                //取消删除
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                //确定删除
                deleteCustom(custom.getId());
                dialog.dismiss();
            }
        }).show();
    }

    //删除客户
    private void deleteCustom(String customer_id){
        /**
         * user_id	是	int[11] 用户ID
         customer_id	是	int[11] 客户/潜在客户ID
         F		string[18] 请求来源：IOS/ANDROID/WEB
         V	是	string[20] 版本号如：1.0.1
         RandStr	是	string[50] 请求加密随机数 time().|.rand()
         Sign	是	string[400] 请求加密值 F_moffice_encode(F.V.RandStr.user_id.customer_id)
         */
        RandStr = CustomUtils.getRandStr();
        Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+customer_id);
        OkGo.get(Constant.URL_DELETE_CUSTOMER).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("customer_id",customer_id)
                .params("F",F)
                .params("V",V)
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
                                finish();
                            }else{
                                MsgUtil.shortToastInCenter(CrmCustomDetailActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

    }
}
