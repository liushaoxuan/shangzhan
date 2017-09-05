package com.wyu.iwork.adapter.viewholder;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.adapter.ChildHomeWorkTaskAdapter;
import com.wyu.iwork.adapter.HomeWorkTaskAdapter;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.view.activity.CreateNewTaskActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/27.10:11
 * 邮箱：2587294424@qq.com
 */

public class HomeWorkTaskViewHolder extends HomeWorkViewHolder {
    //任务adapter
    private ChildHomeWorkTaskAdapter taskAdapter;

    @BindView(R.id.item_home_work_task_viewholder_recyclerview)
    RecyclerView recyclerView;

    //添加任务
    @BindView(R.id.item_home_work_task_viewholder_add)
    TextView add;

    //无数据时的默认布局
    @BindView(R.id.item_home_work_task_viewholder_nodata)
    LinearLayout nodata;

    //提示语
    @BindView(R.id.text_layout_nodata_prompt)
    TextView prompt;


    public HomeWorkTaskViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void setData(List t, int position) {
        if (t != null && t.size() > 0) {
            nodata.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
            taskAdapter = new ChildHomeWorkTaskAdapter(mcontext, t);
            recyclerView.setAdapter(taskAdapter);
        } else {
            nodata.setVisibility(View.VISIBLE);
            prompt.setText("暂无任务通知");
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CustomUtils.showDialogForBusiness(mcontext)) {
                    Intent intent = new Intent(mcontext, CreateNewTaskActivity.class);
                    mcontext.startActivity(intent);
                }
            }
        });
    }

}