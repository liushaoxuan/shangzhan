package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.Application;
import com.wyu.iwork.util.GlideImageLoader;
import com.wyu.iwork.view.activity.WebActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lx on 2017/6/19.
 */

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private static final String TAG = ApplicationAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater mInflater;
    private Application mApplication;
    private boolean isOA = false;
    private boolean isCrm = false;
    private boolean isErp = false;
    private boolean isTopBanner = false;
    private boolean isBottomBanner = false;
    private int count;
    private static final int TYPE_NORMAL = 1;//正常部分
    private static final int TYPE_TOP = 2;//头部
    private static final int TYPE_BOTTOM = 3;//底部
    private ArrayList<String> url = new ArrayList<>();
    private List imageUrls = new ArrayList();

    public ApplicationAdapter(Context context,Application application){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mApplication = application;
        if(mApplication.getData().getOA() != null && mApplication.getData().getOA().size()>0){
            count ++;
            isOA = true;
        }
        if(mApplication.getData().getCRM() != null && mApplication.getData().getCRM().size()>0){
            count ++;
            isCrm = true;
        }
        if(mApplication.getData().getERP() != null && mApplication.getData().getERP().size()>0){
            count ++;
            isErp = true;
        }
        if(mApplication.getData().getTop_banner() != null && mApplication.getData().getTop_banner().size()>0){
            count++;
            isTopBanner = true;
            for (int i = 0;i<mApplication.getData().getTop_banner().size();i++){
                if(!TextUtils.isEmpty(mApplication.getData().getTop_banner().get(i).getUrl())){
                    url.add(mApplication.getData().getTop_banner().get(i).getUrl());
                }else{
                    url.add(" ");
                }
                imageUrls.add(mApplication.getData().getTop_banner().get(i).getImg());
            }
        }

        if(mApplication.getData().getBottom_banner() != null && mApplication.getData().getBottom_banner().size()>0){
            count++;
            isBottomBanner = true;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_TOP){
            return new ViewHolder(mInflater.inflate(R.layout.layout_selected_application_top,parent,false),viewType);
        }else if(viewType == TYPE_BOTTOM){
            return new ViewHolder(mInflater.inflate(R.layout.layout_selected_application_bottom,parent,false),viewType);
        }else{
            return new ViewHolder(mInflater.inflate(R.layout.item_main_content,parent,false),viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(position == 0 && type == TYPE_TOP){
            initImageBannger(holder.banner);
        }else if(position == getItemCount()-1 && type == TYPE_BOTTOM){
            Glide.with(context).load(mApplication.getData().getBottom_banner().get(0).getImg()).into(holder.bottom_image);
        }else if(isOA){
            holder.application_recycleview.setLayoutManager(new GridLayoutManager(context, 4));
            holder.application_title.setText("OA");
            holder.application_recycleview.setAdapter(new SelectedApplicationAdapter(context, mApplication.getData().getOA(),true));
            isOA = false;
        }else if(isCrm){
            holder.application_recycleview.setLayoutManager(new GridLayoutManager(context, 4));
            holder.application_title.setText("CRM");
            holder.application_recycleview.setAdapter(new SelectedApplicationAdapter(context, mApplication.getData().getCRM()));
            isCrm = false;
        }else if(isErp){
            holder.application_recycleview.setLayoutManager(new GridLayoutManager(context, 4));
            holder.application_title.setText("ERP");
            holder.application_recycleview.setAdapter(new SelectedApplicationAdapter(context, mApplication.getData().getERP()));
            isErp = false;
        }
    }

    //设置banner
    private void initImageBannger(Banner banner){
        if(imageUrls != null && imageUrls.size()>0){
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setIndicatorGravity(BannerConfig.RIGHT).setDelayTime(3000).setImageLoader(new GlideImageLoader())
                    .setImages(imageUrls).setBannerAnimation(Transformer.DepthPage).start();
        }
    }

    /**
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if( viewType == 1){
            return new ImageViewHolder()
        }else if(viewType == 2){

        }else{
            return new ViewHolder(mInflater.inflate(R.layout.item_main_content,parent,false));
        }
    }
*/
/**
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.application_recycleview.setLayoutManager(new GridLayoutManager(context, 4));
        if(position == 0){

        }else if(position == getItemCount()){

        }if(isOA){
            holder.application_title.setText("移动办公");
            holder.application_recycleview.setAdapter(new SelectedApplicationAdapter(context, mApplication.getData().getOA(),true));
            isOA = false;
        }else if(isCrm){
            holder.application_title.setText("CRM");
            holder.application_recycleview.setAdapter(new SelectedApplicationAdapter(context, mApplication.getData().getCRM()));
            isCrm = false;
        }else if(isErp){
            holder.application_title.setText("ERP");
            holder.application_recycleview.setAdapter(new SelectedApplicationAdapter(context, mApplication.getData().getERP()));
            isErp = false;
        }
    }
*/
    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        /**
        if( position == 0){
            return 1;
        }else if(position == getItemCount()-1){
            return 2;
        }else{
            return 3;
        }*/

        if(!isBottomBanner && !isTopBanner){
            //1:没有banner
            return TYPE_NORMAL;
        }else if(isTopBanner && !isBottomBanner){
            //2：有topbannner没有bottom
            if(position == 0){
                return TYPE_TOP;
            }else{
                return TYPE_NORMAL;
            }
        }else if(!isTopBanner && isBottomBanner){
            //3：有bottom没有top
            if(position == getItemCount()-1){
                return TYPE_BOTTOM;
            }else{
                return TYPE_NORMAL;
            }
        }else if(isTopBanner && isBottomBanner){
            //4：都有
            if(position == 0) {
                return TYPE_TOP;
            }else if(position == getItemCount()-1){
                return TYPE_BOTTOM;
            }else{
                return TYPE_NORMAL;
            }
        }else{
            return TYPE_NORMAL;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView application_title;

        RecyclerView application_recycleview;

        Banner banner;

        ImageView bottom_image;

        public ViewHolder(View itemView,int itemType) {
            super(itemView);
            if(itemType == TYPE_TOP){
                banner = (Banner) itemView.findViewById(R.id.banner);
                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        if(!TextUtils.isEmpty(url.get(position)) && !" ".equals(url.get(position))){
                            Intent  intent = new Intent(context, WebActivity.class);
                            intent.putExtra("url",url.get(position));
                            context.startActivity(intent);
                        }
                    }
                });
            }else if(itemType == TYPE_NORMAL){
                application_title = (TextView) itemView.findViewById(R.id.application_title);
                application_recycleview = (RecyclerView) itemView.findViewById(R.id.application_recycleview);
            }else if(itemType == TYPE_BOTTOM){
                bottom_image = (ImageView) itemView.findViewById(R.id.bottom_image);
                bottom_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(mApplication.getData().getBottom_banner().get(0).getUrl())){
                            Intent  intent = new Intent(context, WebActivity.class);
                            intent.putExtra("url",mApplication.getData().getBottom_banner().get(0).getUrl());
                            context.startActivity(intent);
                        }
                    }
                });
            }
        }
    }

}
