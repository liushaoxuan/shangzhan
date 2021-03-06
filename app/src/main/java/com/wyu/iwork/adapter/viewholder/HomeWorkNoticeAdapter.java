package com.wyu.iwork.adapter.viewholder;

import android.content.Context;
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
import com.wyu.iwork.model.HomeWorkNoticeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sxliu on 2017/4/12.
 * 首页——工作——公告adapter
 */

public class HomeWorkNoticeAdapter extends RecyclerView.Adapter<HomeWorkNoticeAdapter.ViewHolder> {
    private Context mcontext;
    private List<HomeWorkNoticeModel> list;

    public HomeWorkNoticeAdapter(Context mcontext, List<HomeWorkNoticeModel> list) {
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
        return list.size();
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
        /**
         *
         * @param itemView
         */

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        private void setData(int position){
            HomeWorkNoticeModel item = list.get(position);
            if (position==0){
                clasic.setVisibility(View.VISIBLE);
            }else {
                clasic.setVisibility(View.GONE);
            }
            Glide.with(mcontext).load(item.getFace_img()).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 5)).into(head);
            taskName.setText(item.getTitle());
            taskContent.setText(item.getText());
            taskEndTime.setText(item.getAdd_time());
            clasicName.setText("公告");
            faqiren.setText(item.getUser_name());
        }
    }
}
