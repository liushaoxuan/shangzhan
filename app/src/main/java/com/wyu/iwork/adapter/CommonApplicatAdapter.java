package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.ApplicatCommonModel;
import com.wyu.iwork.view.activity.WebActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/1/17.
 */

public class CommonApplicatAdapter extends RecyclerView.Adapter<CommonApplicatAdapter.ViewHolder> {

    Context context;
    //子适配器的宽度
    private int childwith ;
    //屏幕宽度
    private int screenWith;

    private CommonApplicatChildAdapter adapter;
    private List<ApplicatCommonModel> list;

    public CommonApplicatAdapter(Context context,List<ApplicatCommonModel> list) {
        this.context = context;
        this.list = list;
        screenWith  = MyApplication.screenWith;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_common_application,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //左边图片和文字的父控件
        @BindView(R.id.item_common_application_leftlayout)
        RelativeLayout leftlayout;
        //左边图片
        @BindView(R.id.item_common_application_imageview)
        ImageView imageview;
        //左边文字
        @BindView(R.id.item_common_application_textview)
        TextView textview;
        //Recyclearview
        @BindView(R.id.item_common_application_recyclearview)
        RecyclerView recyclearview;

        //设置常用应用
        @BindView(R.id.common_application_setting)
        Button setting;

        private View mview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mview = itemView;
            ViewGroup.LayoutParams paramas = leftlayout.getLayoutParams();
            childwith = screenWith*3/16;
            paramas.height = childwith*2;
            paramas.width = screenWith*1/4;
            leftlayout.setLayoutParams(paramas);
            recyclearview.setLayoutManager(new GridLayoutManager(context,4));
        }

        public void setData(int position) {

            if (position==list.size()-1){
                setting.setVisibility(View.VISIBLE);
            }else {
                setting.setVisibility(View.GONE);
            }
            final ApplicatCommonModel item = list.get(position);
            adapter = new CommonApplicatChildAdapter(context,childwith,item.getList());
            recyclearview.setAdapter(adapter);

            Glide.with(context).load(item.getIcon()).into(imageview);
            textview.setText(item.getText());
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url",item.getUrl());
                    context.startActivity(intent);
                }
            });

        }
    }
}
