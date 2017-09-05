package com.wyu.iwork.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.view.activity.ContactsDetailActivity;
import com.wyu.iwork.view.dialog.MyAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sxliu on 2017/4/17.
 *  首页——联系人adapter
 */

public class ContactsListviewAdapter extends android.widget.BaseAdapter {
    private Activity mcontext;
    private List<ContactModel> list;

    public ContactsListviewAdapter(Activity mcontext, List<ContactModel> list) {
        this.mcontext = mcontext;
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
        if (convertView==null){
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_home_contacts,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setData(position);

        return convertView;
    }

    public class ViewHolder{
        @BindView(R.id.item_home_contact_head)
        TextView head  ;
        @BindView(R.id.item_home_contact_name)
        TextView name ;
        @BindView(R.id.item_home_contact_phone)
        TextView phone ;
        @BindView(R.id.item_home_contact_py)
        TextView py  ;

        private View view;

        public ViewHolder(View itemView) {
            view = itemView;
            ButterKnife.bind(this,itemView);
        }

        private void setData(int position){

            final ContactModel item = list.get(position);

            if (item.getFirstPY()==null||item.getFirstPY().isEmpty()){
                py.setVisibility(View.GONE);
            }else {
                py.setVisibility(View.VISIBLE);
            }
            py.setText(item.getPinyin());
            name.setText(item.getReal_name());
            phone.setText(item.getPhone());
            try {
                head.setText(item.getReal_name().substring(0,1));
            } catch (Exception e) {
                e.printStackTrace();
                head.setText("");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext,ContactsDetailActivity.class);
                    intent.putExtra("id",item.getUser_id());
                    mcontext.startActivity(intent);
                }

            });
        }
    }
}
