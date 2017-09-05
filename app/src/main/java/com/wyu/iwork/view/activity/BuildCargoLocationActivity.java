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

import com.bigkoo.pickerview.OptionsPickerView;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.StoreModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateCargoUtil;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 新建货位
 */
public class BuildCargoLocationActivity extends BaseActivity {

    private static final String TAG = BuildCargoLocationActivity.class.getSimpleName();

    public final static int StoreCode = 0x00001;
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 货位编号
     */
    @BindView(R.id.activity_build_cargo_location_cargoNum)
    CustomeViewGroup cargoNum;

    /**
     * 货位名称
     */
    @BindView(R.id.activity_build_cargo_location_cargoName)
    CustomeViewGroup cargoName;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_build_cargo_location_storeNum)
    CustomeViewGroup storeNum;

    /**
     * 仓库名称
     */
    @BindView(R.id.activity_build_cargo_location_storeName)
    CustomeViewGroup storeName;
    /**
     * 管理人
     */
    @BindView(R.id.activity_build_cargo_location_storeManager)
    CustomeViewGroup storeManager;

    /**
     * 地址
     */
    @BindView(R.id.activity_build_cargo_location_addr)
    CustomeViewGroup addr;

    /**
     * 详细地址
     */
    @BindView(R.id.activity_build_cargo_location_detail_addr)
    CustomeViewGroup detail_addr;
    /**
     * 创建人
     */
    @BindView(R.id.activity_build_cargo_location_releaser)
    CustomeViewGroup releaser;
    /**
     * 创建人时间
     */
    @BindView(R.id.activity_build_cargo_location_releaseTime)
    CustomeViewGroup releaseTime;



    private UserInfo userInfo;

    /**
     *  地址选择器
     */
    private PickerViewDialog pickerViewDialog;

    /**
     * 仓库id
     */
    private String store_id = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_cargo_location);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        title.setText("新建货位");
        edit.setVisibility(View.GONE);
        userInfo = AppManager.getInstance(this).getUserInfo();
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        pickerViewDialog = new PickerViewDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String province =    pickerViewDialog.options1Items.get(options1).getPickerViewText();
                String city =    pickerViewDialog.options2Items.get(options1).get(options2);
                String area =   pickerViewDialog.options3Items.get(options1).get(options2).get(options3);
                if (province.equals(city)){
                    city = "";
                }
                addr.setRightText(province+city+area);
            }
        });

    }

    @OnClick({R.id.action_back,R.id.activity_build_cargo_location_storeNum,R.id.activity_build_cargo_location_addr,R.id.activity_build_cargo_location_committ})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.activity_build_cargo_location_storeNum://选择仓库
                intent = new Intent(this,StoresManagerActivity.class);
                intent.putExtra("flag","BuildCargoLocationActivity");
                startActivityForResult(intent,StoreCode);
                break;

            case R.id.activity_build_cargo_location_addr://选择地址
                if (pickerViewDialog!=null){
                    pickerViewDialog.show_cityPicker();
                }
                break;

            case R.id.activity_build_cargo_location_committ://提交

                String name = cargoName.getInput();
                String storeNum = this.storeNum.getRightText();


                if (name.isEmpty()){
                    Toast.makeText(BuildCargoLocationActivity.this,"货位名称不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (storeNum.isEmpty()){
                    Toast.makeText(BuildCargoLocationActivity.this,"仓库编号不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }

                UpdateCargoUtil.initParama(this,callback(),name,store_id,"0");

                break;
        }
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
            store_id = model.getId();
        }
    }

    //新建货位回调
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
                    Toast.makeText(BuildCargoLocationActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
