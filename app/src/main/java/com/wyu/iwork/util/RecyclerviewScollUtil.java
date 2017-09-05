package com.wyu.iwork.util;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by lx on 2017/4/14.
 */

public class RecyclerviewScollUtil {
    private Context context;

    public RecyclerviewScollUtil(Context context) {
        this.context = context;
    }

    //解决嵌套滑动不流畅问题
    public void ScrollSmooth(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    //解决RecyclyView宫格类型滑动不流畅的问题
    public void ScrollSmoothforGrid(RecyclerView recyclerView,int line){
        GridLayoutManager layoutManager = new GridLayoutManager(context,line);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
