package com.wyu.iwork.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ParentViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrgnzParent;

/**
 * Created by jhj_Plus on 2016/10/28.
 */
public class CommuOrgnzParentViewHolder extends ParentViewHolder {
    private static final String TAG = "CommuOrgnzParentViewHolder";

    public CommuOrgnzParentViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(OrgnzParent data) {
        if (data == null) return;
        ((TextView) getView(R.id.name)).setText(data.getDepartment_name());
        getView(R.id.indicator).setRotation(isExpanded() ? -180 : -90);
//        getView(R.id.divider).setVisibility(isExpanded() ? View.GONE : View.VISIBLE);
    }
}
