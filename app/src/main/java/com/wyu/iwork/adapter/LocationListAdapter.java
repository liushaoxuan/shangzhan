package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.wyu.iwork.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/7/13.
 */

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {

    private Context context;
    private List<PoiInfo> list;
    private LayoutInflater mInflater;
    private int selectItem = 0;
    public LocationListAdapter(Context context, List<PoiInfo> list){

        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_location_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.location_name.setText(list.get(position).name);
        holder.location_detail_address.setText(list.get(position).address);
        if(position == selectItem){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.location_name)
        TextView location_name;

        @BindView(R.id.location_detail_address)
        TextView location_detail_address;

        @BindView(R.id.location_image)
        ImageView location_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectItem != getLayoutPosition()){
                        selectItem = getLayoutPosition();
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public int getSelectItem(){
        return selectItem;
    }
}
