package com.wyu.iwork.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wyu.iwork.R;
import com.wyu.iwork.adapter.viewholder.AppChoiceViewHolder;
import com.wyu.iwork.model.TestModel;
import com.wyu.iwork.test.Test;

/**
 * Created by jhj_Plus on 2016/10/26.
 */
public class AppCommonUseAdapter extends BaseAdapter<AppChoiceViewHolder,TestModel> {
    private static final String TAG = "AppChoiceAdapter";

    public AppCommonUseAdapter(Context context) {
        super(context, Test.getRandomTestData(6, 8));
    }

    @Override
    protected void onBindViewHolder(AppChoiceViewHolder vh, TestModel item, int position) {
        if (item == null) return;
        TextView tv_name= vh.getView(R.id.name);
        tv_name.setText(item.getName());
        tv_name.setCompoundDrawablesWithIntrinsicBounds(0,item.getIcon(),0,0);
    }

    @Override
    public AppChoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppChoiceViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_app, parent, false));
    }


    public RecyclerView.ItemDecoration getItemDecor() {
        return new ItemDecor();
    }

    private class ItemDecor extends RecyclerView.ItemDecoration {
        private int item_offset_2dp;

        ItemDecor() {
            item_offset_2dp = context.getResources().getDimensionPixelSize(
                    R.dimen.item_offset_2dp);
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) parent.getLayoutManager();
            final int spanCount = gridLayoutManager.getSpanCount();
            final int childAdapterPos = parent.getChildAdapterPosition(view);
            outRect.set(0, 0, (childAdapterPos + 1) % spanCount != 0 ? item_offset_2dp : 0,
                    getItemCount() - (childAdapterPos + 1) < spanCount ? 0 : item_offset_2dp);
        }
    }

}
