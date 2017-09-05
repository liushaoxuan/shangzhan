package com.wyu.iwork.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter;
import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.UniversalCallback;
import com.wyu.iwork.model.OrganzInfo;
import com.wyu.iwork.model.OrganzManageChild;
import com.wyu.iwork.model.OrganzManageParent;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.OrganzEditActivity;
import com.wyu.iwork.view.dialog.MsgDialog;
import com.wyu.iwork.view.fragment.OrganzManageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/22.
 */
public class OrganzManageAdapter extends
        ExpandableAdapter<ParentViewHolder, ChildViewHolder, OrganzManageParent, OrganzManageChild>
{
    private static final String TAG = OrganzManageAdapter.class.getSimpleName();
    private OrganzManageFragment mCtxt;
    private LayoutInflater mInflater;
    private List<OrganzManageParent> mParents;

    public OrganzManageAdapter(OrganzManageFragment ctxt) {
        this(ctxt, null);
    }

    public OrganzManageAdapter(OrganzManageFragment ctxt, List<OrganzManageParent> parents) {
        super(parents == null ? parents = new ArrayList<>() : parents);
        mCtxt = ctxt;
        mInflater = LayoutInflater.from(ctxt.getContext());
        mParents = parents;
    }

    @Override
    public ParentViewHolder onCreateParentViewHolder(ViewGroup parent, int parentType) {
        return new ParentViewHolder<OrganzManageParent>(
                mInflater.inflate(R.layout.item_organz_manage_parent, parent, false))
        {
            @Override
            public int[] onRegisterClickEvent() {
                return new int[]{R.id.edit, R.id.delete};
            }

            @Override
            public void onExpandableItemClick(ParentViewHolder pvh, View v,
                                              OrganzManageParent parent, Void child,
                                              int parentPosition, int childPosition)
            {
                Logger.e(TAG, "Parent_onExpandableItemClick");
                if (parent == null) return;
                switch (v.getId()) {
                    case R.id.edit:
                        doEdit(parent.getDepartment_id());
                        return;
                    case R.id.delete:
                        doDelete(parent.getDepartment_id(), Constant.VALUE_TYPE_ITEM_PARENT,
                                parentPosition, -1);
                        break;
                }
            }
        };
    }


    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup child, int childType) {
        return new ChildViewHolder<OrganzManageParent, OrganzManageChild>(
                mInflater.inflate(R.layout.item_organz_manage_child, child, false))
        {
            @Override
            public int[] onRegisterClickEvent() {
                return new int[]{R.id.edit, R.id.delete};
            }

            @Override
            public void onExpandableItemClick(ChildViewHolder cvh, View v,
                                              OrganzManageParent parent, OrganzManageChild child,
                                              int parentPosition, int childPosition)
            {
                Logger.e(TAG, "Child_onExpandableItemClick");
                if (child == null) return;
                switch (v.getId()) {
                    case R.id.edit:
                        doEdit(child.getDepartment_id());
                        return;
                    case R.id.delete:
                        doDelete(parent.getDepartment_id(), Constant.VALUE_TYPE_ITEM_CHILD,
                                parentPosition, childPosition);
                        break;
                }
            }
        };
    }

    private void doEdit(String id) {
        Bundle args = new Bundle();
        OrganzInfo organzInfo = new OrganzInfo();
        organzInfo.setDepartment_id(id);
        Intent addIntent = new Intent(mCtxt.getActivity(), OrganzEditActivity.class);
        args.putSerializable(Constant.KEY_ENTITY, organzInfo);
        addIntent.putExtras(args);
        mCtxt.startActivity(addIntent);
    }

    private void doDelete(final String id, final String type, final int parentPos,
                          final int childPos)
    {
        MsgDialog msgDialog = new MsgDialog();
        Bundle dialogArgs = new Bundle();
        dialogArgs.putInt(Constant.KEY_MSG, R.string.msg_dialog_delete);
        msgDialog.setArguments(dialogArgs);
        msgDialog.show(mCtxt.getActivity().getSupportFragmentManager(), null);
        msgDialog.setCallback(new UniversalCallback() {
            @Override
            public Object onFinished(Object... backParams) {
                if ((Integer)backParams[0] == DialogInterface.BUTTON_POSITIVE) {
                    Bundle deleteArgs = new Bundle();
                    deleteArgs.putString(Constant.KEY_ID, id);
                    deleteArgs.putString(Constant.KEY_ITEM_TYPE, type);
                    deleteArgs.putInt(Constant.KEY_PARENT_POS, parentPos);
                    deleteArgs.putInt(Constant.KEY_CHILD_POS, childPos);
                    mCtxt.deleteOrganz(deleteArgs);
                }
                return null;
            }
        });
    }


    @Override
    public void onBindParentViewHolder(ParentViewHolder parentViewHolder, int parentPosition,
                                       OrganzManageParent parent)
    {
        ((TextView) parentViewHolder.getView(R.id.name)).setText(parent.getDepartment());
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder childViewHolder, int parentPosition,
                                      int childPosition, OrganzManageChild child)
    {
        ((TextView) childViewHolder.getView(R.id.name)).setText(child.getDepartment());
    }

    public void insertParent(OrganzManageParent parent) {
        if (parent == null) return;
        mParents.add(parent);
        notifyParentItemInserted(mParents.size() - 1);
    }

    public void insertParents(List<OrganzManageParent> parent) {
        if (parent == null) return;
        mParents.addAll(parent);
        notifyParentItemRangeInserted(mParents.size() - parent.size(), parent.size());
    }

    public void insertChild(int parentPos, OrganzManageChild child) {
        if (child == null) return;
        List<OrganzManageChild> children = checkChildren(parentPos);
        children.add(child);
        notifyChildItemInserted(parentPos, children.indexOf(child));
    }

    public void insertChildren(int parentPos, List<OrganzManageChild> children) {
        if (children == null || children.isEmpty()) return;
        List<OrganzManageChild> currChildren = checkChildren(parentPos);
        currChildren.addAll(children);
        notifyChildItemRangeInserted(parentPos, currChildren.indexOf(children.get(0)),
                children.size());
    }

    public void removeParent(int parentPos) {
        mParents.remove(parentPos);
        notifyParentItemRemoved(parentPos);
    }

    public void removeChild(int parentPos, int childPos) {
        List<OrganzManageChild> children = checkChildren(parentPos);
        children.remove(childPos);
        notifyChildItemRemoved(parentPos, childPos, false);
    }

    private List<OrganzManageChild> checkChildren(int parentpos) {
        List<OrganzManageChild> children = mParents.get(parentpos).getChildren();
        return children == null ? new ArrayList<OrganzManageChild>() : children;
    }
}
