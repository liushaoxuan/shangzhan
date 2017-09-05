package com.wyu.iwork.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.DynamicCommentBean;
import com.wyu.iwork.view.activity.DynamicDetailActivity;
import com.wyu.iwork.view.fragment.DynamicDetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2016/12/21.
 * 动态详情适配器
 */

public class DynamicDetailAdapter extends RecyclerView.Adapter<DynamicDetailAdapter.ViewHolder> {

    private DynamicDetailFragment mcontext;
    private List<DynamicCommentBean> list;

    public DynamicDetailAdapter(DynamicDetailFragment mcontext,List<DynamicCommentBean> list) {
        this.mcontext = mcontext;
        this.list = list;
        if (list==null){
            list = new ArrayList<>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext.getActivity()).inflate(R.layout.item_dynamic_detail,parent,false);
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
        //用户名
        @BindView(R.id.item_dynamic_detail_userName)
        TextView userName ;
        //内容
        @BindView(R.id.item_dynamic_detail_content)
        TextView content ;

        @BindView(R.id.item_dynamic_detail_parent)
        LinearLayout mview;
        //回复
        @BindView(R.id.item_dynamic_detail_reply_layout)
        LinearLayout reply_layout;
        //回复的人
        @BindView(R.id.item_dynamic_detail_userName_reply)
        TextView replyer ;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this,itemView);
        }
        public void setData(final int position){

            userName.setText(list.get(position).getNick_name()+":");
            content.setText(Html.fromHtml(list.get(position).getText()));
            reply_layout.setVisibility(View.GONE);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (list.get(position).getUser_id()!=null&&list.get(position).getUser_id().equals(MyApplication.userInfo==null?"":MyApplication.userInfo.getUser_id())){
                        mcontext.DeleteComment(position);
                    }
                    return false;
                }
            });
        }
    }
}
