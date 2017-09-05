package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.model.AssistantModel;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.AprovalAssistantActivity;
import com.wyu.iwork.view.activity.MettingAssistantActivity;
import com.wyu.iwork.view.activity.NoticeAssistantActivity;
import com.wyu.iwork.view.activity.ScheduleAssistantActivity;
import com.wyu.iwork.view.activity.TaskAssistantActivity;
import com.wyu.iwork.view.fragment.HomeMessageFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/4/8.
 * 助手通知adapter
 */

public class AssistantAdapter extends RecyclerView.Adapter<AssistantAdapter.ViewHolder> {
    private Context context;
    private HomeMessageFragment fragment;
    private List<AssistantModel> list;

    public AssistantAdapter(HomeMessageFragment context, List<AssistantModel> list) {
        this.context = context.getActivity();
        this.list = list;
        this.fragment = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_message, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 头像
         */
        @BindView(R.id.item_message_home_head)
        ImageView head;

        /**
         * 昵称
         */
        @BindView(R.id.item_message_chat_name)
        TextView nikeName;
        /**
         * 时间
         */
        @BindView(R.id.item_message_chat_time)
        TextView time;
        /**
         * 内容
         */
        @BindView(R.id.item_message_chat_content)
        TextView content;
        /**
         * 消息数量
         */
        @BindView(R.id.item_message_chat_num)
        TextView messageNum;

        @BindView(R.id.item_message_del)
        TextView del;
        @BindView(R.id.item_message_home_view)
        RelativeLayout view;


        private Intent intent = null;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(final int position) {
            try {
                del.setVisibility(View.GONE);
                final AssistantModel item = list.get(position);
                nikeName.setText(item.getName() + "");
                content.setText(item.getContent());
                if (item.getTime() != 0) {
                    time.setText(DateUtil.ChartTime(item.getTime()));
                }
                messageNum.setText(item.getNotes() + "");
                if (item.getNotes() == 0) {
                    messageNum.setVisibility(View.GONE);
                } else {
                    messageNum.setVisibility(View.VISIBLE);
                }


                switch (item.getId()) {
                    case 1://   * 1  任务助手
                        intent = new Intent(context, TaskAssistantActivity.class);
                        Glide.with(context).load(R.mipmap.icon_message_task).into(head);
                        break;

                    case 2://   * 2   会议通知
                        intent = new Intent(context, MettingAssistantActivity.class);
                        Glide.with(context).load(R.mipmap.icon_message_metting).into(head);

                        break;

                    case 3://   * 3  公告助手
                        intent = new Intent(context, NoticeAssistantActivity.class);
                        Glide.with(context).load(R.mipmap.icon_message_notice).into(head);
                        break;

                    case 4:// * 4  审批助手
                        intent = new Intent(context, AprovalAssistantActivity.class);
                        Glide.with(context).load(R.mipmap.icon_message_aproval).into(head);

                        break;

                    case 5://  日程通知
                        intent = new Intent(context, ScheduleAssistantActivity.class);

                        break;
                    default:
                        break;
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(intent);
                        fragment.updateAssistant(position);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("下标越界", e.getMessage());
            }
        }
    }

}
