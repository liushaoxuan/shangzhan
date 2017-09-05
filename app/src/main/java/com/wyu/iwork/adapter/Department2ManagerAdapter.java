package com.wyu.iwork.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.OrganizeJuniorModel;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/13.
 * 部门管理二级菜单adapter
 */

public class Department2ManagerAdapter extends RecyclerView.Adapter<Department2ManagerAdapter.ViewHolder> {
    private Activity mcontext;
    private List<OrganizeJuniorModel> list;

    public Department2ManagerAdapter(Activity mcontext, List<OrganizeJuniorModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_organize_2, parent, false);
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
        //左边箭头
        @BindView(R.id.item_organize_2_imageview)
        ImageView imageView;
        //部门
        @BindView(R.id.item_organize_2_depart)
        TextView depart;
        //名称
        @BindView(R.id.item_organize_2_name)
        TextView name;
        //头像
        @BindView(R.id.item_organize_2_head)
        ImageView head;
        @BindView(R.id.item_organize_2_recyclerview)
        RecyclerView recyclerView;
        /**
         * 编辑
         */
        @BindView(R.id.department_manager_edit)
        TextView edit;
        /**
         * 删除
         */
        @BindView(R.id.department_manager_del)
        TextView del;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        private void setData(final int position) {
            del.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            final OrganizeJuniorModel item = list.get(position);
            if (item.isExpand()) {
                recyclerView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_bottom));
            } else {
                recyclerView.setVisibility(View.GONE);
                imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_right));
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
            OrganizeChildAdapter adapter = new OrganizeChildAdapter(mcontext, item.getUser());
            recyclerView.setAdapter(adapter);


            if ("1".equals(item.getType())){//用户
                name.setText(item.getName());
                depart.setVisibility(View.GONE);
                head.setVisibility(View.VISIBLE);
                if (item.getDepartment_name()!=null&&item.getDepartment_name().length()>0){
                    Glide.with(mcontext).load(item.getFace_img()).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 5)).error(R.mipmap.head_icon_nodata).into(head);
                }

               String ss =  JSON.toJSONString(item);
               Logger.e("face_img",ss);

            }else if ("2".equals(item.getType())) {//部门
                depart.setVisibility(View.VISIBLE);
                name.setText(item.getDepartment_name());
                head.setVisibility(View.GONE);
                if (item.getDepartment_name()!=null&&item.getDepartment_name().length()>0){
                    depart.setText(item.getDepartment_name().substring(0,1));
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).setExpand(!list.get(position).isExpand());
                    Department2ManagerAdapter.this.notifyDataSetChanged();
                }
            });
            //会话、呼叫、名片
            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MyAlertDialog(mcontext,item.getId(),item.getPhone(),item.getName()).show();
                }
            });
        }
    }
}
