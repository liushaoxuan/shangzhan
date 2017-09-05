package com.wyu.iwork.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.TaskModel;
import com.wyu.iwork.widget.MyProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by lx on 2016/12/19.
 */

public class TaskArrangeAdapter extends android.widget.BaseAdapter {
    private static final String TAG="ScheduleAdapter";
    private ViewHolder vh;
    private Context context;
    private List<TaskModel>list=new ArrayList<>();

    public TaskArrangeAdapter(Context context) {
        this.context = context;
    }

    public TaskArrangeAdapter(Context context, List<TaskModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            vh=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_taskarrangement,viewGroup,false);
            vh.image=(CircleImageView)view.findViewById(R.id.item_userheadimg);
            vh.tv_taskName=(TextView)view.findViewById(R.id.item_tasknname);
            vh.tv_userName=(TextView)view.findViewById(R.id.item_username);
            vh.tv_cycle=(TextView)view.findViewById(R.id.item_taskCycle) ;
            vh.myProgressBar=(MyProgressBar)view.findViewById(R.id.item_taskProgress);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        Glide.with(context).load(list.get(i).getFace_img()).dontAnimate().placeholder(R.mipmap.ic_noimage).into(vh.image);
        vh.tv_userName.setText(list.get(i).getReal_name());
        vh.tv_taskName.setText(list.get(i).getTask());
        String begin=list.get(i).getBegin_time();
        String end=list.get(i).getEnd_time();
        SimpleDateFormat Times=new SimpleDateFormat("yyyy-MM-dd HH:mm:s");
        Date currentTime = new Date();// 当前时间
        String CurrentTime=Times.format(currentTime);
        try {
            Date Current=Times.parse(CurrentTime);
            Date start=Times.parse(begin);
            Date stop=Times.parse(end);
            int total= (int) ((stop.getTime()-start.getTime())/86400000);
            int current = (int) ((Current.getTime()-start.getTime())/86400000);
            if(total<1){
                total=1;
            }
            double pro=(Float.valueOf(current)/Float.valueOf(total))*100;
            int progress= (int) pro;
            if(progress>100){
                vh.myProgressBar.setProgress(100);
            }else{
                vh.myProgressBar.setProgress(progress);
            }
            if(current>total){
                current=total;
            }
            vh.tv_cycle.setText("任务周期"+current+"/"+total+"天");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (i % 2 == 0) {
            view.setBackgroundResource(R.drawable.listitemcolor1);
        } else {
            view.setBackgroundResource(R.drawable.listitemcolor2);
        }
        return view;
    }

    class ViewHolder{
        CircleImageView image;
        TextView tv_taskName,tv_userName,tv_cycle;
        MyProgressBar myProgressBar;
    }
}
