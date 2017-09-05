package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.DepartmentModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/20.
 */

public class DepartmentBuildAdapter extends RecyclerView.Adapter<DepartmentBuildAdapter.ViewHolder> {

    private Context mcontext;
    private List<DepartmentModel> list;
    public static interface IOnItemSelectListener{
        public void onItemClick(int pos);
    };

    public DepartmentBuildAdapter(Context mcontext, List<DepartmentModel> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_spinner,parent,false);
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

        @BindView(R.id.item_spinner_textView)
        TextView textView  ;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void setData(int position){
            textView.setText(list.get(position).getDepartment());
        }
    }
}
