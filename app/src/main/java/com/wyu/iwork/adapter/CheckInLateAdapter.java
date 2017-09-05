package com.wyu.iwork.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CheckInStatisticsModule;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/7.
 */

public class CheckInLateAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CheckInStatisticsModule.Data.Late> list;
    private static final int TYPE_LATE = 1;
    private static final int TYPE_LEAVE_EARLY = 2;
    private int type;

    public CheckInLateAdapter(Context context, ArrayList<CheckInStatisticsModule.Data.Late> list,int type){
        this.context = context;
        this.list = list;
        this.type = type;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CheckInStatisticsModule.Data.Late getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_check_in_detail,null);
            holder = new ViewHolder();
            holder.detail_time = (TextView) convertView.findViewById(R.id.detail_time);
            holder.detail_desc = (TextView) convertView.findViewById(R.id.detail_desc);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(!TextUtils.isEmpty(getItem(position).getSign_time())){
            holder.detail_time.setText(getItem(position).getSign_time());
        }
        if(type == TYPE_LATE){
            if(!TextUtils.isEmpty(getItem(position).getDiff_time())){
                holder.detail_desc.setText("迟到"+getItem(position).getDiff_time());
            }
        }else{
            if(!TextUtils.isEmpty(getItem(position).getDiff_time())){
                holder.detail_desc.setText("早退"+getItem(position).getDiff_time());
            }
        }
        return convertView;
    }

    class ViewHolder{
        TextView detail_time;
        TextView detail_desc;
    }
}
