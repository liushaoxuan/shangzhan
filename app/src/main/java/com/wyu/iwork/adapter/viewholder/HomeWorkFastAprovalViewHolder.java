package com.wyu.iwork.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.HomeApllyModel;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.view.activity.oaApplyForLeaveDetailActivity;
import com.wyu.iwork.view.activity.oaApprovalActivity;
import com.wyu.iwork.view.activity.oaBusinessTravelDetailActivity;
import com.wyu.iwork.view.activity.oaReimbursementDetailActivity;
import com.wyu.iwork.view.activity.oaWorkOverTimeDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/27.10:11
 * 邮箱：2587294424@qq.com
 */

public class HomeWorkFastAprovalViewHolder extends HomeWorkViewHolder {


    //立即处理
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_add)
    TextView fast_aproval;

    //无数据时的默认布局
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_nodata)
    LinearLayout nodata;

    //提示语
    @BindView(R.id.text_layout_nodata_prompt)
    TextView prompt;

    //用户名
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_userName)
    TextView userName;

    //申请类型  请假  加班 报销 出差
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_aproval_type)
    TextView aproval_type;

    //申请类型 请假类型  加班类型 报销类型 出差地点
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_type)
    TextView type;

    //申请类型值 请假类型  加班类型 报销类型 出差地点
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_type_name)
    TextView type_content;

    // 时长   报销总计
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_times)
    TextView times_or_payAccount;

    // 时长   报销总计内容
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_times_content)
    TextView times_or_payAccount_content;

    // 开始时间  费用类型
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_begin)
    TextView begin_cost_type;

    // 开始时间  费用类型内容
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_begin_time)
    TextView begin_cost_type_content;

    // 结束时间
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_end)
    TextView end;

    // 结束时间值
    @BindView(R.id.item_home_work_fast_arpoval_viewholder_end_time)
    TextView endtime;


    private HomeApllyModel item;
    private Intent intent = null;

    public HomeWorkFastAprovalViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(List t, int position) {

        if (t != null && t.size() > 0) {
            nodata.setVisibility(View.GONE);
            item = (HomeApllyModel) t.get(0);
            String types = item.getType() == null ? "" : item.getType();
            userName.setText(item.getUser_name() + "的");

            switch (types) {//类型 1:请假 2:加班 3:出差 4:报销
                case "1"://请假
                    type.setText("请假类型:");
                    times_or_payAccount.setText("请假" + "时长:");
                    begin_cost_type.setText("开始时间:");
                    end.setText("结束时间:");

                    begin_cost_type_content.setText(item.getStart_time());
                    endtime.setText(item.getEnd_time());
                    times_or_payAccount_content.setText(item.getNumber()+"小时");
                    aproval_type.setText("[请假]");
                    type_content.setText(item.getApply_type());
                    intent = new Intent(mcontext, oaApplyForLeaveDetailActivity.class);
                    break;

                case "2"://加班
                    type.setText("加班类型:");
                    times_or_payAccount.setText("加班" + "时长:");
                    begin_cost_type.setText("开始时间:");
                    end.setText("结束时间:");

                    begin_cost_type_content.setText(item.getStart_time());
                    endtime.setText(item.getEnd_time());
                    times_or_payAccount_content.setText(item.getNumber()+"小时");
                    aproval_type.setText("[加班]");
                    type_content.setText(item.getApply_type());
                    intent = new Intent(mcontext, oaWorkOverTimeDetailActivity.class);
                    break;

                case "3"://出差
                    type.setText("出差地点:");
                    times_or_payAccount.setText("出差" + "时长:");
                    begin_cost_type.setText("开始时间:");
                    end.setText("结束时间:");

                    begin_cost_type_content.setText(item.getStart_time());
                    endtime.setText(item.getEnd_time());
                    times_or_payAccount_content.setText(item.getNumber()+"小时");
                    aproval_type.setText("[出差]");
                    type_content.setText(item.getApply_type());
                    intent = new Intent(mcontext, oaBusinessTravelDetailActivity.class);
                    break;

                case "4"://报销
                    type.setText("报销类型:");
                    times_or_payAccount.setText("报销总计:");
                    begin_cost_type.setText("费用类型:");
                    end.setText("");
                    endtime.setText("");

                    begin_cost_type_content.setText(item.getContent());
                    endtime.setText(item.getEnd_time());
                    times_or_payAccount_content.setText(item.getNumber()+"元");
                    aproval_type.setText("[报销]");
                    type_content.setText(item.getApply_type());
                    intent = new Intent(mcontext, oaReimbursementDetailActivity.class);
                    break;
            }
            intent.putExtra("id", item.getApply_id());

            fast_aproval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mcontext.startActivity(intent);
                }
            });

        } else {
            nodata.setVisibility(View.VISIBLE);
            prompt.setText("暂无审批申请");
            fast_aproval.setVisibility(View.GONE);

            fast_aproval.setText("新建申请");
            fast_aproval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CustomUtils.showDialogForBusiness(mcontext)) {
                        intent = new Intent(mcontext, oaApprovalActivity.class);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }
    }
}