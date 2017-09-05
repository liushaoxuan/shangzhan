package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.Person;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/21.
 */

public class AddDailyReportPersonAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Person.PersonMessage> personList;
    public AddDailyReportPersonAdapter(Context context, ArrayList<Person.PersonMessage> personList){
        this.context = context;
        this.personList = personList;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Person.PersonMessage getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_add_person,null);
            holder = new ViewHolder();
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(getItem(position).isSelected()){
            holder.iv_select.setSelected(true);
        }else{
            holder.iv_select.setSelected(false);
        }
        Glide.with(context).load(getItem(position).getFace_img()).dontAnimate().placeholder(R.mipmap.def_img_rect).into(holder.iv_avatar);
        holder.tv_name.setText(getItem(position).getReal_name());
        holder.tv_position.setText(getItem(position).getDepartment());
        return convertView;
    }

    class ViewHolder{
        ImageView iv_select,iv_avatar;
        TextView tv_name,tv_position;
    }
}
