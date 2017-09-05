package com.wyu.iwork.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CommonApplicatAdapter;
import com.wyu.iwork.model.ApplicatCommonModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/1/17.
 * 应用——常用
 */
@SuppressLint("ValidFragment")
public class CommonApplicatFragment extends BaseFragment {

    @BindView(R.id.common_application_search_input)
    EditText inputText;
    @BindView(R.id.common_application_recyclearview)
    RecyclerView recyclerView;

    private CommonApplicatAdapter adapter;

    private List<ApplicatCommonModel> list;
    public CommonApplicatFragment(List<ApplicatCommonModel> list) {
        this.list = list;
    }

    public CommonApplicatFragment(){

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.fragment_applicat_common,null);
        ButterKnife.bind(this,view);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView(){
        adapter = new CommonApplicatAdapter(getActivity(),list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }
}
