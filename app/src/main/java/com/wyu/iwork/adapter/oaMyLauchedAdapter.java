package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.oaAprovalSendLauchModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/6/6.13:42
 * 邮箱：2587294424@qq.com
 *  OA——我发起的adapter
 *
 */

public class oaMyLauchedAdapter extends RecyclerView.Adapter<oaMyLauchedAdapter.ViewHolder>{

    private Context context;
    private List<oaAprovalSendLauchModel> list;
    private  onItemClickListener onItemClickListener;

    public oaMyLauchedAdapter(Context context, List<oaAprovalSendLauchModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_my,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setDate(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        /**
         *  名称
         */
        @BindView(R.id.item_oa_title)
        TextView title;

        /**
         *  状态
         */
        @BindView(R.id.item_oa_state)
        TextView status;

        /**
         *  日期
         */
        @BindView(R.id.item_oa_date)
        TextView date;

        /**
         *  开始时间
         */
        @BindView(R.id.item_oa_start_time)
        TextView start_time;

        /**
         *  结束时间
         */
        @BindView(R.id.item_oa_end_time)
        TextView end_time;

        /**
         *  持续时间
         */
        @BindView(R.id.item_oa_duration)
        TextView duration;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setDate( final int position){

            oaAprovalSendLauchModel item = list.get(position);

            start_time.setText("开始时间："+item.getStart_time());
            end_time.setText("结束时间："+item.getEnd_time());

            if ("4".equals(item.getType())){//类型(1:请假,2:加班,3:出差,4:报销
                start_time.setText("开始时间："+item.getStart_time());
                end_time.setText("报销金额："+item.getNumber());
            }
            date.setText(item.getCreate_time());
            status.setText(item.getIs_pass());
            title.setText(item.getApply_type());

            switch (item.getIs_pass()){
                case "审批中"://审批中
                    status.setTextColor(context.getResources().getColor(R.color.light_blue));
                    status.setBackground(context.getResources().getDrawable(R.drawable.oa_textview_bg_light_blue));
                    break;

                case "已通过"://已通过
                    status.setTextColor(context.getResources().getColor(R.color.oa_passed_color));
                    status.setBackground(context.getResources().getDrawable(R.drawable.oa_textview_bg_passed));
                    break;

                case "未通过"://未通过
                    status.setTextColor(context.getResources().getColor(R.color.oa_refuse_color));
                    status.setBackground(context.getResources().getDrawable(R.drawable.oa_textview_bg_refuse));
                    break;

                case "已撤销"://已撤销
                    status.setTextColor(context.getResources().getColor(R.color.oa_rreturn_color));
                    status.setBackground(context.getResources().getDrawable(R.drawable.oa_textview_bg_return));
                    break;
            }

            if (onItemClickListener!=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(itemView,position);
                    }

                });
            }

        }

    }

    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }
}
