package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Person;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/22.
 */

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.PersonViewHolder> {

    private Context context;
    private ArrayList<Person.PersonMessage> list;
    private LayoutInflater mInflater;

    public PersonListAdapter(Context context, ArrayList<Person.PersonMessage> list){
        this.context = context;
        this.list = list;
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
                    list.remove(getLayoutPosition());
                    notifyDataSetChanged();
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
            holder.civ_image.setText(list.get(position).getReal_name().substring(0,1));
            holder.tv_add_person.setText(list.get(position).getReal_name());
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
