package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Task;
import com.wyu.iwork.view.activity.DetailsTaskActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/11.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private static final String TAG = TaskAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Task.TaskModel> taskList;

    public TaskAdapter(Context context,ArrayList<Task.TaskModel> taskList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.taskList = taskList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_task,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        checkStr(taskList.get(position).getReal_name().substring(0,1),holder.task_avatar);
        checkStr(taskList.get(position).getReal_name(),holder.task_name);
        checkStr(taskList.get(position).getBegin_time().split(" ")[0].replaceAll("-","."),holder.task_date);
        checkStr(taskList.get(position).getTask(),holder.task_content);
        setStatus(taskList.get(position).getStatus(),holder.task_status);
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    private void setStatus(String status,TextView view){
        /**
         * 2：执行中、100：已完成、3：已取消、4：已过期。
         */
        if("2".equals(status)){
            view.setText("执行中");
        }else if("3".equals(status)){
            view.setText("已取消");
            view.setTextColor(context.getResources().getColor(R.color.colorA6B1B8));
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_9fabb2_round_30px_1px));
        }else if("4".equals(status)){
            view.setText("已过期");
            view.setTextColor(context.getResources().getColor(R.color.colorF54545));
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_f54545_round_30px_1px));
        }else if("100".equals(status)){
            view.setText("已完成");
            view.setTextColor(context.getResources().getColor(R.color.colorGreen17C6B3));
            view.setBackground(context.getResources().getDrawable(R.drawable.bg_17c6b3_round_30px_1px));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.task_avatar)
        TextView task_avatar;

        @BindView(R.id.task_name)
        TextView task_name;

        @BindView(R.id.task_date)
        TextView task_date;

        @BindView(R.id.task_status)
        TextView task_status;

        @BindView(R.id.task_content)
        TextView task_content;

        @BindView(R.id.task_check_detail)
        TextView task_check_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            task_check_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDetailPage(getLayoutPosition());
                }
            });
        }

    }

    private void startDetailPage(int itemId){
        Intent intent = new Intent(context, DetailsTaskActivity.class);
        intent.putExtra("task_id",taskList.get(itemId).getTask_id());
        context.startActivity(intent);
    }

}
