package com.wyu.iwork.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.BusinessCardInfo;
import com.wyu.iwork.model.ClueDetail;
import com.wyu.iwork.model.Marketing;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MarketingSelectorActivity;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.wheeldate.CustomTextPicker;
import com.wyu.iwork.widget.CustomCrmItem;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

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
 * CRM -新建销售线索  编辑销售线索  浏览销售线索
 */
@SuppressLint("ValidFragment")
public class SalesLeadDetailFragment extends BaseFragment implements View.OnTouchListener{

    private static final String TAG = SalesLeadDetailFragment.class.getSimpleName();
    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //删除
    @BindView(R.id.tv_edit)
    TextView tv_edit;

    //名称
    @BindView(R.id.sales_name)
    CustomCrmItem sales_name;

    //公司
    @BindView(R.id.sales_company)
    CustomCrmItem sales_company;

    //职位
    @BindView(R.id.sales_position)
    CustomCrmItem sales_position;

    //联系方式
    @BindView(R.id.sales_tel)
    CustomCrmItem sales_tel;

    //地址
    @BindView(R.id.sales_address)
    CustomCrmItem sales_address;

    //详细地址
    @BindView(R.id.sales_detail_address)
    CustomCrmItem sales_detail_address;

    //邮箱
    @BindView(R.id.sales_mail)
    CustomCrmItem sales_mail;

    //线索来源
    @BindView(R.id.sales_source)
    CustomCrmItem sales_source;

    //市场活动名称
    @BindView(R.id.sales_market_name)
    CustomCrmItem sales_market_name;

    //销售线索详情
    @BindView(R.id.sales_detail)
    CustomCrmItem sales_detail;

    //跟进人
    @BindView(R.id.sales_follower)
    CustomCrmItem sales_follower;

    //备注
    @BindView(R.id.sales_remark)
    CustomCrmItem sales_remark;

    //销售详情
    @BindView(R.id.ll_detail)
    AutoLinearLayout ll_detail;

    //详情
    @BindView(R.id.et_detail)
    EditText et_detail;

    //提交
    @BindView(R.id.sales_commit)
    TextView sales_commit;

    @BindView(R.id.tv_detail)
    TextView tv_detail;

    @BindView(R.id.sales_scrollview)
    ScrollView sales_scrollview;

    private String currentType;
    private String clueId;//线索ID
    private static final String TYPE_NEW = "NEW";//新建销售线索
    private static final String TYPE_EDIT = "EDIT";//编辑销售线索
    private static final String TYPE_BROWSER = "BROWSER";//浏览详情

    private CustomTextPicker mPicker;//线索来源弹框

    private PickerViewDialog addrPicker;//地址弹窗

    private String province ;//省
    private String city ;//市
    private String country;//区
    private Gson gson;
    private com.wyu.iwork.model.ClueDetail clueDetail;
    private Marketing.MarketingDetail market;
    private BusinessCardInfo info;
    private static final int REQUEST_CODE = 100;//在startActivityForResult时使用   请求码
    private static final int RESULT_CODE = 101;//在onActivityResult中使用    结果码

    public SalesLeadDetailFragment(String type){
        this.currentType = type;
        this.clueId = "0";
    }

    public SalesLeadDetailFragment(String type,String clueId){
        this.currentType = type;
        this.clueId = clueId;
    }

    public SalesLeadDetailFragment(String type, BusinessCardInfo info){
        this.currentType = type;
        this.clueId = "0";
        this.info = info;
    }

    private void initInfoView(BusinessCardInfo info){
        /**
         * 1:姓名
         * 2：联系电话
         * 3：邮箱
         * 4：职位
         * 5：公司名称
         */
        checkText(info.getData().getUser_name(),sales_name);
        checkText(info.getData().getPhone(),sales_tel);
        checkText(info.getData().getEmail(),sales_mail);
        checkText(info.getData().getPosition(),sales_position);
        checkText(info.getData().getCompany_name(),sales_company);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_lead_detail,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
        initDefault();
        initView();
    }

    private void initDefault(){
        showCustomAddressDialog();//初始化地址弹窗
        initItemTitle();
        setSingleLine();
        setItemBottomLineMustGone();
        ll_back.setOnClickListener(this);
        tv_edit.setText("删除");
        sales_commit.setOnClickListener(this);
        tv_edit.setOnClickListener(this);
        sales_scrollview.setOnTouchListener(this);
    }

    private void initView(){
        if(info != null){
            initInfoView(info);
        }
        if(TYPE_BROWSER.equals(currentType)){
            //浏览详情
            getClueDetail();
            tv_title.setText("线索详情");
            tv_edit.setVisibility(View.VISIBLE);
            sales_commit.setText("编辑");
            setKeyDescVisible(false);
            setRightArrowVisible(false);
            setAllTextViewValueVisible(true);
        }else if(TYPE_EDIT.equals(currentType)){
            //编辑销售线索
            tv_title.setText("编辑线索详情");
            sales_commit.setText("提交");
            tv_edit.setVisibility(View.VISIBLE);
            setKeyDescVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisible(false);
        }else if(TYPE_NEW.equals(currentType)){
            //新建销售线索
            tv_title.setText("新建线索");
            sales_commit.setText("提交");
            tv_edit.setVisibility(View.GONE);
            setKeyDescVisible(true);
            setRightArrowVisible(true);
            setAllTextViewValueVisible(false);
        }

    }

    @OnClick({R.id.sales_source,R.id.sales_address,R.id.sales_market_name})
    void Click(View v){
        if(TYPE_BROWSER.equals(currentType)){
            return;
        }
        switch (v.getId()){
            case R.id.sales_source:
                Hideinputwindown(sales_source.getEditText());
                showCustomResourceDialog(getActivity().getResources().getStringArray(R.array.crm_sales_source),sales_source);
                break;
            case R.id.sales_address:
                //地址
                Hideinputwindown(sales_address.getEditText());
                if (addrPicker!=null){
                    addrPicker.show_cityPicker();
                }
                break;
            case R.id.sales_market_name:
                if(TYPE_EDIT.equals(currentType)){
                    if (market == null){
                        if(!TextUtils.isEmpty(clueDetail.getData().getActivity_id())){
                            Marketing marketing = new Marketing();
                            market = marketing.new MarketingDetail();
                            market.setId(clueDetail.getData().getActivity_id());
                        }
                    }
                }
                Intent intent = new Intent(getActivity(),MarketingSelectorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("market",market);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
                break;
        }
    }

    //初始化每一项标题
    private void initItemTitle(){
        sales_name.setTitle("姓名");
        sales_company.setTitle("公司");
        sales_position.setTitle("职务");
        sales_tel.setTitle("联系电话");
        sales_address.setTitle("地址");
        sales_detail_address.setTitle("详细地址");
        sales_mail.setTitle("邮箱");
        sales_source.setTitle("线索来源");
        sales_market_name.setTitle("市场活动名称");
        sales_detail.setTitle("销售线索详情");
        sales_follower.setTitle("跟进人");
        sales_remark.setTitle("备注");

        sales_tel.setInputType(InputType.TYPE_CLASS_PHONE);
        sales_mail.setDiaitals();
    }

    private void setSingleLine(){
        CustomCrmItem[] itemArr = {sales_name,sales_company,sales_position,sales_tel,sales_address,
                sales_detail_address,sales_mail,sales_source,sales_market_name,
                sales_detail,sales_follower,sales_remark};
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
        sales_position.setBottomLineVisible(false);
        sales_mail.setBottomLineVisible(false);
        sales_detail.setBottomLineVisible(false);
        sales_remark.setBottomLineVisible(false);
    }

    //设置必填选项提示是否显示
    private void setKeyDescVisible(boolean flag){
        sales_name.setKeyDescVisible(flag);
        sales_company.setKeyDescVisible(false);
        sales_position.setKeyDescVisible(false);
        sales_tel.setKeyDescVisible(flag);
        sales_address.setKeyDescVisible(false);
        sales_detail_address.setKeyDescVisible(false);
        sales_mail.setKeyDescVisible(false);
        sales_source.setKeyDescVisible(flag);
        sales_market_name.setKeyDescVisible(false);
        sales_detail.setKeyDescVisible(false);
        sales_follower.setKeyDescVisible(false);
        sales_remark.setKeyDescVisible(false);
    }

    //设置右边箭头是否显示
    private void setRightArrowVisible(boolean flag){
        sales_name.setRightVisible(false);
        sales_company.setRightVisible(false);
        sales_position.setRightVisible(false);
        sales_tel.setRightVisible(false);
        sales_address.setRightVisible(flag);
        sales_detail_address.setRightVisible(false);
        sales_mail.setRightVisible(false);
        sales_source.setRightVisible(flag);
        sales_market_name.setRightVisible(flag);
        sales_detail.setRightVisible(false);
        sales_follower.setRightVisible(false);
        sales_remark.setRightVisible(false);
    }

    //设置显示EditText 还是显示TextView
    private void setAllTextViewValueVisible(boolean flag){

        sales_name.setValueVisible(flag);
        sales_company.setValueVisible(flag);
        sales_position.setValueVisible(flag);
        sales_tel.setValueVisible(flag);
        sales_address.setValueVisible(true);
        sales_detail_address.setValueVisible(flag);
        sales_mail.setValueVisible(flag);
        sales_source.setValueVisible(true);
        sales_market_name.setValueVisible(true);
        sales_detail.setValueVisible(false);
        sales_follower.setValueVisible(flag);
        sales_remark.setValueVisible(flag);

        sales_name.setEditVisible(!flag);
        sales_company.setEditVisible(!flag);
        sales_position.setEditVisible(!flag);
        sales_tel.setEditVisible(!flag);
        sales_address.setEditVisible(false);
        sales_detail_address.setEditVisible(!flag);
        sales_mail.setEditVisible(!flag);
        sales_source.setEditVisible(false);
        sales_market_name.setEditVisible(false);
        sales_detail.setEditVisible(false);
        sales_follower.setEditVisible(!flag);
        sales_remark.setEditVisible(!flag);
        tv_detail.setVisibility(flag?View.VISIBLE:View.GONE);
        et_detail.setVisibility(flag?View.GONE:View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.ll_back:
                getActivity().onBackPressed();
                break;
            case R.id.tv_edit:
                showDeleteDialog();
                break;
            case R.id.sales_commit:
                //
                if(TYPE_BROWSER.equals(currentType)){
                    currentType = TYPE_EDIT;
                    initView();
                }else{
                    newAndEditClue();
                }
                break;
        }
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

    //初始化地址弹窗
    private void showCustomAddressDialog(){

        addrPicker = new PickerViewDialog(getActivity(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                province =  addrPicker.options1Items.get(options1).getPickerViewText();
                city =  addrPicker.options2Items.get(options1).get(options2);
                country =  addrPicker.options3Items.get(options1).get(options2).get(options3);
                Logger.i(TAG,"OPTIONS1="+province+"options2="+city+"options3="+country);
                sales_address.setValue(province+city+country);
            }
        });
    }

    //删除提醒弹窗
    private void showDeleteDialog(){
        new MyCustomDialogDialog(6, getActivity(), R.style.MyDialog, "确定要删除该线索吗?", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteClue();
                dialog.dismiss();
            }
        }).show();
    }

    //删除该线索详情
    private void deleteClue(){
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+MyApplication.userInfo.getUser_id()+clueId);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("clue_id",clueId);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_CLUE,data);
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

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });

        if(data != null){
            data.clear();
            data = null;
        }
    }

    //获取销售线索详情
    private void getClueDetail(){
        /**
         *  user_id	    是   	int[11]     用户ID
            clue_id	    是   	int[11]     线索ID
            F	          是   	string[18]  请求来源：IOS/ANDROID/WEB
            V	          是   	string[20]  版本号如：1.0.1
            RandStr	    是   	string[50]  请求加密随机数 time().|.rand()
            Sign	          是   	string[400] 请求加密值 F_moffice_encode(F.V.RandStr.user_id.clue_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+MyApplication.userInfo.getUser_id()+clueId);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("clue_id",clueId);
        data.put("F",Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CLUE_DETAIL,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).
                execute(new DialogCallback(getActivity()) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.i(TAG,s);
                if(gson == null){
                    gson = new Gson();
                }
                try {
                    clueDetail = gson.fromJson(s,ClueDetail.class);
                    setData();
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

    //设置数据
    private void setData(){
        //放置数据解析异常
        try {
            province = clueDetail.getData().getProvince();
            city = clueDetail.getData().getCity();
            country = clueDetail.getData().getDistrict();
            checkText(clueDetail.getData().getName(),sales_name);
            checkText(clueDetail.getData().getCompany(),sales_company);
            checkText(clueDetail.getData().getJob(),sales_position);
            checkText(clueDetail.getData().getPhone(),sales_tel);
            checkText(clueDetail.getData().getProvince()+clueDetail.getData().getCity()+clueDetail.getData().getDistrict(),sales_address);
            checkText(clueDetail.getData().getAddress(),sales_detail_address);
            checkText(clueDetail.getData().getEmail(),sales_mail);
            checkText(getResources().getStringArray(R.array.crm_sales_source)[Integer.parseInt(clueDetail.getData().getSource_type())-1],sales_source);
            checkText(clueDetail.getData().getActivity_name(),sales_market_name);
            checkText(clueDetail.getData().getFollow_user(),sales_follower);
            checkText(clueDetail.getData().getRemark(),sales_remark);
            if(!TextUtils.isEmpty(clueDetail.getData().getDetail())){
                tv_detail.setText(clueDetail.getData().getDetail());
            }else{
                tv_detail.setHint("未填写");
            }
            if(!TextUtils.isEmpty(clueDetail.getData().getDetail())){
                et_detail.setText(clueDetail.getData().getDetail());
            }else{
                et_detail.setHint("请输入");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //检查数据是否为空  并做出相应的处理
    private void checkText(String text,CustomCrmItem item){
        if (!TextUtils.isEmpty(text)){
            item.setValue(text);
            item.setEditText(text);
        }else{
            item.setHintValue("未填写");
            item.setHintText("请输入");
        }
    }

    //新建  编辑销售线索
    private void newAndEditClue(){
        ////判断必填项是否全部填写
        if(TextUtils.isEmpty(sales_name.getEditTextValue()) ||
                TextUtils.isEmpty(sales_tel.getEditTextValue()) ||
                TextUtils.isEmpty(sales_source.getValue())){
            showRedmineDialog();
            return;
        }
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",MyApplication.userInfo.getUser_id());
        data.put("clue_id",clueId);
        checkData(province,data,"province");
        checkData(city,data,"city");
        checkData(country,data,"district");
        CustomCrmItem[] itemArr = {sales_name,sales_company,sales_position,sales_tel,sales_detail_address,
                sales_mail,sales_follower,sales_remark};
        String[] keyArr = {"name","company","job","phone","address","email","follow_user","remark"};
        for(int i = 0;i<itemArr.length;i++){
            checkData(itemArr[i].getEditTextValue(),data,keyArr[i]);
        }
        if(TYPE_EDIT.equals(currentType)){
            if(market == null){
                checkData(clueDetail.getData().getActivity_id(),data,"activity_id");
            }else{
                checkData(market.getId(),data,"activity_id");
            }
        }else{
            if(market != null){
                checkData(market.getId(),data,"activity_id");
            }
        }
        String[] sourceTypeArr = getResources().getStringArray(R.array.crm_sales_source);
        for(int i = 0;i<sourceTypeArr.length;i++){
            if(!TextUtils.isEmpty(sales_source.getValue())){
                if(sourceTypeArr[i].equals(sales_source.getValue())){
                    data.put("source_type",i+1+"");
                }
            }
        }
        checkData(et_detail.getText().toString(),data,"detail");
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id()+clueId);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_UPDATE_CLUE,data);
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
        if(data != null){
            data.clear();
            data = null;
        }
        if(itemArr != null){
            itemArr = null;
        }
        if(keyArr != null){
            keyArr = null;
        }
        if(sourceTypeArr != null){
            sourceTypeArr = null;
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

    private void checkData(String text, HashMap<String,String> data,String key){
        if(!TextUtils.isEmpty(text)){
            data.put(key,text);
        }
    }

    //回调

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE){
            market = (Marketing.MarketingDetail) data.getSerializableExtra("market");
            sales_market_name.setValue(market.getTitle());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(sales_scrollview);
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
