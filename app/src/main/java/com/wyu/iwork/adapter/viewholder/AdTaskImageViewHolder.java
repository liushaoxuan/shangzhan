package com.wyu.iwork.adapter.viewholder;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.AdTaskModel;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/8/8.15:19
 * 邮箱：2587294424@qq.com
 */

public class AdTaskImageViewHolder extends AdTaskSettingViewHolder {

    @BindView(R.id.item_ad_task_settings_imageview)
    ImageView imageView;
    @BindView(R.id.item_ad_task_image)
    RelativeLayout mview;
    @BindView(R.id.item_ad_task_image_camere_layout)
    LinearLayout cameralayout;
    @BindView(R.id.item_ad_task_progressbar_layout)
    LinearLayout barlayout;

    public AdTaskImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        AutoUtils.autoSize(itemView);
    }

    @Override
    public void setData(AdTaskModel item, final int position) {

        if (!item.getLocal_path().equals(mview.getTag())){
            mview.setTag(item.getLocal_path());
            Glide.with(mcontext).load(item.getLocal_path()).into(imageView);
        }
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.showGetPicPop(position,cameralayout,barlayout);
            }
        });
    }
}
