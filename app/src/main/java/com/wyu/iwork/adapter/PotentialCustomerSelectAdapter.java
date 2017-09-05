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
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.view.activity.CrmPotentialFollowSelectCustomActivity;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/2.
 */

public class PotentialCustomerSelectAdapter extends RecyclerView.Adapter<PotentialCustomerSelectAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<CrmCustom.Custom> customList;
    public PotentialCustomerSelectAdapter(Context context,ArrayList<CrmCustom.Custom> customList){
        this.context = context;
        this.customList = customList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_crm_custom_selector,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setText(customList.get(position).getName(),holder.tv_item_crm_name);
        setText(customList.get(position).getAddress(),holder.tv_item_crm_address);
        if(customList.get(position).isSelect()){
            holder.iv_item_crm_select.setSelected(true);
        }
    }

    private void setText(String text,TextView textView) {
        if(!TextUtils.isEmpty(text)){
            textView.setText(text);
        }
    }
    @Override
    public int getItemCount() {
        return customList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_item_crm_name)
        TextView tv_item_crm_name;

        @BindView(R.id.tv_item_crm_address)
        TextView tv_item_crm_address;

        @BindView(R.id.iv_item_crm_select)
        TextView iv_item_crm_select;

        @BindView(R.id.item_crm_custom)
        AutoLinearLayout item_crm_custom;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            item_crm_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int i = 0;i<customList.size();i++){
                        customList.get(i).setSelect(false);
                    }
                    customList.get(getLayoutPosition()).setSelect(true);
                    //将数据携带回上个启动界面
                    Intent intent = ((CrmPotentialFollowSelectCustomActivity)context).getIntent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("custom",customList.get(getLayoutPosition()));
                    intent.putExtras(bundle);
                    ((CrmPotentialFollowSelectCustomActivity)context).setResult(101,intent);
                    ((CrmPotentialFollowSelectCustomActivity)context).finish();
                }
            });
        }
    }
}
