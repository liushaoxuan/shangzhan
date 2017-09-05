package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.model.CustomerModel;
import com.wyu.iwork.view.activity.BuildSalesOrderActivity;
import com.wyu.iwork.view.activity.SalesOrderDetailActivity;
import com.wyu.iwork.view.activity.SelectCustmerActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sxliu on 2017/5/12.
 * 选择客户adapter
 */

public class SelectCustmerAdapter extends RecyclerView.Adapter<SelectCustmerAdapter.ViewHolder> {

    private SelectCustmerActivity context;
    private ArrayList<CustomerModel> list;

    public SelectCustmerAdapter(SelectCustmerActivity context, ArrayList<CustomerModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_crm_custom_selector,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setdata(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //客户名称
        @BindView(R.id.tv_item_crm_name)
        TextView customeName;

        //客户地址
        @BindView(R.id.tv_item_crm_address)
        TextView addr;

        @BindView(R.id.iv_item_crm_select)
        TextView iv_item_crm_select;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void setdata(int position){
            final CustomerModel item = list.get(position);
            customeName.setText(item.getName());
            addr.setText(item.getAddress());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    iv_item_crm_select.setSelected(true);
                    switch (context.flag){
                        case "BuildSalesOrderActivity"://新建销售订单
                            intent = new Intent(context, BuildSalesOrderActivity.class);
                            intent.putExtra("model",item);
                            context.setResult(BuildSalesOrderActivity.CODE_001,intent);
                            context.finish();
                            break;
                        case "SalesOrderDetailActivity"://销售订单详情
                            intent = new Intent(context, SalesOrderDetailActivity.class);
                            intent.putExtra("model",item);
                            context.setResult(SalesOrderDetailActivity.CODE_001,intent);
                            context.finish();
                            break;
                    }

                }
            });

        }
    }
}
