package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.DetailTaskModel;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/22.
 */

public class TaskerPersonListAdapter extends RecyclerView.Adapter<TaskerPersonListAdapter.PersonViewHolder> {

    private Context context;
    private ArrayList<DetailTaskModel.Data.PartIn> list;
    private LayoutInflater mInflater;
    private int type;

    public TaskerPersonListAdapter(Context context, ArrayList<DetailTaskModel.Data.PartIn> list,int type){
        this.context = context;
        this.list = list;
        this.type = type;
        mInflater = LayoutInflater.from(context);
    }

    public TaskerPersonListAdapter(Context context, ArrayList<DetailTaskModel.Data.PartIn> list){
        this.context = context;
        this.list = list;
        this.type = 0;
        mInflater = LayoutInflater.from(context);
    }


    class PersonViewHolder extends RecyclerView.ViewHolder{

        TextView civ_image;
        TextView tv_add_person;
        public PersonViewHolder(View itemView) {
            super(itemView);
            civ_image = (TextView) itemView.findViewById(R.id.civ_image);
            tv_add_person = (TextView) itemView.findViewById(R.id.tv_add_person);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type == 1 || type == 2){
                        list.remove(getLayoutPosition());
                        notifyDataSetChanged();
                        //((CreateNewTaskActivity)context).removePerson(type,getLayoutPosition());
                    }
                }
            });
        }
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PersonViewHolder(mInflater.inflate(R.layout.item_person,parent,false));
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        try {
            holder.civ_image.setText(list.get(position).getName().substring(0,1));
            holder.tv_add_person.setText(list.get(position).getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
