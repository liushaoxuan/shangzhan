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
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者： sxliu on 2017/6/6.13:42
 * 邮箱：2587294424@qq.com
 * OA——添加人员adapter
 */

public class oaAddPersonAdapter extends RecyclerView.Adapter<oaAddPersonAdapter.ViewHolder> {

    private Context context;
    private List<CompanyContacts> list;
    private onItemClickListener onItemClickListener;

    public oaAddPersonAdapter(Context context, List<CompanyContacts> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oa_addperson, parent, false);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * 首字母
         */
        @BindView(R.id.item_oa_addperson_py)
        TextView first_py;

        /**
         * 是否选中的图标
         */
        @BindView(R.id.item_oa_addperson_checked)
        ImageView is_cheked;

        /**
         * 用户头像
         */
        @BindView(R.id.item_oa_addperson_userface)
        TextView user_head;

        /**
         * 用户昵称
         */
        @BindView(R.id.item_oa_addperson_userName)
        TextView userName;

        /**
         * 部门
         */
        @BindView(R.id.item_oa_addperson_derpartment)
        TextView department;


        @BindView(R.id.item_oa_addperson)
        LinearLayout holview;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setDate(final int position) {
            try {
                CompanyContacts item = list.get(position);

                if (position == 0) {
                    first_py.setVisibility(View.VISIBLE);
                } else {
                    if (list.get(position - 1).getPinyin().equals(item.getPinyin())) {
                        first_py.setVisibility(View.GONE);
                    } else {
                        first_py.setVisibility(View.VISIBLE);
                    }
                }

                first_py.setText(item.getPinyin());

                userName.setText(item.getReal_name());
                department.setText(item.getUser_name());
                user_head.setText(item.getReal_name().substring(0, 1));
                if (item.isChecked()) {
                    is_cheked.setSelected(true);
                } else {
                    is_cheked.setSelected(false);
                }

                if (item.isHaschecked()) {
                    setHeight(holview, 0);
                } else {
                    setHeight(holview, 1);
                }

                if (onItemClickListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(itemView, position);
                        }

                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void setOnItemClickListener(onItemClickListener listener) {
        onItemClickListener = listener;
    }

    private void setHeight(LinearLayout layout, int type) {
        LinearLayout.LayoutParams lp = null;
        switch (type) {
            case 0:
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                layout.setLayoutParams(lp);
                break;
            case 1:
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layout.setLayoutParams(lp);
                break;
        }
    }
}
