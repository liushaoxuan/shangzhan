package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.SaleOrderModel;
import com.wyu.iwork.view.activity.BuildCargoLocationActivity;
import com.wyu.iwork.view.activity.BuildGoodsOutStoreActivity;
import com.wyu.iwork.view.activity.CargoLocationDetailActivity;
import com.wyu.iwork.view.activity.OutStoreDetailActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.SalesOrderActivity;
import com.wyu.iwork.view.activity.SalesOrderDetailActivity;
import com.wyu.iwork.view.activity.StoresDetailActivity;
import com.wyu.iwork.view.activity.StoresManagerActivity;
import com.wyu.iwork.view.activity.SupplierDetailActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 销售订单adapter
 */

public class SalesOrderAdapter extends RecyclerView.Adapter<SalesOrderAdapter.ViewHolder> {

    private SalesOrderActivity mcontext;
    private List<SaleOrderModel> list;

    public SalesOrderAdapter(SalesOrderActivity mcontext,List<SaleOrderModel> list) {
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
         * 订单编号
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView orderNum;
        /**
         * 商品名称
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView goodsName;
        /**
         * 商品总价
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView amount;
        /**
         * 订单状态
         */
        @BindView(R.id.item_pruchase_order_state)
        TextView state;
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

            state.setVisibility(View.VISIBLE);
            if (mcontext.flag!=null&&!mcontext.flag.equals("")){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            final SaleOrderModel item =   list.get(position);
            orderNum.setText("订单编号："+item.getSole_id());
            goodsName.setText("商品名称："+item.getGoods_name());
            amount.setText("商品总价："+item.getGoods_amount());


            switch (item.getStatus()){
                case "1":
                    state.setText("待审批");
                    break;
                case "2":
                    state.setText("已审批");
                    break;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    checked_icon.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_work_plain_icon_select));
                    switch (mcontext.flag){
                        case "BuildGoodsOutStoreActivity"://新建出库
                            intent = new Intent(mcontext, BuildGoodsOutStoreActivity.class);
                            intent.putExtra("model",item);
                            mcontext.setResult(BuildGoodsOutStoreActivity.OutStoreCode,intent);
                            mcontext.finish();
                            break;
                        case "OutStoreDetailActivity"://编辑出库
                            intent = new Intent(mcontext, OutStoreDetailActivity.class);
                            intent.putExtra("model",item);
                            mcontext.setResult(OutStoreDetailActivity.OutStoreDetailCode,intent);
                            mcontext.finish();
                            break;
                        default:
                            intent = new Intent(mcontext, SalesOrderDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }
                }
            });

        }
    }
}
