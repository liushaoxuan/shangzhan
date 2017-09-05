package com.wyu.iwork.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.OrganzSecSelectorAdapter;
import com.wyu.iwork.model.UserOrganzParent;
import com.wyu.iwork.presenter.OrganzSecSelectorPresenter;
import com.wyu.iwork.util.Constant;

import org.json.JSONObject;

import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzSecSelectorFragment extends  BaseFragment<List<UserOrganzParent>> {
    private static final String TAG = OrganzSecSelectorFragment.class.getSimpleName();
    private RecyclerView mRv;
    private OrganzSecSelectorAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetPresenter(new OrganzSecSelectorPresenter(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater,
                (ViewGroup) inflater.inflate(R.layout.part_recyclerview, container, false),
                savedInstanceState);
    }

    @Override
    protected void onInitView(View rootView) {
        mRv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mAdapter = new OrganzSecSelectorAdapter(getActivity());
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
    }

    @Override
    public void onSuccess(List<UserOrganzParent> data, JSONObject origin) {
        super.onSuccess(data,origin);
        if (data == null) return;
        mAdapter.insertParents(data);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                Intent result = new Intent();
                result.putExtra(Constant.KEY_ENTITY, mAdapter.getCheckedItem());
                getActivity().setResult(Activity.RESULT_OK, result);
                getActivity().finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

}
