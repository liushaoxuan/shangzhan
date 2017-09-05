package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.DepartmentManagerAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.DepartmentModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.MyDialog;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.Serializable;
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
 * @author  sxliu
 * 部门管理
 */

public class DepartmentManagerActivity extends BaseActivity {
    private static String TAG = DepartmentManagerActivity.class.getSimpleName();

    @BindView(R.id.activity_department_manager_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.activity_department_manager_add)
    TextView activity_department_manager_add;

    @BindView(R.id.activity_department_manager_del_and_edit)
    LinearLayout delete_edit_layout;

    public DepartmentManagerAdapter adapter;
    private UserInfo userInfo;
    private List<DepartmentModel> list = new ArrayList<>();
    //部门id
    private String department_id = "";
    //部门名称
    private String department_name = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_manager);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        userInfo = MyApplication.userInfo;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DepartmentManagerAdapter(this,list);
        recyclerView.setAdapter(adapter);
        getDepartment();
    }

    @OnClick({R.id.activity_department_manager_back,R.id.activity_department_manager_add,R.id.activity_department_manager_edit,R.id.accountactivity_department_manager_del})
    void CLick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.activity_department_manager_back:
                onBackPressed();
                break;

            case R.id.activity_department_manager_add://新建部门
                intent = new Intent(this,BuildDepartmentActivity.class);
                intent.putExtra("list", (Serializable) list);
                startActivity(intent);
                break;

            case R.id.activity_department_manager_edit://编辑部门
                 intent = new Intent(this,EditDepartmentActivity.class);
                intent.putExtra("department_name",department_name);
                intent.putExtra("department_id",department_id);
                intent.putExtra("list", (Serializable)list);
                startActivity(intent);
                break;
            case R.id.accountactivity_department_manager_del://删除部门
                delDialog(department_id);
                break;

        }
    }

    //获取部门信息
    private void getDepartment(){
        String url = Constant.URL_ORGANZ_MANAGE;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String company_id =userInfo == null ? "" : userInfo.getCompany_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + company_id+user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("company_id", company_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.get(murl)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            Logger.e(TAG,s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONArray data = object.optJSONArray("data");
                                List<DepartmentModel> templist = new ArrayList<DepartmentModel>();
                                templist = JSON.parseArray(data.toString(), DepartmentModel.class);
                                list.clear();
                                list.addAll(templist);
                                adapter.notifyDataSetChanged();
                                templist.clear();
                                templist = null;

                            } else {
                                Toast.makeText(DepartmentManagerActivity.this, object.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public   List<DepartmentModel> getdepartmentList(){
        return list;
    }

    //删除部门的dialog
    public void delDialog(final String id){
        String tv_Content = "确认删除该部门吗？";
        new MyCustomDialogDialog(3, this, R.style.MyDialog, "删除部门",  tv_Content, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                if (!userInfo.getUser_id().isEmpty()) {
                    delDepartment(id);
                }
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                dialog.dismiss();
            }

        }).show();
    }

    //删除部门
    private void delDepartment(String id){
        String url = Constant.URL_ORGANZ_DELETE;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String department_id = id;// 删除部门ID
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id + department_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("department_id", department_id);
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
                              getDepartment();
                            }
                            Toast.makeText(DepartmentManagerActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //展示或隐藏底部的删除 编辑按钮
    public void showEditandDelete(boolean isshow,String id,String name){

        if (isshow){
            delete_edit_layout.setVisibility(View.VISIBLE);
        }else {
            delete_edit_layout.setVisibility(View.GONE);
        }

        department_id = id;
        department_name = name;
    }
}
