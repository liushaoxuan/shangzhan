package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.PurchaseDetailModel;
import com.wyu.iwork.model.PurchaseModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.fragment.PurchaseOrderDetailsFragment;
import com.wyu.iwork.view.fragment.PurchaseOrderEditFragment;
import com.wyu.iwork.widget.CustomeViewGroup;

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
 * @author sxliu
 * 采购订单详情
 */
public class PurchaseOrderDetailsActivity extends BaseActivity {
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
    @BindView(R.id.activity_purchase_order_ediet)
   public TextView btn_edit;

    /**
     * 订单id
     */
    public String id = "";

    private List<Fragment> fragments = new ArrayList<>();

    public PurchaseDetailModel model;

    /**
     * 删除url
     */
    private String url = Constant.URL_PURCHASE_DELETE;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order_details);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("采购订单详情");
        getId();
        fragments.add(new PurchaseOrderDetailsFragment());
        fragments.add(new PurchaseOrderEditFragment());
        setFragment(0);
        edit.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
    }

    //获取订单id
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            id = bundle.getString("id");
        }
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_purchase_order_ediet})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back://
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,url,"purchase_order_id",id,del_callback());
                break;
            case R.id.activity_purchase_order_ediet://编辑

                String text = btn_edit.getText().toString();
                if ("编辑".equals(text)){
                    btn_edit.setText("保存");
                    setFragment(1);
                }else {
                    ((PurchaseOrderEditFragment)fragments.get(1)).commit();
                }
                break;
        }
    }


    //设置fragment
    private void setFragment(int nums){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment,fragments.get(nums));
        ft.commit();
    }

    //设置fragment
    public void save(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment, new PurchaseOrderDetailsFragment());
        ft.commit();
    }


    //
    //权限判断  是管理员或者是自己新建的则可以编辑和删除
    public void getPermission(String user_id){
        String admin = userInfo.getIs_admin(); //是否是管理员用户  0：否，1：是
        if ("1".equals(admin)||userInfo.getUser_id().equals(user_id)){
            edit.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        }
    }

    //删除的请求回调
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
                        Toast.makeText(PurchaseOrderDetailsActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } ;
    }
}
