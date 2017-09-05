package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.IDynamicView;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.DynamicPresenterCompl;
import com.wyu.iwork.presenter.IDynamicPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.view.activity.DynamicDetailActivity;
import com.wyu.iwork.view.activity.PostingDetailsActivity;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by lx on 2017/4/8.
 * 消息——动态——adapter
 */

public class MessageDynamicAdapter extends RecyclerView.Adapter<MessageDynamicAdapter.ViewHolder> implements IDynamicView {

    private Context context;
    private List<DynamicBean> list;
    IDynamicPresenter presenter;
    private UserInfo muserinfo;

    public MessageDynamicAdapter(Context context, List<DynamicBean> list) {
        this.context = context;
        this.list = list;
        presenter = new DynamicPresenterCompl(this);
        muserinfo = AppManager.getInstance(context).getUserInfo();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_dynamic, parent, false);
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
    //动态详情
    @Override
    public void goDynamicDetail(Context context, DynamicBean bean) {
        Intent intent = new Intent(context, DynamicDetailActivity.class);
        intent.putExtra("dynamic_id", bean.getId());
        context.startActivity(intent);
    }
    //点赞 /取消点赞
    @Override
    public void doPraise(Context mcontext, final int position) {
        String url = Constant.URL_PRAISE;
        String F = Constant.F;
        String V = Constant.V;
        String id = list.get(position).getId();
        String user_id = muserinfo==null?"":muserinfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + id + user_id);

        OkGo.get(url)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("dynamic_id", id)
                .params("user_id", user_id)
                .params("F", F)
                .params("V", V)
                .params("RandStr", RandStr)
                .params("Sign", sign)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                String count = list.get(position).getPraise_count();
                                if (count == null || "".equals(count)) {
                                    count = "0";
                                }
                                String is_praised = list.get(position).getIs_praise();
                                if ("0".equals(is_praised)) {
                                    int mcount = Integer.parseInt(count) + 1;
                                    list.get(position).setIs_praise("1");
                                    list.get(position).setPraise_count(mcount + "");
                                } else {
                                    int mcount = Integer.parseInt(count) - 1;
                                    list.get(position).setIs_praise("0");
                                    list.get(position).setPraise_count(mcount + "");
                                }

                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, object.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.e("response------------>", s);
                    }
                });

    }
    //评论
    @Override
    public void doComment(Context context, DynamicBean bean) {
        Intent intent = new Intent(context, PostingDetailsActivity.class);
        intent.putExtra("dynamic_id", bean.getId());
        context.startActivity(intent);
    }

    @Override
    public void showAlert(Context context, DynamicBean bean, int position) {

    }

    @Override
    public void deleteDynamic(int position) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * 头像
         */
        @BindView(R.id.item_message_dynamic_head)
        ImageView head;
        /**
         * 昵称
         */
        @BindView(R.id.item_message_dynamic_releaser)
        TextView nikaName;
        /**
         * 时间
         */
        @BindView(R.id.item_message_dynamic_time)
        TextView time;
        /**
         * 动态内容
         */
        @BindView(R.id.item_message_dynamic_content)
        TextView content;
        /**
         * 赞
         */
        @BindView(R.id.item_message_dynamic_praise)
        LinearLayout praise;
        /**
         * 赞数量
         */
        @BindView(R.id.item_message_dynamic_praise_num)
        TextView praise_num;
        /**
         * 评论
         */
        @BindView(R.id.item_message_dynamic_comment)
        LinearLayout comment;
        /**
         * 评论数量
         */
        @BindView(R.id.item_message_dynamic_comment_num)
        TextView comment_num;
        private View view;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        private void setData(final int position) {
            final DynamicBean item = list.get(position);
            Glide.with(context).load(item.getUser_face_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).placeholder(R.mipmap.head_icon_nodata).into(head);
            nikaName.setText(item.getNick_name());
            time.setText(item.getAdd_time());
            content.setText(item.getText());
            content.setMaxLines(3);
            if ("1".equals(item.getIs_praise())){//0未点赞  1已经点赞
                praise.setSelected(true);
                praise_num.setTextColor(content.getResources().getColor(R.color.dynamic_releaser_color));
            }else {
                praise.setSelected(false);
                praise_num.setTextColor(content.getResources().getColor(R.color.blackb4));
            }
            praise_num.setText(item.getPraise_count().equals("0")?"点赞":item.getPraise_count());
            comment_num.setText(item.getComment_count().equals("0")?"评论":item.getComment_count());

            //点赞、取消点赞
            praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.doPraise(context, position);
                }
            });

            //评论
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.doComment(context, list.get(position));
                }
            });

            //详情
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.goDynamicDetail(context, list.get(position));
                }
            });
        }


    }
}
