package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.InStoreModel;
import com.wyu.iwork.view.activity.AddOrderActivity;
import com.wyu.iwork.view.activity.BuildSalesOrderActivity;
import com.wyu.iwork.view.activity.GoodsDetailActivity;
import com.wyu.iwork.view.activity.GoodsInStoreActivity;
import com.wyu.iwork.view.activity.GoodsManagerActivity;
import com.wyu.iwork.view.activity.InStoreDetailActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.SalesOrderDetailActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 商品入库adapter
 */

public class GoodsInStoreAdapter extends RecyclerView.Adapter<GoodsInStoreAdapter.ViewHolder> {

    private GoodsInStoreActivity mcontext;
    private List<InStoreModel> list;

    public GoodsInStoreAdapter(GoodsInStoreActivity mcontext,List<InStoreModel> data) {
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
         * 入库编号
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView inStoreNum;
        /**
         * 商品名称
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView goodsName;
        /**
         * 入库数量
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView instoreNums;
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

            final InStoreModel item = list.get(position);

            inStoreNum.setText("入库编号："+item.getSole_id());
            goodsName.setText("商品名称："+item.getGoods_name());
            instoreNums.setText("入库数量："+item.getGoods_num());
            date.setText(""+item.getAdd_time());

            if (mcontext.flag!=null&&!"".equals(mcontext.flag)){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext,PurchaseOrderDetailsActivity.class);
                    mcontext.startActivity(intent);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    checked_icon.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_work_plain_icon_select));
                    switch (mcontext.flag){
                        case "BuildSalesOrderActivity"://新建销售订单
                            intent = new Intent(mcontext,BuildSalesOrderActivity.class) ;
                            mcontext.setResult(BuildSalesOrderActivity.CODE_002,intent);
                            mcontext.finish();
                            break;
                        case "AddOrderActivity"://新建订单
                            intent = new Intent(mcontext,AddOrderActivity.class) ;
                            mcontext.setResult(AddOrderActivity.requestCode_goods,intent);
                            mcontext.finish();
                            break;
                        case "SalesOrderDetailActivity"://销售订单详情
                            intent = new Intent(mcontext,AddOrderActivity.class) ;
                            mcontext.setResult(SalesOrderDetailActivity.CODE_002,intent);
                            mcontext.finish();
                        default:
                            intent = new Intent(mcontext,InStoreDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }

                }
            });
        }
    }
}
