package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DeleteListener;
import com.wyu.iwork.model.AprovalAssistentModel;
import com.wyu.iwork.util.PopupList;
import com.wyu.iwork.util.SpannableStringUtils;
import com.wyu.iwork.view.activity.oaApplyForLeaveDetailActivity;
import com.wyu.iwork.view.activity.oaBusinessTravelDetailActivity;
import com.wyu.iwork.view.activity.oaReimbursementDetailActivity;
import com.wyu.iwork.view.activity.oaWorkOverTimeDetailActivity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/7/21.11:17
 * 邮箱：2587294424@qq.com
 */

public class AprovalAssistantFragmentAdapter extends RecyclerView.Adapter<AprovalAssistantFragmentAdapter.ViewHolder> {

    private Context context;
    private List<AprovalAssistentModel> list;
//    private DeleteListener listener;

    private List<String> popupMenuItemList = new ArrayList<>();
    private DeleteListener listener;

    /**
     * 触点位置
     */
    private float mRawX;
    private float mRawY;
    public AprovalAssistantFragmentAdapter(Context context, List<AprovalAssistentModel> list) {
        this.context = context;
        this.list = list;
        popupMenuItemList.add("删除");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_aproval_assistant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{

        /**
         * 标题
         */
        @BindView(R.id.item_aproval_assistant_sendby)
        TextView title;

        /**
         * item头部时间
         */
        @BindView(R.id.item_aproval_assistant_time)
        TextView time;

        /**
         * text1
         */
        @BindView(R.id.item_aproval_assistant_times)
        TextView text1;

        /**
         * text2
         */
        @BindView(R.id.item_aproval_assistant_date)
        TextView text2;

        /**
         * 查看详情
         */
        @BindView(R.id.item_aproval_assistant_details)
        LinearLayout details;

        /**
         * 查看详情
         */
        @BindView(R.id.item_aproval_assistant)
        LinearLayout mview;

        /**
         * 图片
         */
        @BindView(R.id.item_aproval_assistant_type_icon)
        ImageView icon;

        private Intent intent = null;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setData(final int position) {

            AprovalAssistentModel item = list.get(position);
            mview.setOnTouchListener(this);
            time.setText(item.getAdd_time());
            String type = item.getType() == null ? "" : item.getType();
            switch (type) {  //类型  1:请假 2:加班 3:出差 4:报销
                case "1"://请假
                    intent = new Intent(context, oaApplyForLeaveDetailActivity.class);
                    icon.setImageResource(R.mipmap.icon_arpoval_leave);
                    text1.setText("请假类型:"+item.getText1());
                    text2.setText("请假时间:"+item.getText2());
                    break;

                case "2"://加班
                    intent = new Intent(context, oaWorkOverTimeDetailActivity.class);
                    icon.setImageResource(R.mipmap.icon_aproval_workon);
                    text1.setText("加班类型:"+item.getText1());
                    text2.setText("加班时间:"+item.getText2());
                    break;

                case "3"://出差
                    intent = new Intent(context, oaBusinessTravelDetailActivity.class);
                    icon.setImageResource(R.mipmap.icon_aproval_traival);
                    text1.setText("出差时长:"+item.getText1());
                    text2.setText("出差时间:"+item.getText2());
                    break;

                case "4"://报销
                    intent = new Intent(context, oaReimbursementDetailActivity.class);
                    icon.setImageResource(R.mipmap.icon_aproval_fee);
                    text1.setText("报销类型:"+item.getText1());
                    text2.setText("报销总计:"+item.getText2());
                    break;
            }

            String content = item.getTitle();
            int num1 = content.indexOf("[");
            int num2 = content.indexOf("]");

            title.setText(SpannableStringUtils.getBuilder("")
                    .append(content.substring(0,num1))
                     .append(content.substring(num1,num2+1)).setForegroundColor(context.getResources().getColor(R.color.blue))
                    .append(content.substring(num2+1,content.length()))
                    .create()
            );

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

            intent.putExtra("id", item.getApply_id());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
