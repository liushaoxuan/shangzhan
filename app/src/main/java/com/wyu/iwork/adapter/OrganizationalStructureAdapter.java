package com.wyu.iwork.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wyu.iwork.adapter.viewholder.SectionViewHolder;
import com.wyu.iwork.model.Organization;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by lx on 2017/4/12.
 * 组织架构adapter
 */

public class OrganizationalStructureAdapter extends StatelessSection{

    private List<Organization.FirstOrg> list;
    public OrganizationalStructureAdapter(int headerResourceId, int itemResourceId) {
        super(headerResourceId, itemResourceId);
    }

    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        SectionViewHolder holder1 = (SectionViewHolder) holder;
         holder1.name.setText(list.get(position).getDepartment_name());
    }
}
