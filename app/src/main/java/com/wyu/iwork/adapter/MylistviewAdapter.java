package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import okhttp3.Call;

/**
 * Created by lx on 2017/4/20.
 */

public class MylistviewAdapter extends BaseAdapter implements IDynamicView {

    private Context context;
    private List<DynamicBean> list;
    IDynamicPresenter presenter;
    private UserInfo muserinfo;

    public MylistviewAdapter(Context context, List<DynamicBean> list) {
        this.context = context;
        this.list = list;
        presenter = new DynamicPresenterCompl(this);
        muserinfo = AppManager.getInstance(context).getUserInfo();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_message_dynamic, parent, false);
            holder.time = (TextView) convertView.findViewById(R.id.item_message_dynamic_time);
            holder.nikaName = (TextView) convertView.findViewById(R.id.item_message_dynamic_releaser);
            holder.content = (TextView) convertView.findViewById(R.id.item_message_dynamic_content);
            holder.praise_num = (TextView) convertView.findViewById(R.id.item_message_dynamic_praise_num);
            holder.comment_num = (TextView) convertView.findViewById(R.id.item_message_dynamic_comment_num);
            holder.head = (ImageView) convertView.findViewById(R.id.item_message_dynamic_head);
            holder.praise = (LinearLayout) convertView.findViewById(R.id.item_message_dynamic_praise);
            holder.comment = (LinearLayout) convertView.findViewById(R.id.item_message_dynamic_comment);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final DynamicBean item = list.get(position);
        Glide.with(context).load(item.getUser_face_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).placeholder(R.mipmap.head_icon_nodata).into(holder.head);
        holder.nikaName.setText(item.getNick_name());
        holder.time.setText(item.getAdd_time());
        holder.content.setText(item.getText());
        holder.content.setMaxLines(3);
        if ("1".equals(item.getIs_praise())) {//0未点赞  1已经点赞
            holder.praise.setSelected(true);
            holder.praise_num.setTextColor( holder.content.getResources().getColor(R.color.dynamic_releaser_color));
        } else {
            holder.praise.setSelected(false);
            holder.praise_num.setTextColor( holder.content.getResources().getColor(R.color.blackb4));
        }
        holder.praise_num.setText(item.getPraise_count().equals("0") ? "点赞" : item.getPraise_count());
        holder.comment_num.setText(item.getComment_count().equals("0") ? "评论" : item.getComment_count());

        //点赞、取消点赞
        holder.praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doPraise(context, position);
            }
        });

        //评论
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.doComment(context, list.get(position));
            }
        });

        //详情
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.goDynamicDetail(context, list.get(position));
            }
        });
        return convertView;
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
        String user_id = muserinfo == null ? "" : muserinfo.getUser_id();
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


    public class ViewHolder {
        /**
         * 头像
         */
        ImageView head;
        /**
         * 昵称
         */
        TextView nikaName;
        /**
         * 时间
         */
        TextView time;
        /**
         * 动态内容
         */
        TextView content;
        /**
         * 赞
         */
        LinearLayout praise;
        /**
         * 赞数量
         */
        TextView praise_num;
        /**
         * 评论
         */
        LinearLayout comment;
        /**
         * 评论数量
         */
        TextView comment_num;


    }
}