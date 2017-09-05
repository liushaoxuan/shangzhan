package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chaychan.viewlib.NumberRunningTextView;
import com.wyu.iwork.R;
import com.wyu.iwork.model.CrmContract;
import com.wyu.iwork.view.activity.CrmContractDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lx on 2017/6/5.
 */

public class CrmContractAdapter extends RecyclerView.Adapter<CrmContractAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<CrmContract.Contract> contractList;

    public CrmContractAdapter(Context context,ArrayList<CrmContract.Contract> contractList){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.contractList = contractList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_crm_contract,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.contract_money.setContent(checkString(contractList.get(position).getValue()));
        holder.contract_name.setText(checkString(contractList.get(position).getTitle()));
        holder.contract_person.setText(checkString(contractList.get(position).getCustomer_name()));
    }

    private String checkString(String text){
        if(TextUtils.isEmpty(text)){
            return "";
        }else{
            return text;
        }
    }

    @Override
    public int getItemCount() {
        return contractList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.contract_name)
        TextView contract_name;

        @BindView(R.id.contract_person)
        TextView contract_person;

        @BindView(R.id.contract_money)
        NumberRunningTextView contract_money;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CrmContractDetailActivity.class);
                    intent.putExtra("type","BROWSER");
                    intent.putExtra("id",contractList.get(getLayoutPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
