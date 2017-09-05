package com.wyu.iwork.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.huajianjiang.expandablerecyclerview.widget.ChildViewHolder;
import com.wyu.iwork.R;
import com.wyu.iwork.model.OrgnzChild;
import com.wyu.iwork.model.OrgnzParent;
import com.wyu.iwork.net.NetworkManager;

/**
 * Created by jhj_Plus on 2016/10/28.
 */
public class CommuOrgnzChildViewHolder extends ChildViewHolder<OrgnzParent, OrgnzChild> {
    private static final String TAG = "CommuOrgnzChildViewHolder";

    public CommuOrgnzChildViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(OrgnzChild data) {
        if (data == null) return;
        ((TextView) getView(R.id.name)).setText(data.getName());
        NetworkManager.getInstance(itemView.getContext())
                .setNetImage(data.getFace_img(), (ImageView) getView(R.id.avatar));
    }
}
