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
import com.wyu.iwork.model.CargoModel;
import com.wyu.iwork.model.GoodsDetailModel;
import com.wyu.iwork.model.GoodsModel;
import com.wyu.iwork.model.StockDetailModel;
import com.wyu.iwork.model.StoreModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateStockUtil;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
 * 库存详情
 */

public class StockDetailActivity extends BaseActivity {

    private static final String TAG = StockDetailActivity.class.getSimpleName();

    //商品编号
    public static final int CODE_GOODS_NUM = 0x00001;
    //仓库编号
    public static final int CODE_STORE_NUM = 0x00002;
    //货位编号
    public static final int CODE_CARGO_NUM = 0x00003;

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
    @BindView(R.id.activity_build_stock_commit)
    TextView btn_edit;

    /**
     * 商品编号
     */
    @BindView(R.id.activity_build_stock_goodsNum)
    CustomeViewGroup goodsNum;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_stock_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_build_stock_goodsPrise)
    CustomeViewGroup goodsPrise;

    /**
     * 库存数量
     */
    @BindView(R.id.activity_build_stock_goodsCount)
    CustomeViewGroup goodsCount;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_build_stock_storeNum)
    CustomeViewGroup storeNum;

    /**
     * 货位编号
     */
    @BindView(R.id.activity_build_stockcargoNum)
    CustomeViewGroup stockcargoNum;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_stock_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_stock_releaseTime)
    CustomeViewGroup releaseTime;

    private UserInfo userInfo;

    //库存详情model
   private  StockDetailModel model;

    //请求地址
    private String url = Constant.URL_STOCK_DETAIL;

    //删除地址
    private String del_url = Constant.URL_STOCK_DELETE;

    //库存id
    private String store_remain_id = "";

    //编辑库存的model
    private StockDetailModel addModel = new StockDetailModel();

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
        setContentView(R.layout.activity_build_stock);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        getId();
        init();
        initDetaiParama();
    }

    //获取库存id
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            store_remain_id = bundle.getString("id");
        }
    }

    private void init(){
        edit.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);

        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("库存详情");
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        isEdit(false);

        viewGroupList.add(goodsNum);
        viewGroupList.add(goodsName);
        viewGroupList.add(goodsPrise);
        viewGroupList.add(goodsCount);
        viewGroupList.add(storeNum);
        viewGroupList.add(stockcargoNum);
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_build_stock_goodsNum,R.id.activity_build_stock_storeNum,R.id.activity_build_stockcargoNum,R.id.activity_build_stock_commit})
    void CLick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"store_remain_id",store_remain_id,del_callback());
                break;
            case R.id.activity_build_stock_goodsNum://商品编号
                intent = new Intent(this,GoodsManagerActivity.class);
                intent.putExtra("flag","StockDetailActivity");
                startActivityForResult(intent,CODE_GOODS_NUM);
                break;
            case R.id.activity_build_stock_storeNum://仓库编号
                intent = new Intent(this,StoresManagerActivity.class);
                intent.putExtra("flag","StockDetailActivity");
                startActivityForResult(intent,CODE_STORE_NUM);
                break;
            case R.id.activity_build_stockcargoNum://货位编号
                intent = new Intent(this,CargoLocationManagerActivity.class);
                intent.putExtra("flag","StockDetailActivity");
                startActivityForResult(intent,CODE_CARGO_NUM);
                break;
            case R.id.activity_build_stock_commit:// 编辑 保存
                String text = btn_edit.getText().toString();
                if ("编辑".equals(text)){
                    btn_edit.setText("保存");
                    isEdit(true);
                }else {
                    for (int i = 0;i<viewGroupList.size();i++){
                        String text1 = viewGroupList.get(i).getRightText();
                        if (text1.isEmpty()||"请选择".equals(text1)){
                            Toast.makeText(StockDetailActivity.this,viewGroupList.get(i).getNametext()+"不能为空",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    addModel.setId(store_remain_id);
                    UpdateStockUtil.initParama(this,Editcallback(),addModel);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (resultCode){
                case CODE_GOODS_NUM://商品编号
                    GoodsModel goodsModel = (GoodsModel) data.getExtras().get("model");
                    goodsNum.setRightText(goodsModel.getSole_id());
                    goodsName.setRightText(goodsModel.getName());
                    goodsPrise.setRightText(goodsModel.getPrice());
                    goodsCount.setRightText(goodsModel.getNum());
                    addModel.setGoods_num(goodsModel.getSole_id());
                    addModel.setGoods_id(goodsModel.getId());
                    break;
                case CODE_STORE_NUM://仓库编号
                    StoreModel storeModel = (StoreModel) data.getExtras().get("model");
                    storeNum.setRightText(storeModel.getSole_id());

                    addModel.setStore_id(storeModel.getId());
                    break;
                case CODE_CARGO_NUM://货位编号
                    CargoModel cargoModel = (CargoModel) data.getExtras().get("model");
                    stockcargoNum.setRightText(cargoModel.getSole_id());
                    addModel.setStore_goods_id(cargoModel.getId());
                    break;
            }
        }
    }

    //是否是在编辑状态下
    private void isEdit(boolean isedit){
        if (isedit){//编辑状态下
            goodsNum.isShowStar(true);
            goodsNum.is_Showarrow(true);
            storeNum.isShowStar(true);
            storeNum.is_Showarrow(true);
            stockcargoNum.isShowStar(true);
            stockcargoNum.is_Showarrow(true);
            goodsNum.setClickable(true);
            storeNum.setClickable(true);
            stockcargoNum.setClickable(true);

        }else {
            goodsNum.isShowStar(false);
            goodsNum.is_Showarrow(false);
            storeNum.isShowStar(false);
            storeNum.is_Showarrow(false);
            stockcargoNum.isShowStar(false);
            stockcargoNum.is_Showarrow(false);
            goodsNum.setClickable(false);
            storeNum.setClickable(false);
            stockcargoNum.setClickable(false);
        }
    }



    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + store_remain_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("store_remain_id", store_remain_id);
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

    //获取库存详情的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {

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
                        model = JSON.parseObject(data.toString(),StockDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(StockDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //删除库存的请求回调
    private DialogCallback del_callback() {
        return new DialogCallback(this) {

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
                      finish();
                    }else {
                        Toast.makeText(StockDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            goodsNum.setRightText(model.getGoods_sole_id());
            goodsName.setRightText(model.getGoods_name());
            goodsPrise.setRightText(model.getGoods_price());
            goodsCount.setRightText(model.getGoods_num());
            storeNum.setRightText(model.getStore_sole_id());
            stockcargoNum.setRightText(model.getStore_goods_sole_id());
            releaser.setRightText(model.getCreater_name());
            releaseTime.setRightText(model.getAdd_time());

        }
    }




    //编辑库存回调
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
                        isEdit(false);
                    }
                    Toast.makeText(StockDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
        }
    }
}
