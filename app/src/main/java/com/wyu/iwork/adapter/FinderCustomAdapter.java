package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.FinderCustomer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/6/15.
 */

public class FinderCustomAdapter extends RecyclerView.Adapter<FinderCustomAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<FinderCustomer.Customer> list;

    public FinderCustomAdapter(Context context, ArrayList<FinderCustomer.Customer> list){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_finder_customer,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_tel.setText(list.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_tel)
        TextView tv_tel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            /**
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CrmCustomuploadingActivity.class);
                    intent.putExtra("id",list.get(getLayoutPosition()).getId());
                    intent.putExtra("type",101);
                    context.startActivity(intent);
                }
            });*/
        }
    }
}
