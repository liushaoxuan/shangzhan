package com.wyu.iwork.view.activity;

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
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdaStoreUtil;
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
 * @author sxliu
 * 新建仓库
 */
public class BuildStoreActivity extends BaseActivity {

    private static final String TAG = BuildStoreActivity.class.getSimpleName();

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
    @BindView(R.id.activity_build_store_ediet)
    TextView edite_save;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_build_store_store_num)
    CustomeViewGroup store_num;

    /**
     * 仓库名称
     */
    @BindView(R.id.activity_build_store_storeName)
    CustomeViewGroup storeName;

    /**
     * 仓库管理
     */
    @BindView(R.id.activity_build_store_storeManager)
    CustomeViewGroup storeManager;

    /**
     * 地址
     */
    @BindView(R.id.activity_build_store_store_addr)
    CustomeViewGroup store_addr;

    /**
     * 详细地址
     */
    @BindView(R.id.activity_build_store_store_detail_addr)
    CustomeViewGroup store_detail_addr;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_store_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_store_releaseTime)
    CustomeViewGroup releaseTime;
    private UserInfo userInfo;
    private List<CustomeViewGroup> viewlist;

    /**
     *  地址选择器
     */
    private PickerViewDialog pickerViewDialog;

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
        setContentView(R.layout.activity_build_store);
        ButterKnife.bind(this);
        init();
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        getSupportActionBar().hide();
        userInfo = AppManager.getInstance(this).getUserInfo();
        viewlist = new ArrayList<>();
        title.setText("新建仓库");
        edit.setVisibility(View.GONE);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));

        viewlist.add(storeName);
        viewlist.add(storeManager);
        viewlist.add(store_detail_addr);
        viewlist.add(store_addr);
        pickerViewDialog = new PickerViewDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                  province =    pickerViewDialog.options1Items.get(options1).getPickerViewText();
                  city =    pickerViewDialog.options2Items.get(options1).get(options2);
                district =   pickerViewDialog.options3Items.get(options1).get(options2).get(options3);
                store_addr.setRightText(province +city+ district);
                store_addr.setInputContent(province +city+ district);
            }
        });

    }


    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_build_store_store_addr,R.id.activity_build_store_ediet})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_edit://删除
                break;
            case R.id.activity_build_store_store_addr://选择仓库地址
               if (pickerViewDialog!=null){
                   pickerViewDialog.show_cityPicker();
                   Hideinputwindown(title);
               }
                break;
            case R.id.activity_build_store_ediet://提交
                for (int i = 0;i<viewlist.size();i++){
                    String text = "";
                        text = viewlist.get(i).getInput();
                    if (text.isEmpty()){
                        Toast.makeText(BuildStoreActivity.this,viewlist.get(i).getNametext()+"不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                UpdaStoreUtil.initParama(this,viewlist,callback(),province,city,district,"0");
                break;
        }
    }


    //新建仓库回调
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
                    Toast.makeText(BuildStoreActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
