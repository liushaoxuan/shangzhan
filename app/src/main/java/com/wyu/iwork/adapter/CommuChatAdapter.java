package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.huajianjiang.baserecyclerview.viewholder.BaseViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.viewholder.AppChoiceViewHolder;
import com.wyu.iwork.model.TestModel;

import io.rong.imkit.RongIM;

/**
 * Created by jhj_Plus on 2016/10/26.
 */
public class CommuChatAdapter extends BaseAdapter<AppChoiceViewHolder,TestModel> {
    private static final String TAG = "AppChoiceAdapter";

    public CommuChatAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindViewHolder(AppChoiceViewHolder vh, TestModel item, int position) {
        if (position == getItemCount() - 1) {
            vh.getView(R.id.divider).setVisibility(View.GONE);
        }
        if (item == null) return;
    }

    @Override
    public AppChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppChoiceViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)){
            @Override
            public void onItemClick(BaseViewHolder vh, View v, int adapterPosition) {
                RongIM.getInstance().startPrivateChat(context,"19","AAAA");
            }
        };
    }

    @Override
    public int getItemCount() {
        return 6;
    }

}
