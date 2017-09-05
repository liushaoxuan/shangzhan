package com.wyu.iwork.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.Clue;
import com.wyu.iwork.view.activity.SalesLeadDetailActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/6/7.
 */

public class SalesLeadAdapter extends RecyclerView.Adapter<SalesLeadAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<Clue.ClueMessage> clueList;
    public SalesLeadAdapter(Context context,ArrayList<Clue.ClueMessage> clueList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.clueList = clueList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_sales_lead,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.tv_sales_name.setText(clueList.get(position).getName());
            holder.tv_sales_source.setText("客户线索（"+context.getResources()
                    .getStringArray(R.array.crm_sales_source)[Integer.parseInt(clueList.get(position).getSource_type())-1]+"）");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return clueList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //客户名称
        @BindView(R.id.tv_sales_name)
        TextView tv_sales_name;

        //来源
        @BindView(R.id.tv_sales_source)
        TextView tv_sales_source;

        //电话
        @BindView(R.id.iv_sales_call)
        ImageView iv_sales_call;

        //短信
        @BindView(R.id.iv_sales_message)
        ImageView iv_sales_message;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,SalesLeadDetailActivity.class);
                    intent.putExtra("type","BROWSER");
                    intent.putExtra("clue_id",clueList.get(getLayoutPosition()).getId());
                    context.startActivity(intent);
                }
            });
            iv_sales_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(clueList.get(getLayoutPosition()).getPhone());
                }
            });

            iv_sales_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doSendSMSTo(clueList.get(getLayoutPosition()).getPhone());
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
            //intent.putExtra("sms_body", message);
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
