package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.ContractDetail;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomCrmItem;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author juxinhua
 * crm - 合同详情 合同编辑  合同新建
 *
 */
public class CrmContractDetailActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    private static final String TAG = CrmContractDetailActivity.class.getSimpleName();
    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //编辑
    @BindView(R.id.tv_edit)
    TextView tv_edit;

    //客户名称
    @BindView(R.id.contract_custom_name)
    CustomCrmItem contract_custom_name;

    //合同标题
    @BindView(R.id.contract_title)
    CustomCrmItem contract_title;

    //合同金额
    @BindView(R.id.contract_money)
    CustomCrmItem contract_money;

    //合同编号
    @BindView(R.id.contract_number)
    CustomCrmItem contract_number;

    //合同开始时间
    @BindView(R.id.contract_start_time)
    CustomCrmItem contract_start_time;

    //合同结束时间
    @BindView(R.id.contract_end_time)
    CustomCrmItem contract_end_time;

    //跟进人
    @BindView(R.id.contract_follower)
    CustomCrmItem contract_follower;

    //备注
    @BindView(R.id.contract_remark)
    CustomCrmItem contract_remark;

    @BindView(R.id.contract_edit)
    TextView contract_edit;

    @BindView(R.id.contract_scrollview)
    ScrollView contract_scrollview;

    private static final String TYPE_EDIT = "EDIT";
    private static final String TYPE_NEW = "NEW";
    private static final String TYPE_BROWSER = "BROWSER";
    private String currentType;
    private String contractId;
    private Gson gson;
    private CrmCustom.Custom custom = null;

    private static final int REQUEST_CODE = 100;//在startActivityForResult时使用   请求码
    private static final int RESULT_CODE = 101;//在onActivityResult中使用    结果码
    private ContractDetail mDetail;
    private long startTime;//记录开始和结束时间  用来作比较
    private long endTime;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_contract_detail);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
        initClick();
    }

    //初始化部分点击事件
    private void initClick(){
        ll_back.setOnClickListener(this);
        contract_edit.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        contract_scrollview.setOnTouchListener(this);
    }

    private void getExtras(){
        Intent intent = getIntent();
        currentType = intent.getStringExtra("type");
        if(TYPE_BROWSER.equals(currentType)){
            contractId = intent.getStringExtra("id");
        }
        initItemTitle();
        setSingleLine();
        setItemBottemLineMustGone();
    }

    private void initView(){
        tv_edit.setText("删除");
        if(TYPE_BROWSER.equals(currentType)){
            //浏览详情
            tv_title.setText("合同详情");
            tv_edit.setVisibility(View.VISIBLE);
            setKeyVisible(false);
            setRightArrowVisible(false);
            setAllTextViewValueVisible(true);
            contract_edit.setText("编辑");
            getContractDetail();
        }else if(TYPE_NEW.equals(currentType)){
            //新建合同
            tv_title.setText("新建合同");
            tv_edit.setVisibility(View.GONE);
            setKeyVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisible(false);
            contract_edit.setText("提交");
        }else if(TYPE_EDIT.equals(currentType)){
            //编辑合同
            tv_title.setText("编辑合同详情");
            contract_edit.setText("提交");
            tv_edit.setVisibility(View.VISIBLE);
            setKeyVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisible(false);
        }
    }

    //获取合同详情
    private void getContractDetail(){
        /**
         * user_id	是	int[11]用户ID
         contract_id	是	int[11]合同ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.contract_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+contractId);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("contract_id",contractId);
        data.put("F", Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign", Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CONTRACT_DETAIL,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                             @Override
                             public void onSuccess(String s, Call call, Response response) {
                                 super.onSuccess(s,call,response);
                                 Logger.i(TAG,s);
                                parseData(s);
                             }
                         }
                );
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void parseData(String s){
        try {
            if(gson == null){
                gson = new Gson();
            }
            mDetail = gson.fromJson(s,ContractDetail.class);
            if("0".equals(mDetail.getCode())){
                if(mDetail.getData() != null){
                    setData(mDetail);
                }
            }else{
                MsgUtil.shortToastInCenter(this, mDetail.getMsg());
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setData(ContractDetail detail){
        setText(detail.getData().getCustomer_name(),contract_custom_name);
        setText(detail.getData().getTitle(),contract_title);
        setText(detail.getData().getValue(),contract_money);
        setText(detail.getData().getSole_number(),contract_number);
        setText(detail.getData().getStart_time(),contract_start_time);
        setText(detail.getData().getEnd_time(),contract_end_time);
        setText(detail.getData().getFollow_user(),contract_follower);
        setText(detail.getData().getRemark(),contract_remark);
        if(!TextUtils.isEmpty(detail.getData().getStart_time())){
            startTime = CustomUtils.strToDate(detail.getData().getStart_time()).getTime();
        }
        if(!TextUtils.isEmpty(detail.getData().getEnd_time())){
            endTime = CustomUtils.strToDate(detail.getData().getEnd_time()).getTime();
        }
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

    //初始化每一个item的标题
    private void initItemTitle(){
        contract_custom_name.setTitle("客户名称");
        contract_title.setTitle("合同标题");
        contract_money.setTitle("合同金额（￥）");
        contract_number.setTitle("合同编号");
        contract_start_time.setTitle("开始时间");
        contract_end_time.setTitle("结束时间");
        contract_follower.setTitle("跟进人");
        contract_remark.setTitle("备注");

        //设置某些item只能输入规定的字符
        contract_money.setInputType(8194);
        contract_number.setDiaitals();
    }

    private void setSingleLine(){
        CustomCrmItem[] itemArr = {contract_custom_name,contract_title,contract_money,contract_number,
                contract_start_time,contract_end_time,contract_follower,contract_remark};
        for(int i = 0;i<itemArr.length;i++){
            setItemSingleLine(itemArr[i]);
        }
        itemArr = null;
    }

    private void setItemSingleLine(CustomCrmItem item){
        item.setEditSingleLine();
        item.setValueSingle();
    }

    //设置必选项提示是否显示
    private void setKeyVisible(boolean flag){
        contract_custom_name.setKeyDescVisible(flag);
        contract_title.setKeyDescVisible(flag);
        contract_money.setKeyDescVisible(flag);
        contract_number.setKeyDescVisible(false);
        contract_start_time.setKeyDescVisible(false);
        contract_end_time.setKeyDescVisible(false);
        contract_follower.setKeyDescVisible(false);
        contract_remark.setKeyDescVisible(false);
    }

    //设置右边的箭头是否显示
    private void setRightArrowVisible(boolean flag){
        contract_custom_name.setRightVisible(flag);
        contract_title.setRightVisible(false);
        contract_money.setRightVisible(false);
        contract_number.setRightVisible(false);
        contract_start_time.setRightVisible(flag);
        contract_end_time.setRightVisible(flag);
        contract_follower.setRightVisible(false);
        contract_remark.setRightVisible(false);
    }

    //设置底部横线一定不显示
    private void setItemBottemLineMustGone(){
        contract_custom_name.setBottomLineVisible(false);
        contract_end_time.setBottomLineVisible(false);
        contract_remark.setBottomLineVisible(false);
    }

    private void setAllTextViewValueVisible(boolean flag){
        /**
         * 显示详情的时候  全部显示TextView
         * 若为编辑或者是新建合同是   则部分显示TextView
         */
        contract_custom_name.setValueVisible(true);
        contract_title.setValueVisible(flag);
        contract_money.setValueVisible(flag);
        contract_number.setValueVisible(flag);
        contract_start_time.setValueVisible(true);
        contract_end_time.setValueVisible(true);
        contract_follower.setValueVisible(flag);
        contract_remark.setValueVisible(flag);

        contract_custom_name.setEditVisible(false);
        contract_title.setEditVisible(!flag);
        contract_money.setEditVisible(!flag);
        contract_number.setEditVisible(!flag);
        contract_start_time.setEditVisible(false);
        contract_end_time.setEditVisible(false);
        contract_follower.setEditVisible(!flag);
        contract_remark.setEditVisible(!flag);
    }

    //显示时间弹窗选择日期
    private void showCrmTimeDialog(final CustomCrmItem item){
        new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if(item == contract_start_time){
                    startTime = date.getTime();
                }else if(item == contract_end_time){
                    endTime = date.getTime();
                }
                Logger.i(TAG,"DATE="+getTime(date));
                item.setValue(getTime(date));
            }
        }).show_timepicker();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @OnClick({R.id.contract_start_time,R.id.contract_end_time,R.id.contract_custom_name})
    void Click(View v){
        if(TYPE_BROWSER.equals(currentType)){
            return;
        }
        Hideinputwindown(contract_start_time.getEditText());
        switch (v.getId()){
            case R.id.contract_start_time:
                showCrmTimeDialog(contract_start_time);
                break;
            case R.id.contract_end_time:
                showCrmTimeDialog(contract_end_time);
                break;
            case R.id.contract_custom_name:
                Intent intent = new Intent(this,CrmPotentialFollowSelectCustomActivity.class);
                intent.putExtra("type","1");//代表添加潜在客户
                Bundle bundle = new Bundle();
                if(TYPE_EDIT.equals(currentType)){
                    if(custom == null){
                        CrmCustom crmCustom = new CrmCustom();
                        custom = crmCustom.new Custom();
                        custom.setId(mDetail.getData().getCustomer_id());
                    }
                }
                bundle.putSerializable("custom",custom);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.contract_edit:
                if(TYPE_BROWSER.equals(currentType)){
                    currentType = TYPE_EDIT;
                    initView();
                }else{
                    //MsgUtil.shortToastInCenter(this,"提交");
                    NewAndEditData();
                }
                break;
            case R.id.tv_edit:
                showDeleteDialog();
                break;
        }
    }

    /**
     * 删除合同
     */
    private void deleteContract(){
        /**URL_DELETE_CONTRACT
         * user_id	是	int[11]用户ID
         contract_id	是	int[11]合同ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.supplier_id)
         */
        String RandStr = CustomUtils.getRandStr();
        OkGo.get(Constant.URL_DELETE_CONTRACT).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("contract_id",contractId)
                .params("F",Constant.F)
                .params("V",Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",Md5Util.getSign(
                        Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+contractId))
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                onBackPressed();
                            }else{
                                MsgUtil.shortToastInCenter(CrmContractDetailActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    //添加 编辑详情
    private void NewAndEditData(){
        //判断必填项是否全部填写
        if(TextUtils.isEmpty(contract_custom_name.getValue()) ||
                TextUtils.isEmpty(contract_title.getEditTextValue()) ||
                TextUtils.isEmpty(contract_money.getEditTextValue())){
            showRedmineDialog();
            return;
        }
        //合同开始时间和结束时间比较
        if(!TextUtils.isEmpty(contract_start_time.getValue()) && !TextUtils.isEmpty(contract_end_time.getValue())){
            if(startTime > endTime){
                MsgUtil.shortToastInCenter(this,"合同开始时间不能晚于结束时间!");
                Logger.i(TAG,startTime+"   "+endTime);
                return;
            }
        }
        /**
         * user_id	是	int[11]       用户ID
         contract_id	是	int[11]       修改合同ID, 新增传0
         customer_id	是	int[11]       客户ID
         title	      是	string[50]    合同名称
         value	      是	string[50]    合同金额
         sole_number	否	string[50]    合同编号
         start_time	否	string[50]    开始时间 格式：2017-01-12
         end_time	      否	string[50]    结束时间 格式：2017-01-12
         follow_user	否	string[50]    跟进人
         remark	      否	string[50]    备注
         F	            是	string[18]    请求来源：IOS/ANDROID/WEB
         V	            是	string[20]    版本号如：1.0.1
         RandStr	      是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.contract_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        String contractId = "";
        if(TYPE_EDIT.equals(currentType)){
            contractId = mDetail.getData().getId();
        }else{
            contractId = "0";
        }
        data.put("contract_id",contractId);
        if(TYPE_NEW.equals(currentType)){
            /**
             * if(custom == null){
             data.put("customer_id",detail.getData().getCustomer_id());
             }else{
             data.put("customer_id",custom.getId());
             }
             */

            data.put("customer_id",custom.getId());
        }else{
            if(custom == null){
                data.put("customer_id",mDetail.getData().getCustomer_id());
            }else{
                data.put("customer_id",custom.getId());
            }
        }
        data.put("title",contract_title.getEditTextValue());
        data.put("value",contract_money.getEditTextValue());
        putHashMapData(data,"sole_number",contract_number,1);
        putHashMapData(data,"start_time",contract_start_time,2);
        putHashMapData(data,"end_time",contract_end_time,2);
        putHashMapData(data,"follow_user",contract_follower,1);
        putHashMapData(data,"remark",contract_remark,1);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        data.put("RandStr",RandStr);
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+contractId);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CONTRACT,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                onBackPressed();
                            }else{
                                MsgUtil.shortToastInCenter(CrmContractDetailActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void putHashMapData(HashMap<String,String> data,String key,CustomCrmItem item,int type){
        if(type == 1){
            if(!TextUtils.isEmpty(item.getEditTextValue())){
                data.put(key,item.getEditTextValue());
            }
        }else if(type == 2){
            if(!TextUtils.isEmpty(item.getValue())){
                data.put(key,item.getValue());
            }
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(){
        new MyCustomDialogDialog(6, this, R.style.MyDialog, "确定要删除该合同吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteContract();
                dialog.dismiss();
            }
        }).show();
    }

    //提醒弹窗
    private void showRedmineDialog(){
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

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            custom = (CrmCustom.Custom) data.getSerializableExtra("custom");
            contract_custom_name.setValue(custom.getName());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(contract_scrollview);
        return false;
    }
}
