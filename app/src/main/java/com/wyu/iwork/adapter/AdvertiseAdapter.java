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
import com.wyu.iwork.R;
import com.wyu.iwork.model.AdvertiseModel;
import com.wyu.iwork.view.activity.AdTaskDetailActivity;
import com.wyu.iwork.view.activity.AdTaskSettingsActivity;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/8/8.
 */

public class AdvertiseAdapter extends RecyclerView.Adapter<AdvertiseAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<AdvertiseModel.Advertise> list;
    private int type;
    private static final int TYPE_LIST = 1;
    private static final int TYPE_MINE = 2;

    public AdvertiseAdapter(Context context, ArrayList<AdvertiseModel.Advertise> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_advertise, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (type == TYPE_LIST) {
            holder.advertise_check.setText("领取任务");
        } else if (type == TYPE_MINE) {
            holder.advertise_check.setText("查看详情");
        }
        checkStr(list.get(position).getTitle(), holder.advertise_title);
        checkStr(list.get(position).getCompany_name(), holder.advertise_company);
        checkStr(list.get(position).getUnit_price() + "金豆/次", holder.once_money);
        checkStr(list.get(position).getTitle(),holder.advertise_title);
        checkStr(list.get(position).getCompany_name(),holder.advertise_company);
        checkStr(list.get(position).getUnit_price(),holder.once_money);
        //Glide.with(context).load(list.get(position).getAd_pic_url()).dontAnimate().placeholder(R.mipmap.def_advertise).into(holder.advertise_image);
        if ("1".equals(list.get(position).getType())) {
            //（1：CPC 2：CPM）"
            holder.advertise_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.advertise_cpc));
        } else if ("2".equals(list.get(position).getType())) {
            holder.advertise_type.setImageDrawable(context.getResources().getDrawable(R.mipmap.advertise_cpm));
        }

        loadIntoUseFitWidth(context, list.get(position).getAd_pic_url(), R.mipmap.def_advertise, holder.advertise_image);


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

    private void checkStr(String str, TextView tv) {
        if (!TextUtils.isEmpty(str)) {
            tv.setText(str);
        } else {
            tv.setText("");
            tv.setHint("未填写");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.advertise_image)
        ImageView advertise_image;

        @BindView(R.id.advertise_title)
        TextView advertise_title;

        @BindView(R.id.advertise_company)
        TextView advertise_company;

        @BindView(R.id.once_money)
        TextView once_money;

        @BindView(R.id.advertise_check)
        TextView advertise_check;

        @BindView(R.id.advertise_type)
        ImageView advertise_type;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type == TYPE_LIST) {
                        //领取任务
                        Intent intent = new Intent(context, AdTaskSettingsActivity.class);
                        int po = getLayoutPosition();
                        AdvertiseModel.Advertise item =   list.get(po);
                        intent.putExtra("id", item.getId());
                        context.startActivity(intent);
                    } else if (type == TYPE_MINE) {

                    }
                }
            });
        }
    }
}
