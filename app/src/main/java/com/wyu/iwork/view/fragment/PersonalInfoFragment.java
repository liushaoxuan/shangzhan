package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.view.activity.PersonalInfoActivity;

import butterknife.ButterKnife;

/**
 * Created by sxliu on 2017/4/10.
 * 个人资料
 */

public class PersonalInfoFragment extends Fragment {


    private UserInfo infomation;
    private PersonalInfoActivity activity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (PersonalInfoActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_personal_info,container,false);
        ButterKnife.bind(this,view);

        return view;
    }



}
