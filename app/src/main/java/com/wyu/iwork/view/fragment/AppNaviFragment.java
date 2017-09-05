package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class AppNaviFragment extends BaseFragment {
    private static final String TAG = "MyFragments";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup content= (ViewGroup) inflater.inflate(R.layout.fragment_app_navi,container,false);
        return super.onCreateView(inflater, content, savedInstanceState);
    }


    @Override
    public void onInitView(View rootView) {
    }

}
