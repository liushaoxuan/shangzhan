package com.wyu.iwork.adapter.spaceitemdecoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lx on 2017/3/22.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = 0;
        outRect.left = 0;
        outRect.right = 0;
       // if(parent.getChildPosition(view) != 0)
            outRect.right = space;
    }
}
