package com.wyu.iwork.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrganizedModel;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/13.
 * 组织架构一级菜单adapter
 */

public class OrganizeParentAdapter extends RecyclerView.Adapter<OrganizeParentAdapter.ViewHolder> {
    private Activity mcontext;
    private List<OrganizedModel> list ;

    public OrganizeParentAdapter(Activity mcontext, List<OrganizedModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_organize_1,parent,false);
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
        @BindView(R.id.item_organize_1_imageview)
          ImageView imageView;
        @BindView(R.id.item_organize_1_name)
          TextView name;
        @BindView(R.id.item_organize_1_recyclerview)
          RecyclerView recyclerView;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            view = itemView;
        }

        private void setData(final int position){
            OrganizedModel item = list.get(position);
            if (item.isExpand()){
                recyclerView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_parent_bottom));
            }else {
                recyclerView.setVisibility(View.GONE);
                imageView.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_organize_parent_right));
            }
          name.setText(item.getDepartment_name());
            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
            OrganizeAdapter adapter = new OrganizeAdapter(mcontext,item.getJunior());
            recyclerView.setAdapter(adapter);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.get(position).setExpand(!list.get(position).isExpand());
                    OrganizeParentAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }
}
