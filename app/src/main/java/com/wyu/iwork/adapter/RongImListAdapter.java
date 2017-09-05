package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.model.UserModel;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.view.activity.DynamicActivity;
import com.wyu.iwork.view.activity.MettingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;

/**
 * Created by lx on 2017/4/17.
 */

public class RongImListAdapter extends ConversationListAdapter {
    private Context mcontext;
    RongViewHolder holder = null;
    private UserInfo userInfo;
    private List<UserModel> list;
    private boolean showhead = false;

    public RongImListAdapter(Context context, List<UserModel> list) {
        super(context);
        this.mcontext = context;
        this.list = list;
        userInfo = AppManager.getInstance(context).getUserInfo();
        if (list == null) {
            list = new ArrayList<>();
        }
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_home_message, null);
        holder = new RongViewHolder(view);
        return view;
    }

    @Override
    protected void bindView(View v, int position, UIConversation data) {
        holder.setData(position, data);
    }

    public class RongViewHolder {

        //
        /**
         * 我的动态
         */
        @BindView(R.id.fragment_home_message_my_dynamic)
        RelativeLayout my_dynamic;
        /**
         * 我的头像
         */
        @BindView(R.id.home_message_fragment_my_head)
        ImageView my_head;

        /**
         * 我的动态_红点
         */
        @BindView(R.id.fragment_home_message_has_dynamic)
        ImageView has_dynamic;

        /**
         * 会议通知
         */
        @BindView(R.id.fragment_home_message_meetting_note)
        RelativeLayout meetting_note;

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

        private View view;

        public RongViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        private void setData(int position, final UIConversation data) {

            final UserModel item = list.get(position);

            if (showhead) {
                my_dynamic.setVisibility(View.GONE);
                meetting_note.setVisibility(View.GONE);
            }
            if (position == 0) {
                my_dynamic.setVisibility(View.VISIBLE);
                meetting_note.setVisibility(View.VISIBLE);
                showhead = true;
                Glide.with(mcontext).load(userInfo.getUser_face_img()).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 5)).placeholder(R.mipmap.head_icon_nodata).into(my_head);
            } else {
                my_dynamic.setVisibility(View.GONE);
                meetting_note.setVisibility(View.GONE);
            }

            content.setText(data.getConversationContent().toString());
            messageNum.setText(data.getUnReadMessageCount() + "");
            time.setText(DateUtil.ChartTime(data.getUIConversationTime()));
            if (0 == data.getUnReadMessageCount()) {
                messageNum.setVisibility(View.GONE);
            } else {
                messageNum.setVisibility(View.VISIBLE);
            }

            String iconurl = item.getFace_img();
            String title = item.getUser_name();
            nikeName.setText(title);
            Glide.with(mcontext).load(iconurl).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 5)).placeholder(R.mipmap.head_icon_nodata).into(head);

            final String chartTitle = title;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUser(item);
                    RongIM.getInstance().startPrivateChat(mcontext, data.getConversationTargetId(), chartTitle);
                }
            });
        }

        @OnClick({R.id.fragment_home_message_my_dynamic, R.id.fragment_home_message_meetting_note})
        void Click(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.fragment_home_message_my_dynamic://我的动态
                    intent = new Intent(mcontext, DynamicActivity.class);
                    mcontext.startActivity(intent);
                    break;
                case R.id.fragment_home_message_meetting_note://会议通知
                    intent = new Intent(mcontext, MettingActivity.class);
                    mcontext.startActivity(intent);
                    break;
            }
        }
    }

    private void setUser(final UserModel item) {

        //设置用户信息
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public io.rong.imlib.model.UserInfo getUserInfo(String s) {
                Uri uri = Uri.parse(item.getFace_img());
                io.rong.imlib.model.UserInfo rong_userInfo = new io.rong.imlib.model.UserInfo(item.getId()+"", item.getUser_name(), uri);
                return rong_userInfo;
            }
        }, true);
        /**
         * 刷新用户缓存数据。
         *
         * @param userInfo 需要更新的用户缓存数据。
         */
        RongIM.getInstance().refreshUserInfoCache(new io.rong.imlib.model.UserInfo(item.getId()+"", item.getUser_name(), Uri.parse(item.getFace_img())));
    }
}
