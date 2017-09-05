package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.model.AttendanceModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/6/5.17:01
 * 邮箱：2587294424@qq.com
 */

public class AttendanceStatisticsAdapter extends RecyclerView.Adapter<AttendanceStatisticsAdapter.ViewHolder> {

    private Context mcontext;

    private List<AttendanceModel> list;

    public AttendanceStatisticsAdapter(Context mcontext, List<AttendanceModel> list) {
        this.mcontext = mcontext;
        this.list = list;
        if (this.list==null){
            this.list = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_activity_attendance_statistics,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 员工名称
         */
        @BindView(R.id.item_attendance_statistics_name)
        TextView user_name;

        /**
         * 出勤数
         */
        @BindView(R.id.item_attendance_statistics_chuqin)
        TextView count_chuqin;

        /**
         * 缺勤数
         */
        @BindView(R.id.item_attendance_statistics_queqin)
        TextView count_queqin;

        /**
         * 迟到数
         */
        @BindView(R.id.item_attendance_statistics_chidao)
        TextView count_chidao;

        /**
         * 早退数
         */
        @BindView(R.id.item_attendance_statistics_zaotui)
        TextView count_zaotui;

        /**
         * 加班数
         */
        @BindView(R.id.item_attendance_statistics_jiaban)
        TextView count_jiaban;

        /**
         * 出差数
         */
        @BindView(R.id.item_attendance_statistics_chuchai)
        TextView count_chuchai;

        /**
         * 出差数
         */
        @BindView(R.id.item_attendance_statistics_qingjia)
        TextView count_qingjia;

        /**
         *
         * @param itemView
         */

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setData(int position){

            AttendanceModel item = list.get(position);

            user_name.setText(item.getName());
            count_chuqin.setText(item.getAttendance());
            count_queqin.setText(item.getAbsence());
            count_chidao.setText(item.getLate());
            count_zaotui.setText(item.getEarly());
            count_chuchai.setText(item.getWork_on_business());
            count_qingjia.setText(item.getWork_leave());
            count_jiaban.setText(item.getWork_over());

        }
    }
}
