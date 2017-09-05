package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OutSignRecord;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lx on 2017/7/31.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<OutSignRecord.Record> recordList;

    public RecordAdapter(Context context,ArrayList<OutSignRecord.Record> recordList){
        this.context = context;
        this.recordList = recordList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_go_out_record,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        checkStr(recordList.get(position).getUsername(),holder.record_name);
        checkStr(recordList.get(position).getTime(),holder.record_time);
        checkStr(recordList.get(position).getText(),holder.record_content);
        checkStr(recordList.get(position).getAddress(),holder.record_location);
        Glide.with(context).load(recordList.get(position).getImg()).dontAnimate().placeholder(R.mipmap.def_record).into(holder.record_image);
        Glide.with(context).load(recordList.get(position).getFace_img()).dontAnimate().placeholder(R.mipmap.def_img_rect).into(holder.circle_avatar);

    }

    private void checkStr(String str,TextView textView){
        if(!TextUtils.isEmpty(str)){
            textView.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.circle_avatar)
        CircleImageView circle_avatar;

        @BindView(R.id.record_name)
        TextView record_name;

        @BindView(R.id.record_time)
        TextView record_time;

        @BindView(R.id.record_content)
        TextView record_content;

        @BindView(R.id.record_image)
        ImageView record_image;

        @BindView(R.id.record_location)
        TextView record_location;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
