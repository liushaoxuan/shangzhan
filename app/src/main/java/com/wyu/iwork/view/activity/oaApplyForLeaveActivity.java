package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.oaChosePersonAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.oaDelPerson;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.OptionsModel;
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
 * OA——申请请假
 *
 * @author sxliu
 */
public class oaApplyForLeaveActivity extends BaseActivity implements oaDelPerson {

    /**
     * 审批人请求码
     */
    public static final int TYPE_APROVAL = 0x00000001;
    /**
     * 抄送人人请求码
     */
    public static final int TYPE_COPY_SEND = 0x00000002;
    @BindView(R.id.oa_title)
    TextView title;

    /**
     * 请假类型
     */
    @BindView(R.id.oa_apply_type)
    CustomeViewGroup apply_type;

    /**
     * 开始时间
     */
    @BindView(R.id.od_apply_begin_time)
    CustomeViewGroup begin_time;

    /**
     * 结束时间
     */
    @BindView(R.id.od_apply_end_time)
    CustomeViewGroup end_time;

    /**
     * 请假时长
     */
    @BindView(R.id.od_apply_hours)
    CustomeViewGroup apply_hours;

    /**
     * 加班核算方式（这里不需要）
     */
    @BindView(R.id.od_apply_acount_type)
    CustomeViewGroup mtype;

    /**
     * 申请事由
     */
    @BindView(R.id.oa_apply_reason)
    TextView reason;

    /**
     * 申请事由输入框
     */
    @BindView(R.id.oa_apply_input)
    EditText editText;

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

    //开始时间
    private PickerViewDialog startTime;
    //结束时间
    private PickerViewDialog endTime;

    //请假类型的Picker
    private PickerViewDialog Typepicker;

    //请假类型

    //开始结束的毫秒数
    private long start = 0;
    private long end = 0;

    //请假类型
    private List<OptionsModel> optionsModelList = new ArrayList<>();

    //请假时长
    private double Hours = 0;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_apply_for_leave);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        initOptions();
        send_list.add(new CompanyContacts());
        aproal_list.add(new CompanyContacts());
        init();
    }

    //初始化
    private void init() {
        title.setText("请假");
        mtype.setVisibility(View.GONE);
        apply_hours.setInputType(InputType.TYPE_CLASS_NUMBER);
        aproval.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        aproval_adapter = new oaChosePersonAdapter(this, aproal_list, 0);
        aproval.setAdapter(aproval_adapter);
        /**
         * 审批人
         */
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
        /**
         * 抄送人
         */
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

        //开始时间
        startTime = new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                start = date.getTime();
                if (((start > end)||(start==end)) && end != 0) {
                    Toast.makeText(oaApplyForLeaveActivity.this, "开始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
                    return;
                }
//                LeaveHours(start, end);
                begin_time.setRightText(getTime(date));
            }
        }, "");

        //结束时间
        endTime = new PickerViewDialog(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                end = date.getTime();
                if ((start > end)||(start==end)) {
                    Toast.makeText(oaApplyForLeaveActivity.this, "结束时间需大于开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
//                LeaveHours(start, end);
                end_time.setRightText(getTime(date));
            }
        }, "");

        //请假类型
        Typepicker = new PickerViewDialog(this, optionsModelList, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String text = optionsModelList.get(options1).getPickerViewText();
                apply_type.setRightText(text);
            }
        });
    }

    //初始化请假类型的数据
    private void initOptions() {
        optionsModelList.add(new OptionsModel("年假"));
        optionsModelList.add(new OptionsModel("事假"));
        optionsModelList.add(new OptionsModel("病假"));
        optionsModelList.add(new OptionsModel("调休假"));
        optionsModelList.add(new OptionsModel("婚假"));
        optionsModelList.add(new OptionsModel("产假"));
        optionsModelList.add(new OptionsModel("陪产假"));
        optionsModelList.add(new OptionsModel("其他"));
    }

    @OnClick({R.id.oa_back, R.id.oa_history, R.id.oa_apply_type, R.id.od_apply_begin_time, R.id.od_apply_end_time, R.id.oa_apply_commit_activity_oa_apply_for_leave})
    void Click(View v) {
        Hideinputwindown(editText);
        switch (v.getId()) {
            case R.id.oa_back:
                onBackPressed();
                break;

            case R.id.oa_history://历史
                Intent intent = new Intent(oaApplyForLeaveActivity.this, oaApplyHistoryActivity.class);
                intent.setFlags(1);
                startActivity(intent);
                break;
            case R.id.oa_apply_type://请假类型
                if (Typepicker != null) {
                    Typepicker.show_Options();
                }
                break;

            case R.id.od_apply_begin_time://开始时间
                if (startTime != null) {
                    startTime.show_timepicker_h_m();
                }
                break;

            case R.id.od_apply_end_time://结束时间
                if (endTime != null) {
                    endTime.show_timepicker_h_m();
                }
                break;


            case R.id.oa_apply_commit_activity_oa_apply_for_leave://提交
                String type = apply_type.getRightText();
                String start_time = begin_time.getRightText();
                String e_time = end_time.getRightText();
                String number = apply_hours.getInput();
                String content = editText.getText().toString().trim();
                if (type.isEmpty()||type.equals("请选择")) {
                    Toast.makeText(this, "请选择请假类型", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (start_time.isEmpty()||start_time.equals("请选择")) {
                    Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (e_time.isEmpty()||e_time.equals("请选择")) {
                    Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (number.isEmpty()) {
                    Toast.makeText(this, "请输入时长", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (content.isEmpty()) {
                    Toast.makeText(this, "请输入请假事由", Toast.LENGTH_SHORT).show();
                    break;
                }

                //TODO 请假事由字数限制
                if (aproal_list==null|aproal_list.size()==0){
                    Toast.makeText(this, "请至少选择一个审批人", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    if (aproal_list.get(0).getId().isEmpty()){
                        Toast.makeText(this, "请至少选择一个审批人", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                oaApplyUtils.initParama(this, callback(), "1", type, start_time, e_time, Double.valueOf(number), content, "", "", aproal_list, send_list);
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
        Intent intent = new Intent(oaApplyForLeaveActivity.this, oaAddPersonActivity.class);
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

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

//    //计算请假时长
//    private void LeaveHours(long start, long end) {
//        DecimalFormat df = new DecimalFormat("######0.0");
//        if (start != 0 && end != 0) {
//            double hours = (double) (end - start) / (1000 * 60 * 60);
//            Hours = hours;
//            apply_hours.setRightText(df.format(hours) + "小时");
//        }
//    }

    //请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Toast.makeText(oaApplyForLeaveActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        String id= object.optString("data");
                        Intent intent = new Intent(oaApplyForLeaveActivity.this, oaApplyForLeaveDetailActivity.class);
                        intent.putExtra("id",id);
                        intent.putExtra("flag",0);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(oaApplyForLeaveActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
