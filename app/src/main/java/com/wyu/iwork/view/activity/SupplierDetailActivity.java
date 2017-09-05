package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.SurpplierManagerModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

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
 * 供应商详情
 */

public class SupplierDetailActivity extends BaseActivity {

    private static final String  TAG = SupplierDetailActivity.class.getSimpleName();

    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;
    @BindView(R.id.activity_supplier_detail_commit)
    TextView btn_edit;

    /**
     * 供应商名称
     */
    @BindView(R.id.activity_supplier_detail_supplierName)
    CustomeViewGroup supplierName;

    /**
     * 供应商产品类型
     */
    @BindView(R.id.activity_supplier_detail_goods_type)
    CustomeViewGroup goods_type;

    /**
     * 主要联系人
     */
    @BindView(R.id.activity_supplier_detail_main_contacts)
    CustomeViewGroup main_contacts;

    /**
     * 联系电话
     */
    @BindView(R.id.activity_supplier_detail_phone)
    CustomeViewGroup supplier_phone;

    /**
     * 职务
     */
    @BindView(R.id.activity_supplier_detail_position)
    CustomeViewGroup supplier_position;

    /**
     * 地址
     */
    @BindView(R.id.activity_supplier_detail_addr)
    CustomeViewGroup addr;

    /**
     * 详细地址
     */
    @BindView(R.id.activity_supplier_detail_detail_addr)
    CustomeViewGroup detail_addr;

    /**
     * 纳税人识别号
     */
    @BindView(R.id.activity_supplier_detail_see_num)
    CustomeViewGroup see_num;

    /**
     * 开户行
     */
    @BindView(R.id.activity_supplier_detail_bank)
    CustomeViewGroup supplier_bank;

    /**
     * 账号
     */
    @BindView(R.id.activity_supplier_detail_countId)
    CustomeViewGroup supplier_countId;

    /**
     * 创建人
     */
    @BindView(R.id.activity_supplier_detail_releaser)
    CustomeViewGroup supplier_releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_supplier_detail_releaseTime)
    CustomeViewGroup releaseTime;


    private UserInfo userInfo;

    //供货商ID
    private String id = "";

    //供货商详情信息
    private SurpplierManagerModel managerModel;
    //详情请求地址
    private  String url = Constant.URL_SUPPLIER_DETAIL;

    //详情请求地址
    private  String del_url = Constant.URL_SUPPLIER_DEL;
    //编辑请求地址
    private  String edit_url = Constant.URL_SUPPLIER_UPDATE;

    //省市区
    private String province = "",city = "",district = "";

    //控件集合
    private List<CustomeViewGroup> viewGroups = new ArrayList<>();
    private PickerViewDialog addrPicker;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_detail);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        getId();
        init();
        initParama();
    }

    //获取供货商id
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            id = bundle.getString("id");
        }
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        edit.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
        userInfo = AppManager.getInstance(this).getUserInfo();
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        supplier_releaser.setRightText(userInfo.getUser_name());
        title.setText("供应商详情");

        addrPicker = new PickerViewDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                province =  addrPicker.options1Items.get(options1).getPickerViewText();
                city =  addrPicker.options2Items.get(options1).get(options2);
                district =  addrPicker.options3Items.get(options1).get(options2).get(options3);
                addr.setRightText(province+city+district);
            }
        });
        initviewgrops();
        supplier_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        supplier_phone.setInputLength(11);
    }

    private void initviewgrops(){
        viewGroups.add(supplierName);
        viewGroups.add(goods_type);
        viewGroups.add(main_contacts);
        viewGroups.add(supplier_phone);
        viewGroups.add(supplier_position);
        viewGroups.add(addr);
        viewGroups.add(detail_addr);
        viewGroups.add(see_num);
        viewGroups.add(supplier_bank);
        viewGroups.add(supplier_countId);
        viewGroups.add(supplier_releaser);
        viewGroups.add(releaseTime);

    }

    //权限判断  是管理员或者是自己新建的则可以编辑和删除
    private void getPermission(String user_id){
       String admin = userInfo.getIs_admin(); //是否是管理员用户  0：否，1：是
        if ("1".equals(admin)||userInfo.getUser_id().equals(user_id)){
            edit.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_supplier_detail_addr,R.id.activity_supplier_detail_commit})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"supplier_id",id,delcallback());
                break;
            case R.id.activity_supplier_detail_addr://省市区
                if (addrPicker!=null){
                    addrPicker.show_cityPicker();
                    Hideinputwindown(title);
                }
                break;
            case R.id.activity_supplier_detail_commit://编辑
                String text = btn_edit.getText().toString();
                if ("编辑".equals(text)){
                    btn_edit.setText("保存");
                    isEdit(true);
                }else if ("保存".equals(text)){
                    initEditParama();
                }
                break;

        }
    }

    //初始化请求参数

    private void initParama() {
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr +id+ user_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取供货商的请求回调
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
                        managerModel = JSON.parseObject(data.toString(),SurpplierManagerModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(SupplierDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //控件赋值
    private void initviewDatas(){
        if (managerModel!=null){
            getPermission(managerModel.getUser_id());
            supplierName.setRightText(managerModel.getName());
            goods_type.setRightText(managerModel.getType_name());
            main_contacts.setRightText(managerModel.getContacts());
            supplier_phone.setRightText(managerModel.getPhone());
            supplier_position.setRightText(managerModel.getJob());
            supplier_position.setRightText(managerModel.getJob());
            addr.setRightText(managerModel.getProvince()+managerModel.getCity()+managerModel.getDistrict());
            province = managerModel.getProvince();
            city = managerModel.getCity();
            district = managerModel.getDistrict();
            detail_addr.setRightText(managerModel.getAddress());
            see_num.setRightText(managerModel.getTaxpayer());
            supplier_bank.setRightText(managerModel.getOpening_bank());
            supplier_countId.setRightText(managerModel.getBank_number());
            supplier_releaser.setRightText(managerModel.getUser_name());
            releaseTime.setRightText(managerModel.getAdd_time());

        }
    }

    //删除供货商的请求回调
    private DialogCallback delcallback() {
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
                        Toast.makeText(SupplierDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }


    //是否是编辑状态
    private void isEdit(boolean isedit){
        if (isedit){
            for (int i = 0;i<viewGroups.size();i++){
                viewGroups.get(i).is_ShowInput(true);
                viewGroups.get(i).setClickable(true);
                viewGroups.get(i).is_showRightText(false);
                viewGroups.get(i).setInputContent(viewGroups.get(i).getRightText());
                if (i<4){
                    viewGroups.get(i).isShowStar(true);
                }else if (i==5){
                    viewGroups.get(i).is_ShowInput(false);
                    viewGroups.get(i).is_showRightText(true);
                    viewGroups.get(i).is_Showarrow(true);
                }
            }
        }else {
            btn_edit.setText("编辑");
            for (int i = 0;i<viewGroups.size();i++){
                viewGroups.get(i).is_ShowInput(false);
                viewGroups.get(i).setClickable(false);
                viewGroups.get(i).is_showRightText(true);
                viewGroups.get(i).isShowStar(false);
                if (i!=5){
                    viewGroups.get(i).setRightText(viewGroups.get(i).getInput());
                }
                viewGroups.get(i).is_Showarrow(false);
            }

        }
    }

    /**
     * 初始化编辑请求参数
     */
    private void initEditParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String supplier_id = id;// 修改供货商ID, 新增传0
        String name = supplierName.getRightText();//供应商名称
        String type_name = goods_type.getRightText();//供应产品类型
        String contacts = main_contacts.getRightText();//主要联系人
        String phone = supplier_phone.getRightText();//联系电话
        String job = supplier_position.getRightText();//职务
        String province = this.province==null?"":this.province;//省
        String city = this.city==null?"":this.city;//市
        String district =this.district==null?"":this.district;//区
        String address =detail_addr.getRightText();//具体地址
        String taxpayer =see_num.getRightText();//纳税人识别号
        String opening_bank =supplier_bank.getRightText();//开户银行
        String bank_number =supplier_countId.getRightText();//银行卡号
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id+name);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("supplier_id",supplier_id);
        map.put("name",name);
        map.put("type_name",type_name);
        map.put("contacts",contacts);
        map.put("phone",phone);
        map.put("job",job);
        map.put("province",province);
        map.put("city",city);
        map.put("district",district);
        map.put("address",address);
        map.put("taxpayer",taxpayer);
        map.put("opening_bank",opening_bank);
        map.put("bank_number",bank_number);
        map.put("F",Constant.F);
        map.put("V",Constant.V);
        map.put("RandStr",RandStr);
        map.put("Sign",sign);
        edit_url = RequestUtils.getRequestUrl(edit_url, map);

        OkgoRequest(edit_url,Editcallback());
    }


    //编辑供货商回调
    private DialogCallback Editcallback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG,e.getMessage());
                Toast.makeText(SupplierDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        isEdit(false);
                    }
                    Toast.makeText(SupplierDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
