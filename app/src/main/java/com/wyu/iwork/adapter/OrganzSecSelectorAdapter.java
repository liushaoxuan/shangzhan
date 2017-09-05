package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.huajianjiang.baserecyclerview.viewholder.BaseViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrganzManageBase;
import com.wyu.iwork.model.UserOrganzChild;
import com.wyu.iwork.model.UserOrganzParent;
import com.wyu.iwork.util.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzSecSelectorAdapter extends
        ExpandableAdapter<ParentViewHolder, ChildViewHolder, UserOrganzParent, UserOrganzChild>
{
    private static final String TAG = OrganzSecSelectorAdapter.class.getSimpleName();
    private Context mCtxt;
    private LayoutInflater mInflater;
    private List<UserOrganzParent> mParents;
    private String checkedItemType;
    private int checkedParentPos = -1;
    private int checkedChildPos = -1;
    private int mCheckedParentAdapterPos = -1;
    private int mCheckedChildAdapterPos = -1;

    public OrganzSecSelectorAdapter(Context context) {
        this(context, null);
    }

    public OrganzSecSelectorAdapter(Context ctxt, List<UserOrganzParent> parents) {
        super(parents == null ? parents = new ArrayList<>() : parents);
        mCtxt = ctxt;
        mInflater = LayoutInflater.from(ctxt);
        mParents = parents;
    }

    public OrganzManageBase getCheckedItem() {
        OrganzManageBase item = null;
        if (checkedItemType.equals(Constant.VALUE_TYPE_ITEM_PARENT) && checkedParentPos >= 0) {
            item = getParent(checkedParentPos);
        } else if (checkedItemType.equals(Constant.VALUE_TYPE_ITEM_CHILD) &&
                   checkedParentPos >= 0 &&
                   checkedChildPos >= 0)
        {
            item = getChild(checkedParentPos, checkedChildPos);
        }
        return item;
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(final ViewGroup parent, int parentType) {
        return new ParentViewHolder<UserOrganzParent>(
                mInflater.inflate(R.layout.item_organz_selector_parent, parent, false))
        {
            @Override
            public void onExpandableItemClick(ParentViewHolder pvh, View v,
                                              UserOrganzParent p, Void child,
                                              int parentPosition, int childPosition)
            {
                final int parentAdapterPos = getAdapterPosition();
                if (mCheckedParentAdapterPos != parentAdapterPos) {

                    if (mCheckedChildAdapterPos != -1) {
                        ((CheckBox) ((BaseViewHolder) ((RecyclerView) parent)
                                .findViewHolderForAdapterPosition(mCheckedChildAdapterPos))
                                .getView(R.id.checker)).setChecked(false);
                        mCheckedChildAdapterPos = -1;
                    } else if (mCheckedParentAdapterPos != -1) {
                        ((CheckBox) ((BaseViewHolder) ((RecyclerView) parent)
                                .findViewHolderForAdapterPosition(mCheckedParentAdapterPos))
                                .getView(R.id.checker)).setChecked(false);
                    }

                    ((CheckBox) pvh.getView(R.id.checker)).setChecked(true);
                    mCheckedParentAdapterPos = parentAdapterPos;
                    checkedParentPos = parentPosition;
                }
                checkedItemType = Constant.VALUE_TYPE_ITEM_PARENT;
            }
        };
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(final ViewGroup child, int childType) {
        return new ChildViewHolder<UserOrganzParent, UserOrganzChild>(
                mInflater.inflate(R.layout.item_organz_selector_child, child, false))
        {
            @Override
            public void onExpandableItemClick(ChildViewHolder childViewHolder, View v,
                                              UserOrganzParent parent, UserOrganzChild c,
                                              int parentPosition, int childPosition)
            {

                final int adapterPos = getAdapterPosition();
                if ( mCheckedChildAdapterPos != adapterPos) {

                    // 清除 parent 勾选状态
                    if (mCheckedParentAdapterPos != -1) {
                        ((CheckBox) ((BaseViewHolder) ((RecyclerView) child)
                                .findViewHolderForAdapterPosition(mCheckedParentAdapterPos))
                                .getView(R.id.checker)).setChecked(false);
                        mCheckedParentAdapterPos = -1;
                    } else if (mCheckedChildAdapterPos != -1) {
                        ((CheckBox) ((BaseViewHolder) ((RecyclerView) child)
                                .findViewHolderForAdapterPosition(mCheckedChildAdapterPos))
                                .getView(R.id.checker)).setChecked(false);
                    }

                    ((CheckBox) childViewHolder.getView(R.id.checker)).setChecked(true);
                    mCheckedChildAdapterPos = adapterPos;
                    checkedParentPos = parentPosition;
                    checkedChildPos = childPosition;
                }
                checkedItemType = Constant.VALUE_TYPE_ITEM_CHILD;
            }
        };
    }

    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int parentPosition,
                                       UserOrganzParent parent)
    {
        if (parent == null) return;
        ((TextView) parentViewHolder.getView(R.id.name)).setText(parent.getDepartment());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition,
                                      int childPosition, UserOrganzChild child)
    {
        if (child == null) return;
        ((TextView) childViewHolder.getView(R.id.name)).setText(child.getDepartment());
    }

    public void insertParents(List<UserOrganzParent> parent) {
        if (parent == null) return;
        mParents.addAll(parent);
        notifyParentItemRangeInserted(mParents.size() - parent.size(), parent.size());
    }
}
