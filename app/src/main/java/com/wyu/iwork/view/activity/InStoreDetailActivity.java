package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CargoDetailModel;
import com.wyu.iwork.model.InStoreDetailModel;
import com.wyu.iwork.model.PurchaseInModel;
import com.wyu.iwork.model.PurchaseModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateCargoUtil;
import com.wyu.iwork.util.UpdateInStoreUtil;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 入库详情
 */
public class InStoreDetailActivity extends BaseActivity {

    private static final String TAG = InStoreDetailActivity.class.getSimpleName();
    public final static int InStoreDetailCode = 0x2;
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;
    /**
     * 编辑保存
     */
    @BindView(R.id.activity_build_goods_instore_committ)
    TextView btn_edit;

    /**
     * 入库单号
     */
    @BindView(R.id.activity_build_goods_instore_inOrderNum)
    CustomeViewGroup inOrderNum;

    /**
     * 采购单号
     */
    @BindView(R.id.activity_build_goods_instore_buy_order)
    CustomeViewGroup buy_order;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_goods_instore_GoodsName)
    CustomeViewGroup GoodsName;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_build_goods_instore_StoreNum)
    CustomeViewGroup StoreNum;

    /**
     * 货位编号
     */
    @BindView(R.id.activity_build_goods_instore_CargoNum)
    CustomeViewGroup CargoNum;

    /**
     * 入库数量
     */
    @BindView(R.id.activity_build_goods_instore_instoreNums)
    CustomeViewGroup instoreNums;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_build_goods_instore_Goodsprise)
    CustomeViewGroup Goodsprise;

    /**
     * 折扣
     */
    @BindView(R.id.activity_build_goods_instore_disscount)
    CustomeViewGroup disscount;

    /**
     * 总金额
     */
    @BindView(R.id.activity_build_goods_instore_total)
    CustomeViewGroup total;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_goods_instore_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_goods_instore_releaseTime)
    CustomeViewGroup releaseTime;

    private UserInfo userInfo;

    private String url = Constant.URL_INSTORE_DETAIL;

    //删除地址
    private String del_url = Constant.URL_INSTORE_DELETE;
    /**
     * 入库id
     */
    private String store_put_id = "";

    //采购订单号
    private String purchase_id = "";

    //88入库详情model
    private InStoreDetailModel model;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_goods_instore);
        ButterKnife.bind(this);
        getId();
        getSupportActionBar().hide();
        init();
        initDetaiParama();
    }

    /**
     * 获取入库id
     */
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            store_put_id = bundle.getString("id");
        }
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        edit.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("入库详情");
        btn_edit.setText("编辑");
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        buy_order.isShowStar(false);
        buy_order.is_Showarrow(false);
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_build_goods_instore_buy_order,R.id.activity_build_goods_instore_committ})
    void Click(View v){
        String text = btn_edit.getText().toString();
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"store_put_id",store_put_id,del_callback());
                break;
            case R.id.activity_build_goods_instore_buy_order://采购单号
                if ("保存".equals(text)){
                    Intent intent = new Intent(this,PurchaseOrderInActivity.class);
                    intent.putExtra("flag","InStoreDetailActivity");
                    startActivityForResult(intent,InStoreDetailCode);
                }
                break;

            case R.id.activity_build_goods_instore_committ://编辑  保存

                if ("编辑".equals(text)){//编辑状态下
                    btn_edit.setText("保存");
                    buy_order.isShowStar(true);
                    buy_order.is_Showarrow(true);

                }else {
                    UpdateInStoreUtil.initParama(this,Editcallback(),purchase_id,store_put_id);

                }
                break;
        }
    }


    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + store_put_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("store_put_id", store_put_id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取入库详情的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),InStoreDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(InStoreDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }


    //删除入库的请求回调
    private DialogCallback del_callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                      finish();
                    }else {
                        Toast.makeText(InStoreDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            getPermission(model.getCreater_id());
            inOrderNum.setRightText(model.getSole_id());
            buy_order.setRightText(model.getPurchase_sole_id());
            GoodsName.setRightText(model.getGoods_name());
            StoreNum.setRightText(model.getStore_id());
            CargoNum.setRightText(model.getStore_goods_sole_id());
            instoreNums.setRightText(model.getNum());
            Goodsprise.setRightText(model.getPrice());
            disscount.setRightText(model.getDiscount());
            total.setRightText(model.getAmount());
            releaser.setRightText(model.getCreater_name());
            releaseTime.setRightText(model.getAdd_time());

            purchase_id = model.getPurchase_id();
        }
    }

    //新建入库回调
    private DialogCallback Editcallback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG,e.getMessage());
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        btn_edit.setText("编辑");
                        buy_order.isShowStar(false);
                        buy_order.is_Showarrow(false);
                    }
                    Toast.makeText(InStoreDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==InStoreDetailCode){
            PurchaseInModel model = (PurchaseInModel) data.getExtras().get("model");
            buy_order.setRightText(model.getSole_id());
            GoodsName.setRightText(model.getName());
            StoreNum.setRightText(model.getStore_sole_id());
            CargoNum.setRightText(model.getStore_goods_sole_id());
            instoreNums.setRightText(model.getNum());
            Goodsprise.setRightText(model.getPrice());
            disscount.setRightText(model.getDiscount());
            total.setRightText(model.getAmount());
            releaser.setRightText(model.getUser_name());
            releaseTime.setRightText(model.getAdd_time());
            purchase_id = model.getId();
        }
    }

    //权限判断  是管理员或者是自己新建的则可以编辑和删除
    private void getPermission(String user_id){
        String admin = userInfo.getIs_admin(); //是否是管理员用户  0：否，1：是
        if ("1".equals(admin)||userInfo.getUser_id().equals(user_id)){
            edit.setVisibility(View.VISIBLE);
        }
    }
}
