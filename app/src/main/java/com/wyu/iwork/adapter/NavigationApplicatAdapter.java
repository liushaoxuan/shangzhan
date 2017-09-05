package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.ApplicationItemModel;
import com.wyu.iwork.view.activity.WebActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lx on 2017/1/17.
 */

public class NavigationApplicatAdapter extends RecyclerView.Adapter<NavigationApplicatAdapter.ViewHolder> {


    private Context mcontext;
    private List<ApplicationItemModel> list;
    public NavigationApplicatAdapter(Context mcontext,List<ApplicationItemModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public NavigationApplicatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mcontext).inflate(R.layout.item_navigation_application,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NavigationApplicatAdapter.ViewHolder holder, int position) {
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_navigation_applicat_image)
        CircleImageView imageview;

        @BindView(R.id.item_navigation_applicat_text)
        TextView textview;

        private View mview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ViewGroup.LayoutParams parama = itemView.getLayoutParams();
            parama.width = MyApplication.screenWith/3;
            parama.height = MyApplication.screenWith/3  ;
            itemView.setLayoutParams(parama);
            mview = itemView;
        }

        public void setData(int position) {
            final ApplicationItemModel item = list.get(position);
            Glide.with(mcontext).load(item.getIcon()).into(imageview);
            textview.setText(item.getText());
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, WebActivity.class);
                    intent.putExtra("url",item.getUrl());
                    mcontext.startActivity(intent);
                }
            });
        }
    }

}
