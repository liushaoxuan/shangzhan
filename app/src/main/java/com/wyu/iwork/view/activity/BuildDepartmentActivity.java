package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.SpinerPopWindow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * @author  sxliu
 * 新建部门
 */

public class BuildDepartmentActivity extends BaseActivity{

    private static String TAG = BuildDepartmentActivity.class.getSimpleName();

    /**
     * 部门名称
     */
    @BindView(R.id.department_build_edit_name)
    EditText departmentName;

    /**
     * 上级部门名称
     */
    @BindView(R.id.layout_build_edit_department_parent_name)
    TextView parentName;
    private List<DepartmentModel> list;
    private UserInfo userInfo;

    private PickerViewDialog picker;

    //上级部门id
    private String department_superior = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_department);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        userInfo = AppManager.getInstance(this).getUserInfo();
        getList();
        init();
    }

    private void getList(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            list = (List<DepartmentModel>) bundle.get("list");
        }else {
            list = new ArrayList<>();
        }
        DepartmentModel model = new DepartmentModel();
        model.setExpand(false);
        model.setDepartment("无");
        model.setDepartment_id("0");
        model.setList(new ArrayList<DepartmentModel.ListEntity>());
        list.add(0,model);
    }
    //初始化picker
    private void init() {
        picker = new PickerViewDialog(this, list, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String text = list.get(options1).getPickerViewText();
                parentName.setText(text);
                department_superior = list.get(options1).getDepartment_id();
            }
        });
    }
    @OnClick({R.id.activity_build_department_back,R.id.activity_build_department_enter,R.id.layout_build_edit_department_choseLayout})
    void Click(View v){
        switch (v.getId()){
            case R.id.activity_build_department_back:
                onBackPressed();
                break;
            case R.id.activity_build_department_enter:
                String pName = departmentName.getText().toString();
                if (pName.isEmpty()){
                    Toast.makeText(this,"请输入部门名称",Toast.LENGTH_SHORT).show();
                    break;
                }
                AddDepartment(pName);
                break;
            case R.id.layout_build_edit_department_choseLayout://选择上级部门
                Hideinputwindown(v);
                if (picker!=null)
                {
                    picker.show_Options();
                }

                break;
        }
    }
    private void AddDepartment(String name){
        String url = Constant.URL_ORGANZ_UPDATE;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String department_id = "0";//新增传 0 ,修改传修改部门ID
        String department_superior = this.department_superior;
        String department = name;//部门名称
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id + department_id+department_superior+department);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("department_id", department_id);
        map.put("department_superior", department_superior);
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
                                Toast.makeText(BuildDepartmentActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
