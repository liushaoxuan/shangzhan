package com.wyu.iwork.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wyu.iwork.model.AdTaskModel;
import com.wyu.iwork.view.activity.AdTaskSettingsActivity;

/**
 * 作者： sxliu on 2017/8/8.15:04
 * 邮箱：2587294424@qq.com
 */

public abstract class AdTaskSettingViewHolder extends RecyclerView.ViewHolder {
    protected AdTaskSettingsActivity mcontext;
    public boolean refresh = false;
    public AdTaskSettingViewHolder(View itemView) {
        super(itemView);
        mcontext = (AdTaskSettingsActivity) itemView.getContext();
    }

    public abstract void setData(AdTaskModel item,int position);
}
