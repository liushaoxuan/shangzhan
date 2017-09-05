package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.SurpplierManagerModel;
import com.wyu.iwork.view.activity.AddOrderActivity;
import com.wyu.iwork.view.activity.GoodsDetailActivity;
import com.wyu.iwork.view.activity.GoodsManagerActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.SupplierDetailActivity;
import com.wyu.iwork.view.activity.SupplierManagerActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 供应商管理adapter
 */

public class SupplierManagerAdapter extends RecyclerView.Adapter<SupplierManagerAdapter.ViewHolder> {

    private SupplierManagerActivity mcontext;
    private List<SurpplierManagerModel> list;

    public SupplierManagerAdapter(SupplierManagerActivity mcontext, List<SurpplierManagerModel> data) {
        this.mcontext = mcontext;
        list = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_purchase_order,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
                    holder.setDada(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 供应商名称
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView surppliername;
        /**
         * 产品类型
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView type;
        /**
         * 联系人
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView contacts;
        /**
         * 日期
         */
        @BindView(R.id.item_pruchase_order_main_date)
        TextView date;
        /**
         * 选中的图标
         */
        @BindView(R.id.item_pruchase_order_checked_icon)
        ImageView checked_icon;

        @BindView(R.id.item_pruchase_order_bottom)
        View bottomview;
        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this,itemView);
        }
        private void setDada(int position){
            if (position==list.size()-1){
                bottomview.setVisibility(View.VISIBLE);
            }else {
                bottomview.setVisibility(View.GONE);
            }
            date.setVisibility(View.VISIBLE);
            final SurpplierManagerModel item = list.get(position);

            if (mcontext.flag!=null&&!"".equals(mcontext.flag)){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            surppliername.setText("供应商名称："+item.getName());
            type.setText("产品类型："+item.getType_name());
            contacts.setText("联系人："+item.getContacts());
            date.setText(item.getAdd_time());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checked_icon.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_work_plain_icon_select));
                    Intent intent = null;
                    switch (mcontext.flag){
                        case "AddOrderActivity"://新建采购订单
                            intent = new Intent(mcontext,AddOrderActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(AddOrderActivity.requestCode_Supplier,intent);
                            mcontext.finish();
                            break;
                        case "PurchaseOrderEditFragment"://编辑采购订单
                            intent = new Intent(mcontext,PurchaseOrderDetailsActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(AddOrderActivity.requestCode_Supplier,intent);
                            mcontext.finish();
                            break;
                        default:
                            intent = new Intent(mcontext,SupplierDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }

                }
            });

        }
    }
}
