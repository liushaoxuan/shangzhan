package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.OutStoreModel;
import com.wyu.iwork.view.activity.AddOrderActivity;
import com.wyu.iwork.view.activity.BuildSalesOrderActivity;
import com.wyu.iwork.view.activity.GoodsDetailActivity;
import com.wyu.iwork.view.activity.GoodsInStoreActivity;
import com.wyu.iwork.view.activity.GoodsOutStoreActivity;
import com.wyu.iwork.view.activity.OutStoreDetailActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.SalesOrderDetailActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 商品出库adapter
 */

public class GoodsOutStoreAdapter extends RecyclerView.Adapter<GoodsOutStoreAdapter.ViewHolder> {

    private GoodsOutStoreActivity mcontext;
    private List<OutStoreModel> list;

    public GoodsOutStoreAdapter(GoodsOutStoreActivity mcontext,List<OutStoreModel> list) {
        this.mcontext = mcontext;
        this.list = list;
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
         * 出库编号
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView outNum;
        /**
         * 商品名称
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView goodsName;
        /**
         * 入库数量
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView instoreNum;
        /**
         * 时间
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

            if (mcontext.flag!=null&&!"".equals(mcontext.flag)){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            final OutStoreModel item = list.get(position);

            outNum.setText("出库编号："+item.getSole_id());
            goodsName.setText("商品名称："+item.getGoods_name());
            instoreNum.setText("出库数量："+item.getGoods_num());
            date.setText(item.getAdd_time());



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    checked_icon.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_work_plain_icon_select));
                    switch (mcontext.flag){
                        default:
                            intent = new Intent(mcontext,OutStoreDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }

                }
            });
        }
    }
}
