package com.wyu.iwork.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/12.
 * 组织架构一级菜单
 */

public class SectionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_organize_1_imageview)
    public ImageView imageView;
    @BindView(R.id.item_organize_1_name)
    public TextView name;
    public SectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
