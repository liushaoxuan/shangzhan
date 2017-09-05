package com.wyu.iwork.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.IDynamicView;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.DynamicPresenterCompl;
import com.wyu.iwork.presenter.IDynamicPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.DynamicDetailActivity;
import com.wyu.iwork.view.activity.PostingDetailsActivity;
import com.wyu.iwork.view.fragment.DynamicFragment;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * Created by lx on 2016/12/20.
 * 动态——适配器
 */

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> implements IDynamicView {

    private Context mcontext;
    private DynamicFragment fragment;
    private List<DynamicBean> list;
    IDynamicPresenter presenter;

    private UserInfo muserinfo;

    public DynamicAdapter(DynamicFragment mcontext, List<DynamicBean> list) {
        this.mcontext = mcontext.getActivity();
        this.list = list;
        fragment = mcontext;
        presenter = new DynamicPresenterCompl(this);
        muserinfo = MyApplication.userInfo;
    }

    public void setDatas(List<DynamicBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_dynamic_main, parent, false);
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
    public void doPraise(Context context, final int position) {
        String url = Constant.URL_PRAISE_UNPRAISE;
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
                                Toast.makeText(mcontext, object.optString("msg"), Toast.LENGTH_LONG).show();
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
    public void showAlert(final Context context, final DynamicBean bean, final int position) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        View mpopView = LayoutInflater.from(context).inflate(R.layout.item_dynamic_pop, null);
        //TextView huihua = (TextView) mpopView.findViewById(R.id.item_dynamic_pop_huihua);
        TextView call = (TextView) mpopView.findViewById(R.id.item_dynamic_pop_call);
        TextView delete = (TextView) mpopView.findViewById(R.id.item_dynamic_pop_send_email);
        TextView cancel = (TextView) mpopView.findViewById(R.id.item_dynamic_pop_cancel);

        if ((muserinfo==null?"":muserinfo.getUser_id()).equals(list.get(position).getUser_id())){
            delete.setVisibility(View.VISIBLE);
        }else {
            delete.setVisibility(View.GONE);
        }
        alertDialog.setView(mpopView);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("tel:" + bean.getUser_phone()));
                intent.setAction(Intent.ACTION_CALL);
                context.startActivity(intent);
                alertDialog.dismiss();
            }
        });

        //删除动态
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deleteDynamic(position);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    //删除动态
    @Override
    public void deleteDynamic(final int position) {

        final AlertDialog alertDialog = new AlertDialog.Builder(mcontext).create();

        View mpopView = LayoutInflater.from(mcontext).inflate(R.layout.item_dynamic_detail_pop, null);
        TextView delete = (TextView) mpopView.findViewById(R.id.item_dynamic_detail_delete);
        TextView cancel = (TextView) mpopView.findViewById(R.id.item_dynamic_detail_cancel);
        TextView title = (TextView) mpopView.findViewById(R.id.item_dynamic_detail_title);
        title.setText("确定删除该动态吗？");
        alertDialog.setView(mpopView);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteDynamic(position);
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
        alertDialog.getWindow().setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //头像
        @BindView(R.id.item_dynamic_icon)
        CircleImageView headIcon;
        //右边的箭头
        @BindView(R.id.item_dynamic_detail_right_bottom_arrow)
        ImageView right_tobottom_arrow;
        //名称
        @BindView(R.id.item_dynamic_user_name)
        TextView userName;
        //时间
        @BindView(R.id.item_dynamic_time)
        TextView time;
        //内容
        @BindView(R.id.item_dynamic_content)
        TextView content;
        //点赞数
        @BindView(R.id.item_dynamic_zan_num)
        TextView goodNum;
        //评论数
        @BindView(R.id.item_dynamic_comment_num)
        TextView commentNum;
        //公告头像
        @BindView(R.id.item_dynamic_gonggao_icon)
        ImageView gonggao;
        //点赞图标
        @BindView(R.id.item_dynamic_zan)
        ImageView zan;

        @BindView(R.id.item_dynamic_zan_layout)
        LinearLayout zan_layout;
        //评论图标
        @BindView(R.id.item_dynamic_comment)
        ImageView comment;

        @BindView(R.id.item_dynamic_comment_layout)
        LinearLayout comment_layout;

        //点击出现弹窗
        @BindView(R.id.item_dynamic_item)
        LinearLayout mlayout;
        /**
         * 详情
         */
        @BindView(R.id.item_dynamic_detail)
        LinearLayout detail;

        /**
         * 底部view
         */
        @BindView(R.id.item_dynamic_bottom)
        View bottomView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final int position) {
            if (position == list.size() - 1) {
                bottomView.setVisibility(View.VISIBLE);
            } else {
                bottomView.setVisibility(View.GONE);
            }
            DynamicBean item = list.get(position);

            Glide.with(mcontext).load(item.getUser_face_img()).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 5)).placeholder(R.mipmap.head_icon_nodata).into(headIcon);
            userName.setText(item.getNick_name());
            time.setText(item.getAdd_time());
            content.setText(item.getText());
            goodNum.setText(item.getPraise_count() == null ? "0" : item.getPraise_count());
            commentNum.setText(item.getComment_count() == null ? "0" : item.getComment_count());
            if ("1".equals(item.getIs_praise())){
                zan.setImageResource(R.mipmap.icon_zan_select);
                goodNum.setTextColor(mcontext.getResources().getColor(R.color.blue));
            }else {
                zan.setImageResource(R.mipmap.icon_zan_normal);
                goodNum.setTextColor(mcontext.getResources().getColor(R.color.blue));
            }


            /**
             * 点赞
             */
            zan_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.doPraise(mcontext, position);
                }
            });
            /**
             * 评论
             */
            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.doComment(mcontext, list.get(position));
                }
            });

            /**
             * 详情
             */
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.goDynamicDetail(mcontext, list.get(position));
                }
            });

            //mlayout
            mlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.goDynamicDetail(mcontext, list.get(position));
                }
            });
            right_tobottom_arrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.showAlert(mcontext, list.get(position), position);
                }
            });
        }

    }

    /**
     * 删除评论
     */
    private void DeleteDynamic(final int position) {

        String url = Constant.URL_DEL_COMMENT;
        String F = Constant.F;
        String V = Constant.V;
        String id = list.get(position).getId();
        String user_id = muserinfo==null?"":muserinfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + id + user_id);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("dynamic_id", id);
        map.put("user_id", user_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
//        map.put("is_test","1");

        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.get(murl)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                list.remove(position);
                                notifyItemRemoved(position);
                            } else {
                                Toast.makeText(mcontext, object.optString("msg"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                       Logger.e("response------------>", s);
                    }
                });

    }
}
