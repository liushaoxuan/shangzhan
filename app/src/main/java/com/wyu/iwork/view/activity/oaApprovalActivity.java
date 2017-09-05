package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * OA_审批
 * @author sxliu
 */
public class oaApprovalActivity extends BaseActivity {

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView manager;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_approval);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
    }

    //初始化
    private void init(){
        title.setText("审批");
        manager.setText("管理");
        manager.setVisibility(View.GONE);
    }

    /**
     * 事件注入
     */
    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_oa_approval_my_lauch,R.id.activity_oa_approval_my_approval,R.id.activity_oa_approval_chaosong,R.id.activity_oa_approval_leave_apply,
    R.id.activity_oa_approval_work_on_apply,R.id.activity_oa_approval_reimbursement_apply,R.id.activity_oa_approval_go_out_apply})
    void Click(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back://返回
                onBackPressed();
                break;

            case R.id.action_edit://管理
//                intent = new Intent(this,oaZxingcodeActivity.class);
//                startActivity(intent);
                break;

            case R.id.activity_oa_approval_my_lauch://我发起的
                intent = new Intent(this,oaMyLauchedActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_oa_approval_my_approval://待我审批的
                intent = new Intent(this,oaMyAprovalActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_oa_approval_chaosong://抄送我的
                intent = new Intent(this,oaCopySendToMeActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_oa_approval_leave_apply://请假申请
                intent = new Intent(this,oaApplyForLeaveActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_oa_approval_work_on_apply://加班申请
                intent = new Intent(this,oaWorkOverTimeActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_oa_approval_reimbursement_apply://报销申请
                intent = new Intent(this,oaReimbursementActivity.class);
                startActivity(intent);
                break;

            case R.id.activity_oa_approval_go_out_apply://出差申请
                intent = new Intent(this,oaBusinessTravelActivity.class);
                startActivity(intent);
                break;
        }
    }

}
