package com.wyu.iwork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.DepartmentModel;
import com.wyu.iwork.model.OrganizedModel;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.DepartmentManagerActivity;
import com.wyu.iwork.view.activity.EditDepartmentActivity;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/13.
 * 部门管理——adapter
 */

public class DepartmentManagerAdapter extends RecyclerView.Adapter<DepartmentManagerAdapter.ViewHolder> {
    private DepartmentManagerActivity mcontext;
    private List<DepartmentModel> list;
    private Department3ManagerAdapter adapter;

    public DepartmentManagerAdapter(DepartmentManagerActivity mcontext, List<DepartmentModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_organize_1, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_organize_1_item)
        LinearLayout item_parent;

        @BindView(R.id.item_organize_1_imageview)
        ImageView imageView;

        @BindView(R.id.item_organize_1_name)
        TextView name;

        @BindView(R.id.item_organize_1_recyclerview)
        RecyclerView recyclerView;

        @BindView(R.id.item_organize_1_edit)
        ImageView isedit;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        private void setData(final int position) {
            isedit.setVisibility(View.VISIBLE);
            final DepartmentModel item = list.get(position);
            if (item.isExpand()) {
                recyclerView.setVisibility(View.VISIBLE);

                if (item.isedit()) {
                    imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_bottom));
                } else {
                    imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_parent_bottom));
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                if (item.isedit()) {
                    imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_right));
                } else {
                    imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_parent_right));
                }
            }
            name.setText(item.getDepartment());


            isedit.setSelected(item.isedit());
            item_parent.setSelected(item.isedit());

            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
            adapter = new Department3ManagerAdapter(mcontext, item.getList(), position);
            recyclerView.setAdapter(adapter);
            isedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isedit = !(item.isedit());
                    mcontext.showEditandDelete(isedit, item.getDepartment_id(), item.getDepartment());
                    parent_edit(position, isedit);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).setExpand(!list.get(position).isExpand());
                    DepartmentManagerAdapter.this.notifyDataSetChanged();

                }
            });
        }
    }

    //子类是否有处于编辑状态的选项
    public void setIschild_edit(boolean ischild_edit, int mposition, int index) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsedit(false);
            if (list != null) {
                if (i == index) {
                    if (list.get(i) != null && list.get(i).getList() != null) {
                        for (int j = 0; j < list.get(i).getList().size(); j++) {
                            if (j == mposition) {
                                list.get(i).getList().get(j).setIsedit(ischild_edit);
                            } else {
                                list.get(i).getList().get(j).setIsedit(false);
                            }
                        }
                    }
                } else {
                    if (list.get(i) != null && list.get(i).getList() != null) {
                        for (int j = 0; j < list.get(i).getList().size(); j++) {
                            list.get(i).getList().get(j).setIsedit(false);
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private void parent_edit(int position, boolean edit) {

        Logger.e("DepartmentManagerAdapter", "第" + position + "项" + edit);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    list.get(i).setIsedit(edit);
                } else {
                    list.get(i).setIsedit(false);
                }
                if (list.get(i) != null && list.get(i).getList() != null) {

                    for (int j = 0; j < list.get(i).getList().size(); j++) {
                        list.get(i).getList().get(j).setIsedit(false);
                    }
                }
            }
        }
        DepartmentManagerAdapter.this.notifyDataSetChanged();
    }

}
