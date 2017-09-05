package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AppCommonUseAdapter;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class AppCommonUseFragment extends BaseFragment {
    private static final String TAG = "MyFragments";
    private RecyclerView mCrmGrid;
    private RecyclerView mPurchaseGrid;
    private RecyclerView mStockGrid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup content= (ViewGroup) inflater.inflate(R.layout.fragment_app_common_use,container,false);
        return super.onCreateView(inflater, content, savedInstanceState);
    }


    @Override
    public void onInitView(View rootView) {
        mCrmGrid= (RecyclerView) rootView.findViewById(R.id.crmGrid);
        mPurchaseGrid= (RecyclerView) rootView.findViewById(R.id.purchaseGrid);
        mStockGrid= (RecyclerView) rootView.findViewById(R.id.stockGrid);

        AppCommonUseAdapter adapter = new AppCommonUseAdapter(getActivity());

        mCrmGrid.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mCrmGrid.setAdapter(adapter);
        mCrmGrid.setHasFixedSize(true);
        mCrmGrid.addItemDecoration(adapter.getItemDecor());

        mPurchaseGrid.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mPurchaseGrid.setAdapter(adapter);
        mPurchaseGrid.setHasFixedSize(true);
        mPurchaseGrid.addItemDecoration(adapter.getItemDecor());

        mStockGrid.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mStockGrid.setAdapter(adapter);
        mStockGrid.setHasFixedSize(true);
        mStockGrid.addItemDecoration(adapter.getItemDecor());
    }

}
