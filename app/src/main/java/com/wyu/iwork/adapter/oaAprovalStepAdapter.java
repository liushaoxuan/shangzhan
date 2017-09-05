package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.oaAprovalModel;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/6/6.13:42
 * 邮箱：2587294424@qq.com
 *  OA——审批流程Adapter
 *
 */

public class oaAprovalStepAdapter extends RecyclerView.Adapter<oaAprovalStepAdapter.ViewHolder>{

    private Context context;
    private List<oaAprovalModel> list;
    private onItemClickListener  onItemClickListener;

    public oaAprovalStepAdapter(Context context, List<oaAprovalModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_aproval_step,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setDate(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        /**
         *  当前状态的图标
         */
        @BindView(R.id.oa_aproval_step_state_image)
        ImageView stateImage;


        /**
         *  下面的线
         */
        @BindView(R.id.oa_aproval_step_bottom_point)
        View bottom_point;

        /**
         *  用户头像
         */
        @BindView(R.id.oa_aproval_step_head)
        TextView user_head;

        /**
         *  用户昵称
         */
        @BindView(R.id.oa_aproval_step_userName)
        TextView userName;

        /**
         *  当前状态
         */
        @BindView(R.id.oa_aproval_step_state)
        TextView state;

        /**
         *  意见
         */
        @BindView(R.id.oa_aproval_step_isure)
        TextView isure;
        /**
         *  意见
         */
        @BindView(R.id.oa_aproval_step_isure_)
        LinearLayout isure_;

        /**
         *  时间
         */
        @BindView(R.id.oa_aproval_step_stime)
        TextView text_time;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setDate(final int position){
            oaAprovalModel item = list.get(position);
            userName.setText(item.getUser_name());

            if (position==list.size()-1){
                bottom_point.setVisibility(View.GONE);
            }else {
                bottom_point.setVisibility(View.VISIBLE);
            }


            if (item.getContent()!=null&&!"".equals(item.getContent())){
                isure.setText("意见:"+item.getContent());
                isure_.setVisibility(View.VISIBLE);
            }else {
                isure_.setVisibility(View.GONE);
            }
            text_time.setText(item.getCreate_time());
            String status = item.getStatus();

            switch (status){ //审核状态(0:未传达 1:审核中 2:同意 3:驳回 4:已被转交 5:抄送 6:撤销终止 7:自己发起)
                case "0"://未传达
                    stateImage.setImageResource(R.mipmap.oa_aproval_state_unsend);
                    state.setText("");
                    break;

                case "1"://审核中
                    stateImage.setImageResource(R.mipmap.oa_aproval_state_watting);
                    state.setText("审核中");
                    state.setTextColor(context.getResources().getColor(R.color.orange));
                   //  user_head.setVisibility(View.VISIBLE);
                    break;

                case "2"://同意
                    stateImage.setImageResource(R.mipmap.oa_aproval_state_finish);
                    state.setText("已同意");
                    state.setTextColor(context.getResources().getColor(R.color.blue));
                   //  user_head.setVisibility(View.VISIBLE);
                    break;

                case "3"://驳回
                    stateImage.setImageResource(R.mipmap.oa_aproval_state_confuse);
                    state.setText("已拒绝");
                    state.setTextColor(context.getResources().getColor(R.color.text_red));
                   //  user_head.setVisibility(View.VISIBLE);
                    break;

                case "4"://已被转交
                    break;

                case "5"://抄送
                    break;

                case "6"://撤销终止
                    stateImage.setImageResource(R.mipmap.oa_aproval_state_confuse);
                    state.setText("已撤销");
                    state.setTextColor(context.getResources().getColor(R.color.text_red));
                   //  user_head.setVisibility(View.GONE);
                    break;

                case "7"://:自己发起
                    stateImage.setImageResource(R.mipmap.oa_aproval_state_finish);
                    state.setText("发起申请");
                    state.setTextColor(context.getResources().getColor(R.color.blue));
                   //  user_head.setVisibility(View.VISIBLE);
                    break;
            }


            try {
                user_head.setText(item.getUser_name().substring(0,1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }
}
