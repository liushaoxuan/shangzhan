package com.wyu.iwork.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.fragment.HomeWorkFragment;

import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/27.10:06
 * 邮箱：2587294424@qq.com
 * 工作首页baseviewholder
 */

public abstract class HomeWorkViewHolder<T> extends RecyclerView.ViewHolder {

    protected MainActivity mcontext;

    public  HomeWorkViewHolder(View itemView) {
        super(itemView);
        mcontext = (MainActivity)itemView.getContext();
        ButterKnife.bind(this,itemView);
    }

    public abstract void setData(List<T> t, int position);
}
