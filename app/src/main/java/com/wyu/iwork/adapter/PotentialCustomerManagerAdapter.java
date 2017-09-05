package com.wyu.iwork.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.CrmCustom;
import com.wyu.iwork.view.activity.CrmCustomDetailActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/2.
 */

public class PotentialCustomerManagerAdapter extends RecyclerView.Adapter<PotentialCustomerManagerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private static final String TYPE_CUSTOM = "CUSTOM";//客户管理
    private static final String TYPE_POTENTIAL = "POTENTIAL";//潜在客户管理
    private String type;
    private ArrayList<CrmCustom.Custom> customList;
    private Intent intent;
    private Bundle bundle;

    public PotentialCustomerManagerAdapter(Context context,String type,ArrayList<CrmCustom.Custom> customList){
        this.context = context;
        this.type = type;
        this.customList = customList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_crm_custom,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!TextUtils.isEmpty(customList.get(position).getName())){
            holder.tv_item_crm_name.setText(customList.get(position).getName());
        }
        if(!TextUtils.isEmpty(customList.get(position).getAddress())){
            holder.tv_item_crm_address.setText(customList.get(position).getAddress());
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

        @BindView(R.id.iv_item_crm_phone)
        ImageView iv_item_crm_phone;

        @BindView(R.id.iv_item_crm_message)
        ImageView iv_item_crm_message;

        @BindView(R.id.item_crm_custom)
        AutoLinearLayout item_crm_custom;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            item_crm_custom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(intent == null){
                        intent = new Intent(context, CrmCustomDetailActivity.class);
                        bundle = new Bundle();
                    }
                    bundle.putSerializable("CUSTOM",customList.get(getLayoutPosition()));
                    intent.putExtras(bundle);
                    if (TYPE_CUSTOM.equals(type)){
                        intent.putExtra("type","BROWSE");
                    }else{
                        intent.putExtra("type","POTENTIAL_BROWSE");
                    }
                    context.startActivity(intent);
                }
            });
            iv_item_crm_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(customList.get(getLayoutPosition()).getPhone());
                }
            });
            iv_item_crm_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSendSMSTo(customList.get(getLayoutPosition()).getPhone());
                }
            });
        }
    }

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     */
    public void doSendSMSTo(String phoneNumber){
        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
            context.startActivity(intent);
        }
    }

    private void showDialog(final String phone){
        MyCustomDialogDialog dialog = new MyCustomDialogDialog(4, context, R.style.MyDialog, phone, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + phone));
                intent.setAction(Intent.ACTION_DIAL);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
