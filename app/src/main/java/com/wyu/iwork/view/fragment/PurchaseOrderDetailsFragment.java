package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.PurchaseDetailModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sxliu on 2017/5/9.
 * 采购订单详情fragment
 */

public class PurchaseOrderDetailsFragment extends Fragment {


    /**
     * 采购单号
     */
    @BindView(R.id.activity_purchase_order_details_orderNum)
    CustomeViewGroup orderNum;
    /**
     * 采购类型
     */
    @BindView(R.id.activity_purchase_order_details_type)
    CustomeViewGroup ordertype;
    /**
     * 商品名称
     */
    @BindView(R.id.activity_purchase_order_details_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_purchase_order_details_goodsPrise)
    CustomeViewGroup goodsPrise;

    /**
     * 商品数量
     */
    @BindView(R.id.activity_purchase_order_details_nums)
    CustomeViewGroup goodsNum;

    /**
     * 折扣
     */
    @BindView(R.id.activity_purchase_order_details_zhekou)
    CustomeViewGroup zhekou;

    /**
     * 总金额
     */
    @BindView(R.id.activity_purchase_order_details_total_pay)
    CustomeViewGroup total;

    /**
     * 供应商名称
     */
    @BindView(R.id.activity_purchase_order_details_supplier_name)
    CustomeViewGroup supplier_name;

    /**
     * 主要联系人
     */
    @BindView(R.id.activity_purchase_order_details_main_contact)
    CustomeViewGroup main_releaser;

    /**
     * 联系电话
     */
    @BindView(R.id.activity_purchase_order_details_phone)
    CustomeViewGroup phone;

    /**
     * 运货方式
     */
    @BindView(R.id.activity_purchase_order_delivery_mode)
    CustomeViewGroup delivery_mode;

    /**
     * 到货时间
     */
    @BindView(R.id.activity_purchase_order_arrive_time)
    CustomeViewGroup arrive_time;

    /**
     * 创建人
     */
    @BindView(R.id.activity_purchase_order_releaser)
    CustomeViewGroup order_releaser;
    /**
     * 创建时间
     */
    @BindView(R.id.activity_purchase_order_release_time)
    CustomeViewGroup  release_time;

    private String url = Constant.URL_PURCHASE_DETAIL;

    private PurchaseOrderDetailsActivity activity;

    private View rootview;

    public UserInfo userInfo;
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
            rootview = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_purchase_order_details,null);
            ButterKnife.bind(this,rootview);
            initParama();
        }
        return rootview;
    }

    private void initParama() {
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr +activity.id+user_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("id", activity.id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        activity.OkgoRequest(url,callback());
    }

    //获取供货商详情的请求回调
    private DialogCallback callback() {
        return new DialogCallback(getActivity()) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),PurchaseDetailModel.class);
                        activity.model = model;
                        initviewDatas();

                    }else {
                        Toast.makeText(activity,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } ;
    }

    /**
     * 给控件赋值
     */
    private void initviewDatas(){
        if (model!=null){
            activity.getPermission(model.getUser_id());
            orderNum.setRightText(model.getSole_id());
            switch (model.getType()){
                case "0":
                    ordertype.setRightText("其他 ");
                    break;
                case "1":
                    ordertype.setRightText("销售商品");
                    break;
                case "2":
                    ordertype.setRightText("日常用品");
                    break;
            }
            goodsName.setRightText(model.getGoods_name());
            goodsPrise.setRightText(model.getGoods_price());
            goodsNum.setRightText(model.getGoods_num());
            zhekou.setRightText(model.getDiscount());
            total.setRightText(model.getGoods_amount());
            supplier_name.setRightText(model.getSupplier_name());
            main_releaser.setRightText(model.getSupplier_contacst());
            phone.setRightText(model.getSupplier_phone());
            delivery_mode.setRightText(model.getFreight());
            arrive_time.setRightText(model.getArrive_time());
            order_releaser.setRightText(model.getUser_name());
            release_time.setRightText(model.getAdd_time());
        }
    }
}
