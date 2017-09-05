package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CargoModel;
import com.wyu.iwork.view.activity.BuildStockActivity;
import com.wyu.iwork.view.activity.CargoLocationDetailActivity;
import com.wyu.iwork.view.activity.CargoLocationManagerActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.StockDetailActivity;
import com.wyu.iwork.view.activity.StoresDetailActivity;
import com.wyu.iwork.view.activity.StoresManagerActivity;
import com.wyu.iwork.view.activity.SupplierDetailActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 货位管理adapter
 */

public class CargoLocationManagerAdapter extends RecyclerView.Adapter<CargoLocationManagerAdapter.ViewHolder> {

    private CargoLocationManagerActivity mcontext;
    private List<CargoModel> list;

    public CargoLocationManagerAdapter(CargoLocationManagerActivity mcontext,List<CargoModel> data) {
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
         * 货位编号
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView cargoNum;
        /**
         * 货位名称
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView cargoName;
        /**
         * 仓库名称
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView storeName;
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

            if (mcontext.flag!=null&&!"".equals(mcontext.flag)){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            final CargoModel item = list.get(position);

            cargoNum.setText("货位编号："+item.getSole_id());
            cargoName.setText("货位名称："+item.getName());
            storeName.setText("货位名称："+item.getStore_name());
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
                        case "BuildStockActivity"://新建库存
                            intent = new Intent(mcontext,BuildStockActivity.class);
                            intent.putExtra("model",item);
                            mcontext.setResult(BuildStockActivity.CODE_CARGO_NUM,intent);
                            mcontext.finish();
                            break;
                        case "StockDetailActivity"://库存详情
                            intent = new Intent(mcontext,StockDetailActivity.class);
                            intent.putExtra("model",item);
                            mcontext.setResult(StockDetailActivity.CODE_CARGO_NUM,intent);
                            mcontext.finish();
                            break;
                        default:
                            intent = new Intent(mcontext,CargoLocationDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }

                }
            });
        }
    }
}
