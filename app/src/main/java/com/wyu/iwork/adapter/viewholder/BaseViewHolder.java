package com.wyu.iwork.adapter.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * 作者： sxliu on 2017/8/21.11:25
 * 邮箱：2587294424@qq.com
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void setData(Context context,int position, List<T> t);
}
