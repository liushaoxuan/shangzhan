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
import com.wyu.iwork.model.ApplicationItemModel;
import com.wyu.iwork.view.activity.WebActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/1/17.
 */

public class CommonApplicatChildAdapter extends RecyclerView.Adapter<CommonApplicatChildAdapter.ViewHolder>{


    private Context mcontext;
    private int itemWith;

    private List<ApplicationItemModel> list;

    public CommonApplicatChildAdapter(Context mcontext,int itemWith,List<ApplicationItemModel> list) {
        this.mcontext = mcontext;
        this.itemWith = itemWith;
        this.list = list;
        int leng = 8-list.size();
        for (int i = 0;i<leng;i++){
            this.list.add(new ApplicationItemModel("","","",""));
        }
    }

    @Override
    public CommonApplicatChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mcontext).inflate(R.layout.item_common_applicat_child,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonApplicatChildAdapter.ViewHolder holder, int position) {
        holder.setData(position);

    }

    @Override
    public int getItemCount() {
        return 8;//这里固定返回8个
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.item_common_applicat_child_imageview)
        ImageView imageview;

        @BindView(R.id.item_selected_applicat_child_text)
        TextView textview;

        @BindView(R.id.item_selected_applicat_child_topline)
        View topline;

        @BindView(R.id.item_selected_applicat_child_leftline)
        View leftline;

        private View mview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ViewGroup.LayoutParams parama = itemView.getLayoutParams();
            parama.width = itemWith;
            parama.height = itemWith ;
            itemView.setLayoutParams(parama);
            mview = itemView;
        }

        public void setData(int position) {
            if (position>3){
                topline.setVisibility(View.VISIBLE);
            }else {
                topline.setVisibility(View.INVISIBLE);
            }

            final ApplicationItemModel itemModel = list.get(position);

            Glide.with(mcontext).load(itemModel.getIcon()).into(imageview);
            textview.setText(itemModel.getText());

            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(mcontext, WebActivity.class);
                    intent.putExtra("url",itemModel.getUrl());
                    mcontext.startActivity(intent);
                }
            });

        }
    }
}
