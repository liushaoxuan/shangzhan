package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AppChoiceAdapter;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class AppChoiceFragment extends BaseFragment {
    private static final String TAG = "MyFragments";
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup content = (ViewGroup) inflater.inflate(R.layout.fragment_app_choice, container,
                false);
        return super.onCreateView(inflater, content, savedInstanceState);
    }


    @Override
    public void onInitView(View rootView) {
        mRecyclerView= (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        AppChoiceAdapter adapter = new AppChoiceAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(adapter.getItemDecor());
    }

}
