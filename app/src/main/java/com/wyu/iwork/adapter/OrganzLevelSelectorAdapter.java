package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.github.huajianjiang.baserecyclerview.viewholder.BaseViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrganzSelectorItem;

/**
 * @author HuaJian Jiang.
 *         Date 2016/12/28.
 */
public class OrganzLevelSelectorAdapter extends BaseAdapter<BaseViewHolder,OrganzSelectorItem> {
    private static final String TAG = OrganzLevelSelectorAdapter.class.getSimpleName();
    private int checkedPos = -1;

    public OrganzLevelSelectorAdapter(Context context) {
        super(context);
    }

    public int getCheckedPos() {
        return checkedPos;
    }

    public OrganzSelectorItem getCheckedItem() {
        return checkedPos >= 0 ? getItem(checkedPos) : null;
    }

    @Override
    protected void onBindViewHolder(BaseViewHolder vh, OrganzSelectorItem item, int position) {
        if (item == null) return;
        ((TextView) vh.getView(R.id.name)).setText(item.getDepartment());
    }

    @Override
    public BaseViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater.inflate(R.layout.item_organz_selector_parent, parent, false)) {
            @Override
            public void onItemClick(BaseViewHolder vh, View v, int adapterPosition) {
                if (checkedPos != adapterPosition) {
                    if (checkedPos != -1) {
                        ((CheckBox) ((BaseViewHolder) ((RecyclerView) parent).findViewHolderForAdapterPosition(checkedPos)).getView(R.id.checker)).setChecked(false);
                    }
                    ((CheckBox) vh.getView(R.id.checker)).setChecked(true);
                    checkedPos = adapterPosition;
                }
            }
        };
    }
}
