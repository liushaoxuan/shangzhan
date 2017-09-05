package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.Person;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/22.
 */

public class TaskAddPersonAdapter extends RecyclerView.Adapter<TaskAddPersonAdapter.PersonViewHolder> {

    private Context context;
    private ArrayList<Person.PersonMessage> list;
    private LayoutInflater mInflater;
    private int type;

    private static final int TYPE_PERSON = 1;//显示选择的人
    private static final int TYPE_OMIT = 2;//显示省略图
    private static final int TYPE_ADDMORE = 3;//显示添加更多人

    private static final int TYPE_HEADER = 1;
    private static final int TYPE_JOINER = 2;

    public TaskAddPersonAdapter(Context context, ArrayList<Person.PersonMessage> list, int type){
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }


    class PersonViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_image;
        ImageView iv_delet;
        AutoRelativeLayout item_task_image_delte;
        public PersonViewHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            iv_delet = (ImageView) itemView.findViewById(R.id.iv_delet);
            item_task_image_delte = (AutoRelativeLayout) itemView.findViewById(R.id.item_task_image_delte);
            item_task_image_delte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(getPosition());
                    notifyDataSetChanged();
                    if(type == TYPE_HEADER){
                        //((CreateNewTaskActivity)context).removeHeaderList(getPosition());
                    }else if(type == TYPE_JOINER){
                        //((CreateNewTaskActivity)context).removejoinerList(getPosition());
                    }
                }
            });
        }
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(mInflater.inflate(R.layout.item_task_image_delet,parent,false));
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_PERSON){
            Glide.with(context).load(list.get(position).getFace_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 2)).placeholder(R.mipmap.def_img_rect).into(holder.iv_image);
        }else if(getItemViewType(position) == TYPE_OMIT){
            holder.iv_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.icon_more));
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_PERSON;
    }
}
