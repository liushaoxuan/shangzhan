package com.wyu.iwork.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wyu.iwork.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lx on 2017/4/7.
 */

public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private int weekday;
    private ArrayList<Integer> calendarList;
    private HashMap<String,Integer> stateList;
    //状态码
    //其他
    private static final int CODE_OTHER = -1;
    //正常
    private static final int CODE_NORMAL = 0;
    //迟到
    private static final int CODE_LATE = 1;
    //早退
    private static final int CODE_LEAVE_EARLY = 2;
    //缺勤
    private static final int CODE_ABSENCE = 3;
    //出差
    private static final int CODE_BUSINESS = 4;
    //请假
    private static final int CODE_VACATE = 5;
    //加班
    private static final int CODE_OVERTIME = 6;
    //非工作日
    private static final int CODE_HOLIDAY = 7;
    private String day = "";

    public CalendarAdapter(Context context, int weekday, ArrayList<Integer> calendarList,HashMap<String,Integer> stateList){
        this.context = context;
        this.weekday = weekday;
        this.calendarList = calendarList;
        this.stateList = stateList;
    }

    @Override
    public int getCount() {
        return calendarList.size();
    }

    @Override
    public Integer getItem(int position) {
        return calendarList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return calendarList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_calendar,null);
            holder = new ViewHolder();
            holder.tv_calendar = (TextView) convertView.findViewById(R.id.tv_calendar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(getItem(position)>0 && getItem(position)<10){
            day = "0" + getItem(position);
        }else{
            day = getItem(position)+"";
        }
        if(getItem(position) == -1){
            holder.tv_calendar.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else if(stateList.containsKey(day)){
            if(stateList.get(day) == CODE_LATE){
                //迟到
                holder.tv_calendar.setText(getItem(position)+"");
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_calendar.setBackground(context.getResources().getDrawable(R.drawable.calendar_red));
            }else if(stateList.get(day) == CODE_BUSINESS){
                //出差
                holder.tv_calendar.setText(getItem(position)+"");
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_calendar.setBackground(context.getResources().getDrawable(R.drawable.calendar_blue));
            }else if(stateList.get(day) == CODE_LEAVE_EARLY){
                //早退
                holder.tv_calendar.setText(getItem(position)+"");
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_calendar.setBackground(context.getResources().getDrawable(R.drawable.calendar_pink));
            }else if(stateList.get(day) == CODE_VACATE){
                //请假
                holder.tv_calendar.setText(getItem(position)+"");
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_calendar.setBackground(context.getResources().getDrawable(R.drawable.calendar_green));
            }else if(stateList.get(day) == CODE_OVERTIME){
                //加班
                holder.tv_calendar.setText(getItem(position)+"");
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_calendar.setBackground(context.getResources().getDrawable(R.drawable.calendar_orange));
            }else{
                //正常
                holder.tv_calendar.setText(getItem(position)+"");
                holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.colorGray585858));
                holder.tv_calendar.setBackgroundColor(context.getResources().getColor(R.color.white));
            }
        }else{
            //正常
            holder.tv_calendar.setText(getItem(position)+"");
            holder.tv_calendar.setTextColor(context.getResources().getColor(R.color.colorGray585858));
            holder.tv_calendar.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    class ViewHolder{
        TextView tv_calendar;
    }
}
