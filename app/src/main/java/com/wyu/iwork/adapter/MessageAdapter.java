package com.wyu.iwork.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.UserModel;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by lx on 2017/4/8.
 * 消息adapter
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<Conversation> list;
    private List<UserModel> users;

    public MessageAdapter(Context context,List<UserModel> users, List<Conversation> list) {
        this.context = context;
        this.list = list;
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_home_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        if (users==null){
            return 0;
        }
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        /**
         * 头像
         */
        @BindView(R.id.item_message_scrollview)
        HorizontalScrollView scrollView;
        /**
         * 头像
         */
        @BindView(R.id.item_message_home_head)
        CircleImageView head;

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
        /**
         * 删除
         */
        @BindView(R.id.item_message_del)
        TextView del;
        @BindView(R.id.item_message_home_view)
        RelativeLayout view;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        private void setData(final int position){
            scrollView.scrollTo(0,0);
            try {
                final Conversation item = list.get(position);
                final UserModel user_item = users.get(position);
                nikeName.setText(user_item.getUser_name());
                String lastmessage = "";
                if (item.getLatestMessage().getSearchableWord()!=null&&item.getLatestMessage().getSearchableWord().size()>0){
                    lastmessage = item.getLatestMessage().getSearchableWord().get(0);
                }

                if ("RC:VcMsg".equals(item.getObjectName())){
                    lastmessage = "[语音消息]";
                }
                content.setText(lastmessage);
                messageNum.setText(item.getUnreadMessageCount()+"");
                time.setText(DateUtil.ChartTime(item.getSentTime()));
                if (0 == item.getUnreadMessageCount()){
                    messageNum.setVisibility(View.GONE);
                }else {
                    messageNum.setVisibility(View.VISIBLE);
                }
                Glide.with(context).load(user_item.getFace_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).into(head);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setUser(user_item);
                        RongIM.getInstance().startPrivateChat(context,user_item.getId(),user_item.getUser_name());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("下标越界",e.getMessage());
            }
                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeConversation(list.get(position).getTargetId(),position);
                    }
                });
        }
    }
    private void setUser(final UserModel item) {

        //设置用户信息
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public io.rong.imlib.model.UserInfo getUserInfo(String s) {
                Uri uri = Uri.parse(item.getFace_img());
                io.rong.imlib.model.UserInfo rong_userInfo = new io.rong.imlib.model.UserInfo(item.getId(), item.getUser_name(), uri);
                return rong_userInfo;
            }
        }, true);
        /**
         * 刷新用户缓存数据。
         *
         * @param userInfo 需要更新的用户缓存数据。
         */
        RongIM.getInstance().refreshUserInfoCache(new io.rong.imlib.model.UserInfo(item.getId(), item.getUser_name(), Uri.parse(item.getFace_img())));
    }

    private void removeConversation(String  targetId,final int position){
        RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                list.remove(position);
                users.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.e("删除会话失败",errorCode.getMessage());
            }
        });
    }
}
