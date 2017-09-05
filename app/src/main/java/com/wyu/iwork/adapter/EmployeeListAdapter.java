package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Person;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.PersonListActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.wyu.iwork.net.NetworkManager.TAG;

/**
 * Created by lx on 2017/8/12.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Person.PersonMessage> list;
    private LayoutInflater mLayoutInflater;

    public EmployeeListAdapter(Context context,ArrayList<Person.PersonMessage> list){
        this.context = context;
        this.list = list;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_task_person,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!TextUtils.isEmpty(list.get(position).getPinyin())){
            holder.tv_letter.setText(list.get(position).getPinyin());
        }else{
            holder.tv_letter.setText("");
        }
        if(position == 0){
            holder.tv_letter.setVisibility(View.VISIBLE);
        }else{
            if(position>0){
                if(list.get(position).getPinyin().equals(list.get(position-1).getPinyin())){
                    holder.tv_letter.setVisibility(View.GONE);
                }else{
                    holder.tv_letter.setVisibility(View.VISIBLE);
                }
            }
        }
        if(!TextUtils.isEmpty(list.get(position).getUser_name())){
            holder.tv_telphone.setText(list.get(position).getUser_name());
        }else{
            holder.tv_telphone.setText("");
        }
        if(!TextUtils.isEmpty(list.get(position).getReal_name())){
            holder.tv_name.setText(list.get(position).getReal_name());
            holder.iv_avatar.setText(list.get(position).getReal_name().substring(0,1));
        }else{
            holder.tv_name.setText("");
        }
        holder.tv_select.setSelected(list.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_avatar)
        TextView iv_avatar;

        @BindView(R.id.tv_letter)
        TextView tv_letter;

        @BindView(R.id.tv_select)
        TextView tv_select;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_telphone)
        TextView tv_telphone;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAndRemoveList(getLayoutPosition());
                }
            });
        }
    }

    private void setAndRemoveList(int position){
        list.get(position).setSelected(!list.get(position).isSelected());
        Logger.i(TAG,list.get(position).isSelected()+"");
        ((PersonListActivity)context).addOrRemoveList(list.get(position));
        notifyDataSetChanged();
    }
}
