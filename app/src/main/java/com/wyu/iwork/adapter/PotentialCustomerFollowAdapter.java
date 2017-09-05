package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CrmCustomSeaModule;
import com.wyu.iwork.model.CrmFollowCustom;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.PotentialCustomDetailActivity;
import com.wyu.iwork.view.activity.PotentialOpenSeaDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lx on 2017/5/2.
 */

public class PotentialCustomerFollowAdapter extends RecyclerView.Adapter<PotentialCustomerFollowAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private String type;
    private static final String TYPE_FOLLOW = "FOLLOW";//潜在客户跟进类型
    private static final String TYPE_OPENSEA = "OPENSEA";//客户公海类型
    private ArrayList<CrmFollowCustom.FollowCustom> followCustomList;
    private ArrayList<CrmCustomSeaModule.Data.CustomSea> list;

    public PotentialCustomerFollowAdapter(Context context,String type,ArrayList<CrmFollowCustom.FollowCustom> followCustomList){
        this.context = context;
        this.type = type;
        this.followCustomList = followCustomList;
        inflater = LayoutInflater.from(context);
    }

    public PotentialCustomerFollowAdapter(Context context,ArrayList<CrmCustomSeaModule.Data.CustomSea> list,String type){
        this.context = context;
        this.type = type;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_crm_custom_potential,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(TYPE_OPENSEA.equals(type)){
            //潜在客户公海
            holder.tv_crm_custom_potential_address.setVisibility(View.GONE);
            holder.tv_crm_follow_person.setVisibility(View.VISIBLE);
            setText(list.get(position).getName(),holder.tv_crm_custom_potential_name);
            setText("跟进人："+list.get(position).getFollow_user(),holder.tv_crm_follow_person);
            setText("最近跟进时间："+list.get(position).getFollow_time(),holder.tv_crm_custom_potential_time);
            setText("跟进次数："+list.get(position).getFollow_num(),holder.tv_crm_custom_potential_way);
        }else{
            //潜在客户跟进
            holder.tv_crm_custom_potential_address.setVisibility(View.VISIBLE);
            holder.tv_crm_follow_person.setVisibility(View.GONE);
            setText(followCustomList.get(position).getCustomer_name(),holder.tv_crm_custom_potential_name);
            setText(followCustomList.get(position).getAddress(),holder.tv_crm_custom_potential_address);
            setText("最近跟进时间："+followCustomList.get(position).getFollow_time(),holder.tv_crm_custom_potential_time);
            setText("跟进方式："+getFollowType(followCustomList.get(position).getType()),holder.tv_crm_custom_potential_way);
        }
    }

    //跟进方式
    private String getFollowType(String followType){
        if("4".equals(followType)){
            return "会面";
        }else if("1".equals(followType)){
            return "电话";
        }else if("2".equals(followType)){
            return "短信";
        }else if("3".equals(followType)){
            return "社交工具";
        }else{
            return "其他";
        }
    }

    //设置内容
    private void setText(String text,TextView textView){
        if(!TextUtils.isEmpty(text)){
            textView.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        if(TYPE_OPENSEA.equals(type)){
            return list.size();
        }else{
            return followCustomList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_crm_custom_potential_name)
        TextView tv_crm_custom_potential_name;

        @BindView(R.id.tv_crm_custom_potential_address)
        TextView tv_crm_custom_potential_address;

        @BindView(R.id.tv_crm_custom_potential_time)
        TextView tv_crm_custom_potential_time;

        @BindView(R.id.tv_crm_custom_potential_way)
        TextView tv_crm_custom_potential_way;

        @BindView(R.id.tv_crm_follow_person)
        TextView tv_crm_follow_person;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if(TYPE_OPENSEA.equals(type)){
                        //进入客户公海详情页
                        intent = new Intent(context,PotentialOpenSeaDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("custom",list.get(getLayoutPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }else{
                        //进入潜在客户详情页
                        intent = new Intent(context, PotentialCustomDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("FollowCustom",followCustomList.get(getLayoutPosition()));
                        Logger.i("followCustomList",followCustomList.get(getLayoutPosition()).toString());
                        intent.putExtras(bundle);
                        intent.putExtra("type","BROWSER");
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
