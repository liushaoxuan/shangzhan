package com.wyu.iwork.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.DetailTaskModel;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/11.
 */

public class TaskJoinerAdapter extends RecyclerView.Adapter<TaskJoinerAdapter.TaskViewHolder> {

    private Activity context;
    private ArrayList<DetailTaskModel.Data.PartIn> list;
    private LayoutInflater mLayoutInflater;
    public TaskJoinerAdapter(Activity context, ArrayList<DetailTaskModel.Data.PartIn> list){
        this.context = context;
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image)
        ImageView image;
        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public TaskJoinerAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(mLayoutInflater.inflate(R.layout.item_image,parent,false));
    }

    @Override
    public void onBindViewHolder(TaskJoinerAdapter.TaskViewHolder holder, int position) {
        final DetailTaskModel.Data.PartIn item =  list.get(position);
        Glide.with(context).load(list.get(position).getFace_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 3)).placeholder(R.mipmap.def_img_rect).into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAlertDialog(context,item.getId(),item.getPhone(),item.getName()).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
