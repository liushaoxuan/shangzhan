package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.model.StoreModel;
import com.wyu.iwork.view.activity.AddOrderActivity;
import com.wyu.iwork.view.activity.BuildCargoLocationActivity;
import com.wyu.iwork.view.activity.BuildStockActivity;
import com.wyu.iwork.view.activity.CargoLocationDetailActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.StockDetailActivity;
import com.wyu.iwork.view.activity.StoresDetailActivity;
import com.wyu.iwork.view.activity.StoresManagerActivity;
import com.wyu.iwork.view.activity.SupplierDetailActivity;
import com.wyu.iwork.view.activity.SupplierManagerActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 仓库管理adapter
 */

public class StoresManagerAdapter extends RecyclerView.Adapter<StoresManagerAdapter.ViewHolder> {

    private StoresManagerActivity mcontext;
    private List<StoreModel> list;

    public StoresManagerAdapter(StoresManagerActivity mcontext,List<StoreModel> data) {
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
         * 仓库编号
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView storeNum;
        /**
         * 仓库名称
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView storeName;
        /**
         * 仓库管理
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView storerName;

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
            if (mcontext.flag!=null&&!mcontext.flag.equals("")){
                checked_icon.setVisibility(View.VISIBLE);
            }else {
                checked_icon.setVisibility(View.GONE);
            }

            final StoreModel item = list.get(position);
            storeNum.setText("仓库编号："+item.getSole_id());
            storeName.setText("仓库名称："+item.getName());
            storerName.setText("仓库管理："+item.getUser_name());
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
                            intent = new Intent(mcontext,BuildStockActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(BuildStockActivity.CODE_STORE_NUM,intent);
                            mcontext.finish();
                            break;
                        case "StockDetailActivity"://库存详情
                            intent = new Intent(mcontext,StockDetailActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(StockDetailActivity.CODE_STORE_NUM,intent);
                            mcontext.finish();
                            break;
                        case "BuildCargoLocationActivity"://新建货位
                            intent = new Intent(mcontext,BuildCargoLocationActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(BuildCargoLocationActivity.StoreCode,intent);
                            mcontext.finish();
                            break;
                        case "CargoLocationDetailActivity"://货位详情
                            intent = new Intent(mcontext,CargoLocationDetailActivity.class) ;
                            intent.putExtra("model",item);
                            mcontext.setResult(CargoLocationDetailActivity.StoreCode,intent);
                            mcontext.finish();
                            break;
                        default:
                            intent = new Intent(mcontext,StoresDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }

                }
            });

        }
    }
}
