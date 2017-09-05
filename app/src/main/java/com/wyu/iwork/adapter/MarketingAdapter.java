package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Marketing;
import com.wyu.iwork.view.activity.MarketingDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/6/8.
 */

public class MarketingAdapter extends RecyclerView.Adapter<MarketingAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Marketing.MarketingDetail> marketList;

    public MarketingAdapter(Context context,ArrayList<Marketing.MarketingDetail> marketList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.marketList = marketList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_markrting,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //防止数据解析异常
        try {
            holder.marketing_title.setText(setText(marketList.get(position).getTitle()));
            holder.marketing_type.setText("活动类型："+setText(context.getResources().getStringArray(R.array.crm_marketing_type)[Integer.parseInt(marketList.get(position).getType())-1]));
            holder.marketing_date.setText("活动周期："+marketList.get(position).getStart_time() + " - "+marketList.get(position).getEnd_time());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String setText(String text){
        if(!TextUtils.isEmpty(text)){
            return text;
        }else{
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return marketList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //标题
        @BindView(R.id.marketing_title)
        TextView marketing_title;

        //日期
        @BindView(R.id.marketing_date)
        TextView marketing_date;

        //活动类型
        @BindView(R.id.marketing_type)
        TextView marketing_type;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MarketingDetailActivity.class);
                    intent.putExtra("type","BROWSER");
                    intent.putExtra("activity_id",marketList.get(getLayoutPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
