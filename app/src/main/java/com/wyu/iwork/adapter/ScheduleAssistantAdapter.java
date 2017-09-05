package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.ScheduleAssistentModel;
import com.wyu.iwork.view.dialog.DelPopWindown;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/21.11:17
 * 邮箱：2587294424@qq.com
 */

public class ScheduleAssistantAdapter extends RecyclerView.Adapter<ScheduleAssistantAdapter.ViewHolder> {

    private Context context;
    private List<ScheduleAssistentModel> list  = new ArrayList<>();

    public ScheduleAssistantAdapter(Context context,List<ScheduleAssistentModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ScheduleAssistantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assistant,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        holder.setdata(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 时间
         */
        @BindView(R.id.item_assistant_addtime)
        TextView crate_time;
        /**
         * 标题
         */
        @BindView(R.id.item_assistant_title)
        TextView title;

        /**
         * 内容
         */
        @BindView(R.id.item_assistant_content)
        TextView content;

        /**
         * 来源
         */
        @BindView(R.id.item_assistant_from)
        TextView from;

        /**
         * 图标
         */
        @BindView(R.id.item_assistant_icon)
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void setdata(final int position){

            ScheduleAssistentModel item = list.get(position);

            crate_time.setText(item.getAdd_time());
            title.setText(item.getTitle());
            content.setText(item.getContent());
            from.setText("来自日程通知");

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }

}
