package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/6/6.
 */

public class CrmProcessAdapter extends RecyclerView.Adapter<CrmProcessAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater mInflater;
    private String type;

    public CrmProcessAdapter(Context context,String type){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.type = type;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == 1){
            return new ViewHolder(mInflater.inflate(R.layout.item_crm_process_one,parent,false));
        }else if(viewType == 2){
            return new ViewHolder(mInflater.inflate(R.layout.item_crm_process_two,parent,false));
        }else {
            return new ViewHolder(mInflater.inflate(R.layout.item_crm_process_three,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.process_number.setText(position+1+"");
        if (position == 0){
            holder.process_point_first.setVisibility(View.INVISIBLE);
        }else if(position == getItemCount()-1){
            holder.process_point_sencond.setVisibility(View.INVISIBLE);
        }
        if("1".equals(type)){
            setState(0,position,holder);
        }else if("2".equals(type)){
            setState(1,position,holder);
        }else if("3".equals(type)){
            setState(2,position,holder);
        }else if("4".equals(type)){
            setState(3,position,holder);
        }else if("5".equals(type)){
            setState(4,position,holder);
        }else if("6".equals(type)){
            setState(4,position,holder);
        }else if("7".equals(type)){
            setState(4,position,holder);
        }
    }

    private void setState(int item,int position,ViewHolder holder){
        if(position == 0){
            if(item>position){
                setFinishState(holder,"已完成","验证客户","赢率   10%");
            }else if(item == position){
                setworkingState(holder,"验证客户","赢率   10%");
            }else{
                setUnStartState(holder,"未开始","验证客户","赢率   10%");
            }
        }else if(position == 1){
            if(item>position){
                setFinishState(holder,"已完成","需求确定","赢率   30%");
            }else if(item == position){
                setworkingState(holder,"需求确定","赢率   30%");
            }else{
                setUnStartState(holder,"未开始","需求确定","赢率   30%");
            }
        }else if(position == 2){
            if(item>position){
                setFinishState(holder,"已完成","方案报价","赢率   60%");
            }else if(item == position){
                setworkingState(holder,"方案报价","赢率   60%");
            }else{
                setUnStartState(holder,"未开始","方案/报价","赢率   60%");
            }
        }else if(position == 3){
            if(item>position){
                setFinishState(holder,"已完成","谈判审核","赢率   80%");
            }else if(item == position){
                setworkingState(holder,"谈判审核","赢率   80%");
            }else{
                setUnStartState(holder,"未开始","谈判审核","赢率   80%");
            }
        }else if(position == 4){
            if(item>position){
                setFinishState(holder,"已完成","验证客户","赢率   80%");
            }else{
                if("5".equals(type)){
                    holder.process_title.setText("赢单");
                }else if("6".equals(type)){
                    holder.process_title.setText("输单");
                }else if("7".equals(type)){
                    holder.process_title.setText("无效");
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if("5".equals(type) || "6".equals(type) || "7".equals(type)){
            return 5;
        }
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        //1：当前状态在验证客户 需求确定 方案报价 谈判审核阶段
        //2:当前状态在赢单 输单 无效阶段 1：验证客户 2：需求确定 3：方案/报价 4：谈判审核 5：赢单 6：输单 7：无效
        if("1".equals(type) || "2".equals(type) || "3".equals(type) || "4".equals(type)){
            if((position+1+"").equals(type)){
                return 2;
            }else{
                return 1;
            }
        }else{
            if (position<getItemCount()-1){
                return 1;
            }else{
                return 3;
            }
        }
    }

    private void setUnStartState(ViewHolder holder,String textone,String texttwo,String textthree){
        holder.process_number.setTextColor(context.getResources().getColor(R.color.colorGray999999));
        holder.process_number.setBackground(context.getResources().getDrawable(R.mipmap.oval_gray));
        holder.process_content.setBackground(context.getResources().getDrawable(R.drawable.bg_dadada_rect_10px));
        holder.process_state.setText(textone);
        holder.process_state.setTextColor(context.getResources().getColor(R.color.colorGray666666));
        holder.process_title.setText(texttwo);
        holder.process_title.setTextColor(context.getResources().getColor(R.color.colorGray666666));
        holder.process_rate.setTextColor(context.getResources().getColor(R.color.colorGray333333));
        holder.process_rate.setText(textthree);
    }

    private void setFinishState(ViewHolder holder,String textone,String texttwo,String textthree){
        setUnStartState(holder,textone,texttwo,textthree);
    }

    private void setworkingState(ViewHolder holder,String textone,String texttwo){
        holder.process_title.setText(textone);
        holder.process_rate.setText(texttwo);
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        //布局上面的虚线
        @BindView(R.id.process_point_first)
        View process_point_first;

        //排序号
        @BindView(R.id.process_number)
        TextView process_number;

        //布局下面的虚线
        @BindView(R.id.process_point_sencond)
        View process_point_sencond;

        //流程标题
        @BindView(R.id.process_title)
        TextView process_title;

        //赢率
        @BindView(R.id.process_rate)
        TextView process_rate;

        //当前状态
        @BindView(R.id.process_state)
        TextView process_state;

        @BindView(R.id.process_content)
        RelativeLayout process_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
