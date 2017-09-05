package com.wyu.iwork.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import java.util.List;

/**
 * Created by sxliu on 2017/4/11.
 * 联系人adapter
 */

public class HomeContactsAdapter extends BaseAdapter {
    private Activity context;
    private List<ContactModel> list;

    public HomeContactsAdapter(Activity context, List<ContactModel> list) {
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

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_contacts, parent, false);
            holder.head = (ImageView) convertView.findViewById(R.id.item_home_contact_head);
            holder.name = (TextView) convertView.findViewById(R.id.item_home_contact_name);
            holder.phone = (TextView) convertView.findViewById(R.id.item_home_contact_phone);
            holder.py = (TextView) convertView.findViewById(R.id.item_home_contact_py);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ContactModel item = list.get(position);
        if (position == 0) {
            holder.py.setVisibility(View.VISIBLE);
        } else {
            if (item.getPinyin().equals(list.get(position - 1).getPinyin())) {
                holder.py.setVisibility(View.GONE);
            } else {
                holder.py.setVisibility(View.VISIBLE);
            }
        }
        holder.py.setText(item.getPinyin());
        holder.name.setText(item.getReal_name());
        holder.phone.setText(item.getPhone());
        Glide.with(context).load(item.getFace_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).placeholder(R.mipmap.head_icon_nodata).into(holder.head);

        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAlertDialog(context, item.getUser_id(), item.getPhone(), item.getReal_name());
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private ImageView head;
        private TextView phone;
        private TextView name;
        private TextView py;
    }
}
