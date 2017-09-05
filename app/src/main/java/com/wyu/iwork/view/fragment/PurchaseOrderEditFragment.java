package com.wyu.iwork.view.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.PurchaseDetailModel;
import com.wyu.iwork.model.SurpplierManagerModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.AddOrderActivity;
import com.wyu.iwork.view.activity.GoodsManagerActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.SupplierManagerActivity;
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
 * Created by sxliu on 2017/5/9.
 * 订单编辑 update
 */

public class PurchaseOrderEditFragment extends Fragment implements TextWatcher {



    private static final String TAG = AddOrderActivity.class.getSimpleName();

    public static final int requestCode_goods = 0x0001;
    public static final int requestCode_Supplier = 0x0002;

    /**
     * 采购单号
     */
    @BindView(R.id.activity_add_order_Nums)
    CustomeViewGroup orderNum;
    /**
     * 采购类型
     */
    @BindView(R.id.activity_add_order_type)
    CustomeViewGroup order_type;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_add_order_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_add_order_goodsPrise)
    CustomeViewGroup goodsPrise;

    /**
     * 折扣
     */
    @BindView(R.id.activity_add_order_zhekou)
    EditText discount;
    /**
     * 商品数量
     */
    @BindView(R.id.activity_add_order_goodsNum)
    EditText goodsNum;

    /**
     * 总金额
     */
    @BindView(R.id.activity_add_order_total)
    CustomeViewGroup total;

    /**
     * 供应商名称
     */
    @BindView(R.id.activity_add_order_Supplier_name)
    CustomeViewGroup Supplier_name;

    /**
     * 主要联系人
     */
    @BindView(R.id.activity_add_order_main_contacts)
    CustomeViewGroup main_contacts;

    /**
     * 联系电话
     */
    @BindView(R.id.activity_add_order_main_contact_phone)
    CustomeViewGroup contact_phone;

    /**
     * 运货方式
     */
    @BindView(R.id.activity_add_order_main_sendType)
    CustomeViewGroup sendType;

    /**
     * 预计到货时间
     */
    @BindView(R.id.activity_add_order_main_arriveTime)
    CustomeViewGroup arriveTime;

    /**
     * 创建人
     */
    @BindView(R.id.activity_add_order_main_releaser)
    CustomeViewGroup releaser;

    private PickerViewDialog timepicke;
    /**
     * 创建时间
     */
    @BindView(R.id.activity_add_order_main_releaseTime)
    CustomeViewGroup releaseTime;

    //销售商品
    private TextView sail_textview;
    //日常商品
    private TextView dayly_textview;
    //其他
    private TextView other_textview;
    //采购类型 0：其他 1：销售商品 2：日常用品
    private String type = "";

    //商品Id
    private String goods_id = "";
    //供应商id
    private String supplier_id = "";

    /**
     * 个人信息
     */
    private UserInfo userInfo;

    /**
     * 采购类型的弹窗
     */
    private Dialog Procurement_dialog;

    /**
     * 采购类型的view
     */
    private View dialogView;

    //控件集合
    private List<CustomeViewGroup> viewGroups = new ArrayList<>();

    private String url = Constant.URL_PURCHASE_UPDATE;
    private View rootview;

    private PurchaseOrderDetailsActivity activity;
    /**
     * 订单详情model
     */
    private PurchaseDetailModel model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PurchaseOrderDetailsActivity) getActivity();
        userInfo = MyApplication.userInfo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootview==null){
            rootview = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_purchase_order_edit,null);
            ButterKnife.bind(this,rootview);
            init();
            ProcurementPop();
        }
        model = activity.model;
        initviewDatas();
        return rootview;
    }


    /**
     * 一些初始化操作
     */
    private void init(){
        userInfo = MyApplication.userInfo;
        releaser.setRightText(userInfo.getUser_name());
        long currenttime = System.currentTimeMillis();
        releaseTime.setRightText(DateUtil.ChartTime(currenttime));
        discount.addTextChangedListener(this);
        goodsNum.addTextChangedListener(this);
        timepicke = new PickerViewDialog(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                arriveTime.setRightText(getTime(date));
                arriveTime.setInputContent(getTime(date));
            }
        });

        viewGroups.add(order_type);
        viewGroups.add(goodsName);
        viewGroups.add(goodsPrise);
        viewGroups.add(total);
        viewGroups.add(Supplier_name);
        viewGroups.add(main_contacts);
        viewGroups.add(contact_phone);
        viewGroups.add(sendType);
        viewGroups.add(arriveTime);
    }

    @OnClick({R.id.activity_add_order_type,R.id.activity_add_order_goodsName,R.id.activity_add_order_reduce,
            R.id.activity_add_order_add,R.id.activity_add_order_Supplier_name,R.id.activity_add_order_main_arriveTime})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.activity_add_order_type://采购类型
                if (Procurement_dialog!=null){
                    Procurement_dialog.show();
                }
                break;
            case R.id.activity_add_order_goodsName://商品名称
                intent = new Intent(activity,GoodsManagerActivity.class);
                intent.putExtra("flag","PurchaseOrderEditFragment");
                startActivityForResult(intent,AddOrderActivity.requestCode_goods);
                break;
            case R.id.activity_add_order_reduce://商品数量 减
                try {
                    String s_num = goodsNum.getText().toString();
                    if (s_num==null||"".equals(s_num)){
                        s_num = "0";
                    }
                    int num = Integer.parseInt(s_num);
                    if (num>0){
                        num--;
                    }
                    goodsNum.setText(num+"");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    goodsNum.setText("0");
                }
                setTotal();
                break;
            case R.id.activity_add_order_add://商品数量 加
                try {
                    String add_num = goodsNum.getText().toString();
                    if (add_num==null||"".equals(add_num)){
                        add_num = "0";
                    }
                    int addnum = Integer.parseInt(add_num);
                    addnum++;
                    goodsNum.setText(addnum+"");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    goodsNum.setText("0");
                }
                setTotal();
                break;
            case R.id.activity_add_order_Supplier_name://供货商名称
                intent = new Intent(activity,SupplierManagerActivity.class);
                intent.putExtra("flag","PurchaseOrderEditFragment");
                startActivityForResult(intent,AddOrderActivity.requestCode_Supplier);
                break;
            case R.id.activity_add_order_main_arriveTime://预计到货时间
                if (timepicke!=null){
                    timepicke.show_timepicker();
                }
                break;

        }
    }

    //保存
    public void commit(){
        String goods_Num = goodsNum.getText().toString();//商品数量
        String m_disscount  = discount.getText().toString();//折扣

        for (int i =0;i<viewGroups.size();i++){
            String text = viewGroups.get(i).getInput();
            if (text.isEmpty()){
                Toast.makeText(activity, viewGroups.get(i).getNametext()+"不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (goods_Num.isEmpty()){
            Toast.makeText(activity,"商品数量不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (m_disscount.isEmpty()){
            Toast.makeText(activity,"折扣不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        initParama();
    }


    //初始化请求参数
    private void initParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String purchase_order_id = "0";//修改采购订单ID，新增传0
        String type = this.type;//采购类型 0：其他 1：销售商品 2：日常用品
        String goods_id = this.goods_id;//商品ID
        String goods_name = goodsName.getRightText();//商品名称
        String goods_price = goodsPrise.getRightText();//价格 单位：元
        String goods_num = goodsNum.getText().toString();//
        String discount = this.discount.getText().toString();//折扣
        String supplier_id = this.supplier_id;//供应商ID
        String freight = sendType.getInput();//运货方式
        String arrive_time = arriveTime.getInput();//预计到货时间

        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+type);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("purchase_order_id",purchase_order_id);
        map.put("type",type);
        map.put("goods_id",goods_id);
        map.put("goods_name",goods_name);
        map.put("goods_price",goods_price);
        map.put("goods_num",goods_num);
        map.put("discount",discount);
        map.put("supplier_id",supplier_id);
        map.put("freight",freight);
        map.put("arrive_time",arrive_time);
        map.put("F",Constant.F);
        map.put("V",Constant.V);
        map.put("RandStr",RandStr);
        map.put("Sign",sign);
        url = RequestUtils.getRequestUrl(url, map);
        activity.OkgoRequest(url,callback());
    }
    //新建采购订单回调
    private DialogCallback callback(){
        return new DialogCallback(getActivity()) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG,e.getMessage());
                Toast.makeText(activity,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        activity.btn_edit.setText("编辑");
                       activity.save();
                    }
                    Toast.makeText(activity,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
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
                activity.Hideinputwindown(discount);
                Toast.makeText(getActivity(),"折扣范围为0 — 100",Toast.LENGTH_SHORT).show();
                return;
            }
            setTotal();
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
                total.setInputContent(dd);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 采购类型的弹窗
     */
    private void ProcurementPop(){
        if(dialogView==null){
            dialogView = LayoutInflater.from(activity).inflate(R.layout.layout_pop_procurement_type,null);
            sail_textview = (TextView) dialogView.findViewById(R.id.procurement_type_saild_type);
            dayly_textview  = (TextView) dialogView.findViewById(R.id.procurement_type_dayly_type);
            other_textview = (TextView) dialogView.findViewById(R.id.procurement_type_others);
        }
        if (dialogView != null) {
            Procurement_dialog = new Dialog(activity, R.style.custom_dialog);
            Procurement_dialog.setCancelable(true);//不能点外面取消,也不 能点back取消
            Procurement_dialog.setContentView(dialogView);
            Window dialogWindow = Procurement_dialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            Procurement_dialog.getWindow().setWindowAnimations(R.style.pickerview_dialogAnim1);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
        }
        sail_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
                order_type.setRightText("销售商品");
                order_type.setInputContent("销售商品");
                type = "1";
            }
        });

        dayly_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
                order_type.setRightText("日常用品");
                order_type.setInputContent("日常用品");
                type = "2";
            }
        });
        other_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
                order_type.setRightText("其他");
                order_type.setInputContent("其他");
                type = "0";
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case requestCode_goods://商品名称
                goodsPrise.setRightText("5.00");
                goodsPrise.setInputContent("5.00");
                goodsName.setRightText("铅笔");
                goodsName.setInputContent("铅笔");
                goods_id = "3";
                break;

            case requestCode_Supplier://供应商
                if (data!=null){
                    SurpplierManagerModel model = (SurpplierManagerModel) data.getExtras().get("model");
                    Supplier_name.setRightText(model.getName());
                    Supplier_name.setInputContent(model.getName());
                    main_contacts.setRightText(model.getContacts());
                    main_contacts.setInputContent(model.getContacts());
                    contact_phone.setRightText(model.getPhone());
                    contact_phone.setInputContent(model.getPhone());
                    supplier_id = model.getId();
                }
                break;
        }
    }

    /**
     * 给控件赋值
     */
    private void initviewDatas(){
        if (model!=null){
            orderNum.setRightText(model.getSole_id());
            type = model.getType();
            switch (model.getType()){
                case "0":
                    order_type.setRightText("其他 ");
                    order_type.setInputContent("其他 ");

                    break;
                case "1":
                    order_type.setRightText("销售商品");
                    order_type.setInputContent("销售商品");
                    break;
                case "2":
                    order_type.setRightText("日常用品");
                    order_type.setInputContent("日常用品");
                    break;
                default:
                    type = "2";
                    order_type.setRightText("日常用品");
                    order_type.setInputContent("日常用品");
                    break;
            }
            goodsName.setRightText(model.getGoods_name());
            goodsName.setInputContent(model.getGoods_name());
            goodsPrise.setRightText(model.getGoods_price());
            goodsPrise.setInputContent(model.getGoods_price());
            goodsNum.setText(model.getGoods_num());
            discount.setText(model.getDiscount());
            total.setRightText(model.getGoods_amount());
            total.setInputContent(model.getGoods_amount());
            Supplier_name.setRightText(model.getSupplier_name());
            Supplier_name.setInputContent(model.getSupplier_name());
            main_contacts.setRightText(model.getSupplier_contacst());
            main_contacts.setInputContent(model.getSupplier_contacst());
            contact_phone.setRightText(model.getSupplier_phone());
            contact_phone.setInputContent(model.getSupplier_phone());
            sendType.setInputContent(model.getFreight());
            arriveTime.setRightText(model.getArrive_time());
            arriveTime.setInputContent(model.getArrive_time());
            releaser.setRightText(model.getUser_name());
            releaser.setInputContent(model.getUser_name());
            releaseTime.setRightText(model.getAdd_time());
            releaseTime.setInputContent(model.getAdd_time());
        }
    }
}
