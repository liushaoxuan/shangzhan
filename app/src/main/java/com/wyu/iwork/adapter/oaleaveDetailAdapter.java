package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.oaPaymentModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/6/13.14:47
 * 邮箱：2587294424@qq.com
 * OA——请假详情的adapter
 */

public class oaleaveDetailAdapter extends RecyclerView.Adapter<oaleaveDetailAdapter.ViewHolder> {

    private Context context;
    private List<oaPaymentModel> list;

    public oaleaveDetailAdapter(Context context, List<oaPaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.oa_item_reimbursement_detail, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        /**
         * 费用类型
         */
        @BindView(R.id.oa_item_reimbursement_detail_type)
        TextView type;

        /**
         * 时间
         */
        @BindView(R.id.oa_item_reimbursement_detail_date)
        TextView feeTime;

        /**
         * 费用
         */
        @BindView(R.id.oa_item_reimbursement_detail_money)
        TextView coast;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setData(int position) {


            oaPaymentModel item = list.get(position);
            type.setText(item.getCost_type());
            feeTime.setText(item.getStart_time());
            coast.setText(item.getMoney());

        }
    }
}
