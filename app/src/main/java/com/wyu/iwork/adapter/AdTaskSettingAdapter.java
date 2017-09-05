package com.wyu.iwork.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.viewholder.AdTaskArticalViewHolder;
import com.wyu.iwork.adapter.viewholder.AdTaskImageViewHolder;
import com.wyu.iwork.adapter.viewholder.AdTaskSettingViewHolder;
import com.wyu.iwork.model.AdTaskModel;
import com.wyu.iwork.view.activity.AdTaskSettingsActivity;

import java.util.List;

/**
 * 作者： sxliu on 2017/8/8.15:07
 * 邮箱：2587294424@qq.com
 * 广告联盟 任务设置 adapter
 */

public class AdTaskSettingAdapter extends RecyclerView.Adapter<AdTaskSettingViewHolder> {

    private AdTaskSettingsActivity context;
    private List<AdTaskModel> list;

    public AdTaskSettingAdapter(AdTaskSettingsActivity context, List<AdTaskModel> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<AdTaskModel> data){
        list = data;
    }
    @Override
    public AdTaskSettingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdTaskSettingViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case 1://文字
                view = LayoutInflater.from(context).inflate(R.layout.item_ad_task_setting_artical, parent, false);
                holder = new AdTaskArticalViewHolder(view);
                break;

            case 2://图片
                view = LayoutInflater.from(context).inflate(R.layout.item_ad_task_setting_images, parent, false);
                holder = new AdTaskImageViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(AdTaskSettingViewHolder holder, int position) {
        holder.setData(list.get(position), position);
    }

    @Override
    public void onBindViewHolder(AdTaskSettingViewHolder holder, int position, List<Object> payloads) {

        if (payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }else {
            holder.setData(list.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }
}
