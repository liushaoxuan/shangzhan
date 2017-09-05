package com.wyu.iwork.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MarketingDetail;
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

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * Created by lx on 2017/6/8.
 */
@SuppressLint("ValidFragment")
public class MarketingDetailFragment extends BaseFragment implements View.OnTouchListener{

    private static final String TAG = MarketingDetailFragment.class.getSimpleName();
    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //删除
    @BindView(R.id.tv_edit)
    TextView tv_edit;

    //市场活动名称
    @BindView(R.id.marketing_name)
    CustomCrmItem marketing_name;

    //市场活动开始时间
    @BindView(R.id.marketing_start_time)
    CustomCrmItem marketing_start_time;

    //市场活动结束时间
    @BindView(R.id.marketing_end_time)
    CustomCrmItem marketing_end_time;

    //市场活动类型
    @BindView(R.id.marketing_type)
    CustomCrmItem marketing_type;

    //市场活动地点
    @BindView(R.id.marketing_address)
    CustomCrmItem marketing_address;

    //市场活动计划
    @BindView(R.id.marketing_plan)
    CustomCrmItem marketing_plan;

    //市场活动预计成本
    @BindView(R.id.marketing_predict_money)
    CustomCrmItem marketing_predict_money;

    //市场活动实际成本
    @BindView(R.id.marketing_true_money)
    CustomCrmItem marketing_true_money;

    //市场活动预计收入
    @BindView(R.id.marketing_predict_gain)
    CustomCrmItem marketing_predict_gain;

    //市场活动效果
    @BindView(R.id.marketing_result)
    CustomCrmItem marketing_result;

    //市场活动实际收入
    @BindView(R.id.marketing_true_gain)
    CustomCrmItem marketing_true_gain;

    //市场活动跟进人
    @BindView(R.id.marketing_follower)
    CustomCrmItem marketing_follower;

    //市场活动计划详情
    @BindView(R.id.ll_plan_detail)
    AutoLinearLayout ll_plan_detail;

    //市场活动备注
    @BindView(R.id.marketing_remark)
    CustomCrmItem marketing_remark;

    //编辑市场活动计划详情
    @BindView(R.id.et_plan_detail)
    EditText et_plan_detail;

    //浏览市场活动计划详情
    @BindView(R.id.tv_plan_detail)
    TextView tv_plan_detail;

    //市场活动效果详情
    @BindView(R.id.ll_result_detail)
    AutoLinearLayout ll_result_detail;

    //编辑市场活动效果详情
    @BindView(R.id.et_result_detail)
    EditText et_result_detail;

    //浏览市场活动效果详情
    @BindView(R.id.tv_result_detail)
    TextView tv_result_detail;

    //市场活动提交
    @BindView(R.id.marketing_commit)
    TextView marketing_commit;

    @BindView(R.id.marketing_scrollview)
    ScrollView marketing_scrollview;

    private CustomTextPicker mPicker;

    private String currentType;
    private String activity_id;
    private static final String TYPE_BROWSER = "BROWSER";//浏览市场活动详情
    private static final String TYPE_EDIT = "EDIT";//编辑市场活动详情
    private static final String TYPE_NEW = "NEW";//新建市场活动详情
    private Gson gson;
    private MarketingDetail detail;
    private long startTime;//活动开始时间和结束时间  开始时间不能大于结束时间
    private long endTime;


    public MarketingDetailFragment(String type){
        this.currentType = type;
        this.activity_id = "0";
    }

    public MarketingDetailFragment(String type,String activity_id){
        this.currentType = type;
        this.activity_id = activity_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_marketing_detail,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
        init();
        initView();

    }

    private void init(){
        initItemTitle();
        setSingleLine();
        setItemBottomLineMustGone();
        ll_back.setOnClickListener(this);
        tv_edit.setText("删除");
        tv_edit.setOnClickListener(this);
        marketing_commit.setOnClickListener(this);
        marketing_scrollview.setOnTouchListener(this);
    }

    private void initView(){
        if(TYPE_BROWSER.equals(currentType)){
            //浏览市场活动详情
            getMarketingActivityDetail();
            tv_title.setText("活动详情");
            tv_edit.setVisibility(View.VISIBLE);
            marketing_commit.setText("编辑");
            setKeyDescVisible(false);
            setRightArrowVisible(false);
            setAllTextViewValueVisi(true);
        }else if(TYPE_EDIT.equals(currentType)){
            //编辑市场详情
            tv_title.setText("编辑活动详情");
            tv_edit.setVisibility(View.VISIBLE);
            marketing_commit.setText("提交");
            setKeyDescVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisi(false);
        }else if(TYPE_NEW.equals(currentType)){
            //新建市场详情
            tv_title.setText("新建活动");
            tv_edit.setVisibility(View.GONE);
            marketing_commit.setText("提交");
            setKeyDescVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisi(false);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_edit:
                //删除
                showDeleteDialog();
                break;
            case R.id.marketing_commit:
                //提交
                if(TYPE_BROWSER.equals(currentType)){
                    currentType = TYPE_EDIT;
                    initView();
                }else{
                    newAndEditMarketing();
                }
                break;
        }
    }

    @OnClick({R.id.marketing_start_time,R.id.marketing_end_time,R.id.marketing_type})
   void Click(View view){
        if(TYPE_BROWSER.equals(currentType)){
            return;
        }
        switch (view.getId()){
            case R.id.marketing_start_time:
                Hideinputwindown(marketing_start_time.getEditText());
                showCrmTimeDialog(marketing_start_time);
                break;
            case R.id.marketing_end_time:
                Hideinputwindown(marketing_end_time.getEditText());
                showCrmTimeDialog(marketing_end_time);
                break;
            case R.id.marketing_type:
                Hideinputwindown(marketing_type.getEditText());
                showCustomResourceDialog(getActivity().getResources().getStringArray(R.array.crm_marketing_type),marketing_type);
                break;
        }
    }

    //初始化每一项标题
    private void initItemTitle(){
        marketing_name.setTitle("名称");
        marketing_start_time.setTitle("开始时间");
        marketing_end_time.setTitle("结束时间");
        marketing_type.setTitle("活动类型");
        marketing_address.setTitle("活动地点");
        marketing_plan.setTitle("活动计划");
        marketing_predict_money.setTitle("预计成本(元)");
        marketing_true_money.setTitle("实际成本(元)");
        marketing_predict_gain.setTitle("预计收入(元)");
        marketing_true_gain.setTitle("实际收入(元)");
        marketing_result.setTitle("活动效果");
        marketing_follower.setTitle("跟进人");
        marketing_remark.setTitle("备注");
    }

    private void setSingleLine(){
        CustomCrmItem[] itemArr = {marketing_name,marketing_start_time,marketing_end_time,
                marketing_type,marketing_address,marketing_plan,marketing_predict_money,marketing_true_money,
                marketing_predict_gain,marketing_true_gain,marketing_result,marketing_follower,marketing_remark};
        for(int i = 0;i<itemArr.length;i++){
            setItemSingleLine(itemArr[i]);
        }
        itemArr = null;
    }

    private void setItemSingleLine(CustomCrmItem item){
        item.setEditSingleLine();
        item.setValueSingle();
    }

    //设置底部横线一定不显示
    private void setItemBottomLineMustGone(){
        marketing_plan.setBottomLineVisible(false);
        marketing_result.setBottomLineVisible(false);
        marketing_remark.setBottomLineVisible(false);
    }

    //设置必填选项提示是否显示
    private void setKeyDescVisible(boolean flag){
        marketing_name.setKeyDescVisible(flag);
        marketing_start_time.setKeyDescVisible(flag);
        marketing_end_time.setKeyDescVisible(flag);
        marketing_type.setKeyDescVisible(flag);
        marketing_address.setKeyDescVisible(false);
        marketing_plan.setKeyDescVisible(false);
        marketing_predict_money.setKeyDescVisible(false);
        marketing_true_money.setKeyDescVisible(false);
        marketing_predict_gain.setKeyDescVisible(false);
        marketing_true_gain.setKeyDescVisible(false);
        marketing_result.setKeyDescVisible(false);
        marketing_follower.setKeyDescVisible(false);
        marketing_remark.setKeyDescVisible(false);
    }

    //设置右边箭头是否显示
    private void setRightArrowVisible(boolean flag){
        marketing_name.setRightVisible(false);
        marketing_start_time.setRightVisible(flag);
        marketing_end_time.setRightVisible(flag);
        marketing_type.setRightVisible(flag);
        marketing_address.setRightVisible(false);
        marketing_plan.setRightVisible(false);
        marketing_predict_money.setRightVisible(false);
        marketing_true_money.setRightVisible(false);
        marketing_predict_gain.setRightVisible(false);
        marketing_true_gain.setRightVisible(false);
        marketing_result.setRightVisible(false);
        marketing_follower.setRightVisible(false);
        marketing_remark.setRightVisible(false);
    }

    //设置显示EditText 还是显示TextView
    private void setAllTextViewValueVisi(boolean flag){

        marketing_name.setValueVisible(flag);
        marketing_start_time.setValueVisible(true);
        marketing_end_time.setValueVisible(true);
        marketing_type.setValueVisible(true);
        marketing_address.setValueVisible(flag);
        marketing_plan.setValueVisible(false);
        marketing_predict_money.setValueVisible(flag);
        marketing_true_money.setValueVisible(flag);
        marketing_predict_gain.setValueVisible(flag);
        marketing_true_gain.setValueVisible(flag);
        marketing_result.setValueVisible(false);
        marketing_follower.setValueVisible(flag);
        marketing_remark.setValueVisible(flag);

        marketing_name.setEditVisible(!flag);
        marketing_start_time.setEditVisible(false);
        marketing_end_time.setEditVisible(false);
        marketing_type.setEditVisible(false);
        marketing_address.setEditVisible(!flag);
        marketing_plan.setEditVisible(false);
        marketing_predict_money.setEditVisible(!flag);
        marketing_true_money.setEditVisible(!flag);
        marketing_predict_gain.setEditVisible(!flag);
        marketing_true_gain.setEditVisible(!flag);
        marketing_result.setEditVisible(false);
        marketing_follower.setEditVisible(!flag);
        marketing_remark.setEditVisible(!flag);
        et_plan_detail.setVisibility(flag?View.GONE:View.VISIBLE);
        tv_plan_detail.setVisibility(flag?View.VISIBLE:View.GONE);
        et_result_detail.setVisibility(flag?View.GONE:View.VISIBLE);
        tv_result_detail.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    //显示时间弹窗选择日期
    private void showCrmTimeDialog(final CustomCrmItem item){
        new PickerViewDialog(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Logger.i(TAG,"DATE="+getTime(date));
                item.setValue(getTime(date));
                if(item == marketing_start_time){
                    startTime = date.getTime();
                }else if(item == marketing_end_time){
                    endTime = date.getTime();
                }
            }
        }).show_timepicker();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //显示客户来源选项弹框
    private void showCustomResourceDialog(String[] arr, final CustomCrmItem item){
        mPicker = new CustomTextPicker(getActivity(),arr);
        mPicker.show();
        mPicker.setBirthdayListener(new CustomTextPicker.OnBirthListener() {
            @Override
            public void onClick(String Mmin) {
                Logger.i(TAG,"Min="+Mmin);
                item.setValue(Mmin);
            }
        });
    }

    //获取活动详情
    private void getMarketingActivityDetail(){
        /**
         *  user_id	是	int[11]       用户ID
            activity_id	是	int[11]       活动ID
            F	      是	string[18]    请求来源：IOS/ANDROID/WEB
            V	      是	string[20]    版本号如：1.0.1
            RandStr	是	string[50]    请求加密随机数 time().|.rand()
            Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.activity_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id()+activity_id);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("activity_id",activity_id);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_ACTIVITY_DETAIL,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {

                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseMarketDetailData(s);
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    //解析市场活动详情数据
    private void parseMarketDetailData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            detail = gson.fromJson(data, MarketingDetail.class);
            if("0".equals(detail.getCode())){
                setData();
            }else {
                //请求失败
                MsgUtil.shortToastInCenter(getActivity(), detail.getMsg());
                onBackPressed();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //设置详情数据显示
    private void setData(){
        checkItemText(detail.getData().getTitle(),marketing_name);
        checkItemText(detail.getData().getStart_time(),marketing_start_time);
        checkItemText(detail.getData().getEnd_time(),marketing_end_time);
        checkItemText(getResources().getStringArray(R.array.crm_marketing_type)[Integer.parseInt(detail.getData().getType())-1],marketing_type);
        checkItemText(detail.getData().getAddress(),marketing_address);
        checkItemText(detail.getData().getPredict_cost(),marketing_predict_money);
        checkItemText(detail.getData().getCost(),marketing_true_money);
        checkItemText(detail.getData().getPredict_income(),marketing_predict_gain);
        checkItemText(detail.getData().getIncome(),marketing_true_gain);
        checkItemText(detail.getData().getFollow_user(),marketing_follower);
        checkItemText(detail.getData().getRemark(),marketing_remark);
        if(!TextUtils.isEmpty(detail.getData().getPlan())){
            tv_plan_detail.setText(detail.getData().getPlan());
        }else{
            tv_plan_detail.setHint("未填写");
        }
        if(!TextUtils.isEmpty(detail.getData().getPlan())){
            et_plan_detail.setText(detail.getData().getPlan());
        }else{
            et_plan_detail.setHint("请输入");
        }
        if(!TextUtils.isEmpty(detail.getData().getEffect())){
            tv_result_detail.setText(detail.getData().getEffect());
        }else{
            tv_result_detail.setHint("未填写");
        }
        if(!TextUtils.isEmpty(detail.getData().getEffect())){
            et_result_detail.setText(detail.getData().getEffect());
        }else{
            et_result_detail.setHint("请输入");
        }
        if(!TextUtils.isEmpty(detail.getData().getStart_time())){
            startTime = CustomUtils.strToDate(detail.getData().getStart_time()).getTime();
        }
        if(!TextUtils.isEmpty(detail.getData().getEnd_time())){
            endTime = CustomUtils.strToDate(detail.getData().getEnd_time()).getTime();
        }
    }

    //检查String是否为空并设置相应的数据
    private void checkItemText(String text,CustomCrmItem item){
        if(!TextUtils.isEmpty(text)){
            item.setValue(text);
            item.setEditText(text);
        }else{
            item.setHintText("请输入");
            item.setHintValue("未填写");
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(){
        new MyCustomDialogDialog(6, getActivity(), R.style.MyDialog, "确定要删除该活动吗?", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteMarketing();
                dialog.dismiss();
            }
        }).show();
    }

    //删除活动
    private void deleteMarketing(){
        /**
         *  user_id	是	int[11]       用户ID
         activity_id	是	int[11]       活动ID
         F	      是	string[18]    请求来源：IOS/ANDROID/WEB
         V	      是	string[20]    版本号如：1.0.1
         RandStr	是	string[50]    请求加密随机数 time().|.rand()
         Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.activity_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+MyApplication.userInfo.getUser_id()
                +activity_id);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("activity_id",activity_id);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_ACTIVITY, data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                getActivity().onBackPressed();
                            }else{
                                MsgUtil.shortToastInCenter(getActivity(),object.getString("code"));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    //创建市场活动  编辑市场活动
    private void newAndEditMarketing(){
        //判断必填项是否为空 若为空  则给出提示
        if(TextUtils.isEmpty(marketing_name.getEditTextValue()) ||
                TextUtils.isEmpty(marketing_start_time.getValue()) ||
                TextUtils.isEmpty(marketing_end_time.getValue()) ||
                TextUtils.isEmpty(marketing_type.getValue())){

            showRedmineDialog();
            return;

        }
        if(!TextUtils.isEmpty(marketing_start_time.getValue()) && !TextUtils.isEmpty(marketing_end_time.getValue())){
            if(startTime>endTime){
                MsgUtil.shortToastInCenter(getActivity(),"活动开始时间不能晚于结束时间!");
                return;
            }
        }
        HashMap<String,String> data = new HashMap<>();
        //判断如下item的是否含有内容  若有 则添加到请求的url里
        CustomCrmItem[] itemArr = {marketing_name,marketing_start_time,marketing_end_time,marketing_address,marketing_predict_money,
                marketing_true_money,marketing_predict_gain,marketing_true_gain,marketing_follower,marketing_remark};
        String[] keyArr = {"title","start_time","end_time","address","predict_cost","cost","predict_income",
                "income","follow_user","remark"};
        for(int i = 0;i<itemArr.length;i++){
            if(i == 1 || i == 2){
                if(!TextUtils.isEmpty(itemArr[i].getValue())){
                    data.put(keyArr[i],itemArr[i].getValue());
                }
            }else{
                if(!TextUtils.isEmpty(itemArr[i].getEditTextValue())){
                    data.put(keyArr[i],itemArr[i].getEditTextValue());
                }
            }
        }
        setDateToHashMap(data,et_plan_detail,"plan");
        setDateToHashMap(data,et_result_detail,"effect");
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("activity_id",activity_id);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+MyApplication.userInfo.getUser_id()+activity_id);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String[] typeArr = getResources().getStringArray(R.array.crm_marketing_type);
        for(int i = 0;i<typeArr.length;i++){
            if(marketing_type.getValue().equals(typeArr[i])){
                data.put("type",i+1+"");
            }
        }
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_ACTIVITY,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
        .execute(new DialogCallback(getActivity()) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s, call, response);
                Logger.i(TAG,s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if("0".equals(object.getString("code"))){
                        getActivity().onBackPressed();
                    }else{
                        MsgUtil.shortToastInCenter(getActivity(),object.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
        if(itemArr != null){
            itemArr = null;
        }
        if(keyArr != null){
            keyArr = null;
        }
        if(data != null){
            data.clear();
            data = null;
        }
        if(typeArr != null){
            typeArr = null;
        }
    }

    private void setDateToHashMap(HashMap<String,String> data,EditText et,String key){
        if(!TextUtils.isEmpty(et.getText().toString())){
            data.put(key,et.getText().toString());
        }
    }

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, getActivity(), R.style.MyDialog, null, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(marketing_scrollview);
        return false;
    }

    //强制隐藏软键盘
    public   void Hideinputwindown(View view){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (isOpen){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }
}
