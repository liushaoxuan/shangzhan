package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.model.CustomerModel;
import com.wyu.iwork.model.GoodsModel;
import com.wyu.iwork.model.OptionsModel;
import com.wyu.iwork.model.SaleOrderDetailModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateSaleOrderUtil;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 新建销售订单
 */
public class BuildSalesOrderActivity extends BaseActivity implements TextWatcher {

    private static final String TAG = BuildSalesOrderActivity.class.getSimpleName();


    public static final int CODE_001 = 0x01;
    public static final int CODE_002 = 0x02;
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 客户名称
     */
    @BindView(R.id.activity_build_sales_CustomerName)
    CustomeViewGroup CustomerName;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_sales_order_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_build_sales_order_goodsPrise)
    CustomeViewGroup goodsPrise;

    /**
     * 折扣
     */
    @BindView(R.id.activity_build_sales_order_zhekou)
    EditText discount;
    /**
     * 商品数量
     */
    @BindView(R.id.activity_build_sales_order_goodsNum)
    EditText goodsNum;

    /**
     * 总金额
     */
    @BindView(R.id.activity_build_sales_order_total)
    CustomeViewGroup total;

    /**
     * 订单状态
     */
    @BindView(R.id.activity_build_sales_order_state)
    CustomeViewGroup order_state;

    /**
     * 下单日期
     */
    @BindView(R.id.activity_build_sales_order_date)
    CustomeViewGroup order_date;

    /**
     * 发货方式
     */
    @BindView(R.id.activity_build_sales_order_Delivery_type)
    CustomeViewGroup Delivery_type;
    /**
     * 创建人
     */
    @BindView(R.id.activity_build_sales_order_main_releaser)
    CustomeViewGroup releaser;

    //时间选择器
    private PickerViewDialog timepicke;

    //订单状态选择器
    private PickerViewDialog statepicker;
    //配送方式选择器
    private PickerViewDialog sendTypepicker;
    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_sales_order_main_releaseTime)
    CustomeViewGroup releaseTime;
    /**
     * 个人信息
     */
    private UserInfo userInfo;

    //订单状态集合
    private List<OptionsModel> StateList;
    //配送方式集合
    private List<OptionsModel> sendTypeList;

    //新增的销售订单model
    private SaleOrderDetailModel addModel = new SaleOrderDetailModel();

    //控件集合
    private List<CustomeViewGroup> viewGroupList = new ArrayList<>();

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_sales_order);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        iniviewgrop();
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("新建销售订单");
        StateList = new ArrayList<>();
        sendTypeList = new ArrayList<>();
        StateList.add(new OptionsModel("待审批"));
        StateList.add(new OptionsModel("已审批"));
        sendTypeList.add(new OptionsModel("物流快递"));
        sendTypeList.add(new OptionsModel("上门自提"));
        sendTypeList.add(new OptionsModel("送货上门"));
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
             String text =    StateList.get(options1).getPickerViewText();
                order_state.setRightText(text);
                int statu = options1+1;
                addModel.setStatus(statu+"");
//                Toast.makeText(BuildSalesOrderActivity.this,statu+"",Toast.LENGTH_SHORT).show();
            }
        });

        sendTypepicker = new PickerViewDialog(this, sendTypeList, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String text =    sendTypeList.get(options1).getPickerViewText();
                Delivery_type.setRightText(text);
                int deliver = options1+1;
                addModel.setDeliver(deliver+"");
            }
        });
    }

    //控件集合
    private void iniviewgrop(){
        viewGroupList.add(CustomerName);
        viewGroupList.add(goodsName);
        viewGroupList.add(goodsPrise);
        viewGroupList.add(total);
        viewGroupList.add(order_state);
        viewGroupList.add(order_date);
        viewGroupList.add(Delivery_type);
    }

    @OnClick({R.id.action_back,R.id.activity_build_sales_CustomerName,R.id.activity_build_sales_order_goodsName,R.id.activity_build_sales_order_reduce,R.id.activity_build_sales_order_Delivery_type,
            R.id.activity_build_sales_order_add,R.id.activity_build_sales_order_state,R.id.activity_build_sales_order_date,R.id.activity_build_sales_order_commit})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back://返回
                onBackPressed();
                break;
            case R.id.activity_build_sales_CustomerName://客户名称
                intent = new Intent(this,SelectCustmerActivity.class);
                intent.putExtra("flag","BuildSalesOrderActivity");
                startActivityForResult(intent,CODE_001);

                break;
            case R.id.activity_build_sales_order_goodsName://商品名称
                intent = new Intent(this,GoodsManagerActivity.class);
                intent.putExtra("flag","BuildSalesOrderActivity");
                startActivityForResult(intent,CODE_002);
                break;
            case R.id.activity_build_sales_order_reduce://商品数量 减
                String s_num = goodsNum.getText().toString();
                if (s_num==null||"".equals(s_num)){
                    s_num = "0";
                }
                int num = Integer.parseInt(s_num);
                if (num>0){
                    num--;
                }
                goodsNum.setText(num+"");
                setTotal();
                break;
            case R.id.activity_build_sales_order_add://商品数量 加
                String add_num = goodsNum.getText().toString();
                if (add_num==null||"".equals(add_num)){
                    add_num = "0";
                }
                int addnum = Integer.parseInt(add_num);
                addnum++;
                goodsNum.setText(addnum+"");
                setTotal();
                break;

            case R.id.activity_build_sales_order_state://订单状态
                if (statepicker!=null){
                    statepicker.show_Options();
                }
                break;

            case R.id.activity_build_sales_order_date://下单日期
                if (timepicke!=null){
                    timepicke.show_timepicker();
                }
                break;

            case R.id.activity_build_sales_order_Delivery_type://发货方式
                    if (sendTypepicker!=null){
                        sendTypepicker.show_Options();
                    }
                break;

            case R.id.activity_build_sales_order_commit://提交

                String goodsnum = goodsNum.getText().toString();
                String disscount = discount.getText().toString();

                if (goodsnum.isEmpty()){
                    Toast.makeText(BuildSalesOrderActivity.this, "商品数量不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }  if (disscount.isEmpty()){
                    Toast.makeText(BuildSalesOrderActivity.this, "折扣不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ("0".equals(goodsnum)){
                    Toast.makeText(BuildSalesOrderActivity.this, "商品数量不能为0", Toast.LENGTH_SHORT).show();
                    break;
                }
                if ("0".equals(disscount)){
                    Toast.makeText(BuildSalesOrderActivity.this, "折扣不能为0", Toast.LENGTH_SHORT).show();
                    break;
                }

                for (int i = 0;i<viewGroupList.size();i++){
                    String text = viewGroupList.get(i).getRightText();
                    if (text.isEmpty()||"请选择".equals(text)){
                        Toast.makeText(BuildSalesOrderActivity.this, viewGroupList.get(i).getNametext()+"不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                addModel.setId("0");
                addModel.setPlace_time(order_date.getRightText());
                addModel.setGoods_num(goodsnum);
                addModel.setDiscount(disscount);
                UpdateSaleOrderUtil.initParama(this,callback(),addModel);
                break;
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            switch (requestCode){
                case CODE_001://客户名称
                    CustomerModel item = (CustomerModel) data.getExtras().get("model");
                   addModel.setCustomer_id(item.getId());
                    CustomerName.setRightText(item.getName());
                    break;

                case CODE_002://商品名称
                    GoodsModel model = (GoodsModel) data.getExtras().get("model");
                    goodsName.setRightText(model.getName());
                    goodsPrise.setRightText(model.getPrice());
                    addModel.setGoods_id(model.getId());
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
                Toast.makeText(BuildSalesOrderActivity.this,"折扣范围为0 — 100",Toast.LENGTH_SHORT).show();
                return;
            }
            setTotal();
        }
    }

    //新建销售订单回调
    private DialogCallback callback() {
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
                        finish();
                    }
                    Toast.makeText(BuildSalesOrderActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
