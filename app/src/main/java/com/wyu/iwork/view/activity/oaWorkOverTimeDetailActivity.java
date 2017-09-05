package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment; 
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.OaDetailCopySendAdapter;
import com.wyu.iwork.adapter.oaAprovalStepAdapter;
import com.wyu.iwork.adapter.oaleaveDetailAdapter;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.oaApplyDetialModel;
import com.wyu.iwork.model.oaPaymentModel;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.oaDetailRequestUtil;
import com.wyu.iwork.util.oaRevokeRemindersUtils;

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
 * @author sxliu
 * oa加班详情
 */
public class oaWorkOverTimeDetailActivity extends BaseActivity { 
    private static String TAG = oaWorkOverTimeDetailActivity.class.getSimpleName();

    public static int REQUESTCODE = 0x00021;

    /**
     * 标题
     */
    @BindView(R.id.oa_title)
    TextView mTitle;

    /**
     * 头像
     */
    @BindView(R.id.activity_oa_work_over_time_detail_head)
    ImageView mHead;

    /**
     * 发起人
     */
    @BindView(R.id.activity_oa_work_over_time_detail_my_apply)
    TextView my_apply;

    /**
     * 审批状态
     */
    @BindView(R.id.activity_oa_work_over_time_detail_state)
    TextView status;

    /**
     * 加班类型
     */
    @BindView(R.id.activity_oa_work_over_time_detail_pay_type)
    TextView coast_type;

    /**
     * 加班天数
     */
    @BindView(R.id.activity_oa_work_over_time_detail_money)
    TextView coast_total;
    /**
     * 事由
     */
    @BindView(R.id.activity_oa_work_over_time_detail_reason)
    TextView reson;

    /**
     * 明细的recyclerview
     */
    @BindView(R.id.activity_oa_work_over_time_detail_reibursent_recyclerview)
    RecyclerView detailRecyclerview;

    /**
     * 审批流程的recyclerview
     */
    @BindView(R.id.activity_oa_work_over_time_detail_state_recyclerview)
    RecyclerView recyclerView;

    /**
     * 催办 撤销
     */
    @BindView(R.id.activity_oa_apply_leave_detail_bottom_view)
    LinearLayout layout_bottom;

    /**
     * 同意、驳回、转交
     */
    @BindView(R.id.activity_oa_apply_leave_detail_bottom_view_arpoval)
    LinearLayout layout_agree_toback_toothers;
    /**
     *
     */
    @BindView(R.id.activity_oa_work_over_time_detail_scrollview)
    NestedScrollView detailview;

    /**
     * 收起 展开 layout
     */
    @BindView(R.id.activity_oa_work_over_time_detail_open_close_layout)
    LinearLayout close_open_layout;
    /**
     * 收起 展开
     */
    @BindView(R.id.activity_oa_work_over_time_detail_open_close)
    TextView close_open;


    @BindView(R.id.activity_oa_work_over_time_detail_open_close_detail)
    TextView close_open_detail;
    /**
     * 抄送人recyclerview
     */
    @BindView(R.id.activity_oa_work_over_time_detail_copysend_recyclerview)
    RecyclerView copysendRecyclerview;
    /**
     * 加班详情的layout
     */
    @BindView(R.id.activity_oa_work_over_time_detail_layout)
    LinearLayout detail_layout;

    /**
     * 职位
     */
    @BindView(R.id.activity_oa_work_over_time_detail_post)
    TextView post_position;

    @BindView(R.id.oa_detail_copysend_line)
    View line;

    private String Id = "";

    private oaApplyDetialModel detailModel;

    /**
     * 审批流程adapter
     */
    private oaAprovalStepAdapter adapter;
    /**
     * 明细adapter
     */
    private oaleaveDetailAdapter detailAdapter;
    /**
     * 抄送人adapter
     */
    private OaDetailCopySendAdapter copySendAdapter;

    private List<oaPaymentModel> detaillist = new ArrayList<>();
    //抄送人list
    private List<String> copysendList = new ArrayList<>();
    private String meetingId = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_work_over_time_detail);
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
            meetingId = bundle.getString("meetingId");
        }
    }


    //初始化
    private void init() {
        mTitle.setText("加班详情");
        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        detailRecyclerview.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        copysendRecyclerview.setLayoutManager(new GridLayoutManager(this,5));
        copySendAdapter = new OaDetailCopySendAdapter(this,copysendList);
        copysendRecyclerview.setAdapter(copySendAdapter);
    }


    @OnClick({R.id.oa_back, R.id.activity_oa_apply_leave_detail_cuiban, R.id.activity_oa_apply_leave_detail_getback, R.id.activity_oa_apply_leave_detail_agree,
            R.id.activity_oa_apply_leave_detail_to_others, R.id.activity_oa_apply_leave_detail_to_back,R.id.activity_oa_work_over_time_detail_open_close_layout})
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
            case R.id.activity_oa_work_over_time_detail_open_close_layout://展开 收起
                boolean isopen = close_open.isSelected();
                if (isopen){
                    detail_layout.setVisibility(View.VISIBLE);
                    close_open_detail.setVisibility(View.INVISIBLE);
                    close_open.setText("收起");
                    close_open_layout.setSelected(false);
                }else {
                    detail_layout.setVisibility(View.GONE);
                    close_open_detail.setVisibility(View.VISIBLE);
                    close_open.setText("展开");
                    close_open_layout.setSelected(true);
                }

                break;

            case R.id.activity_oa_apply_leave_detail_to_others://转交
                intent = new Intent(this, oaTransferApplyActivity.class);
                intent.putExtra("id", Id);
                List<CompanyContacts> list = new ArrayList<>();
                CompanyContacts item = null;
                for (int i = 0; i < detailModel.getApproval_list().size(); i++) {
                    item = new CompanyContacts();
                    item.setId(detailModel.getApproval_list().get(i).getUser_id());
                    list.add(item);
                    item = null;
                }
                list.add(new CompanyContacts(detailModel.getUser_id(), "", "", "", "", "", "",""));
                intent.putExtra("list", (Serializable) list);
                intent.setFlags(1);
                startActivityForResult(intent, REQUESTCODE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout_agree_toback_toothers.setVisibility(View.GONE);
        layout_bottom.setVisibility(View.GONE);
        oaDetailRequestUtil.initParama(this, Id, meetingId, callback());

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
                    Logger.e(TAG, s);
                    if ("0".equals(code)) {
                        detailview.setVisibility(View.VISIBLE);
                        JSONObject data = object.optJSONObject("data");
                        detailModel = JSON.parseObject(data.toString(), oaApplyDetialModel.class);
                        if (detailModel != null) {
                            setData4Views();
                        }
                    } else {
                        Toast.makeText(oaWorkOverTimeDetailActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
    }

    //给控件赋值
    private void setData4Views() {
        Glide.with(this).load(detailModel.getFace_img()).transform(new CenterCrop(this), new GlideRoundTransform(this, 5)).into(mHead);

        String statu = detailModel.getIs_pass();
        if (detailModel.getCopy_list() != null) {
            String[] copysends = detailModel.getCopy_list().split(",");
            copysendList.clear();
            for (int i = 0;i<copysends.length;i++){
                copysendList.add(copysends[i]);
            }
            if (copysendList.size()>0){
                line.setVisibility(View.VISIBLE);
            }
            copySendAdapter.notifyDataSetChanged();
        }

        adapter = new oaAprovalStepAdapter(this, detailModel.getApproval_list());
        recyclerView.setAdapter(adapter);
        detaillist.clear();
        detaillist.add(new oaPaymentModel(detailModel.getType(),detailModel.getStart_time(),detailModel.getEnd_time(),detailModel.getContent()));
        detailAdapter = new oaleaveDetailAdapter(this, detaillist);
        detailRecyclerview.setAdapter(detailAdapter);

        status.setText(statu);
        my_apply.setText(detailModel.getUser_name()+"的加班申请");
        post_position.setText(detailModel.getUser_job());
        coast_type.setText(detailModel.getApply_type());
        coast_total.setText(detailModel.getNumber() + "小时");
        reson.setText(detailModel.getContent());
        //申请人id
       String uid =  detailModel.getUser_id();
        //申请人是自己
        if (uid.equals(userInfo.getUser_id())){
            layout_bottom.setVisibility(View.VISIBLE);
        }else {//申请人不是自己 则显示审布局
            for (int i = 0;i<detailModel.getApproval_list().size();i++){
                if (userInfo.getUser_id().equals(detailModel.getApproval_list().get(i).getUser_id())){
                    layout_agree_toback_toothers.setVisibility(View.VISIBLE);
                }
            }
        }
        //已审核人的id集合
       String ids =  detailModel.getUser_id_list()==null?"":detailModel.getUser_id_list();
        String[] idslist = ids.split(",");
        for (int i = 0;i<idslist.length;i++){
            if (userInfo.getUser_id().equals(idslist[i])){
                //自己已经审批过了
                layout_agree_toback_toothers.setVisibility(View.GONE);
            }
        }


        switch (statu) {  //审核状态(审批中,已通过,已拒绝,已撤销)
            case "审批中":
                status.setTextColor(getResources().getColor(R.color.oa_light_blue));
                status.setBackground(getResources().getDrawable(R.drawable.oa_state_aprovalling));
                break;

            case "已通过":
                status.setTextColor(getResources().getColor(R.color.oa_passed_color));
                status.setBackground(getResources().getDrawable(R.drawable.oa_state_passed));
                layout_agree_toback_toothers.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                break;

            case "已拒绝":
                status.setTextColor(getResources().getColor(R.color.oa_refuse_color));
                status.setBackground(getResources().getDrawable(R.drawable.oa_state_refused));
                layout_agree_toback_toothers.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                break;

            case "已撤销":
                status.setTextColor(getResources().getColor(R.color.oa_rreturn_color));
                status.setBackground(getResources().getDrawable(R.drawable.oa_state_return));
                layout_agree_toback_toothers.setVisibility(View.GONE);
                layout_bottom.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.e("requestCode", requestCode + "");
        Logger.e("requestCode", resultCode + "resultCode");
        if (resultCode == REQUESTCODE) {
            Logger.e("requestCode", requestCode + "finish");
            finish();
        }
    }

    @Override
    public void Refresh() {
        onResume();
    }
}
