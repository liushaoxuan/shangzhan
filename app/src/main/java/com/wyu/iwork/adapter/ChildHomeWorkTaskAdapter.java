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

import com.wyu.iwork.R;
import com.wyu.iwork.model.HomeTaskModel;
import com.wyu.iwork.view.activity.DetailsTaskActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/28.16:56
 * 邮箱：2587294424@qq.com
 */

public class ChildHomeWorkTaskAdapter extends RecyclerView.Adapter<ChildHomeWorkTaskAdapter.ViewHolder> {

    private Context mcontext;
    private List<HomeTaskModel> list;

    public ChildHomeWorkTaskAdapter(Context mcontext, List<HomeTaskModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.child_item_home_work_task,parent,false);
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
         *
         */
        @BindView(R.id.chil_item_home_work_task_ring)
        ImageView icon;

        /**
         * 任务内容
         */
        @BindView(R.id.chil_item_home_work_task_content)
        TextView task_content;

        /**
         * 任务状态
         */
        @BindView(R.id.chil_item_home_work_task_state)
        TextView task_state;

        @BindView(R.id.chil_item_home_work_task)
        LinearLayout mview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void setData(int position){

           final  HomeTaskModel item = list.get(position);
            task_content.setText(item.getTitle());
            task_state.setText(item.getStatus());
            String statu = item.getStatus()==null?"":item.getStatus();
            switch (statu){//任务状态 2：待执行；3：已取消；4：已过期 100：任务完成
                case "2":
                    task_state.setText("待执行");
                    break;
                case "3":
                    task_state.setText("已取消");
                    break;
                case "4":
                    task_state.setText("已过期");
                    break;
                case "100":
                    task_state.setText("任务完成");
                    break;
            }

            //任务详情
            mview.setOnClickListener(new View.OnClickListener() {
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
