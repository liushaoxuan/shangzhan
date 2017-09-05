package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.oaAprovalStepAdapter;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.oaApplyDetialModel;
import com.wyu.iwork.model.user_excludeModel;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.oaDetailRequestUtil;
import com.wyu.iwork.util.oaRevokeRemindersUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * oa 请假/加班 详情
 */
public class oaApplyDetailActivity extends BaseActivity {

    public static int REQUESTCODE = 0x0001;
    @BindView(R.id.oa_title)
    TextView title;

    /**
     * 头像
     */
    @BindView(R.id.activity_oa_apply_leave_detail_head)
    ImageView face_image;

    /**
     * 状态
     */
    @BindView(R.id.activity_oa_apply_leave_detail_state)
    TextView state;
    /**
     * 请假 加班 出差
     */
    @BindView(R.id.activity_oa_apply_leave_detail_my_apply)
    TextView my_apply;

    /**
     * 开始时间
     */
    @BindView(R.id.activity_oa_apply_leave_detail_startTime)
    TextView statrt_time;

    /**
     * 结束时间
     */
    @BindView(R.id.activity_oa_apply_leave_detail_endTime)
    TextView end_time;

    /**
     * 类型
     */
    @BindView(R.id.activity_oa_apply_leave_detail_type)
    TextView type;

    /**
     * 时长
     */
    @BindView(R.id.activity_oa_apply_leave_detail_hours)
    TextView hours;

    /**
     * 事由  （请假、加班）
     */
    @BindView(R.id.activity_oa_apply_leave_detail_reson_text)
    TextView resontext;

    /**
     * 事由  （具体内容）
     */
    @BindView(R.id.activity_oa_apply_leave_detail_reson)
    TextView reson;

    /**
     * 审批流程的recyclerview
     */
    @BindView(R.id.activity_oa_apply_leave_detail_recyclerview)
    RecyclerView recyclerView;

    /**
     * 抄送人
     */
    @BindView(R.id.activity_oa_apply_leave_detail_copy_send)
    TextView copy_send;
    /**
     * 催办 撤销
     */
    @BindView(R.id.activity_oa_apply_leave_detail_bottom_view)
    LinearLayout layout_bottom;
    /**
     * 类型
     */
    @BindView(R.id.activity_oa_apply_leave_detail_leixing)
    TextView leixing;

    /**
     * 出差的layout
     */
    @BindView(R.id.activity_oa_apply_leave_detail_travel_layout)
    RelativeLayout travel_layout;

    /**
     * 出差时长
     */
    @BindView(R.id.activity_oa_apply_leave_detail_travel_hours)
    TextView travel_Hours;

    /**
     * 同意、驳回、转交
     */
    @BindView(R.id.activity_oa_apply_leave_detail_bottom_view_arpoval)
    LinearLayout layout_agree_toback_toothers;

    /**
     *
     */
    @BindView(R.id.activity_oa_apply_leave_detail_view)
    RelativeLayout detail_view;

    private String mTitle = "";

    private String Id = "";

    private oaApplyDetialModel detailModel;

    private oaAprovalStepAdapter adapter;

    private int flag = -1;

    private int index = 0;

    private String meetingId = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_apply_leave_detail);
        getSupportActionBar().hide();
        getId();
        ButterKnife.bind(this);
        init();
    }


    //获取id
    private void getId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Id = bundle.getString("id");
            mTitle = bundle.getString("title");
            flag = bundle.getInt("flag");
            meetingId = bundle.getString("meetingId");
        }
    }

    //初始化
    private void init() {
        title.setText(mTitle + "详情");
        leixing.setText(mTitle + "类型");
        if ("出差".equals(mTitle)) {
            leixing.setText(mTitle + "地点");
            travel_layout.setVisibility(View.VISIBLE);
        }
        my_apply.setText("我的" + mTitle + "申请");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ScrollSmooth(recyclerView);
    }

    @OnClick({R.id.oa_back, R.id.activity_oa_apply_leave_detail_cuiban, R.id.activity_oa_apply_leave_detail_getback, R.id.activity_oa_apply_leave_detail_agree,
            R.id.activity_oa_apply_leave_detail_to_others, R.id.activity_oa_apply_leave_detail_to_back})
    void Click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.oa_back:
                onBackPressed();
                break;

            case R.id.activity_oa_apply_leave_detail_cuiban://催办
                oaRevokeRemindersUtils.Reminders(this, Id);
                break;

            case R.id.activity_oa_apply_leave_detail_getback://撤销
                oaRevokeRemindersUtils.Revoke(this, Id);
                break;

            case R.id.activity_oa_apply_leave_detail_agree://同意
                intent = new Intent(this, oaAgreeApllyActivity.class);
                intent.putExtra("id", Id);
                startActivity(intent);
                break;

            case R.id.activity_oa_apply_leave_detail_to_back://驳回
                intent = new Intent(this, oaRejectApplyActivity.class);
                intent.putExtra("id", Id);
                startActivity(intent);
                break;

            case R.id.activity_oa_apply_leave_detail_to_others://转交
                intent = new Intent(this, oaTransferApplyActivity.class);
                intent.putExtra("id", Id);
                List<CompanyContacts> list = new ArrayList<>();
                CompanyContacts  item = null;
                for (int i = 0;i<detailModel.getApproval_list().size();i++){
                    item = new CompanyContacts();
                    item.setId(detailModel.getApproval_list().get(i).getUser_id());
                    list.add(item);
                    item = null;
                }
                list.add(new CompanyContacts(detailModel.getUser_id(),"","","","","","",""));
                intent.putExtra("list", (Serializable) list);
                Logger.e("REQUESTCODE",REQUESTCODE+"");
                startActivityForResult(intent,REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout_agree_toback_toothers.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);
        oaDetailRequestUtil.initParama(this, Id,meetingId, callback());
    }

    //请求回调
    private DialogCallback callback() {
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
                        detail_view.setVisibility(View.VISIBLE);
                        JSONObject data = object.optJSONObject("data");
                        detailModel = JSON.parseObject(data.toString(), oaApplyDetialModel.class);

                        if (detailModel != null) {
                            setData4Views();
                        }
                    } else {
                        Toast.makeText(oaApplyDetailActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //给控件赋值
    private void setData4Views() {
        Glide.with(this).load(detailModel.getFace_img()).transform(new CenterCrop(this), new GlideRoundTransform(this, 5)).into(face_image);

        String statu = detailModel.getIs_pass();
        statrt_time.setText(detailModel.getStart_time());
        end_time.setText(detailModel.getEnd_time());
        type.setText(detailModel.getApply_type());
        hours.setText(detailModel.getNumber() + "小时");
        travel_Hours.setText(detailModel.getNumber() + "小时");
        reson.setText(detailModel.getContent());

        if ("出差".equals(mTitle)) {
            hours.setText("");
        }
        if (detailModel.getCopy_list()!=null&&detailModel.getCopy_list().length()>0){
            copy_send.setText(detailModel.getCopy_list().replace(",", "  "));
        }

        adapter = new oaAprovalStepAdapter(this, detailModel.getApproval_list());
        recyclerView.setAdapter(adapter);

        state.setText(statu);
        switch (statu) {  //审核状态(审批中,已通过,已拒绝,已撤销)
            case "审批中":
                state.setTextColor(getResources().getColor(R.color.oa_light_blue));
                state.setBackground(getResources().getDrawable(R.drawable.oa_state_aprovalling));
                if (flag == 1) {
                    layout_agree_toback_toothers.setVisibility(View.VISIBLE);
                    //如果自己已经审批过了，则审批按钮不出现
                    String users_id = detailModel.getUser_id_list();
                    if (users_id != null && users_id.length() > 0) {
                        String[] users_id_array = users_id.split(",");
                        for (int i = 0; i < users_id_array.length; i++) {
                            if (users_id_array[i].equals(userInfo.getUser_id())) {
                                layout_agree_toback_toothers.setVisibility(View.GONE);
                            }
                        }
                    }
                } else if (flag==0){
                    layout_bottom.setVisibility(View.VISIBLE);
                    //如果自己已经审批过了，则审批按钮不出现
                    String users_id = detailModel.getUser_id_list();
                    if (users_id != null && users_id.length() > 0) {
                        String[] users_id_array = users_id.split(",");
                        for (int i = 0; i < users_id_array.length; i++) {
                            if (users_id_array[i].equals(userInfo.getUser_id())) {
                                layout_bottom.setVisibility(View.GONE);
                            }
                        }
                    }
                }else {
                    layout_agree_toback_toothers.setVisibility(View.GONE);
                    layout_bottom.setVisibility(View.GONE);
                }
                break;

            case "已通过":
                state.setTextColor(getResources().getColor(R.color.oa_passed_color));
                state.setBackground(getResources().getDrawable(R.drawable.oa_state_passed));
                layout_agree_toback_toothers.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                break;

            case "已拒绝":
                state.setTextColor(getResources().getColor(R.color.oa_refuse_color));
                state.setBackground(getResources().getDrawable(R.drawable.oa_state_refused));
                layout_agree_toback_toothers.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                break;

            case "已撤销":
                state.setTextColor(getResources().getColor(R.color.oa_rreturn_color));
                state.setBackground(getResources().getDrawable(R.drawable.oa_state_return));
                layout_agree_toback_toothers.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("requestCode",requestCode+"");
        Logger.e("requestCode",resultCode+"resultCode");
        if (resultCode==REQUESTCODE){
            Logger.e("requestCode",requestCode+"finish");
            finish();
        }
    }
}
