package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chaychan.viewlib.NumberRunningTextView;
import com.wyu.iwork.R;
import com.wyu.iwork.model.MineAdvertiseModel;
import com.wyu.iwork.view.activity.AdTaskDetailActivity;

import java.util.ArrayList;

import static com.wyu.iwork.R.id.view;

/**
 * Created by lx on 2017/8/10.
 */

public class MineAdvertiseAdapter extends RecyclerView.Adapter<MineAdvertiseAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 1;//头部布局
    private static final int TYPE_ITEM = 2;//条目布局
    private static final int TYPE_NODATA = 3;//无条目时显示的无数据界面
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<MineAdvertiseModel.AdvertiseMessage.AdvertiseTask> list;
    private MineAdvertiseModel.AdvertiseMessage.Mall mall;

    public MineAdvertiseAdapter(Context context, MineAdvertiseModel.AdvertiseMessage.Mall mall, ArrayList<MineAdvertiseModel.AdvertiseMessage.AdvertiseTask> list){
        this.context = context;
        this.mall = mall;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_HEADER){
            return new ViewHolder(mInflater.inflate(R.layout.layout_mine_ad_header,parent,false),viewType);
        }else if(viewType == TYPE_ITEM){
            return new ViewHolder(mInflater.inflate(R.layout.item_advertise,parent,false),viewType);
        }else if(viewType == TYPE_NODATA){
            return new ViewHolder(mInflater.inflate(R.layout.item_mine_no_adv,parent,false),viewType);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(position == 0){
            //设置赚取个人佣金数据
            setMineData(mall,holder);
        }else if(type == TYPE_ITEM){
            setItemData(holder,position-1);
        }
    }

    private void setItemData(ViewHolder holder,int position){
        holder.advertise_check.setText("查看详情");
        checkStr(list.get(position).getTitle(),holder.advertise_title);
        checkStr(list.get(position).getCompany_name(),holder.advertise_company);
        checkStr(list.get(position).getUnit_price(),holder.once_money);
        if("1".equals(list.get(position).getType())){
            //（1：CPC 2：CPM）"
            holder.advertise_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.advertise_cpc));
        }else if("2".equals(list.get(position).getType())){
            holder.advertise_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.advertise_cpm));
        }

        loadIntoUseFitWidth(context,list.get(position).getAd_pic_url(),R.mipmap.def_advertise,holder.advertise_image);
    }

    //设置赚取个人佣金数据
    private void setMineData(MineAdvertiseModel.AdvertiseMessage.Mall mall,ViewHolder holder){
        if(checkStr(mall.getAll_mail_count())){
            holder.fortunella_venosa.setText(String .format("%.2f",mall.getAll_mail_count()));
        }else{
            holder.fortunella_venosa.setHint("0.00");
        }
        if(checkStr(mall.getMail_count())){
            holder.all_earning.setText(String.format("%.2f",mall.getMail_count()));
        }else{
            holder.all_earning.setHint("0.00");
        }
        if(checkStr(mall.getThis_mail_count())){
            holder.today_earning.setText(String.format("%.2f",mall.getThis_mail_count()));
        }else{
            holder.today_earning.setHint("0.00");
        }
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public static void loadIntoUseFitWidth(Context context, final String imageUrl, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }


    //判断Double类型是否为空
    private boolean checkStr(double str){
        Double dou = Double.valueOf(str);
        return dou != null;
    }

    private void checkStr(String str,TextView tv){
        if(!TextUtils.isEmpty(str)){
            tv.setText(str);
        }else{
            tv.setText("");
            tv.setHint("未填写");
        }
    }

    @Override
    public int getItemCount() {
        if(list != null && list.size()>0){
            return list.size()+1;
        }else{
            return 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        }else{
            if(list != null && list.size()>0){
                return TYPE_ITEM;
            }else{
                return TYPE_NODATA;
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //佣金总额
        NumberRunningTextView fortunella_venosa;

        //今日收益
        TextView today_earning;

        //累计收益
        TextView all_earning;

        //无任务
        TextView tv_notavailable;

        //广告图片
        ImageView advertise_image;

        //广告标题
        TextView advertise_title;

        //广告公司
        TextView advertise_company;

        //广告佣金
        TextView once_money;

        //查看详情
        TextView advertise_check;

        //广告类型
        ImageView advertise_type;

        public ViewHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType == TYPE_HEADER){
                //头部布局
                fortunella_venosa = (NumberRunningTextView) itemView.findViewById(R.id.fortunella_venosa);
                today_earning = (TextView) itemView.findViewById(R.id.today_earning);
                all_earning = (TextView) itemView.findViewById(R.id.all_earning);
            }else if(viewType == TYPE_ITEM){
                advertise_image = (ImageView) itemView.findViewById(R.id.advertise_image);
                advertise_title = (TextView) itemView.findViewById(R.id.advertise_title);
                advertise_company = (TextView) itemView.findViewById(R.id.advertise_company);
                once_money = (TextView) itemView.findViewById(R.id.once_money);
                advertise_check = (TextView) itemView.findViewById(R.id.advertise_check);
                advertise_type = (ImageView) itemView.findViewById(R.id.advertise_type);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //查看详情
                        //领取任务
                        Intent intent = new Intent(context, AdTaskDetailActivity.class);
                        int po = getLayoutPosition()-1;
                        MineAdvertiseModel.AdvertiseMessage.AdvertiseTask item =   list.get(po);
                        intent.putExtra("id", item.getAd_receive_id());
                        intent.putExtra("url", item.getUrl());
                        context.startActivity(intent);
                    }
                });
            }else if(view == TYPE_NODATA){
                tv_notavailable = (TextView) itemView.findViewById(R.id.tv_notavailable);
            }
        }
    }

}
