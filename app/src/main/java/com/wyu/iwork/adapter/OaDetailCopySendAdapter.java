package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wyu.iwork.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/24.15:30
 * 邮箱：2587294424@qq.com
 * oa详情抄送人adapter
 */

public class OaDetailCopySendAdapter extends RecyclerView.Adapter<OaDetailCopySendAdapter.ViewHolder>{

    private Context context;
    private List<String> list;

    public OaDetailCopySendAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_detail_copysend,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 用户名第一个字
         */
        @BindView(R.id.oa_detail_copysend_name_first)
        TextView first_names;
        /**
         * 用户名
         */
        @BindView(R.id.oa_detail_copysend_name)
        TextView username;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void setData(int position){

            String name = list.get(position);
            try {
                username.setText(name);
                first_names.setText(name.substring(0,1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
