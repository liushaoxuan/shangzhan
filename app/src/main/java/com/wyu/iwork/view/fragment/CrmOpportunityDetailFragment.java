package com.wyu.iwork.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.model.OpportunityDetail;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.CrmPotentialFollowSelectCustomActivity;
import com.wyu.iwork.view.activity.SalesProcessActivity;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomCrmItem;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.ProcessSelectDialog;
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



/**
 * Created by lx on 2017/6/6.
 */
@SuppressLint("ValidFragment")
public class CrmOpportunityDetailFragment extends BaseFragment implements View.OnTouchListener{

    private static final String TAG = CrmOpportunityDetailFragment.class.getSimpleName();

    //客户名称
    @BindView(R.id.opportunity_detail_custom_name)
    CustomCrmItem opportunity_detail_custom_name;

    //商机名称
    @BindView(R.id.opportunity_detail_name)
    CustomCrmItem opportunity_detail_name;

    //预计成交日期
    @BindView(R.id.opportunity_detail_date)
    CustomCrmItem opportunity_detail_date;

    //商机金额
    @BindView(R.id.opportunity_detail_money)
    CustomCrmItem opportunity_detail_money;

    //跟进人
    @BindView(R.id.opportunity_detail_follower)
    CustomCrmItem opportunity_detail_follower;

    //备注
    @BindView(R.id.opportunity_detail_remark)
    CustomCrmItem opportunity_detail_remark;

    //编辑
    @BindView(R.id.opportunity_detail_edit)
    TextView opportunity_detail_edit;

    //查看详情
    @BindView(R.id.check_detail)
    TextView check_detail;

    //desc
    @BindView(R.id.desc)
    AutoLinearLayout desc;

    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //编辑
    @BindView(R.id.tv_edit)
    TextView tv_edit;

    //删除
    @BindView(R.id.tv_delete)
    TextView tv_delete;

    @BindView(R.id.desc_text)
    TextView desc_text;

    @BindView(R.id.opportunity_scrollview)
    ScrollView opportunity_scrollview;

    private String type;
    private String chance_id;//商机ID 查看删除商机是需要使用
    private static final String TYPE_EDIT = "EDIT";
    private static final String TYPE_BROWSER = "BROWSER";
    private static final String TYPE_NEW = "NEW";
    private Gson gson;
    private OpportunityDetail detail;
    private CrmCustom.Custom custom = null;

    private static final int REQUEST_CODE = 100;//在startActivityForResult时使用   请求码
    private static final int RESULT_CODE = 101;//在onActivityResult中使用    结果码
    private ArrayList<String> list;

    private String currentProcess;//代表当前的流程状态
    private String finishOrder;//代表点击赢单或无效或保存的状态

    @SuppressLint("ValidFragment")
    public CrmOpportunityDetailFragment( String type){
        this.type = type;
        this.chance_id = "0";
    }

    @SuppressLint("ValidFragment")
    public CrmOpportunityDetailFragment(String type,String chance_id){
        this.type = type;
        this.chance_id = chance_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_opportunity_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
        initClick();
        initItemTitle();
        setSingleLine();
        setItemBottomLineMustGone();
        list = new ArrayList<>();//为后面流程做判断准备
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");

    }

    @Override
    public void onResume() {
        super.onResume();
        init();//初始化布局
    }

    //初始化部分点击事件
    private void initClick(){
        tv_edit.setText("编辑");
        tv_delete.setText("删除");
        tv_edit.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        opportunity_detail_edit.setOnClickListener(this);
        check_detail.setOnClickListener(this);
        opportunity_scrollview.setOnTouchListener(this);
    }

    private void init(){
        //设置标题
        if(TYPE_BROWSER.equals(type)){
            getOpportunityDetail();//获取商机详情
            tv_title.setText("商机详情");
            setKeyDescVisible(false);
            setRightArrowVisible(false);
            setAllTextViewValueVisible(true);
            desc.setVisibility(View.VISIBLE);
            tv_edit.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.VISIBLE);
        }else if(TYPE_NEW.equals(type)){
            tv_title.setText("新建商机");
            setKeyDescVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisible(false);
            desc.setVisibility(View.GONE);
            tv_edit.setVisibility(View.GONE);
            tv_delete.setVisibility(View.GONE);
            opportunity_detail_edit.setText("提交");
        }else if(TYPE_EDIT.equals(type)){
            tv_title.setText("编辑商机详情");
            setKeyDescVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisible(false);
            desc.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.VISIBLE);
        }
    }

    //设置每个Item的标题
    private void initItemTitle() {
        opportunity_detail_custom_name.setTitle("客户名称");
        opportunity_detail_name.setTitle("商机名称");
        opportunity_detail_date.setTitle("预计成交日期");
        opportunity_detail_money.setTitle("商机金额（￥）");
        opportunity_detail_follower.setTitle("跟进人");
        opportunity_detail_remark.setTitle("备注");

        //设置一些特殊的item只能输入特定的字符
        opportunity_detail_money.setInputType(8194);
    }

    private void setSingleLine(){
        CustomCrmItem[] itemArr = {opportunity_detail_custom_name,opportunity_detail_name,opportunity_detail_date,
                opportunity_detail_money,opportunity_detail_follower,opportunity_detail_remark};
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
    private void setKeyDescVisible(boolean flag) {
        opportunity_detail_custom_name.setKeyDescVisible(flag);
        opportunity_detail_name.setKeyDescVisible(flag);
        opportunity_detail_date.setKeyDescVisible(flag);
        opportunity_detail_money.setKeyDescVisible(flag);
        opportunity_detail_follower.setKeyDescVisible(false);
        opportunity_detail_remark.setKeyDescVisible(false);
    }

    //设置右边的箭头是否显示
    private void setRightArrowVisible(boolean flag) {
        opportunity_detail_custom_name.setRightVisible(flag);
        opportunity_detail_name.setRightVisible(false);
        opportunity_detail_date.setRightVisible(flag);
        opportunity_detail_money.setRightVisible(false);
        opportunity_detail_follower.setRightVisible(false);
        opportunity_detail_remark.setRightVisible(false);
    }

    //设置底部横线一定不显示
    private void setItemBottomLineMustGone() {
        opportunity_detail_custom_name.setBottomLineVisible(false);
        opportunity_detail_money.setBottomLineVisible(false);
        opportunity_detail_remark.setBottomLineVisible(false);
    }

    //设置显示EditText还是TextView
    private void setAllTextViewValueVisible(boolean flag){
        /**
         * 当当前为浏览信息状态时，则全部显示TextView
         * 当当前为新建或编辑是，则部分显示
         */
        opportunity_detail_custom_name.setValueVisible(true);
        opportunity_detail_name.setValueVisible(flag);
        opportunity_detail_date.setValueVisible(true);
        opportunity_detail_money.setValueVisible(flag);
        opportunity_detail_follower.setValueVisible(flag);
        opportunity_detail_remark.setValueVisible(flag);

        opportunity_detail_custom_name.setEditVisible(false);
        opportunity_detail_name.setEditVisible(!flag);
        opportunity_detail_date.setEditVisible(false);
        opportunity_detail_money.setEditVisible(!flag);
        opportunity_detail_follower.setEditVisible(!flag);
        opportunity_detail_remark.setEditVisible(!flag);
    }

    @OnClick({R.id.opportunity_detail_custom_name,R.id.opportunity_detail_date})
    void Click(View v){
        if(TYPE_BROWSER.equals(type)){
            return;
        }
        switch (v.getId()){
            case R.id.opportunity_detail_custom_name:
                Intent intent = new Intent(getActivity(),CrmPotentialFollowSelectCustomActivity.class);
                intent.putExtra("type","0");//代表添加正式客户
                Bundle bundle = new Bundle();
                if(TYPE_EDIT.equals(type)){
                    if(custom == null){
                        CrmCustom crmCustom = new CrmCustom();
                        custom = crmCustom.new Custom();
                        custom.setId(detail.getData().getCustomer_id());
                    }
                }
                bundle.putSerializable("custom",custom);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.opportunity_detail_date:
                Hideinputwindown(opportunity_detail_date.getEditText());
                showCrmTimeDialog(opportunity_detail_date);
                break;
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
                //编辑
                if(TYPE_BROWSER.equals(type)){
                    type = TYPE_EDIT;
                    tv_edit.setText("提交");
                    init();
                }else{
                    newAndEditDetail();
                }
                break;
            case R.id.tv_delete:
                //删除
                showDeleteDialog();
                break;
            case R.id.opportunity_detail_edit:
                if(TYPE_BROWSER.equals(type)){
                    if(list.contains(detail.getData().getStatus())){
                        showSelectorDialog();
                    }else{
                        return;
                    }
                }else if(TYPE_NEW.equals(type)){
                    newAndEditDetail();
                }
                break;
            case R.id.check_detail:
                Intent intent = new Intent(getActivity(),SalesProcessActivity.class);
                intent.putExtra("chance_id",chance_id);
                getActivity().startActivity(intent);
                break;
        }
    }

    //显示流程变更弹窗
    private void showSelectorDialog(){
        new ProcessSelectDialog(getActivity(), currentProcess, new ProcessSelectDialog.DialogListener() {
            @Override
            public void itemClick(String text) {
                Logger.i(TAG, text);
                if(!text.equals("赢单") && !text.equals("无效")){
                    updateChanceStatus(text);
                }
            }

            @Override
            public void showClickMessage(String text) {
                finishOrder = text;
                Logger.i(TAG, text);
                updateChanceStatus(finishOrder);
            }
        }).show();
    }

    //获取商机详情
    private void getOpportunityDetail(){
        /**
         *  user_id	是	int[11]       用户ID
            chance_id	是	int[11]       商机ID
            F	      是	string[18]    请求来源：IOS/ANDROID/WEB
            V	      是	string[20]    版本号如：1.0.1
            RandStr	是	string[50]    请求加密随机数 time().|.rand()
            Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.id.user_id.chance_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("chance_id",chance_id);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+MyApplication.userInfo.getUser_id()+chance_id);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CHANCE_DETAIL,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        try {
                            if(gson == null){
                                gson = new Gson();
                            }
                            detail = gson.fromJson(s,OpportunityDetail.class);
                            setData();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    //设置商机详情到相应位置
    private void setData(){
        setText(detail.getData().getCustomer_name(),opportunity_detail_custom_name);
        setText(detail.getData().getTitle(),opportunity_detail_name);
        setText(detail.getData().getPredict_date(),opportunity_detail_date);
        setText(detail.getData().getValue(),opportunity_detail_money);
        setText(detail.getData().getFollow_user(),opportunity_detail_follower);
        setText(detail.getData().getRemark(),opportunity_detail_remark);
        try {
            //初始化当前流程状态
            currentProcess = getActivity().getResources().getStringArray(R.array.crm_process_change)[Integer.parseInt(detail.getData().getStatus())-1];
            if(list.contains(detail.getData().getStatus())){
                opportunity_detail_edit.setText(detail.getData().getStatus()+"."+
                        getActivity().getResources().getStringArray(R.array.crm_process_change)[Integer.parseInt(detail.getData().getStatus())-1]);
                desc_text.setVisibility(View.VISIBLE);
                desc_text.setText("当前阶段"+Integer.parseInt(detail.getData().getStatus())+"，共4阶段");
            }else{
                opportunity_detail_edit.setText(
                        getActivity().getResources().getStringArray(R.array.crm_process_change)[Integer.parseInt(detail.getData().getStatus())-1]);
                desc_text.setVisibility(View.GONE);
            }
            if(!list.contains(detail.getData().getStatus())){
                tv_edit.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //判断字段是否为空并设置字段显示
    private void setText(String text,CustomCrmItem item){
        if(!TextUtils.isEmpty(text)){
            item.setValue(text);
            item.setEditText(text);
        }else{
            item.setHintValue("未填写");
            item.setHintText("请输入");
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(){
        new MyCustomDialogDialog(6, getActivity(), R.style.MyDialog, "确定要删除该商机吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteOpportunity();
                dialog.dismiss();
            }
        }).show();
    }

    //删除商机
    private void deleteOpportunity(){
        /**
         * URL_DELETE_CHANCE
         * user_id	是	int[11]       用户ID
           chance_id	是	int[11]       商机ID
           F	      是	string[18]    请求来源：IOS/ANDROID/WEB
           V	      是	string[20]    版本号如：1.0.1
           RandStr	是	string[50]    请求加密随机数 time().|.rand()
           Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.chance_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+MyApplication.userInfo.getUser_id()+chance_id);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("chance_id",chance_id);
        data.put("F", Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_CHANCE, data);
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
                                MsgUtil.shortToastInCenter(getActivity(),object.getString("msg"));
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

    //新建、编辑商机详情
    private void newAndEditDetail(){
        //判断必填项是否全部填写
        if(TextUtils.isEmpty(opportunity_detail_custom_name.getValue()) ||
                TextUtils.isEmpty(opportunity_detail_name.getEditTextValue()) ||
                TextUtils.isEmpty(opportunity_detail_date.getValue()) ||
                TextUtils.isEmpty(opportunity_detail_money.getEditTextValue())){
            showRedmineDialog();
            return;
        }
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("chance_id",chance_id);
        if(TYPE_EDIT.equals(type)){
            if(custom == null){
                data.put("customer_id",detail.getData().getCustomer_id());
            }else{
                data.put("customer_id",custom.getId());
            }
        }else if(TYPE_NEW.equals(type)){
            data.put("customer_id",custom.getId());
        }
        data.put("title",opportunity_detail_name.getEditTextValue());
        data.put("predict_date",opportunity_detail_date.getValue());
        data.put("value",opportunity_detail_money.getEditTextValue());
        putHashMapData(data,"follow_user",opportunity_detail_follower,1);
        putHashMapData(data,"remark",opportunity_detail_remark,1);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ MyApplication.userInfo.getUser_id()+chance_id);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CHANCE,data);
        Logger.i(TAG,murl);
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
                                MsgUtil.shortToastInCenter(getActivity(),object.getString("msg"));
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

    //检查从CustomItem取出的值  不为空则加入请求参数
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

    //显示时间弹窗选择日期
    private void showCrmTimeDialog(final CustomCrmItem item){
        new PickerViewDialog(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Logger.i(TAG,"DATE="+getTime(date));
                item.setValue(getTime(date));
            }
        }).show_timepicker();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    //更新商机状态
    private void updateChanceStatus(String status){
        /**
         *  user_id	是	int[11]       用户ID
            chance_id	是	int[11]       商机ID
            status	是	int[11]       更改阶段ID 1：验证客户 2：需求确定 3：方案/报价 4：谈判审核 5：赢单 6：输单 7：无效
            fail_status	否	int[11]       只在更改状态为输单时填写  输单原因 1：客户不想购买了 2：未满足客户需求 3：被竞争对手抢单
            F	      是	string[18]    请求来源：IOS/ANDROID/WEB
            V	      是	string[20]    版本号如：1.0.1
            RandStr	是	string[50]    请求加密随机数 time().|.rand()
            Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.chance_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("chance_id",detail.getData().getId());
        //流程变更
        String[] processArr = getActivity().getResources().getStringArray(R.array.crm_process_change);
        //输单原因
        String[] reasonArr = getActivity().getResources().getStringArray(R.array.crm_lost_opportunity);
        if("确定商机赢单?".equals(status) ){
            data.put("status",5+"");
        }else if("确定商机无效?".equals(status)){
            data.put("status",7+"");
        }else{
            for(int i = 0;i<processArr.length;i++){
                if(processArr[i].equals(status)){
                    data.put("status",i+1+"");
                }
            }
            for(int i = 0;i<reasonArr.length;i++){
                if(reasonArr[i].equals(status)){
                    data.put("status","6");
                    data.put("fail_status",i+1+"");
                }
            }
        }
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+
                MyApplication.userInfo.getUser_id()+detail.getData().getId());
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CHANCE_STATUS,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(getActivity()) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                getOpportunityDetail();//获取商机详情
                            }else{
                                MsgUtil.shortToastInCenter(getActivity(),object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
        if(processArr != null){
            processArr = null;
        }
        if(reasonArr != null){
            reasonArr = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            custom = (CrmCustom.Custom) data.getSerializableExtra("custom");
            Logger.i(TAG,custom.getName());
            opportunity_detail_custom_name.setValue(custom.getName());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(opportunity_scrollview);
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
