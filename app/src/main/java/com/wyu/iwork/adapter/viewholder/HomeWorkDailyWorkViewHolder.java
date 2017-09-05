package com.wyu.iwork.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.HomeDailyModel;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.view.activity.AddDailyActivity;
import com.wyu.iwork.view.activity.DetailDailyReportActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/27.10:11
 * 邮箱：2587294424@qq.com
 */

public class HomeWorkDailyWorkViewHolder extends HomeWorkViewHolder {

    //查看详情
    @BindView(R.id.item_home_work_daily_work_add)
    TextView detail;

    //无数据时的默认布局
    @BindView(R.id.item_home_work_daily_work_nodata)
    LinearLayout nodata;

    //提示语
    @BindView(R.id.text_layout_nodata_prompt)
    TextView prompt;

    //标题
    @BindView(R.id.item_home_work_daily_work_title)
    TextView title;

    //内容
    @BindView(R.id.item_home_work_daily_work_content)
    TextView mcontent;

    //发布人
    @BindView(R.id.item_home_work_daily_work_add_releaser)
    TextView releaser;

    //日期
    @BindView(R.id.item_home_work_daily_work_date)
    TextView date;


    private HomeDailyModel item ;

    public HomeWorkDailyWorkViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(List t, int position) {

        if (t!=null&& t.size()>0){
            nodata.setVisibility(View.GONE);
            item = (HomeDailyModel) t.get(0);
            mcontent.setText(item.getFinish_work());
            releaser.setText(item.getUser_name());
            date.setText(item.getAdd_time());

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  Intent intent = new Intent(mcontext,DetailDailyReportActivity.class);
                    intent.putExtra("daily_id",item.getDaily_id());
                    mcontext.startActivity(intent);
                }
            });
        }else {
            nodata.setVisibility(View.VISIBLE);
            prompt.setText("暂无工作日报");
            detail.setVisibility(View.GONE);

            detail.setText("添加工作日报");
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CustomUtils.showDialogForBusiness(mcontext)) {
                        Intent intent = new Intent(mcontext, AddDailyActivity.class);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }
    }

}