package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.wyu.iwork.R;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.view.activity.CrmCheckRouteActivity;
import com.wyu.iwork.view.activity.CrmRouteMapActivity;
import com.wyu.iwork.view.dialog.LoadingDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/15.
 */

public class RouteItemAdapter extends RecyclerView.Adapter<RouteItemAdapter.RouteViewHolder> {

    private Context context;
    private List<DrivingRouteLine> drivingRoutes;
    private List<WalkingRouteLine> walkingRoutes;
    private List<TransitRouteLine> transRoutes;
    private LayoutInflater mInflater;
    private int type;
    private static final int TYPE_DRIVING = 1;//开车路线
    private static final int TYPE_TRANS = 2;//换乘路线
    private static final int TYPE_WALKING = 3;//走路路线

    public RouteItemAdapter(Context context, List<DrivingRouteLine> routes, int type){
        this.context = context;
        this.drivingRoutes = routes;
        this.type = type;
        this.mInflater = LayoutInflater.from(context);
    }

    public RouteItemAdapter(List<WalkingRouteLine> routes, Context context, int type){
        this.context = context;
        this.walkingRoutes = routes;
        this.type = type;
        this.mInflater = LayoutInflater.from(context);
    }

    public RouteItemAdapter(List<TransitRouteLine> routes, int type,Context context){
        this.context = context;
        this.transRoutes = routes;
        this.type = type;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RouteViewHolder(mInflater.inflate(R.layout.item_crm_route,parent,false));
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        if(type == TYPE_TRANS){
            holder.tv_walking_distance.setVisibility(View.VISIBLE);
            holder.route_view.setVisibility(View.VISIBLE);
            holder.tv_route_number.setText(position<10?"0"+(position+1):position+1+"");
            holder.tv_route_time.setText(transRoutes.get(position).getDuration()/60+"分钟");
            holder.tv_route_distance.setText(transRoutes.get(position).getDistance()<1000?transRoutes.get(position).getDistance()+"米":transRoutes.get(position).getDistance()/1000.0+"公里");
            String trans = "";
            int index = 1;
            for(int i = 0;i<transRoutes.get(position).getAllStep().size();i++){
                if(transRoutes.get(position).getAllStep().get(i).getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.BUSLINE ||
                        transRoutes.get(position).getAllStep().get(i).getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.SUBWAY){
                    if(index >1){
                        trans += " - ";
                    }
                    trans += transRoutes.get(position).getAllStep().get(i).getVehicleInfo().getTitle();
                    index += 1;
                }
            }
            index = 1;
            holder.tv_route_title.setText(trans);
            int distance = 0;
            for(TransitRouteLine.TransitStep step:transRoutes.get(position).getAllStep()){
                if(step.getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING){
                   distance += step.getDistance();
                }
            }
            holder.tv_walking_distance.setText(distance+"米");
        }else if(type == TYPE_DRIVING){
            holder.tv_route_number.setText(position<10?"0"+(position+1):position+1+"");
            holder.tv_route_title.setText(drivingRoutes.get(position).getAllStep().get(0).getEntranceInstructions());
            holder.tv_route_time.setText(drivingRoutes.get(position).getDuration()/60+"分钟");
            holder.tv_route_distance.setText(drivingRoutes.get(position).getDistance()<1000?drivingRoutes.get(position).getDistance()+"米":drivingRoutes.get(position).getDistance()/1000.0+"公里");

        }else if(type == TYPE_WALKING){
            holder.tv_route_number.setText(position<10?"0"+(position+1):position+1+"");
            holder.tv_route_title.setText(walkingRoutes.get(position).getAllStep().get(0).getInstructions());
            holder.tv_route_time.setText(walkingRoutes.get(position).getDuration()/60+"分钟");
            holder.tv_route_distance.setText(walkingRoutes.get(position).getDistance()<1000?walkingRoutes.get(position).getDistance()+"米":walkingRoutes.get(position).getDistance()/1000.0+"公里");
        }

    }

    @Override
    public int getItemCount() {
        if(type == TYPE_DRIVING){
            return drivingRoutes.size();
        }else if(type == TYPE_TRANS){
            return transRoutes.size();
        }else{
            return walkingRoutes.size();
        }
    }

    public class RouteViewHolder extends RecyclerView.ViewHolder{

        //路线序号
        @BindView(R.id.tv_route_number)
        TextView tv_route_number;

        //路线标题
        @BindView(R.id.tv_route_title)
        TextView tv_route_title;

        //路线耗时
        @BindView(R.id.tv_route_time)
        TextView tv_route_time;

        //路线距离
        @BindView(R.id.tv_route_distance)
        TextView tv_route_distance;

        //路线步行长度
        @BindView(R.id.tv_walking_distance)
        TextView tv_walking_distance;

        @BindView(R.id.route_view)
        View route_view;

        public RouteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final LoadingDialog dialog = new LoadingDialog();
                    dialog.show(((CrmCheckRouteActivity)context).getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
                    Intent intent = new Intent(context, CrmRouteMapActivity.class);
                    intent.putExtra("TYPE",type+"");
                    Bundle bundle = new Bundle();
                    if(type == 1){
                        bundle.putParcelable("ROUTE_LINE",drivingRoutes.get(getLayoutPosition()));
                    }else if(type == 2){
                        bundle.putParcelable("ROUTE_LINE",transRoutes.get(getLayoutPosition()));
                    }else if(type == 3){
                        bundle.putParcelable("ROUTE_LINE",walkingRoutes.get(getLayoutPosition()));
                    }
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    dialog.dismiss();
                }
            });
        }
    }
}
