package com.wyu.iwork.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Person;

import java.util.ArrayList;


/**
 * Created by lx on 2017/4/13.
 */

public class TaskPersonListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Person.PersonMessage> list;
    private LayoutInflater mInflater;
    public TaskPersonListAdapter(Context context,ArrayList<Person.PersonMessage> list){
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Person.PersonMessage getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_task_person,null);
            holder = new ViewHolder();
            holder.iv_avatar = (TextView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_letter = (TextView) convertView.findViewById(R.id.tv_letter);
            holder.tv_select = (TextView) convertView.findViewById(R.id.tv_select);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_telphone = (TextView) convertView.findViewById(R.id.tv_telphone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(getItem(position).getPinyin())){
            holder.tv_letter.setText(getItem(position).getPinyin());
        }else{
            holder.tv_letter.setText("");
        }
        if(position == 0){
            holder.tv_letter.setVisibility(View.VISIBLE);
        }else{
            if(position>0){
                if(getItem(position).getPinyin().equals(getItem(position-1).getPinyin())){
                    holder.tv_letter.setVisibility(View.GONE);
                }else{
                    holder.tv_letter.setVisibility(View.VISIBLE);
                }
            }
        }
        if(!TextUtils.isEmpty(getItem(position).getUser_name())){
            holder.tv_telphone.setText(getItem(position).getUser_name());
        }else{
            holder.tv_telphone.setText("");
        }
        if(!TextUtils.isEmpty(getItem(position).getReal_name())){
            holder.tv_name.setText(getItem(position).getReal_name());
            holder.iv_avatar.setText(getItem(position).getReal_name().substring(0,1));
        }else{
            holder.tv_name.setText("");
        }
        holder.tv_select.setSelected(getItem(position).isSelected());
        return convertView;
    }

    class ViewHolder {
        TextView tv_letter,tv_select,tv_name,tv_telphone;
        TextView iv_avatar;
    }
}
