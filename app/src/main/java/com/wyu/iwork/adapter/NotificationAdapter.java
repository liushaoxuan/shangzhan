package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.Notification;
import com.wyu.iwork.view.activity.NotificationDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private ArrayList<Notification.Data.Notific> notificationList;
    private LayoutInflater mLayoutInflater;
    public NotificationAdapter(Context context,ArrayList<Notification.Data.Notific> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.date_line)
        TextView date_line;

        @BindView(R.id.jump_layout)
        android.support.v7.widget.CardView jump_layout;

        @BindView(R.id.title)
        TextView title;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.iv_image)
        ImageView iv_image;

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.show_more)
        TextView show_more;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            jump_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent(context, NotificationDetailActivity.class);
                    it.putExtra("URL",notificationList.get(getPosition()).getUrl());
                    context.startActivity(it);
                }
            });
        }
    }


    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NotificationViewHolder(mLayoutInflater.inflate(R.layout.item_notification,parent,false));
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        holder.date_line.setText(notificationList.get(position).getTime());
        holder.title.setText(notificationList.get(position).getTitle());
        holder.date.setText(notificationList.get(position).getTime());
        holder.content.setText(notificationList.get(position).getIntro());
        //CustomUtils.setBitmap(context,notificationList.get(position).getImg(),holder.iv_image);
        Glide.with(context).load(notificationList.get(position).getImg()).dontAnimate().placeholder(R.drawable.bg).into(holder.iv_image);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
