package com.wyu.iwork.view.fragment;

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
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.CommuOrgnzAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.OrgnzParent;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.CommuOrganzPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Debug;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.OrganzUserEditActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/25.
 */
public class CommuOrgnzFragment extends BaseFragment<List<OrgnzParent>> {
    private static final String TAG = "CommuOrgnzFragment";
    private RecyclerView mRv;
    private CommuOrgnzAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childTag = TAG;
        setNetPresenter(new CommuOrganzPresenter(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        ViewGroup content = (ViewGroup) inflater
                .inflate(R.layout.fragment_commu_orgnz, container, false);
        return super.onCreateView(inflater, content, savedInstanceState);
    }

    @Override
    public void onInitView(View rootView) {
        mRv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mAdapter = new CommuOrgnzAdapter(getActivity());
        mAdapter.addParentExpandCollapseListener(
                new ExpandableAdapter.OnParentExpandCollapseListener() {
                    @Override
                    public void onParentExpanded(ParentViewHolder pvh, int parentPosition,
                                                 boolean pendingCause, boolean byUser)
                    {
                        if (pvh == null) return;
                        View arrow = pvh.getView(R.id.indicator);
                        arrow.animate().rotation(-180).start();
                    }

                    @Override
                    public void onParentCollapsed(ParentViewHolder pvh, int parentPosition,
                                                  boolean pendingCause, boolean byUser)
                    {
                        if (pvh == null) return;
                        View arrow = pvh.getView(R.id.indicator);
                        arrow.animate().rotation(-90).start();
                    }
                });
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        if (getNetPresenter().getCachedData() != null)
            mAdapter.insertParents((List<OrgnzParent>) getNetPresenter().getCachedData());

        UserInfo userInfo = MyApplication.userInfo;
        ((TextView) rootView.findViewById(R.id.name))
                .setText(userInfo != null ? userInfo.getCompany() : "");
    }

    @Override
    public void onSuccess(List<OrgnzParent> data, JSONObject origin) {
        super.onSuccess(data, origin);
        if (data == null) return;
        mAdapter.insertParents(data);
        Logger.e(TAG, "origin=" + Debug.printPrettyJson(origin));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent userEditIntent = new Intent(getActivity(), OrganzUserEditActivity.class);
                Bundle args = new Bundle();
                args.putString(Constant.KEY_ACTION_TYPE,Constant.VALUE_TYPE_C);
                userEditIntent.putExtras(args);
                startActivity(userEditIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
