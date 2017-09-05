package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/16.
 */

public class RouteMapAdapter extends RecyclerView.Adapter<RouteMapAdapter.RouteMapViewHolder> {

    private Context context;
    private int type;
    private DrivingRouteLine drivingRouteLine;//驾车路线
    private TransitRouteLine transitRouteLine;//换乘路线
    private WalkingRouteLine walkingRouteLine;//步行路线
    private LayoutInflater mInflater;
    private static final int TYPE_DRIVING = 1;//开车路线
    private static final int TYPE_TRANS = 2;//换乘路线
    private static final int TYPE_WALKING = 3;//走路路线

    public RouteMapAdapter(Context context, int type, DrivingRouteLine drivingRouteLine){
        this.context = context;
        this.type = type;
        this.drivingRouteLine = drivingRouteLine;
        this.mInflater = LayoutInflater.from(context);
    }

    public RouteMapAdapter(Context context, int type, TransitRouteLine transitRouteLine){
        this.context = context;
        this.type = type;
        this.transitRouteLine = transitRouteLine;
        this.mInflater = LayoutInflater.from(context);
    }

    public RouteMapAdapter(Context context, int type, WalkingRouteLine walkingRouteLine){
        this.context = context;
        this.type = type;
        this.walkingRouteLine = walkingRouteLine;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RouteMapViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(TYPE_DRIVING == type){
            return new RouteMapViewHolder(mInflater.inflate(R.layout.layout_route_driving,parent,false));
        }else if(TYPE_TRANS == type){
            if(viewType == TYPE_WALKING){
                return new RouteMapViewHolder(mInflater.inflate(R.layout.layout_route_walking,parent,false));
            }else{
                return new RouteMapViewHolder(mInflater.inflate(R.layout.layout_route_trans,parent,false));
            }
        }else {
            return new RouteMapViewHolder(mInflater.inflate(R.layout.layout_route_walking,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RouteMapViewHolder holder, int position) {
        if(TYPE_DRIVING == type){
            //驾车路线

            holder.route_textview.setText(drivingRouteLine.getAllStep().get(position).getInstructions().replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
                    .replaceAll("</[a-zA-Z]+[1-9]?>", ""));
        }else if(TYPE_TRANS == type){
            //换乘路线
            holder.route_textview.setText(transitRouteLine.getAllStep().get(position).getInstructions().replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
                    .replaceAll("</[a-zA-Z]+[1-9]?>", ""));
        }else {
            //步行路线
            holder.route_textview.setText(walkingRouteLine.getAllStep().get(position).getInstructions().replaceAll("<[a-zA-Z]+[1-9]?[^><]*>", "")
                    .replaceAll("</[a-zA-Z]+[1-9]?>", ""));
        }
    }

    @Override
    public int getItemCount() {
        if(TYPE_DRIVING == type){
            //驾车路线
            return drivingRouteLine.getAllStep().size();
        }else if(TYPE_TRANS == type){
            //换乘路线
            return transitRouteLine.getAllStep().size();
        }else {
            //步行路线
            return walkingRouteLine.getAllStep().size();
        }
    }

    public class RouteMapViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.route_textview)
        TextView route_textview;

        public RouteMapViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(TYPE_TRANS == type) {
            if (transitRouteLine.getAllStep().get(position).getStepType() == TransitRouteLine.TransitStep.TransitRouteStepType.WAKLING) {
                return TYPE_WALKING;
            } else {
                return TYPE_TRANS;
            }
        }else{
            return super.getItemViewType(position);
        }
    }
}
