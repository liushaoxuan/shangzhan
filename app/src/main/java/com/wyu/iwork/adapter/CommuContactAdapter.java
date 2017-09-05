package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.github.huajianjiang.baserecyclerview.viewholder.BaseViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.Contact;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/3.
 */
public class CommuContactAdapter extends BaseAdapter<BaseViewHolder, Contact> {
    private static final String TAG = CommuContactAdapter.class.getSimpleName();

    public CommuContactAdapter(Context context) {
        super(context);
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder vh, Contact item, int position) {
        if (item == null) return;
        ((TextView) vh.getView(R.id.name)).setText(item.getName());
        ((TextView) vh.getView(R.id.phone)).setText(item.getPhone());
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(R.layout.item_contact, parent, false)) {
            @Override
            public void onItemClick(BaseViewHolder vh, View v, int adapterPosition) {
                super.onItemClick(vh, v, adapterPosition);
            }
        };
    }
}
