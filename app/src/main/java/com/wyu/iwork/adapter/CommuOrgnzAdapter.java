package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.viewholder.CommuOrgnzChildViewHolder;
import com.wyu.iwork.adapter.viewholder.CommuOrgnzParentViewHolder;
import com.wyu.iwork.model.OrgnzChild;
import com.wyu.iwork.model.OrgnzParent;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.OrganzUserEditActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/28.
 */
public class CommuOrgnzAdapter extends
        ExpandableAdapter<CommuOrgnzParentViewHolder, CommuOrgnzChildViewHolder, OrgnzParent, OrgnzChild>
{
    private static final String TAG = "CommuOrgnzAdapter";
    public static final int CHILD_USER_TYPE = 1;
    public static final int CHILD_ORGANZ_TYPE = 2;
    private Context mCtxt;
    private LayoutInflater mInflater;
    private List<OrgnzParent> mParents;

    public CommuOrgnzAdapter(Context ctxt) {
        this(ctxt, null);
    }

    public CommuOrgnzAdapter(Context ctxt, List<OrgnzParent> parents) {
        super(parents == null ? parents = new ArrayList<>() : parents);
        mCtxt = ctxt;
        mInflater = LayoutInflater.from(ctxt);
        mParents = parents;
    }

    @Override
    public CommuOrgnzParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        return new CommuOrgnzParentViewHolder(
                mInflater.inflate(R.layout.item_orgnz_parent, parent, false));
    }

    @Override
    public CommuOrgnzChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        Logger.e(TAG, "ct=====>" + childType);
        return new CommuOrgnzChildViewHolder(mInflater.inflate(
                childType == CHILD_USER_TYPE ? R.layout.item_orgnz_child
                                             : childType == CHILD_ORGANZ_TYPE
                                               ? R.layout.item_orgnz_child_rv : 0, child, false))
        {

            @Override
            public int[] onRegisterClickEvent() {
                return new int[]{R.id.edit};
            }

            @Override
            public void onExpandableItemClick(ChildViewHolder childViewHolder, View v,
                                              OrgnzParent parent, OrgnzChild child,
                                              int parentPosition, int childPosition)
            {
                Logger.e(TAG, "CHILD_onExpandableItemClick");
                final int id = v.getId();
                if (id == R.id.edit) {
                    Intent userEditIntent = new Intent(mCtxt, OrganzUserEditActivity.class);
                    Bundle args = new Bundle();
                    args.putString(Constant.KEY_ACTION_TYPE, Constant.VALUE_TYPE_R);
                    args.putString(Constant.KEY_ID, child.getId());
                    userEditIntent.putExtras(args);
                    mCtxt.startActivity(userEditIntent);
                }
            }
        };
    }

    @Override
    public void onBindParentViewHolder(CommuOrgnzParentViewHolder parentViewHolder,
                                       int parentPosition, OrgnzParent parent)
    {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(CommuOrgnzChildViewHolder childViewHolder, int parentPosition,
                                      int childPosition, OrgnzChild child)
    {
        final int type = getChildType(parentPosition, childPosition);
        if (type == CHILD_USER_TYPE) childViewHolder.bind(child);
        else if (type == CHILD_ORGANZ_TYPE) setupInnerRv(childViewHolder,child);
    }

    @Override
    public int getChildType(int parentPosition, int childPosition) {
        return getChild(parentPosition, childPosition).getType();
    }

    private void setupInnerRv(CommuOrgnzChildViewHolder childViewHolder, OrgnzChild child) {
        Logger.e(TAG, "onBindChildViewHolder");
        RecyclerView rv = childViewHolder.getView(R.id.recyclerView);
        CommuOrgnzInnerAdapter adapter = new CommuOrgnzInnerAdapter(mCtxt,
                new ArrayList<>(Collections.singletonList(child)));
        rv.setAdapter(adapter);
        adapter.addParentExpandCollapseListener(new OnParentExpandCollapseListener() {

            @Override
            public void onParentExpanded(ParentViewHolder pvh, int parentPosition, boolean pendingCause, boolean byUser) {
                if (pvh == null) return;
                final ImageView arrow = pvh.getView(R.id.indicator);
                final float currRotate=arrow.getRotation();
                //重置为从0开始旋转
                if (currRotate == 360) {
                    arrow.setRotation(0);
                }
                arrow.animate().rotation(180).setDuration(300).start();
            }

            @Override
            public void onParentCollapsed(ParentViewHolder pvh, int parentPosition, boolean pendingCause, boolean byUser) {
                if (pvh == null) return;
                final ImageView arrow = pvh.getView(R.id.indicator);
                final float currRotate = arrow.getRotation();
                float rotate = 360;
                //未展开完全并且当前旋转角度小于180，逆转回去
                if (currRotate < 180) {
                    rotate = 0;
                }
                arrow.animate().rotation(rotate).setDuration(300).start();
            }
        });
    }

    public void insertParent(OrgnzParent parent) {
        if (parent == null) return;
        mParents.add(parent);
        notifyParentItemInserted(mParents.size() - 1);
    }

    public void insertParents(List<OrgnzParent> parent) {
        if (parent == null) return;
        mParents.addAll(parent);
        notifyParentItemRangeInserted(mParents.size() - parent.size(), parent.size());
    }
}
