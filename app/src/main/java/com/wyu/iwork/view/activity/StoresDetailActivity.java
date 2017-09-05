package com.wyu.iwork.view.activity;

import android.graphics.Color;
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
import com.wyu.iwork.model.StoreDetailModel;
import com.wyu.iwork.model.SurpplierManagerModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdaStoreUtil;
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
 * 仓库详情
 */
public class StoresDetailActivity extends BaseActivity {

    private static final String TAG = StoresDetailActivity.class.getSimpleName();

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
    @BindView(R.id.activity_stores_detail_ediet)
    TextView edite_save;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_stores_detail_store_num)
    CustomeViewGroup store_num;

    /**
     * 仓库名称
     */
    @BindView(R.id.activity_stores_detail_storeName)
    CustomeViewGroup storeName;

    /**
     * 仓库管理
     */
    @BindView(R.id.activity_stores_detail_storeManager)
    CustomeViewGroup storeManager;

    /**
     * 地址
     */
    @BindView(R.id.activity_stores_detail_store_addr)
    CustomeViewGroup store_addr;

    /**
     * 详细地址
     */
    @BindView(R.id.activity_stores_detail_store_detail_addr)
    CustomeViewGroup store_detail_addr;

    /**
     * 创建人
     */
    @BindView(R.id.activity_stores_detail_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_stores_detail_releaseTime)
    CustomeViewGroup releaseTime;

    private List<CustomeViewGroup> viewlist;
    private UserInfo userInfo;

    /**
     *  地址选择器
     */
    private PickerViewDialog pickerViewDialog;

    private String url = Constant.URL_STORE_DETAIL;

    /**
     * 删除的地址
     */
    private String del_url = Constant.URL_STORE_DELETE;

    private StoreDetailModel model;

    private String store_id = "";

    //省市区
    private String province = "",city = "",district = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_detail);
        ButterKnife.bind(this);
        getstore_id();
        init();
        initDetaiParama();

    }

    //获取仓库id
    private void getstore_id(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            store_id = bundle.getString("id");
        }
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        edit.setVisibility(View.GONE);
        edite_save.setVisibility(View.GONE);
        getSupportActionBar().hide();
        viewlist = new ArrayList<>();
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("仓库详情");
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        viewlist.add(storeName);
        viewlist.add(storeManager);
        viewlist.add(store_detail_addr);

        pickerViewDialog = new PickerViewDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
               province =    pickerViewDialog.options1Items.get(options1).getPickerViewText();
               city =    pickerViewDialog.options2Items.get(options1).get(options2);
               district =   pickerViewDialog.options3Items.get(options1).get(options2).get(options3);
                store_addr.setRightText(province+city+ district);
            }
        });

    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_stores_detail_store_addr,R.id.activity_stores_detail_ediet})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"store_id",store_id,del_callback());
                break;
            case R.id.activity_stores_detail_store_addr://选择仓库地址  只有在编辑的时候才管用
                if ("保存".equals(edite_save.getText().toString())){
                    pickerViewDialog.show_cityPicker();
                }
                break;
            case R.id.activity_stores_detail_ediet://编辑  保存
                String text = edite_save.getText().toString();
                if ("编辑".equals(text)){
                    edite_save.setText("保存");
                    store_addr.is_Showarrow(true);
                    for (int i = 0;i<viewlist.size();i++){
                        viewlist.get(i).is_ShowInput(true);
                        viewlist.get(i).is_showRightText(false);
                        if (i<2){
                            viewlist.get(i).isShowStar(true);
                        }
                        viewlist.get(i).setInputContent(viewlist.get(i).getRightText());
                    }
                }else if ("保存".equals(text)){
                    for (int i = 0;i<viewlist.size();i++){
                        String mtext = "";
                        if (i==2){
                            mtext = viewlist.get(i).getRightText();
                        }else {
                            mtext = viewlist.get(i).getInput();
                        }
                        if (mtext.isEmpty()){
                            Toast.makeText(StoresDetailActivity.this,viewlist.get(i).getNametext()+"不能为空",Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    UpdaStoreUtil.initParama(this,viewlist,Editcallback(),province,city,district,store_id);
                }
                break;
        }
    }

    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + store_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("store_id", store_id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取仓库的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),StoreDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(StoresDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //删除仓库的请求回调
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
                        Toast.makeText(StoresDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            store_num.setRightText(model.getSole_id());
            storeName.setRightText(model.getName());
            storeManager.setRightText(model.getManager());
            store_addr.setRightText(model.getProvince()+model.getCity()+model.getDistrict());
            store_detail_addr.setRightText(model.getAddress());
            releaser.setRightText(model.getCreater_name());
            releaseTime.setRightText(model.getAdd_time());

        }
    }

    //编辑仓库回调
    private DialogCallback Editcallback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG,e.getMessage());
                Toast.makeText(StoresDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        edite_save.setText("编辑");
                        store_addr.is_Showarrow(false);
                        for (int i = 0;i<viewlist.size();i++){
                            viewlist.get(i).is_ShowInput(false);
                            viewlist.get(i).is_showRightText(true);
                            if (i<3){
                                viewlist.get(i).isShowStar(false);
                            }
                            viewlist.get(i).setRightText(viewlist.get(i).getInput());
                        }
                    }
                    Toast.makeText(StoresDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            edite_save.setVisibility(View.VISIBLE);
        }
    }
}
