package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.PurchaseInModel;
import com.wyu.iwork.model.PurchaseModel;
import com.wyu.iwork.view.activity.BuildGoodsInstoreActivity;
import com.wyu.iwork.view.activity.InStoreDetailActivity;
import com.wyu.iwork.view.activity.PurchaseOrderActivity;
import com.wyu.iwork.view.activity.PurchaseOrderDetailsActivity;
import com.wyu.iwork.view.activity.PurchaseOrderInActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/3.
 * 订单采购入库adapter
 */

public class PurchaseOrderInAdapter extends RecyclerView.Adapter<PurchaseOrderInAdapter.ViewHolder> {

    private PurchaseOrderInActivity mcontext;
    private List<PurchaseInModel> list;

    public PurchaseOrderInAdapter(PurchaseOrderInActivity mcontext, List<PurchaseInModel> data) {
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
         * 单号
         */
        @BindView(R.id.item_pruchase_order_orderNum)
        TextView orderNum;
        /**
         * 采购类型
         */
        @BindView(R.id.item_pruchase_order_type)
        TextView type;
        /**
         * 主要负责人
         */
        @BindView(R.id.item_pruchase_order_main_person)
        TextView main_person;

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

            if (mcontext.flag!=null && !"".equals(mcontext.flag)){
                checked_icon.setVisibility(View.VISIBLE);
            }
            final PurchaseInModel item = list.get(position);
            orderNum.setText("采购单号："+item.getSole_id());
            switch (item.getType()){//分类类型  0：其他 1：销售商品 2：日常用品
                case "0":
                    type.setText("采购类型：其他" );
                    break;

                case "1":
                    type.setText("采购类型：销售商品" );
                    break;

                case "2":
                    type.setText("采购类型：日常用品" );
                    break;
            }
            main_person.setText("负责人："+item.getUser_name());

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    checked_icon.setImageDrawable(mcontext.getResources().getDrawable(R.mipmap.home_work_plain_icon_select));
                    switch (mcontext.flag){
                        case "BuildGoodsInstoreActivity"://新建入库
                            intent = new Intent(mcontext,BuildGoodsInstoreActivity.class);
                            intent.putExtra("model",item);
                            mcontext.setResult(BuildGoodsInstoreActivity.InStoreCode,intent);
                            mcontext.finish();
                            break;
                        case "InStoreDetailActivity"://入库详情
                            intent = new Intent(mcontext,InStoreDetailActivity.class);
                            intent.putExtra("model",item);
                            mcontext.setResult(InStoreDetailActivity.InStoreDetailCode,intent);
                            mcontext.finish();
                            break;
                        default:
                            intent = new Intent(mcontext,PurchaseOrderDetailsActivity.class);
                            intent.putExtra("id",item.getId());
                            mcontext.startActivity(intent);
                            break;
                    }
                }
            });
        }
    }
}
