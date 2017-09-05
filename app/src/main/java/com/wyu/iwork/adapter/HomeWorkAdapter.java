package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.viewholder.HomeWorkDailyWorkViewHolder;
import com.wyu.iwork.adapter.viewholder.HomeWorkFastAprovalViewHolder;
import com.wyu.iwork.adapter.viewholder.HomeWorkScheduleViewHolder;
import com.wyu.iwork.adapter.viewholder.HomeWorkTaskViewHolder;
import com.wyu.iwork.adapter.viewholder.HomeWorkViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： sxliu on 2017/7/28.09:48
 * 邮箱：2587294424@qq.com
 */

public class HomeWorkAdapter extends RecyclerView.Adapter<HomeWorkViewHolder> {
    private Context context;
    private List<List> list ;

    public HomeWorkAdapter(Context context, List<List> list) {
        this.context = context;
        this.list = list;
        if (list==null){
            list = new ArrayList<>();
        }
    }

    @Override
    public HomeWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeWorkViewHolder holder = null;
        View view = null;
        switch (viewType){
            case 1://日程安排
                view  = LayoutInflater.from(context).inflate(R.layout.item_home_work_schedule,parent,false);
                holder = new HomeWorkScheduleViewHolder(view);
                break;

            case 2://任务安排
                view  = LayoutInflater.from(context).inflate(R.layout.item_home_work_task_viewholder,parent,false);
                holder = new HomeWorkTaskViewHolder(view);
                break;

            case 3://快捷审批
                view  = LayoutInflater.from(context).inflate(R.layout.item_home_work_fast_arpoval_viewholder,parent,false);
                holder = new HomeWorkFastAprovalViewHolder(view);
                break;

            case 4://工作日报
                view  = LayoutInflater.from(context).inflate(R.layout.item_home_work_daily_wrok,parent,false);
                holder = new HomeWorkDailyWorkViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(HomeWorkViewHolder holder, int position) {
        holder.setData(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position+1;
    }
}
