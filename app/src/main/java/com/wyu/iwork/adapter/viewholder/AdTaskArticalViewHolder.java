package com.wyu.iwork.adapter.viewholder;

import android.view.View;
import android.widget.EditText;

import com.wyu.iwork.R;
import com.wyu.iwork.model.AdTaskModel;
import com.zhy.autolayout.utils.AutoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/8/8.15:19
 * 邮箱：2587294424@qq.com
 */

public class AdTaskArticalViewHolder extends AdTaskSettingViewHolder {

    @BindView(R.id.item_ad_task_settings_content)
    EditText editText;
    public AdTaskArticalViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        AutoUtils.autoSize(itemView);
    }

    @Override
    public void setData(AdTaskModel item, int position) {
        editText.setText(item.getMsg());
    }
}
