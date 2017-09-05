package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DeleteListener;
import com.wyu.iwork.model.MessageNoticeAssistanBean;
import com.wyu.iwork.util.PopupList;
import com.wyu.iwork.view.activity.NoticeAssistantActivity;
import com.wyu.iwork.view.activity.WebActivity;
import com.wyu.iwork.view.dialog.DelPopWindown;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/12.17:37
 * 邮箱：2587294424@qq.com
 * 消息助手adapter
 */

public class NoticeAssistantAdapter extends RecyclerView.Adapter<NoticeAssistantAdapter.ViewHolder>{

    private NoticeAssistantActivity context;

    private List<MessageNoticeAssistanBean> list = new ArrayList();
    private List<String> popupMenuItemList = new ArrayList<>();
    private DeleteListener listener;
    /**
     * 触点位置
     */
    private float mRawX;
    private float mRawY;
    public NoticeAssistantAdapter(NoticeAssistantActivity context,List list) {
        this.context = context;
        this.list = list;
        popupMenuItemList.add("删除");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assistant,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setdata(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

        /**
         * 时间
         */
        @BindView(R.id.item_assistant_addtime)
        TextView addTime;
        /**
         * 标题
         */
        @BindView(R.id.item_assistant_title)
        TextView title;
        /**
         * view
         */
        @BindView(R.id.item_assistant)
        AutoRelativeLayout mview;

        /**
         * 内容
         */
        @BindView(R.id.item_assistant_content)
        TextView content;

        /**
         * 来源
         */
        @BindView(R.id.item_assistant_from)
        TextView from;

        /**
         * 图标
         */
        @BindView(R.id.item_assistant_icon)
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void setdata(final int position){
            from.setText("来自公告助手");
            mview.setOnTouchListener(this);
           final  MessageNoticeAssistanBean item = list.get(position);

            title.setText(item.getTitle());
            content.setText(item.getContent());
            addTime.setText(item.getAdd_time());
            icon.setImageResource(R.mipmap.icon_message_notice);

            //长按事件
            mview.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    PopupList popupList = new PopupList(view.getContext());
                    popupList.showPopupListWindow(view, position, mRawX, mRawY, popupMenuItemList, new PopupList.PopupListListener() {
                        @Override
                        public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                            return true;
                        }

                        @Override
                        public void onPopupListClick(View contextView, int contextPosition, int po) {
                            if (listener!=null){
                                listener.onDeleteListener(contextView,position);
                            }
                        }
                    });
                    return true;
                }
            });

            //点击事件
            mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url",item.getUrl());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mRawX = event.getRawX();
            mRawY = event.getRawY();
            return false;
        }
    }

    public void setOnDeletListener(DeleteListener listener){
        this.listener = listener;
    }

}
