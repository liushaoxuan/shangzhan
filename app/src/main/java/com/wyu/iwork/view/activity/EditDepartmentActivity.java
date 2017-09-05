package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.SpinnerAdapter;
import com.wyu.iwork.model.DepartmentModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;
import com.wyu.iwork.widget.SpinerPopWindow;

import org.json.JSONArray;
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
 * 编辑部门
 */
public class EditDepartmentActivity extends BaseActivity {
    private static String TAG = EditDepartmentActivity.class.getSimpleName();

    /**
     * 部门名称
     */
    @BindView(R.id.activity_edit_department_departmentName)
    CustomeViewGroup departmentName;

    /**
     * 部门名称
     */
    @BindView(R.id.activity_edit_department_parent)
    CustomeViewGroup parent;

    private List<DepartmentModel> list;
    private UserInfo userInfo;

    //部门名称
    private String department_name;
    //部门id
    private String department_id;

    private PickerViewDialog picker;
    /**
     * 上级部门ID
     */
    private String parent_id = "0";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_department);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        userInfo = AppManager.getInstance(this).getUserInfo();
        getList();
        getDepartment();

    }

    private void getList() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            department_id = bundle.getString("department_id");
            department_name = bundle.getString("department_name");
            departmentName.setInputContent(department_name);
        }
        list = new ArrayList<>();
    }

    //
    private void init(){
        picker = new PickerViewDialog(this, list, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String text =    list.get(options1).getPickerViewText();
                parent.setRightText(text);
                parent_id = list.get(options1).getDepartment_id();
            }
        });
    }

    @OnClick({R.id.activity_edit_department_back,R.id.activity_edit_department_enter,R.id.activity_edit_department_parent})
    void Click(View v){
        switch (v.getId()){
            case R.id.activity_edit_department_back:
                onBackPressed();
                break;
            case R.id.activity_edit_department_enter:
                String pName = departmentName.getInput();
                if (pName.isEmpty()){
                    Toast.makeText(this,"请输入部门名称",Toast.LENGTH_SHORT).show();
                    break;
                }
                EditDepartment(pName);
                break;
            case R.id.activity_edit_department_parent://选择上级部门
                if (list!=null&&list.size()>0){
                    if (picker!=null){
                        picker.show_Options();
                    }
                }else {
                    Toast.makeText(this,"暂无上级部门",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void EditDepartment(String name) {
        String url = Constant.URL_ORGANZ_UPDATE;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String department = name;//部门名称
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id + department_id + parent_id + department);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("department_id", department_id);
        map.put("department_superior", parent_id);
        map.put("department", department);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.get(murl)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                finish();
                            }
                            Toast.makeText(EditDepartmentActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //获取一级部门列表
    private void getDepartment(){

        String company_id = userInfo==null?"":userInfo.getCompany_id();//公司id
        String url = Constant.URL_ORGANZ_LIST;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr +company_id + department_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("company_id", company_id);
        map.put("department_id", department_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
       url = RequestUtils.getRequestUrl(url, map);
        Logger.e("getDepartment_url",url);
        OkGo.get(url)
                .tag(TAG)
                .execute(new StringCallback() {
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
                                JSONArray data = object.optJSONArray("data");
                                List<DepartmentModel> temp = JSON.parseArray(data.toString(),DepartmentModel.class);
                                list.clear();
                                list.addAll(temp);
                                temp.clear();
                                temp=null;
                                init();
                            }else {
                                Toast.makeText(EditDepartmentActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
