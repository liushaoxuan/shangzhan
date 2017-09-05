package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.HomeTaskModel;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.view.activity.DetailsTaskActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.TaskActivity;
import com.wyu.iwork.view.activity.TaskDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sxliu on 2017/4/12.
 * 首页——工作——任务adapter
 */

public class HomeWorkTaskAdapter extends RecyclerView.Adapter<HomeWorkTaskAdapter.ViewHolder> {
    private Context mcontext;
    private List<HomeTaskModel> list;

    public HomeWorkTaskAdapter(Context mcontext, List<HomeTaskModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_home_work_task,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
//        return list.size();
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 分类
         */
        @BindView(R.id.item_home_work_task_more_layout)
        LinearLayout clasic;
        /**
         * 分类名称
         */
        @BindView(R.id.item_home_work_task_type)
        TextView clasicName;
        /**
         * 更多
         */
        @BindView(R.id.item_home_work_task_more)
        TextView more;
        /**
         * 头像
         */
        @BindView(R.id.item_home_work_task_head)
        ImageView head;
        /**
         * 任务名称
         */
        @BindView(R.id.item_home_work_task_taskName)
        TextView taskName;
        /**
         * 任务内容
         */
        @BindView(R.id.item_home_work_task_content)
        TextView taskContent;
        /**
         * 任务结束时间
         */
        @BindView(R.id.item_home_work_task_end_time)
        TextView taskEndTime;
        /**
         * 任务发起人layout
         */
        @BindView(R.id.item_home_work_task_faqiren_layout)
        LinearLayout faqirenLayout;
        /**
         * 任务发起人
         */
        @BindView(R.id.item_home_work_task_faqiren)
        TextView faqiren;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }
        private void setData(int position){
            faqirenLayout.setVisibility(View.GONE);
            final HomeTaskModel item = list.get(position);
            if (position==0){
                clasic.setVisibility(View.VISIBLE);
            }else {
                clasic.setVisibility(View.GONE);
            }
            taskEndTime.setText(item.getEnd_time());
            clasicName.setText("任务安排");

            //更多
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!CustomUtils.showDialogForBusiness((MainActivity)mcontext)){
                        return;
                    }
                    Intent intent = new Intent(mcontext, TaskActivity.class);
                    mcontext.startActivity(intent);
                }
            });

            //任务详情
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, DetailsTaskActivity.class);
                    intent.putExtra("task_id",item.getTask_id());
                    mcontext.startActivity(intent);
                }
            });
        }
    }
}
