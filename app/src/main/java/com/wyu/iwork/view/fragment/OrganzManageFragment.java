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
import com.wyu.iwork.adapter.OrganzManageAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.OrganzManageParent;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.OrganzManagePresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.view.activity.OrganzEditActivity;

import org.json.JSONObject;

import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class OrganzManageFragment extends BaseFragment<List<OrganzManageParent>> {
    private static final String TAG = OrganzManageFragment.class.getSimpleName();
    private RecyclerView mRv;
    private OrganzManageAdapter mAdapter;
    private OrganzManagePresenter mPresenter;
    private Bundle mMyArgs = new Bundle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNetPresenter(mPresenter = new OrganzManagePresenter(getActivity()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater,
                (ViewGroup) inflater.inflate(R.layout.fragment_organz_manage, container, false),
                savedInstanceState);
    }

    @Override
    protected void onInitView(View rootView) {
        super.onInitView(rootView);
        mRv = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mMyArgs.putString(Constant.KEY_ACTION_TYPE, Constant.VALUE_TYPE_R);
        mAdapter = new OrganzManageAdapter(this);
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL));
        mRv.setAdapter(mAdapter);
        mAdapter.addParentExpandCollapseListener(new ExpandableAdapter.OnParentExpandCollapseListener() {
            @Override
            public void onParentExpanded(ParentViewHolder pvh, int parentPosition, boolean pendingCause, boolean byUser) {
                if (pvh == null) return;
                View arrow = pvh.getView(R.id.arrow);
                arrow.animate().rotation(-180).start();
            }

            @Override
            public void onParentCollapsed(ParentViewHolder pvh, int parentPosition, boolean pendingCause, boolean byUser) {
                if (pvh == null) return;
                View arrow = pvh.getView(R.id.arrow);
                arrow.animate().rotation(-90).start();
            }
        });
        UserInfo userInfo = MyApplication.userInfo;
        ((TextView) rootView.findViewById(R.id.name))
                .setText(userInfo != null ? userInfo.getCompany() : "");
    }

    @Override
    public void onSuccess(List<OrganzManageParent> data, JSONObject origin) {
        super.onSuccess(data, origin);
        final String actionType = mMyArgs.getString(Constant.KEY_ACTION_TYPE, "");
        if (actionType.equals(Constant.VALUE_TYPE_R)) {
            if (data != null) {
                mAdapter.insertParents(data);
            }
        } else if (actionType.equals(Constant.VALUE_TYPE_D)) {
            final String itemType = mMyArgs.getString(Constant.KEY_ITEM_TYPE, "");
            final int parentPos = mMyArgs.getInt(Constant.KEY_PARENT_POS, -1);
            final int childPos = mMyArgs.getInt(Constant.KEY_CHILD_POS, -1);
            if (itemType.equals(Constant.VALUE_TYPE_ITEM_PARENT)) {
                mAdapter.removeParent(parentPos);
            } else if (itemType.equals(Constant.VALUE_TYPE_ITEM_CHILD)) {
                mAdapter.removeChild(parentPos,childPos);
            }
        }
    }

    public void deleteOrganz(Bundle args) {
        mMyArgs.putString(Constant.KEY_ACTION_TYPE, Constant.VALUE_TYPE_D);
        mMyArgs.putAll(args);
        mPresenter.deleteOrganz(args.getString(Constant.KEY_ID));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent addIntent = new Intent(getActivity(), OrganzEditActivity.class);
                startActivity(addIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
