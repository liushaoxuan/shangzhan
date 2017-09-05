package com.wyu.iwork.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.OrganizeUserModel;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/13.
 * 组织架构三级级菜单adapter
 */

public class OrganizeChildAdapter extends RecyclerView.Adapter<OrganizeChildAdapter.ViewHolder> {
    private Activity mcontext;
    private List<OrganizeUserModel> list ;

    public OrganizeChildAdapter(Activity mcontext, List<OrganizeUserModel> list) {
        this.mcontext = mcontext;
        this.list = list;
        if (this.list==null){
            this.list = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_organize_3,parent,false);
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
        @BindView(R.id.item_organize_3_head)
          ImageView head;
        @BindView(R.id.item_organize_3_name)
          TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        private void setData(final int position){
            final OrganizeUserModel item = list.get(position);
            name.setText(item.getName());
            Glide.with(mcontext).load(item.getFace_img()).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 5)).placeholder(R.mipmap.head_icon_nodata).into(head);
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
