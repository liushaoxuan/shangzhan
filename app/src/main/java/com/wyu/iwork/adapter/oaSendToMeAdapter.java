package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/6/6.13:42
 * 邮箱：2587294424@qq.com
 *  OA——抄送我的adapter
 *
 */

public class oaSendToMeAdapter extends RecyclerView.Adapter<oaSendToMeAdapter.ViewHolder>{

    private Context context;
    private List list;

    public oaSendToMeAdapter(Context context, List list) {
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
        return 4;
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

        private void setDate( int position){

            start_time.setText("开始时间："+"2017年4月28日  14:00");
            end_time.setText("结束时间："+"2017年4月28日  18:00");

        }

    }
}
