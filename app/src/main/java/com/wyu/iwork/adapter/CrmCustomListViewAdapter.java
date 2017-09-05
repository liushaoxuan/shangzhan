package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.MapCustomModule;
import com.wyu.iwork.view.activity.CrmCheckRouteActivity;
import com.wyu.iwork.view.activity.CrmCustomMapActivity;
import com.wyu.iwork.view.activity.CrmCustomMapSearchActivity;
import com.wyu.iwork.view.activity.CrmMapCustomDetailActivity;

import java.util.ArrayList;

/**
 * Created by lx on 2017/5/10.
 */

public class CrmCustomListViewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<MapCustomModule.Custom> list;
    private int type;
    private static final int TYPE_MAP = 1;
    private static final int TYPE_SEARCH = 2;

    public CrmCustomListViewAdapter(Context context, ArrayList<MapCustomModule.Custom> list,int type){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.type = type;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MapCustomModule.Custom getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_map_custom,null);
            holder = new ViewHolder();
            holder.item_map_custom_name = (TextView) convertView.findViewById(R.id.item_map_custom_name);
            holder.item_map_custom_type = (TextView) convertView.findViewById(R.id.item_map_custom_type);
            holder.item_map_custom_distance = (TextView) convertView.findViewById(R.id.item_map_custom_distance);
            holder.item_map_custom_address = (TextView) convertView.findViewById(R.id.item_map_custom_address);
            holder.item_map_custom_tohere = (TextView) convertView.findViewById(R.id.item_map_custom_tohere);
            holder.item_map_custom_detail = (Button) convertView.findViewById(R.id.item_map_custom_detail);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        setText(getItem(position).getName(),holder.item_map_custom_name);
        setText(setType(getItem(position).getType()),holder.item_map_custom_type);
        setText(getItem(position).getDistance()+"米",holder.item_map_custom_distance);
        setText(getItem(position).getAddress(),holder.item_map_custom_address);
        final int index = position;
        holder.item_map_custom_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDetail(index);
            }
        });
        holder.item_map_custom_tohere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRoute(index);//跳转到检查路线规划界面
            }
        });
        return convertView;
    }

    //路线规划
    private void checkRoute(int index){
        Intent intent = new Intent(context, CrmCheckRouteActivity.class);
        intent.putExtra("longitude",list.get(index).getLng());//经度
        intent.putExtra("latitude",list.get(index).getLat());//纬度
        intent.putExtra("address",list.get(index).getAddress());
        context.startActivity(intent);
    }
    //查看客户详情
    private void showDetail(int index){
        Intent intent = new Intent(context, CrmMapCustomDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("custom",list.get(index));
        intent.putExtras(bundle);
        if(type == TYPE_MAP){
            ((CrmCustomMapActivity)context).startActivityForResult(intent,101);
        }else if(type == TYPE_SEARCH){
            ((CrmCustomMapSearchActivity)context).startActivityForResult(intent,101);
        }
    }

    private String setType(String type){
        if(!TextUtils.isEmpty(type)){
            if(type.equals("0")){
                return "潜在客户";
            }else{
                return "正式客户";
            }
        }else{
            return null;
        }
    }

    private void setText(String text,TextView textView){
        if(!TextUtils.isEmpty(text)){
            textView.setText(text);
        }
    }

    class ViewHolder{
        //客户名称
        TextView item_map_custom_name;

        //客户类型 ： 正式客户 或 潜在客户
        TextView item_map_custom_type;

        //距离
        TextView item_map_custom_distance;

        //距离
        TextView item_map_custom_address;

        //到这里
        TextView item_map_custom_tohere;

        //查看详情
        Button item_map_custom_detail;

    }
}
