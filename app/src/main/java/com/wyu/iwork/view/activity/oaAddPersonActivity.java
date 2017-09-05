package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.oaAddPersonAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.TaskContactsModule;
import com.wyu.iwork.model.user_excludeModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

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
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 *         添加人员
 */
public class oaAddPersonActivity extends BaseActivity implements onItemClickListener,TextWatcher,BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.activity_oa_add_person_editText)
    EditText editText;

//    /**
//     * 全选图标
//     */
//    @BindView(R.id.activity_oa_add_person_select_all_icon)
//    ImageView select_all_icon;
//
//    /**
//     * 全选
//     */
//    @BindView(R.id.activity_oa_add_person_select_all_text)
//    TextView select_all;

    @BindView(R.id.activity_oa_add_person_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.activity_oa_add_person_bga)
    BGARefreshLayout freshLayout;

    @BindView(R.id.activity_oa_add_person_nodata)
    RelativeLayout nodata_layout;

    private oaAddPersonAdapter adapter;

    private ArrayList<CompanyContacts> list = new ArrayList<>();
    private ArrayList<CompanyContacts> datas = new ArrayList<>();

    private ArrayList<CompanyContacts> searchList = new ArrayList<>();

    private ArrayList<CompanyContacts> removeList = new ArrayList<>();

    private ArrayList<user_excludeModel> user_excludelist = new ArrayList<>();

    private CompanyContacts checke_model;

    /**
     * 是否全选
     */
    private boolean isCheckedAll = false;

    private int flag = -1;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_add_person);
        getSupportActionBar().hide();
        getFlag();
        ButterKnife.bind(this);
        init();
    }

    //初始化
    private void init() {

        adapter = new oaAddPersonAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        editText.addTextChangedListener(this);
        setRefresh(freshLayout);
        freshLayout.setDelegate(this);
        initParama();
    }

    private void getFlag() {
        try {
            flag = getIntent().getFlags();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                removeList = (ArrayList<CompanyContacts>) bundle.get("contacts");
                for (int i = 0; i < removeList.size(); i++) {
                    if (removeList.get(i).getId() != null && !"".equals(removeList.get(i).getId())) {
                        user_excludelist.add(new user_excludeModel(removeList.get(i).getId()));
                    }
                }
                user_excludelist.add(new user_excludeModel(userInfo.getUser_id()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //事件注入
    @OnClick({R.id.activity_oa_add_person_cancel, R.id.activity_oa_add_person_sure})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.activity_oa_add_person_cancel:
                onBackPressed();
                break;

//            case R.id.activity_oa_add_person_select_all://全选
//                if (isCheckedAll) {
//                    for (int i = 0; i < list.size(); i++) {
//                        list.get(i).setChecked(false);
//                    }
//                } else {
//                    for (int i = 0; i < list.size(); i++) {
//                        list.get(i).setChecked(true);
//                    }
//                }
//                adapter.notifyDataSetChanged();
//                break;

            case R.id.activity_oa_add_person_sure://根据是哪个页面来的再回传到原来的页面
                if (checke_model == null) {
                    Toast.makeText(this, "请选择人员", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    Intent intent = null;
                    switch (flag) {
                        case oaApplyForLeaveActivity.TYPE_APROVAL://请假审批人
                            intent = new Intent(this, oaApplyForLeaveActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;
                        case oaApplyForLeaveActivity.TYPE_COPY_SEND://请假抄送人
                            intent = new Intent(this, oaApplyForLeaveActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaWorkOverTimeActivity.TYPE_APROVAL://加班审批人
                            intent = new Intent(this, oaWorkOverTimeActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaWorkOverTimeActivity.TYPE_COPY_SEND://加班抄送人
                            intent = new Intent(this, oaWorkOverTimeActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaBusinessTravelActivity.TYPE_APROVAL://出差审批人
                            intent = new Intent(this, oaWorkOverTimeActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaBusinessTravelActivity.TYPE_COPY_SEND://出差抄送人
                            intent = new Intent(this, oaWorkOverTimeActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaReimbursementActivity.TYPE_APROVAL://报销审批人
                            intent = new Intent(this, oaReimbursementActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaReimbursementActivity.TYPE_COPY_SEND://报销抄送人
                            intent = new Intent(this, oaReimbursementActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                        case oaTransferApplyActivity.REQUESTCODE://转交审批人
                            intent = new Intent(this, oaTransferApplyActivity.class);
                            intent.putExtra("model", checke_model);
                            setResult(flag, intent);
                            finish();
                            break;

                    }
                }
                Hideinputwindown(v);
                break;
        }
    }

    //initparama
    private void initParama() {
        String url = Constant.URL_GET_COMPANYUSERLIST;
        String company_id = null;
        company_id = AppManager.getInstance(this).getUserInfo().getCompany_id();
         String   user_exclude = JSON.toJSONString(user_excludelist);
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + company_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("company_id", company_id);
        map.put("user_exclude", user_exclude);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(murl, callback());
    }

    /**
     * 请求回调
     */
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                freshLayout.endRefreshing();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s, call, response);
                try {
                    freshLayout.endRefreshing();
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONArray array = object.optJSONArray("data");
                        ArrayList<CompanyContacts> templist = (ArrayList<CompanyContacts>) JSON.parseArray(array.toString(), CompanyContacts.class);
                        list.clear();
                        list.addAll(templist);
                        datas.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                    } else {
                        Toast.makeText(oaAddPersonActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }

                    if (list==null||list.size()==0){
                        nodata_layout.setVisibility(View.VISIBLE);
                    }else {
                        nodata_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onItemClick(View view, int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setChecked(true);
                checke_model = list.get(position);
            } else {
                list.get(i).setChecked(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()==0){
            list.clear();
            list.addAll(datas);
            adapter.notifyDataSetChanged();
        }
        if (s.length()>0){
            list.clear();
            for(int i = 0;i<datas.size();i++){
                //进行遍历   若有相同的  则提取到搜索列表中
                if(datas.get(i).getReal_name().indexOf(editText.getText().toString()) != -1 ||
                        datas.get(i).getReal_name().indexOf(editText.getText().toString()) != -1){
                    list.add(datas.get(i));
                }
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Hideinputwindown(editText);
        return false;
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        editText.setText("");
        list.clear();
        datas.clear();
        initParama();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
