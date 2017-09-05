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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.model.CrmFollowCustom;
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
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class PotentialCustomDetailActivity extends BaseActivity implements View.OnClickListener,View.OnTouchListener{

    private static final String TAG = PotentialCustomDetailActivity.class.getSimpleName();
    //客户名称
    @BindView(R.id.crm_potential_name)
    CustomCrmItem crm_potential_name;

    //跟进方式
    @BindView(R.id.crm_potential_follow_way)
    CustomCrmItem crm_potential_follow_way;

    //跟进时间
    @BindView(R.id.crm_potential_follow_time)
    CustomCrmItem crm_potential_follow_time;

    //跟进情况
    @BindView(R.id.crm_potential_follow_case)
    CustomCrmItem crm_potential_follow_case;

    //需求反馈
    @BindView(R.id.crm_potential_need)
    CustomCrmItem crm_potential_need;

    //跟进人
    @BindView(R.id.crm_potential_follow_person)
    CustomCrmItem crm_potential_follow_person;

    //备注
    @BindView(R.id.crm_potential_remark)
    CustomCrmItem crm_potential_remark;

    //提交按钮
    @BindView(R.id.crm_potential_commit)
    TextView crm_potential_commit;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.activity_potential_custom_detail_scrollview)
    ScrollView activity_potential_custom_detail_scrollview;

    private static final String TYPE_BROWSER = "BROWSER";//查看详情状态
    private static final String TYPE_EDIT = "EDIT";//编辑状态
    private static final String TYPE_NEW = "NEW";//新建客户
    private String type = "";
    private static final int REQUEST_CODE = 100;//在startActivityForResult时使用   请求码
    private static final int RESULT_CODE = 101;//在onActivityResult中使用    结果码
    private CrmCustom.Custom custom = null;
    private CrmFollowCustom.FollowCustom followCustom;
    //private LoadingDialog mLoadingDialog;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_custom_detail);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        //mLoadingDialog = new LoadingDialog();
        activity_potential_custom_detail_scrollview.setOnTouchListener(this);
    }

    //获取跳转页面的传参，判断当前
    private void getExtras(){
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        if(TYPE_BROWSER.equals(type)){
            followCustom = (CrmFollowCustom.FollowCustom) intent.getSerializableExtra("FollowCustom");
            if(followCustom == null){
                MsgUtil.shortToastInCenter(this,"请重试！");
                finish();
            }
        }
        initView();
    }

    //初始化布局
    private void initView(){
        if(TYPE_BROWSER.equals(type)){
            tv_title.setText("潜在客户跟进详情");
            tv_edit.setVisibility(View.VISIBLE);
            tv_edit.setText("删除");
            crm_potential_commit.setText("编辑");
        }else if(TYPE_EDIT.equals(type)){
            tv_title.setText("潜在客户跟进详情");
            tv_edit.setVisibility(View.VISIBLE);
            tv_edit.setText("删除");
            crm_potential_commit.setText("提交");
        }else {
            tv_title.setText("新建潜在客户跟进");
        }
        //初始化item的title
        crm_potential_name.setTitle("客户名称");
        crm_potential_follow_way.setTitle("跟进方式");
        crm_potential_follow_time.setTitle("跟进时间");
        crm_potential_follow_case.setTitle("跟进情况");
        crm_potential_need.setTitle("需求反馈");
        crm_potential_follow_person.setTitle("跟进人");
        crm_potential_remark.setTitle("备注");
        ((AutoLinearLayout)findViewById(R.id.ll_back)).setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        crm_potential_commit.setOnClickListener(this);
        if(TYPE_BROWSER.equals(type)){
            setAllKeyDescVisible(false);
            setRightVisible(false);
            setValueTextVisible(false);
            initData();
        }else{
            setAllKeyDescVisible(true);
            setRightVisible(true);
            setValueTextVisible(true);
        }
    }

    //若当前为查看潜在客户跟进详情，则初始化界面数据
    private void initData(){
        try {
            setText(followCustom.getCustomer_name(),crm_potential_name);
            setText(getResources().getStringArray(R.array.crm_custom_follow_way)[Integer.parseInt(followCustom.getType())],crm_potential_follow_way);
            setText(followCustom.getFollow_time(),crm_potential_follow_time);
            setText(followCustom.getSituation(),crm_potential_follow_case);
            setText(followCustom.getDemand(),crm_potential_need);
            setText(followCustom.getFollow_user(),crm_potential_follow_person);
            setText(followCustom.getRemark(),crm_potential_remark);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //判断String是否为空  不为空则设置
    private void setText(String text,CustomCrmItem item){
        if(!TextUtils.isEmpty(text)){
            item.setValue(text);
            item.setEditText(text);
        }else{
            item.setHintValue("未填写");
            item.setHintText("请输入");
        }
    }

    @OnClick({R.id.crm_potential_name,R.id.crm_potential_follow_way,R.id.crm_potential_follow_time})
    void Click(View v){
        if(TYPE_BROWSER.equals(type)){
            return;
        }
        Hideinputwindown(crm_potential_name.getEditText());
        switch (v.getId()){
            case R.id.crm_potential_name:
                //若当前为新建潜在客户跟进状态   则跳转到选择潜在客户的页面
                if(TYPE_NEW.equals(type)){
                    Intent intent = new Intent(this,CrmPotentialFollowSelectCustomActivity.class);
                    intent.putExtra("type","0");//代表添加潜在客户
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("custom",custom);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,REQUEST_CODE);
                }
                break;
            case R.id.crm_potential_follow_way:
                //跟进方式
                showSelectDialog(getResources().getStringArray(R.array.crm_custom_follow_way),crm_potential_follow_way);
                break;
            case R.id.crm_potential_follow_time:
                //跟进时间
                showTimePickerDialog(crm_potential_follow_time);
                break;
        }
    }

    //跟进方式选择弹窗
    private void showSelectDialog(String[] arr, final CustomCrmItem item){
        CustomTextPicker picker = new CustomTextPicker(this,arr);
        picker.setBirthdayListener(new CustomTextPicker.OnBirthListener() {
            @Override
            public void onClick(String Mmin) {
                item.setValue(Mmin);
                Logger.i(TAG,Mmin);
            }
        });
        picker.show();
    }

    //时间选择弹窗
    private void showTimePickerDialog(final CustomCrmItem item){
        new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                item.setValue(getTime(date));
            }
        }).show_timepicker();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //设置部分item是否需要显示必选提示
    private void setAllKeyDescVisible(boolean flag){
        crm_potential_name.setKeyDescVisible(flag);
        crm_potential_follow_way.setKeyDescVisible(flag);
        crm_potential_follow_time.setKeyDescVisible(flag);
        //一直不显示
        crm_potential_follow_case.setKeyDescVisible(false);
        crm_potential_need.setKeyDescVisible(false);
        crm_potential_follow_person.setKeyDescVisible(false);
        crm_potential_remark.setKeyDescVisible(false);
    }

    //设置右边的箭头是否显示
    private void setRightVisible(boolean flag){
        if(TYPE_EDIT.equals(type)){
            crm_potential_name.setRightVisible(false);
        }else{
            crm_potential_name.setRightVisible(flag);
        }
        crm_potential_follow_way.setRightVisible(flag);
        crm_potential_follow_time.setRightVisible(flag);
        //一直不显示
        crm_potential_follow_case.setRightVisible(false);
        crm_potential_need.setRightVisible(false);
        crm_potential_follow_person.setRightVisible(false);
        crm_potential_remark.setRightVisible(false);
    }

    //设置每个CustomCrmItem的EditText和TextView是否显示
    private void setValueTextVisible(boolean flag){
        crm_potential_name.setEditVisible(false);
        crm_potential_follow_way.setEditVisible(false);
        crm_potential_follow_time.setEditVisible(false);
        crm_potential_follow_case.setEditVisible(flag);
        crm_potential_need.setEditVisible(flag);
        crm_potential_follow_person.setEditVisible(flag);
        crm_potential_remark.setEditVisible(flag);

        crm_potential_name.setValueVisible(true);
        crm_potential_follow_way.setValueVisible(true);
        crm_potential_follow_time.setValueVisible(true);
        crm_potential_follow_case.setValueVisible(!flag);
        crm_potential_need.setValueVisible(!flag);
        crm_potential_follow_person.setValueVisible(!flag);
        crm_potential_remark.setValueVisible(!flag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.crm_potential_commit:
                if(TYPE_BROWSER.equals(type)){
                    type = TYPE_EDIT;
                    initView();
                }else{
                    //提交数据
                    postData();
                }
                break;
            case R.id.tv_edit:
                //显示是否删除的提醒弹窗
                showDeleteDialog();
                break;
        }
    }

    private void postData(){
        //判断必填项是否全部填写
        if(TextUtils.isEmpty(crm_potential_name.getValue()) ||
                TextUtils.isEmpty(crm_potential_follow_way.getValue()) ||
                TextUtils.isEmpty(crm_potential_follow_time.getValue())){
            showRedmineDialog();
            return;
        }
        //mLoadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        String customer_follow_id = "";
        if(TYPE_NEW.equals(type)){
            customer_follow_id = "0";
        }else{
            customer_follow_id = followCustom.getId();
        }
        //跟进方式
        String followType = "";
        String[] typeArr = getResources().getStringArray(R.array.crm_custom_follow_way);
        for(int i = 0;i < typeArr.length;i++){
            if(crm_potential_follow_way.getValue().equals(typeArr[i])){
                followType = i+"";
            }
        }
        //跟进时间
        String follow_time = crm_potential_follow_time.getValue();
        //跟进情况
        String situation = crm_potential_follow_case.getEditTextValue();
        //跟进需求
        String demand = crm_potential_need.getEditTextValue();
        //跟进人
        String follow_user = crm_potential_follow_person.getEditTextValue();
        //备注
        String remark = crm_potential_remark.getEditTextValue();
        String RandStr = CustomUtils.getRandStr();
        String custom_id = "";
        if(TYPE_EDIT.equals(type)){
            custom_id = followCustom.getId();
        }else{
            custom_id = custom.getId();
        }
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id()+custom_id);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("customer_follow_id",customer_follow_id);
        data.put("customer_id",custom_id);
        data.put("type",followType);
        data.put("follow_time",follow_time);
        if(!TextUtils.isEmpty(situation)){
            data.put("situation",situation);
        }
        if(!TextUtils.isEmpty(demand)){
            data.put("demand",demand);
        }
        if(!TextUtils.isEmpty(follow_user)){
            data.put("follow_user",follow_user);
        }
        if(!TextUtils.isEmpty(remark)){
            data.put("remark",remark);
        }
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CUSTOMER_FOLLOW,data);
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
                                MsgUtil.shortToastInCenter(PotentialCustomDetailActivity.this,object.getString("msg"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

        data.clear();
        data = null;
        typeArr = null;
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

    //删除提醒弹窗
    private void showDeleteDialog(){
        new MyCustomDialogDialog(6, this, R.style.MyDialog, null, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteCustom();
                dialog.dismiss();
            }
        }).show();
    }

    //删除潜在客户
    private void deleteCustom(){
        /**URL_DELETE_CUSTOMER_FOLLOW
         * user_id	是	int[11]用户ID
         customer_follow_id	是	int[11]客户/潜在客户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.customer_follow_id)
         */
        //mLoadingDialog.show(getSupportFragmentManager(),Constant.DIALOG_TAG_LOADING);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+followCustom.getId());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("customer_follow_id",followCustom.getId());
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_CUSTOMER_FOLLOW,data);
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
                                MsgUtil.shortToastInCenter(PotentialCustomDetailActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        //手动清理不用的资源
        data.clear();
        data = null;
    }

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            custom = (CrmCustom.Custom) data.getSerializableExtra("custom");
            crm_potential_name.setValue(custom.getName());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_potential_custom_detail_scrollview);
        return false;
    }
}
