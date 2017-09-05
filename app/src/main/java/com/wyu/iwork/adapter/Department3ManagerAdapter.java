package com.wyu.iwork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.DepartmentModel;
import com.wyu.iwork.model.OrganizeUserModel;
import com.wyu.iwork.view.activity.DepartmentManagerActivity;
import com.wyu.iwork.view.activity.EditDepartmentActivity;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/13.
 * 组织架构三级级菜单adapter
 */

public class Department3ManagerAdapter extends RecyclerView.Adapter<Department3ManagerAdapter.ViewHolder> {
    private DepartmentManagerActivity mcontext;
    private List<DepartmentModel.ListEntity> list ;
    private int  index = 0;

    public Department3ManagerAdapter(DepartmentManagerActivity mcontext, List<DepartmentModel.ListEntity> list,int index) {
        this.mcontext = mcontext;
        this.list = list;
        this.index = index;
        if (this.list==null){
            this.list = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_department_2,parent,false);
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

        @BindView(R.id.item_organize_2_itenm_parent)
        LinearLayout item_parent;
        @BindView(R.id.item_organize_3_head)
        TextView head;

        @BindView(R.id.item_organize_3_name)
          TextView name;

        @BindView(R.id.item_organize_2_edit)
          ImageView isedit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        private void setData(final int position){
            isedit.setVisibility(View.VISIBLE);
            final DepartmentModel.ListEntity item = list.get(position);
            name.setText(item.getDepartment());
            if (item.getDepartment()!=null&&item.getDepartment().length()>0){
                head.setText(item.getDepartment().substring(0,1));
            }

            isedit.setSelected(item.isedit());
            item_parent.setSelected(item.isedit());
            isedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isslected = item.isedit();
                    mcontext.showEditandDelete(!isslected,item.getDepartment_id(),item.getDepartment());
                    mcontext.adapter.setIschild_edit(!isslected,position,index);
                }
            });

        }
    }
}
