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

import com.chaychan.viewlib.NumberRunningTextView;
import com.wyu.iwork.R;
import com.wyu.iwork.model.CrmOpportunity;
import com.wyu.iwork.view.activity.CrmOpportunityDetailActivity;
import com.wyu.iwork.widget.CircleProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/6/5.
 */

public class BusinessOpportunityAdapter extends RecyclerView.Adapter<BusinessOpportunityAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<CrmOpportunity.Opportunity> opportunityList;
    public BusinessOpportunityAdapter(Context context,ArrayList<CrmOpportunity.Opportunity> opportunityList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.opportunityList = opportunityList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_business_opportunity,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.oppoptunity_money.setContent(checkString(opportunityList.get(position).getValue()));
        setProgress(opportunityList.get(position).getStatus(),holder);
        holder.opportunity_title.setText(checkString(opportunityList.get(position).getTitle()));
        holder.opportunity_company.setText(checkString(opportunityList.get(position).getCustomer_name()));
        holder.opportunity_time.setText(checkString(opportunityList.get(position).getAdd_time()));

    }

    private void setProgress(String status,ViewHolder holder){
        if("1".equals(status)){
            holder.opportunity_image.setVisibility(View.GONE);
            holder.oppoptunity_circleprogress.showProgress(25,holder.oppoptunity_circleprogress);
            holder.oppoprunity_stage.setText("阶段1");
            holder.opportunity_type.setText("验证客户");
        }else if("2".equals(status)){
            holder.opportunity_image.setVisibility(View.GONE);
            holder.oppoptunity_circleprogress.showProgress(50,holder.oppoptunity_circleprogress);
            holder.oppoprunity_stage.setText("阶段2");
            holder.opportunity_type.setText("需求确定");
        }else if("3".equals(status)){
            holder.opportunity_image.setVisibility(View.GONE);
            holder.oppoptunity_circleprogress.showProgress(75,holder.oppoptunity_circleprogress);
            holder.oppoprunity_stage.setText("阶段3");
            holder.opportunity_type.setText("方案/报价");
        }else if("4".equals(status)){
            holder.opportunity_image.setVisibility(View.GONE);
            holder.oppoptunity_circleprogress.showProgress(100,holder.oppoptunity_circleprogress);
            holder.oppoprunity_stage.setText("阶段4");
            holder.opportunity_type.setText("谈判审核");
        }else if("5".equals(status)){
            holder.oppoptunity_circleprogress.setVisibility(View.GONE);
            holder.opportunity_image.setVisibility(View.VISIBLE);
            holder.opportunity_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.win_prder));
            holder.oppoprunity_stage.setText("赢单");
            holder.opportunity_type.setVisibility(View.GONE);
        }else if("6".equals(status)){
            holder.oppoptunity_circleprogress.setVisibility(View.GONE);
            holder.opportunity_image.setVisibility(View.VISIBLE);
            holder.opportunity_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.lose_order));
            holder.oppoprunity_stage.setText("输单");
            holder.opportunity_type.setVisibility(View.GONE);
        }else if("7".equals(status)){
            holder.oppoptunity_circleprogress.setVisibility(View.GONE);
            holder.opportunity_image.setVisibility(View.VISIBLE);
            holder.opportunity_image.setImageDrawable(context.getResources().getDrawable(R.mipmap.useless));
            holder.oppoprunity_stage.setText("无效");
            holder.opportunity_type.setVisibility(View.GONE);
        }
    }

    private String checkString(String text){
        if(TextUtils.isEmpty(text)){
            return "";
        }else{
            return text;
        }
    }

    @Override
    public int getItemCount() {
        return opportunityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //圆形进度条
        @BindView(R.id.oppoptunity_circleprogress)
        CircleProgressBar oppoptunity_circleprogress;

        //商机标题
        @BindView(R.id.opportunity_title)
        TextView opportunity_title;

        //公司
        @BindView(R.id.opportunity_company)
        TextView opportunity_company;

        //商机阶段
        @BindView(R.id.oppoprunity_stage)
        TextView oppoprunity_stage;

        //商机类型
        @BindView(R.id.opportunity_type)
        TextView opportunity_type;

        //商机时间
        @BindView(R.id.opportunity_time)
        TextView opportunity_time;

        //商机金钱
        @BindView(R.id.oppoptunity_money)
        NumberRunningTextView oppoptunity_money;

        @BindView(R.id.opportunity_image)
        ImageView opportunity_image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            oppoptunity_circleprogress.setTag(getLayoutPosition());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,CrmOpportunityDetailActivity.class);
                    intent.putExtra("type","BROWSER");
                    intent.putExtra("chance_id",opportunityList.get(getLayoutPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
