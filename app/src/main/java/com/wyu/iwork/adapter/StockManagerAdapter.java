package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.StockModel;
import com.wyu.iwork.view.activity.AddOrderActivity;
import com.wyu.iwork.view.activity.BuildSalesOrderActivity;
import com.wyu.iwork.view.activity.GoodsDetailActivity;
import com.wyu.iwork.view.activity.GoodsManagerActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.SalesOrderDetailActivity;
import com.wyu.iwork.view.activity.StockDetailActivity;
import com.wyu.iwork.view.activity.StockManagerActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 库存管理adapter
 */

public class StockManagerAdapter extends RecyclerView.Adapter<StockManagerAdapter.ViewHolder> {

    private StockManagerActivity mcontext;
    private List<StockModel> list;

    public StockManagerAdapter(StockManagerActivity mcontext,List<StockModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_stock_manager,parent,false);
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
         * 商品名称
         */
        @BindView(R.id.item_stock_manager_GoodsName)
        TextView GoodsName;
        /**
         * 真实库存
         */
        @BindView(R.id.item_stock_manager_real_count)
        TextView real_count;
        /**
         * 日期
         */
        @BindView(R.id.item_stock_manager_date)
        TextView date;
        /**
         * 选中的图标
         */
        @BindView(R.id.item_stock_manager_checked_icon)
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

            if (mcontext.flag!=null&&!"".equals(mcontext.flag)){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            final StockModel item = list.get(position);

            GoodsName.setText("商品名称："+item.getGoods_name());
            real_count.setText("真实库存："+item.getNum());
            date.setText(item.getAdd_time());


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
                            intent.putExtra("model",item);
                            mcontext.setResult(BuildSalesOrderActivity.CODE_002,intent);
                            mcontext.finish();
                            break;
                        case "AddOrderActivity"://新建订单
                            intent = new Intent(mcontext,AddOrderActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(AddOrderActivity.requestCode_goods,intent);
                            mcontext.finish();
                            break;
                        case "SalesOrderDetailActivity"://销售订单详情
                            intent = new Intent(mcontext,AddOrderActivity.class) ;
                             intent.putExtra("model",item);
                            mcontext.setResult(SalesOrderDetailActivity.CODE_002,intent);
                            mcontext.finish();
                        default:
                            intent = new Intent(mcontext,StockDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }

                }
            });
        }
    }
}
