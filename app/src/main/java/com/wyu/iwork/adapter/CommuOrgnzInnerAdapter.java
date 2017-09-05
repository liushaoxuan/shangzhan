package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrgnzChild;
import com.wyu.iwork.net.NetworkManager;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.OrganzUserEditActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/28.
 */
public class CommuOrgnzInnerAdapter
        extends ExpandableAdapter<ParentViewHolder, ChildViewHolder, OrgnzChild, OrgnzChild.User>
{
    private static final String TAG = "CommuOrgnzAdapter";
    private Context mCtxt;
    private LayoutInflater mInflater;
    private List<OrgnzChild> mParents;

    public CommuOrgnzInnerAdapter(Context ctxt) {
        this(ctxt, null);
    }

    public CommuOrgnzInnerAdapter(Context ctxt, List<OrgnzChild> parents) {
        super(parents == null ? parents = new ArrayList<>() : parents);
        mCtxt = ctxt;
        mInflater = LayoutInflater.from(ctxt);
        mParents = parents;
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        return new ParentViewHolder(
                mInflater.inflate(R.layout.item_orgnz_parent_inner, parent, false));
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        return new ChildViewHolder<OrgnzChild, OrgnzChild.User>(
                mInflater.inflate(R.layout.item_orgnz_child, child, false))
        {
            @Override
            public int[] onRegisterClickEvent() {
                Logger.e(TAG,"onRegisterClickEvent");
                return new int[]{R.id.edit};
            }

            @Override
            public void onExpandableItemClick(ChildViewHolder childViewHolder, View v,
                                              OrgnzChild parent, OrgnzChild.User child,
                                              int parentPosition, int childPosition)
            {
                Logger.e(TAG,"CHILD_onExpandableItemClick");
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
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int parentPosition,
                                       OrgnzChild parent)
    {
        parentViewHolder.getView(R.id.indicator)
                .setRotation(parentViewHolder.isExpanded() ? 90 : 0);
        ((TextView) parentViewHolder.getView(R.id.name)).setText(parent.getDepartment_name());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition,
                                      int childPosition, OrgnzChild.User child)
    {

        ((TextView) childViewHolder.getView(R.id.name)).setText(child.getName());
        NetworkManager.getInstance(mCtxt)
                .setNetImage(child.getFace_img(), (ImageView) childViewHolder.getView(R.id.avatar));
    }

}
