package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;

import java.util.List;

/**
 * Created by lx on 2017/3/7.
 * 联系人——右边的字母adapter
 */

public class PersonAZAdapter extends android.widget.BaseAdapter {
    private Context context;
    private List<String> list;

    public PersonAZAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_initials_person,parent,false);
            holder.textView = (TextView) convertView.findViewById(R.id.item_initials_textview);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position));
        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
    }
}
