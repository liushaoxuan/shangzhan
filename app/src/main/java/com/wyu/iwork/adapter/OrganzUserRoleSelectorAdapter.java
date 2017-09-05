package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.github.huajianjiang.baserecyclerview.viewholder.BaseViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrganzUserRoleSelectorItem;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzUserRoleSelectorAdapter
        extends BaseAdapter<BaseViewHolder, OrganzUserRoleSelectorItem>
{
    private static final String TAG = OrganzUserRoleSelectorAdapter.class.getSimpleName();
    private int checkedPos = -1;

    public OrganzUserRoleSelectorAdapter(Context context) {
        super(context);
    }

    public OrganzUserRoleSelectorItem getCheckedItem() {
        return checkedPos >= 0 ? getItem(checkedPos) : null;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder vh, OrganzUserRoleSelectorItem item,
                                    int position)
    {
        if (item == null) return;
        ((TextView) vh.getView(R.id.name)).setText(item.getName());
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(R.layout.item_organz_user_role_selector, parent, false)) {
            @Override
            public void onItemClick(BaseViewHolder vh, View v, int adapterPosition) {
                if (checkedPos != adapterPosition) {
                    if (checkedPos != -1) {
                        ((CheckBox) ((BaseViewHolder) ((RecyclerView) parent)
                                .findViewHolderForAdapterPosition(checkedPos))
                                .getView(R.id.checker)).setChecked(false);
                    }
                    ((CheckBox) vh.getView(R.id.checker)).setChecked(true);
                    checkedPos = adapterPosition;
                }
            }
        };
    }
}
