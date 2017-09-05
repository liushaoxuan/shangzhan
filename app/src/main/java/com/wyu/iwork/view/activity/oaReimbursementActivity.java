package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.oaChosePersonAdapter;
import com.wyu.iwork.adapter.oaReimbursementAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.oaDelPerson;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.OptionsModel;
import com.wyu.iwork.model.oaPaymentModel;
import com.wyu.iwork.util.oaApplyUtils;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * OA——申请报销
 */
public class oaReimbursementActivity extends BaseActivity implements oaDelPerson {



    /**
     * 审批人请求码
     */
    public static final int TYPE_APROVAL = 0x00000009;
    /**
     * 抄送人人请求码
     */
    public static final int TYPE_COPY_SEND = 0x00000010;
    @BindView(R.id.oa_title)
    TextView title;

    /**
     * 总金额
     */
    @BindView(R.id.activity_oa_reimbursement_total)
    TextView total;

    /**
     * 报销类型
     */
    @BindView(R.id.activity_oa_reimbursement_apply_type)
    CustomeViewGroup apply_type;
    
    /**
     * 报销事由
     */
    @BindView(R.id.activity_oa_reimbursement_apply_input)
    EditText input;

    /**
     * 审批人列表
     */
    @BindView(R.id.oa_apply_aproval)
    RecyclerView aproval;

    /**
     * 抄送人列表
     */
    @BindView(R.id.oa_apply_coppy_send)
    RecyclerView coppy_send;

    /**
     * 抄送人列表
     */
    @BindView(R.id.activity_oa_reimbursement_recyclerview)
    RecyclerView payment_recyclerview;

    //明细adapter
    private oaReimbursementAdapter reimbursementAdapter;

    //审批人adapter
    private oaChosePersonAdapter aproval_adapter;

    //抄送人adapter
    private oaChosePersonAdapter send_adapter;

    //审批人集合
    private List<CompanyContacts> aproal_list = new ArrayList<>();

    //抄送人集合
    private List<CompanyContacts> send_list = new ArrayList<>();

    //审批人和抄送人的集合  因为审批人和抄送人不能重复选择
    private List<CompanyContacts> allList = new ArrayList<>();

    //报销类型的Picker
    private PickerViewDialog Typepicker;

    //报销类型
    private List<OptionsModel> optionsModelList = new ArrayList<>();

    //报销model
    private List<oaPaymentModel> paymentList = new ArrayList<>();
    private double total_coast = 0;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_reimbursement);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        send_list.add(new CompanyContacts());
        aproal_list.add(new CompanyContacts());
        initOptions();
        init();
    }


    //初始化报销类型的数据
    private void initOptions() {
        optionsModelList.add(new OptionsModel("差旅费"));
        optionsModelList.add(new OptionsModel("交通费"));
        optionsModelList.add(new OptionsModel("招待费"));
        optionsModelList.add(new OptionsModel("其他"));

    }
    //初始化
    private void init() {
        title.setText("报销");
        apply_type.setNameContent("报销类型");
        aproval.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        aproval_adapter = new oaChosePersonAdapter(this, aproal_list, 0);
        aproval.setAdapter(aproval_adapter);
        aproval_adapter.setOnItemClickListener(new onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (aproal_list.get(position).getId()==null||"".equals(aproal_list.get(position).getId())){
                    Chose(TYPE_APROVAL);
                }else {
                    del(aproval,position,0);
                }
            }
        });

        aproval_adapter.setDelperson(this);
        coppy_send.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        send_adapter = new oaChosePersonAdapter(this, send_list, 1);
        coppy_send.setAdapter(send_adapter);
        send_adapter.setOnItemClickListener(new onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (send_list.get(position).getId()==null||"".equals(send_list.get(position).getId())){
                    Chose(TYPE_COPY_SEND);
                }else {
                    del(coppy_send,position,1);
                }
            }
        });
        send_adapter.setDelperson(this);


        //报销类型
        Typepicker = new PickerViewDialog(this, optionsModelList, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String text = optionsModelList.get(options1).getPickerViewText();
                apply_type.setRightText(text);
            }
        });

        paymentList.add(new oaPaymentModel());
        reimbursementAdapter = new oaReimbursementAdapter(this,paymentList);
        payment_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        payment_recyclerview.setAdapter(reimbursementAdapter);
         ScrollSmooth(payment_recyclerview);

    }


    @OnClick({R.id.oa_back, R.id.oa_history, R.id.activity_oa_reimbursement_apply_type, R.id.oa_apply_commit_activity_oa_reimbursement})
    void Click(View v) {
        Hideinputwindown(v);
        switch (v.getId()) {
            case R.id.oa_back:
                onBackPressed();
                break;

            case R.id.activity_oa_reimbursement_apply_type://报销类型

                if (Typepicker != null) {
                    Typepicker.show_Options();
                }
                break;

            case R.id.oa_apply_commit_activity_oa_reimbursement://提交
                String type = apply_type.getRightText();
                String content = input.getText().toString().trim();



                if (type.isEmpty()||type.equals("请选择")) {
                    Toast.makeText(this, "请选择报销类型", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (content.isEmpty()) {
                    Toast.makeText(this, "请输入报销事由", Toast.LENGTH_SHORT).show();
                    break;
                }

                int num = 0;
                for (int i = 0;i<paymentList.size();i++){
                    num = i+1;
                    if (paymentList.get(i).getCost_type()==null||"".equals(paymentList.get(i).getCost_type())){
                        Toast.makeText(this, "请选择报销明细"+num+"的费用类型", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (paymentList.get(i).getStart_time()==null||"".equals(paymentList.get(i).getStart_time())||"请选择".equals(paymentList.get(i).getStart_time())){
                        Toast.makeText(this, "请选择报销明细"+num+"的发生时间", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (paymentList.get(i).getMoney()==null||"".equals(paymentList.get(i).getMoney())){
                        Toast.makeText(this, "请输入报销明细"+num+"的费用金额", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    if (paymentList.get(i).getContent()==null||"".equals(paymentList.get(i).getContent())){
//                        Toast.makeText(this, "请输入报销明细"+num+"的费用说明", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                }

           /*     if (content.length()<10){
                    Toast.makeText(this,"报销事由不少于10个字",Toast.LENGTH_SHORT).show();
                    break;
                }*/
                //TODO 报销事由字数限制
                String payment = JSON.toJSONString(paymentList);
                if (aproal_list==null|aproal_list.size()==0){
                    Toast.makeText(this, "请至少选择一个审批人", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    if (aproal_list.get(0).getId().isEmpty()){
                        Toast.makeText(this, "请至少选择一个审批人", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                oaApplyUtils.initParama(this, callback(), "4", type, "", "", total_coast, content,"", payment, aproal_list, send_list);
                break;


        }
    }


    //删除
    @Override
    public void del(View view, int position, int type) {

        switch (type) {
            case 0:
                aproal_list.remove(position);
                if (aproal_list.size() == 0||(aproal_list.size()<4&&!"".equals(aproal_list.get(aproal_list.size()-1).getId()))) {
                    aproal_list.add(new CompanyContacts());
                }
                aproval_adapter.notifyDataSetChanged();
                break;
            case 1:
                send_list.remove(position);
                if (send_list.size() == 0||(send_list.size()<4&&!"".equals(send_list.get(send_list.size()-1).getId()))) {
                    send_list.add(new CompanyContacts());
                }
                send_adapter.notifyDataSetChanged();
                break;
        }
    }

    private void Chose(int type) {
        allList.clear();
        allList.addAll(aproal_list);
        allList.addAll(send_list);
        Intent intent = new Intent(oaReimbursementActivity.this, oaAddPersonActivity.class);
        intent.putExtra("contacts", (Serializable) allList);
        intent.setFlags(type);
        startActivityForResult(intent, type);
    }

    //回调

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            CompanyContacts item = (CompanyContacts) data.getExtras().get("model");
            if (item != null) {
                switch (requestCode) {
                    case TYPE_APROVAL:
                        if (aproal_list.size() < 4) {
                            aproal_list.add(aproal_list.size() - 1, item);
                        } else {
                            aproal_list.set(aproal_list.size() - 1, item);
                        }
                        aproval_adapter.notifyDataSetChanged();
                        break;
                    case TYPE_COPY_SEND:
                        if (send_list.size() < 4) {
                            send_list.add(send_list.size() - 1, item);
                        } else {
                            send_list.set(send_list.size() - 1, item);
                        }
                        send_adapter.notifyDataSetChanged();
                        break;
                }
            }

        }
    }


    //计算报销总金额
    public void setTotal() {
        DecimalFormat df = new DecimalFormat("######0.00");
        double temp_account = 0;
        for (int i = 0;i<paymentList.size();i++){
         double money = Double.parseDouble("".equals(paymentList.get(i).getMoney())?"0":paymentList.get(i).getMoney())  ;
            temp_account = temp_account + money;
        }
        total_coast = temp_account;
        total.setText(df.format(total_coast)+"元");
    }

    //请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Toast.makeText(oaReimbursementActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        String id= object.optString("data");
                        Intent intent = new Intent(oaReimbursementActivity.this, oaReimbursementDetailActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("flag",0);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(oaReimbursementActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 删除明细
     */
    public void del(int position){
        paymentList.remove(position);
        reimbursementAdapter.notifyDataSetChanged();
        setTotal();
    }

    /**
     * 新增明细
     */
    public void add(){
        paymentList.add(new oaPaymentModel());
        reimbursementAdapter.notifyDataSetChanged();
    }

    /**
     * 设置报销类型
     */
    public void setType(int position ,String text){
        paymentList.get(position).setCost_type(text);
    }

    /**
     * 设置发生时间
     */
    public void setTime(int position ,String text){
        paymentList.get(position).setStart_time(text);
    }

    /**
     * 设置费用
     */
    public void setCoast(int position ,String text){
        paymentList.get(position).setMoney(text);
    }

    /**
     * 设置费用说明
     */
    public void setdescribe(int position ,String text){
        paymentList.get(position).setContent(text);
    }

}
