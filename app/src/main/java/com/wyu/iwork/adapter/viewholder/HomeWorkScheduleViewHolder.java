package com.wyu.iwork.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.HomeWorkPlainAdapter;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.view.activity.CreateScheduleActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/27.10:11
 * 邮箱：2587294424@qq.com
 */

public class HomeWorkScheduleViewHolder extends HomeWorkViewHolder {

    //日程安排adapter
    private HomeWorkPlainAdapter plainAdapter;

    @BindView(R.id.item_home_work_schedule_recyclerview)
    RecyclerView recyclerView;

    //添加日程
    @BindView(R.id.item_home_work_schedule_add)
    TextView add;

    //无数据时的默认布局
    @BindView(R.id.item_home_work_schedule_nodata)
    LinearLayout nodata;

    //提示语
    @BindView(R.id.text_layout_nodata_prompt)
    TextView prompt;


    public HomeWorkScheduleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    @Override
    public void setData(List t, int position) {
        nodata.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        plainAdapter = new HomeWorkPlainAdapter(mcontext,t);
        recyclerView.setAdapter(plainAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CustomUtils.showDialogForBusiness(mcontext)) {
                    Intent intent = new Intent(mcontext, CreateScheduleActivity.class);
                    mcontext.startActivity(intent);
                }
            }
        });

        if (t==null||t.size()==0){
            nodata.setVisibility(View.VISIBLE);
            prompt.setText("暂无日程安排");
        }

    }
}