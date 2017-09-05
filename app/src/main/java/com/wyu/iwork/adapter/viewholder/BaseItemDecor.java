package com.wyu.iwork.adapter.viewholder;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wyu.iwork.R;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/21.
 */
public class BaseItemDecor extends RecyclerView.ItemDecoration {
    private static final String TAG = BaseItemDecor.class.getSimpleName();
    protected int itemOffset;//8dp
    private Context mCtxt;

    private BaseItemDecor(Context ctxt) {
        mCtxt = ctxt.getApplicationContext();
        itemOffset = mCtxt.getResources().getDimensionPixelSize(R.dimen.item_offset_8dp);
    }

    public static class BaseLinearDecor extends BaseItemDecor {
        public BaseLinearDecor(Context ctxt) {
            super(ctxt);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            int adapterPos = parent.getChildAdapterPosition(view);
            outRect.set(0, itemOffset, 0, adapterPos == manager.getItemCount() - 1 ? itemOffset : 0);
        }
    }

    public static class BaseGridDecor extends BaseItemDecor {
        public BaseGridDecor(Context ctxt) {
            super(ctxt);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            int adapterPos = parent.getChildAdapterPosition(view);
            outRect.set(0, itemOffset, 0, adapterPos == manager.getItemCount() - 1 ? itemOffset : 0);
        }
    }
}
