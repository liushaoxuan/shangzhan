package com.wyu.iwork.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.NavigationApplicatAdapter;
import com.wyu.iwork.model.ApplicationItemModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/1/17.
 * 应用——导航
 */
@SuppressLint("ValidFragment")
public class NavigatApplicatFragment extends BaseFragment {

    @BindView(R.id.navigation_application_search_input)
    EditText inputText;

    @BindView(R.id.navigation_application_recyclearview)
    RecyclerView recyclerView;

    private NavigationApplicatAdapter adapter;

    private List<ApplicationItemModel> list;
    public NavigatApplicatFragment(List<ApplicationItemModel> list) {
        this.list = list;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_applicat_navigation,null);
        ButterKnife.bind(this,container);
        setRecyclerView();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setRecyclerView(){
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        adapter = new NavigationApplicatAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
    }
}
