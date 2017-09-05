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
import com.bigkoo.pickerview.OptionsPickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CargoDetailModel;
import com.wyu.iwork.model.StoreDetailModel;
import com.wyu.iwork.model.StoreModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateCargoUtil;
import com.wyu.iwork.view.dialog.PickerViewDialog;
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
 * 货位详情
 */
public class CargoLocationDetailActivity extends BaseActivity {

    private static final String TAG = CargoLocationDetailActivity.class.getSimpleName();
    public final static int StoreCode = 0x0000001;
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
    @BindView(R.id.activity_cargo_location_detail_committ)
    TextView btn_edit;

    /**
     * 货位编号
     */
    @BindView(R.id.activity_cargo_location_detail_cargoNum)
    CustomeViewGroup cargoNum;

    /**
     * 货位名称
     */
    @BindView(R.id.activity_cargo_location_detail_cargoName)
    CustomeViewGroup cargoName;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_cargo_location_detail_storeNum)
    CustomeViewGroup storeNum;

    /**
     * 仓库名称
     */
    @BindView(R.id.activity_cargo_location_detail_storeName)
    CustomeViewGroup storeName;

    /**
     * 管理人
     */
    @BindView(R.id.activity_cargo_location_manager)
    CustomeViewGroup storeManager;

    /**
     * 地址
     */
    @BindView(R.id.activity_cargo_location_detail_addr)
    CustomeViewGroup addr;

    /**
     * 详细地址
     */
    @BindView(R.id.activity_cargo_location_detail_detail_addr)
    CustomeViewGroup detail_addr;
    /**
     * 创建人
     */
    @BindView(R.id.activity_cargo_location_detail_releaser)
    CustomeViewGroup releaser;
    /**
     * 创建人时间
     */
    @BindView(R.id.activity_cargo_location_detail_releaseTime)
    CustomeViewGroup releaseTime;



    private UserInfo userInfo;

    private List<CustomeViewGroup> viewGroups;

    //货位ID
    private String store_goods_id = "";
    /**
     *  地址选择器
     */
    private PickerViewDialog pickerViewDialog;

    //省市区
    private String province = "",city = "",district = "";

    private String url = Constant.URL_CARGO_DETAIL;
    //删除地址
    private String del_url = Constant.URL_CARGO_DELETE;
    /**
     * 仓库id
     */
    private String store_id = "";


    //货位详情model
    private CargoDetailModel model;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo_location_detail);
        ButterKnife.bind(this);
        getId();
        getSupportActionBar().hide();
        init();
        initDetaiParama();
    }

    //获取货位ID

    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            store_goods_id = bundle.getString("id");
        }
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        edit.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
        title.setText("货位详情");
        userInfo = AppManager.getInstance(this).getUserInfo();
        viewGroups = new ArrayList<>();
        viewGroups.add(cargoName);
        viewGroups.add(storeNum);
        viewGroups.add(storeName);
        viewGroups.add(addr);
        viewGroups.add(detail_addr);
        viewGroups.add(releaser);
        viewGroups.add(releaseTime);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        pickerViewDialog = new PickerViewDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                  province =    pickerViewDialog.options1Items.get(options1).getPickerViewText();
                  city =    pickerViewDialog.options2Items.get(options1).get(options2);
                  district =   pickerViewDialog.options3Items.get(options1).get(options2).get(options3);

                addr.setRightText(province+ city+ district);
            }
        });

    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_cargo_location_detail_storeNum,R.id.activity_cargo_location_detail_addr,R.id.activity_cargo_location_detail_committ})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"store_goods_id",store_id,del_callback());
                break;

            case R.id.activity_cargo_location_detail_storeNum://选择仓库
                if ("保存".equals(btn_edit.getText().toString())){
                    intent = new Intent(this,StoresManagerActivity.class);
                    intent.putExtra("flag","CargoLocationDetailActivity");
                    startActivityForResult(intent,StoreCode);
                }
                break;

            case R.id.activity_cargo_location_detail_addr://选择地址
                if ("保存".equals(btn_edit.getText().toString())){
                    if (pickerViewDialog!=null){
                        pickerViewDialog.show_cityPicker();
                    }
                }
                break;

            case R.id.activity_cargo_location_detail_committ://编辑 保存
                if ("编辑".equals(btn_edit.getText().toString())){//编辑状态下
                    btn_edit.setText("保存");
                    cargoName.setInputContent(cargoName.getRightText());
                    cargoName.is_ShowInput(true);
                    cargoName.is_showRightText(false);
                    cargoName.isShowStar(true);
                    storeNum.is_Showarrow(true);
                }else {//保存
                    String name = cargoName.getInput();
                    String storeNum = this.storeNum.getRightText();
                    if (name.isEmpty()){
                        Toast.makeText(CargoLocationDetailActivity.this,"货位名称不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (storeNum.isEmpty()){
                        Toast.makeText(CargoLocationDetailActivity.this,"仓库编号不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    UpdateCargoUtil.initParama(this,Editcallback(),name,store_id,store_goods_id);
                }
                break;
        }
    }




    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + store_goods_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("store_goods_id", store_goods_id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取货位详情的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),CargoDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(CargoLocationDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //删除货位的请求回调
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
                        Toast.makeText(CargoLocationDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            cargoNum.setRightText(model.getSole_id());
            cargoName.setRightText(model.getName());
            storeNum.setRightText(model.getStore_id());
            storeManager.setRightText(model.getManager());
            addr.setRightText(model.getProvince()+model.getCity()+model.getDistrict());
            detail_addr.setRightText(model.getAddress());
            releaser.setRightText(model.getCreater_name());
            releaseTime.setRightText(model.getAdd_time());
            store_id = model.getId();
        }
    }



    //编辑货位回调
    private DialogCallback Editcallback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG,e.getMessage());
                Toast.makeText(CargoLocationDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        btn_edit.setText("编辑");
                        cargoName.setRightText(cargoName.getInput());
                        cargoName.is_ShowInput(false);
                        cargoName.is_showRightText(true);
                        cargoName.isShowStar(false);
                        storeNum.is_Showarrow(false);
                    }
                    Toast.makeText(CargoLocationDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==StoreCode){
            StoreModel model = (StoreModel) data.getExtras().get("model");
            storeNum.setRightText(model.getSole_id());
            storeName.setRightText(model.getName());
            storeManager.setRightText(model.getUser_name());
            addr.setRightText(model.getProvince()+model.getCity()+model.getDistrict());
            detail_addr.setRightText(model.getAddress());

        }
    }

    //权限判断  是管理员或者是自己新建的则可以编辑和删除
    public void getPermission(String user_id){
        String admin = userInfo.getIs_admin(); //是否是管理员用户  0：否，1：是
        if ("1".equals(admin)||userInfo.getUser_id().equals(user_id)){
            edit.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        }
    }
}
