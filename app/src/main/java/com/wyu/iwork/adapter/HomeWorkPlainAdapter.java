package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.HomeWorkSchedule;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.view.activity.CheckWorkAttendanceActivity;
import com.wyu.iwork.view.activity.ScheduleDetailActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/10.
 * 首页——工作——安排adapter
 */

public class HomeWorkPlainAdapter extends RecyclerView.Adapter<HomeWorkPlainAdapter.ViewHolder> {

    private Context context;
    private List<HomeWorkSchedule> list;
    private boolean show_next_step = false;

    public HomeWorkPlainAdapter(Context context, List<HomeWorkSchedule> list) {
        this.context = context;
        this.list = list;
        if (list==null){
            list = new ArrayList<>();
        }
    }

    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_work_plain,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
                    holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        //时间
        @BindView(R.id.item_home_work_time)
        TextView time;
        //内容
        @BindView(R.id.item_home_work_content)
        TextView content;
        //图片
        @BindView(R.id.item_home_work_imageview)
        ImageView imageview;
        //图片
        @BindView(R.id.item_home_work_imageview_)
        ImageView imageview_;
//        //上部分线
//        @BindView(R.id.item_home_work_upline)
//        View upline;
//        //下部分线
//        @BindView(R.id.item_home_work_dwonline)
//        View dwonline;
        //状态
        @BindView(R.id.child_item_home_work_schedule_state)
        TextView state;
        //状态
        @BindView(R.id.item_home_work_view)
        LinearLayout mview;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        private void setData(int position){
            final HomeWorkSchedule item = list.get(position);

            String statu = item.getStatus()==null?"":item.getStatus();
            state.setVisibility(View.VISIBLE);
            switch (statu){// [status] => 上下午签到状态 0：正常签到 1：迟到 2：早退 9：未打卡 99：非工作日签到
                case "0":
                    state.setText("正常签到");
                    break;
                case "1":
                    state.setText("迟到");
                    break;
                case "2":
                    state.setText("早退");
                    break;
                case "9":
                    state.setText("未打卡");
                    break;

                case "99":
                    state.setText("非工作日签到");
                    break;
                default:
                    state.setVisibility(View.GONE);
                    break;
            }
            content.setText(item.getText());
            time.setText(item.getBegin_time());



            time.setText(item.getBegin_time());
            content.setText(item.getText());
           long currenttime =   System.currentTimeMillis();
            String now_time = DateUtil.getCurrDate2HMS(currenttime);
            Date now_date = DateUtil.String2Date(now_time);
            Date begin_date = DateUtil.String2Date(item.getBegin_time());
            if (!show_next_step){//在没有选中当前时间点的下一个任务时
                if (begin_date.compareTo(now_date)>0){//任务安排的时间大于当前时间时，show_next_step状态设为true
                    show_next_step = true;
                    imageview_.setVisibility(View.VISIBLE);
                    time.setTextColor(content.getResources().getColor(R.color.black3));
                    content.setTextColor(content.getResources().getColor(R.color.black3));
                }else {
                    imageview_.setVisibility(View.GONE);
                    time.setTextColor(content.getResources().getColor(R.color.black6));
                    content.setTextColor(content.getResources().getColor(R.color.black6));
                }
            }else {//已经显示下一个安排时，其他的都显示灰色
                imageview_.setVisibility(View.GONE);
                time.setTextColor(content.getResources().getColor(R.color.black6));
                content.setTextColor(content.getResources().getColor(R.color.black6));
            }

            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    switch (item.getType()){//日程类型 1：工作事务 2：个人事务 3:上午签到 4：下午签到
                        case "1"://1：工作事务
                            intent = new Intent(context,ScheduleDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            context.startActivity(intent);
                            break;
                        case "2"://2：个人事务
                            intent = new Intent(context,ScheduleDetailActivity.class);
                            intent.putExtra("id",item.getId());
                            context.startActivity(intent);
                            break;
                        case "3"://3:上午签到
                            intent = new Intent(context, CheckWorkAttendanceActivity.class);
                            context.startActivity(intent);
                            break;
                        case "4"://4：下午签到
                            intent = new Intent(context, CheckWorkAttendanceActivity.class);
                            context.startActivity(intent);
                            break;
                    }
                }
            });
        }
    }
}
