package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CustomerModel;
import com.wyu.iwork.model.GoodsModel;
import com.wyu.iwork.model.OptionsModel;
import com.wyu.iwork.model.SaleOrderDetailModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateSaleOrderUtil;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 销售订单详情
 */

public class SalesOrderDetailActivity extends BaseActivity implements TextWatcher {

    private static final String TAG = SalesOrderDetailActivity.class.getSimpleName();

    public static final int CODE_001 = 0x001;
    public static final int CODE_002 = 0x002;
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;
    /**
     * 编辑 保存
     */
    @BindView(R.id.activity_sales_order_detail_order_commit)
    TextView btn_edi_save;

    /**
     * 订单编号
     */
    @BindView(R.id.activity_sales_order_detail_orderNum)
    CustomeViewGroup orderNum;
    /**
     * 客户名称
     */
    @BindView(R.id.activity_sales_order_detail_CustomerName)
    CustomeViewGroup CustomerName;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_sales_order_detail_order_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_sales_order_detail_order_goodsPrise)
    CustomeViewGroup goodsPrise;
    /**
     * 商品数量
     */
    @BindView(R.id.activity_sales_order_detail_order_salegoodsNum)
    CustomeViewGroup goodsNum_cus;
    /**
     * 商品数量
     */
    @BindView(R.id.activity_sales_order_detail_order_salegoodszhekou)
    CustomeViewGroup cus_zhekou;

    /**
     * 折扣
     */
    @BindView(R.id.activity_sales_order_detail_order_zhekou)
    EditText discount;
    /**
     * 商品数量
     */
    @BindView(R.id.activity_sales_order_detail_order_goodsNum)
    EditText goodsNum;

    /**
     * 总金额
     */
    @BindView(R.id.activity_sales_order_detail_order_total)
    CustomeViewGroup total;

    /**
     * 订单状态
     */
    @BindView(R.id.activity_sales_order_detail_order_state)
    CustomeViewGroup order_state;

    /**
     * 下单日期
     */
    @BindView(R.id.activity_sales_order_detail_order_date)
    CustomeViewGroup order_date;

    /**
     * 发货方式
     */
    @BindView(R.id.activity_sales_order_detail_order_Delivery_type)
    CustomeViewGroup Delivery_type;
    /**
     * 创建人
     */
    @BindView(R.id.activity_sales_order_detail_order_main_releaser)
    CustomeViewGroup releaser;

    /**
     * 编辑状态下的商品数量布局
     */
    @BindView(R.id.activity_sales_order_detail_order_salegoodsNum_layout)
    LinearLayout goodsNum_layout;
    /**
     * 编辑状态下的折扣布局
     */
    @BindView(R.id.activity_sales_order_detail_order_salegoodszhekou_layout)
    LinearLayout zhekou_layout;

    //时间选择器
    private PickerViewDialog timepicke;

    //订单状态选择器
    private PickerViewDialog statepicker;
    //配送方式选择器
    private PickerViewDialog sendTypepicker;
    /**
     * 创建时间
     */
    @BindView(R.id.activity_sales_order_detail_order_main_releaseTime)
    CustomeViewGroup releaseTime;
    /**
     * 个人信息
     */
    private UserInfo userInfo;

    //订单状态集合
    private List<OptionsModel> StateList;
    //配送方式集合
    private List<OptionsModel> sendTypeList;
    //控件集合
    private List<CustomeViewGroup> viewGroups = new ArrayList<>();

    //销售订单id
    private String sale_order_id = "";

    //销售订单详情model
    private SaleOrderDetailModel model;

    //修改的model
    private SaleOrderDetailModel edit_model = new SaleOrderDetailModel();

    //获取详情的url
    private String url = Constant.URL_SALE_ORDER_DETAI;
    //删除的url
    private String del_url = Constant.URL_SALE_ORDER_DELETE;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order_detail);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        getId();
        init();
        initDetaiParama();
    }

    //获取销售订单id
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            sale_order_id = bundle.getString("id");
        }
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        edit.setVisibility(View.GONE);
        btn_edi_save.setVisibility(View.GONE);
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("销售订单详情");
        StateList = new ArrayList<>();
        sendTypeList = new ArrayList<>();
        StateList.add(new OptionsModel("待审批"));
        StateList.add(new OptionsModel("已审批"));
        sendTypeList.add(new OptionsModel("物流快递"));
        sendTypeList.add(new OptionsModel("上门自提"));
        sendTypeList.add(new OptionsModel("送货上门"));

        viewGroups.add(CustomerName);
        viewGroups.add(goodsName);
        viewGroups.add(goodsPrise);
        viewGroups.add(goodsNum_cus);
        viewGroups.add(cus_zhekou);
        viewGroups.add(total);
        viewGroups.add(order_state);
        viewGroups.add(order_date);
        viewGroups.add(Delivery_type);

        edit.setVisibility(View.GONE);
        releaser.setRightText(userInfo.getUser_name());
        long currenttime = System.currentTimeMillis();
        releaseTime.setRightText(DateUtil.ChartTime(currenttime));
        discount.addTextChangedListener(this);
        goodsNum.addTextChangedListener(this);
        timepicke = new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                order_date.setRightText(getTime(date));
            }
        });

        statepicker = new PickerViewDialog(this, StateList, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int statu = options1+1;
                String text =    StateList.get(options1).getPickerViewText();
                order_state.setRightText(text);
                edit_model.setStatus(statu+"");
            }
        });

        sendTypepicker = new PickerViewDialog(this, sendTypeList, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                int deliver = options1+1;
                String text =    sendTypeList.get(options1).getPickerViewText();
                Delivery_type.setRightText(text);
                edit_model.setDeliver(deliver+"");
            }
        });

        isEdit(false);
    }

    //编辑 正常状态下的控件显示
    private void isEdit(boolean isEdit){
        if (isEdit){//编辑状态下
            goodsNum_layout.setVisibility(View.VISIBLE);
            zhekou_layout.setVisibility(View.VISIBLE);
            for (int i = 0;i<viewGroups.size();i++){
                viewGroups.get(i).is_Showarrow(true);
                viewGroups.get(i).isShowStar(true);
                viewGroups.get(i).setClickable(true);
                if (i==2||i==5){
                    viewGroups.get(i).is_Showarrow(false);
                }
                if (i==4||i==3){
                    viewGroups.get(i).setVisibility(View.GONE);
                }
            }
        }else {//正常状态下
            goodsNum_layout.setVisibility(View.GONE);
            zhekou_layout.setVisibility(View.GONE);
            for (int i = 0;i<viewGroups.size();i++){
                viewGroups.get(i).is_Showarrow(false);
                viewGroups.get(i).setClickable(false);
                viewGroups.get(i).isShowStar(false);
                viewGroups.get(i).setVisibility(View.VISIBLE);
                if (!discount.getText().toString().isEmpty()){
                    cus_zhekou.setRightText(discount.getText().toString().trim());
                }

            }
        }
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_sales_order_detail_CustomerName,R.id.activity_sales_order_detail_order_goodsName,R.id.activity_sales_order_detail_order_reduce,R.id.activity_sales_order_detail_order_Delivery_type,
            R.id.activity_sales_order_detail_order_add,R.id.activity_sales_order_detail_order_state,R.id.activity_sales_order_detail_order_date,R.id.activity_sales_order_detail_order_commit})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back://返回
                onBackPressed();
                break;

            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"sale_order_id",sale_order_id,del_callback());
                break;
            case R.id.activity_sales_order_detail_CustomerName://客户名称
                intent = new Intent(this,SelectCustmerActivity.class);
                intent.putExtra("flag","SalesOrderDetailActivity");
                startActivityForResult(intent,CODE_001);
                break;
            case R.id.activity_sales_order_detail_order_goodsName://商品名称
                intent = new Intent(this,GoodsManagerActivity.class);
                intent.putExtra("flag","SalesOrderDetailActivity");
                startActivityForResult(intent,CODE_002);
                break;
            case R.id.activity_sales_order_detail_order_reduce://商品数量 减
                String s_num = goodsNum.getText().toString();
                if (s_num==null||"".equals(s_num)){
                    s_num = "0";
                }
                int num = Integer.parseInt(s_num);
                if (num>0){
                    num--;
                }
                goodsNum.setText(num+"");
                goodsNum_cus.setRightText(num+"");

                setTotal();
                break;
            case R.id.activity_sales_order_detail_order_add://商品数量 加
                String add_num = goodsNum.getText().toString();
                if (add_num==null||"".equals(add_num)){
                    add_num = "0";
                }
                int addnum = Integer.parseInt(add_num);
                addnum++;
                goodsNum.setText(addnum+"");
                goodsNum_cus.setRightText(addnum+"");
                setTotal();
                break;

            case R.id.activity_sales_order_detail_order_state://订单状态
                if (statepicker!=null){
                    statepicker.show_Options();
                }
                break;

            case R.id.activity_sales_order_detail_order_date://下单日期
                if (timepicke!=null){
                    timepicke.show_timepicker();
                }
                break;

            case R.id.activity_sales_order_detail_order_Delivery_type://发货方式
                if (sendTypepicker!=null){
                    sendTypepicker.show_Options();
                }
                break;

            case R.id.activity_sales_order_detail_order_commit://编辑
                String text = btn_edi_save.getText().toString();
                if ("编辑".equals(text)){//编辑 （编辑状态下）
                    btn_edi_save.setText("保存");
                    isEdit(true);
                }else {//保存（正常状态下）
                    String goodsnum = goodsNum.getText().toString();
                    String disscount = discount.getText().toString();

                    if (goodsnum.isEmpty()){
                        Toast.makeText(SalesOrderDetailActivity.this, "商品数量不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }  if (disscount.isEmpty()){
                        Toast.makeText(SalesOrderDetailActivity.this, "折扣不能为空", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("0".equals(goodsnum)){
                        Toast.makeText(SalesOrderDetailActivity.this, "商品数量不能为0", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("0".equals(disscount)){
                        Toast.makeText(SalesOrderDetailActivity.this, "折扣不能为0", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    for (int i = 0;i<viewGroups.size();i++){
                        String text1 = viewGroups.get(i).getRightText();
                        if (text.isEmpty()||"请选择".equals(text1)){
                            Toast.makeText(SalesOrderDetailActivity.this, viewGroups.get(i).getNametext()+"不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    edit_model.setId(sale_order_id);
                    edit_model.setPlace_time(order_date.getRightText());
                    edit_model.setGoods_num(goodsnum);
                    edit_model.setDiscount(disscount);
                    UpdateSaleOrderUtil.initParama(this,Editcallback(),edit_model);
                }
                break;
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null) {
            switch (requestCode) {
                case CODE_001://客户名称
                    CustomerModel item = (CustomerModel) data.getExtras().get("model");
                    edit_model.setCustomer_id(item.getId());
                    CustomerName.setRightText(item.getName());
                    break;

                case CODE_002://商品名称
                    GoodsModel model = (GoodsModel) data.getExtras().get("model");
                    goodsName.setRightText(model.getName());
                    goodsPrise.setRightText(model.getPrice());
                    edit_model.setGoods_id(model.getId());
                    break;
            }
        }
    }

    //计算总金额
    private void setTotal(){
        try {
            String sprise = goodsPrise.getRightText();
            String snum = goodsNum.getText().toString().trim();
            String sdiscount = discount.getText().toString().trim();
            if (!sprise.isEmpty()&&!snum.isEmpty()&&!sdiscount.isEmpty()){
                float mprise = Float.parseFloat(sprise.equals("")?"0":sprise);
                int mdiscount = Integer.parseInt(sdiscount.equals("")?"0":sdiscount);
                int nums = Integer.parseInt(snum);
                float mtotal = (mprise * nums * mdiscount)/100;
                DecimalFormat fnum  =   new  DecimalFormat("##0.00");//保留两位小数
                String dd=fnum.format(mtotal);
                total.setRightText(dd);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            total.setRightText("100.00");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()==0){
            total.setRightText("");
        }else {
            int acount = Integer.parseInt(s.toString());
            if (acount>100){
                discount.setText("100");
                discount.setSelection(s.length());
                Hideinputwindown(discount);
                Toast.makeText(SalesOrderDetailActivity.this,"折扣范围为0 — 100",Toast.LENGTH_SHORT).show();
                return;
            }
            setTotal();
        }
    }



    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + sale_order_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("sale_order_id", sale_order_id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        Logger.e(TAG,url);
        OkgoRequest(url,callback());
    }

    //获取销售订单的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),SaleOrderDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(SalesOrderDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }
    //获取销售订单的请求回调
    private DialogCallback del_callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                     finish();
                    }else {
                        Toast.makeText(SalesOrderDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //控件赋值
    private void initviewDatas(){
        if (model!=null){
            getPermission(model.getUser_id());
            orderNum.setRightText(model.getSole_id());
            CustomerName.setRightText(model.getCustomer_name());
            CustomerName.setInputContent(model.getCustomer_name());
            goodsName.setRightText(model.getGoods_name());
            goodsName.setInputContent(model.getGoods_name());
            goodsPrise.setRightText(model.getGoods_price());
            goodsPrise.setInputContent(model.getGoods_price());
            goodsNum_cus.setRightText(model.getGoods_num());
            goodsNum_cus.setInputContent(model.getGoods_num());
            cus_zhekou.setRightText(model.getDiscount());
            cus_zhekou.setInputContent(model.getDiscount());
            total.setRightText(model.getGoods_amount());
            total.setInputContent(model.getGoods_amount());

            order_date.setRightText(model.getAdd_time());
            order_date.setInputContent(model.getAdd_time());

            switch (model.getDeliver()){
                case "1":
                    Delivery_type.setRightText("物流快递");
                    Delivery_type.setInputContent("物流快递");
                    break;
                case "2":
                    Delivery_type.setRightText("上门自提");
                    Delivery_type.setInputContent("上门自提");
                    break;
                case "3":
                    Delivery_type.setRightText("送货上门 ");
                    Delivery_type.setInputContent("送货上门 ");
                    break;
            }

            switch (model.getStatus()){
                case "1":
                    order_state.setRightText("待审批");
                    order_state.setInputContent("待审批");
                    break;
                case "2":
                    order_state.setRightText("已审批");
                    order_state.setInputContent("已审批");
                    break;
            }

            releaser.setRightText(model.getUser_name());
            releaser.setInputContent(model.getUser_name());
            releaseTime.setRightText(model.getAdd_time());
            releaseTime.setInputContent(model.getAdd_time());


            edit_model.setStatus(model.getStatus());
            edit_model.setDeliver(model.getDeliver());
        }
    }

    //编辑销售订单回调
    private DialogCallback Editcallback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        btn_edi_save.setText("编辑");
                        isEdit(false);

                    }
                    Toast.makeText(SalesOrderDetailActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //权限判断  是管理员或者是自己新建的则可以编辑和删除
    private void getPermission(String user_id){
        String admin = userInfo.getIs_admin(); //是否是管理员用户  0：否，1：是
        if ("1".equals(admin)||userInfo.getUser_id().equals(user_id)){
            edit.setVisibility(View.VISIBLE);
            btn_edi_save.setVisibility(View.VISIBLE);
        }
    }
}
