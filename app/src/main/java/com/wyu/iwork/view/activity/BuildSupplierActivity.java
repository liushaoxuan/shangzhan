package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
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
 * 新建供货商
 */
public class BuildSupplierActivity extends BaseActivity {

    private static final String TAG = BuildSupplierActivity.class.getSimpleName();

    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 供应商名称
     */
    @BindView(R.id.activity_build_supplier_supplierName)
    CustomeViewGroup supplierName;

    /**
     * 供应商产品类型
     */
    @BindView(R.id.activity_build_supplier_goods_type)
    CustomeViewGroup goods_type;

    /**
     * 主要联系人
     */
    @BindView(R.id.activity_build_supplier_main_contacts)
    CustomeViewGroup main_contacts;

    /**
     * 联系电话
     */
    @BindView(R.id.activity_build_supplier_phone)
    CustomeViewGroup supplier_phone;

    /**
     * 职务
     */
    @BindView(R.id.activity_build_supplier_position)
    CustomeViewGroup supplier_position;

    /**
     * 地址
     */
    @BindView(R.id.activity_build_supplier_addr)
    CustomeViewGroup addr;

    /**
     * 详细地址
     */
    @BindView(R.id.activity_build_supplier_detail_addr)
    CustomeViewGroup detail_addr;

    /**
     * 纳税人识别号
     */
    @BindView(R.id.activity_build_supplier_see_num)
    CustomeViewGroup see_num;

    /**
     * 开户行
     */
    @BindView(R.id.activity_build_supplier_bank)
    CustomeViewGroup supplier_bank;

    /**
     * 账号
     */
    @BindView(R.id.activity_build_supplier_countId)
    CustomeViewGroup supplier_countId;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_supplier_releaser)
    CustomeViewGroup supplier_releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_supplier_releaseTime)
    CustomeViewGroup releaseTime;


    private UserInfo userInfo;

    private PickerViewDialog addrPicker;

    private String url = Constant.URL_SUPPLIER_UPDATE;

    //省市区
    private String province = "",city = "",district = "";

    //控件集合
    private List<CustomeViewGroup> viewGroups = new ArrayList<>();
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_supplier);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        userInfo = AppManager.getInstance(this).getUserInfo();
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        supplier_releaser.setRightText(userInfo.getUser_name());
        title.setText("新建供应商");
        edit.setVisibility(View.GONE);
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

    @OnClick({R.id.action_back,R.id.activity_build_supplier_addr,R.id.activity_build_supplier_commit})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.activity_build_supplier_addr://选择地址
                if (addrPicker!=null){
                    addrPicker.show_cityPicker();
                    Hideinputwindown(title);
                }
                break;
            case R.id.activity_build_supplier_commit://提交
                for (int i = 0;i<viewGroups.size();i++){
                    String rightText = viewGroups.get(i).getRightText();
                    viewGroups.get(i).is_showRightText(false);
                    if (rightText.isEmpty()){
                        rightText = viewGroups.get(i).getInput();
                        viewGroups.get(i).setRightText(rightText);
                    }
                    if (rightText.isEmpty()){
                        Toast.makeText(BuildSupplierActivity.this,viewGroups.get(i).getNametext()+"不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                initParama();
                break;

        }
    }

    /**
     * 初始化请求参数
     */
    private void initParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String supplier_id = "0";// 修改供货商ID, 新增传0
        String name = supplierName.getRightText();//供应商名称
        String type_name = goods_type.getRightText();//供应产品类型
        String contacts = main_contacts.getRightText();//主要联系人
        String phone = supplier_phone.getRightText();//联系电话
        String job = supplier_position.getRightText();//职务
        String province = this.province;//省
        String city = this.city;//市
        String district =this.district;//区
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
        url = RequestUtils.getRequestUrl(url, map);

        OkgoRequest(url,callback());
    }


    //新建供货商回调
    private DialogCallback callback(){
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
                        finish();
                    }
                    Toast.makeText(BuildSupplierActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
