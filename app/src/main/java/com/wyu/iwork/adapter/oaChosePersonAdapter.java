package com.wyu.iwork.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.oaDelPerson;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.CompanyContacts;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 作者： sxliu on 2017/6/7.14:59
 * 邮箱：2587294424@qq.com
 * OA——审批人/抄送人 adapter
 */

public class oaChosePersonAdapter extends RecyclerView.Adapter<oaChosePersonAdapter.ViewHolder> {

    private Context mcontext;
    private List<CompanyContacts> list;
    private int PersonType = 0;// 0 代表审批人，1代表抄送人
    private onItemClickListener  onItemClickListener;
    private oaDelPerson delperson;

    public oaChosePersonAdapter(Context mcontext, List<CompanyContacts> list,int type) {
        this.mcontext = mcontext;
        PersonType = type;
        if (list==null){
            list = new ArrayList<>();
        }
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.oa_item_chose_person,parent,false);
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 每个头像之间的小点
         */
        @BindView(R.id.oa_point)
        ImageView point;
        /**
         *  头像
         */
        @BindView(R.id.oa_person_face)
        CircleImageView face;

        /**
         *  删除
         */
        @BindView(R.id.oa_person_del)
        ImageView del;

        /**
         *  昵称
         */
        @BindView(R.id.oa_chose_person_userName)
        TextView userName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            AutoUtils.autoSize(itemView);
        }

        private void setData(final int position){
            CompanyContacts item = list.get(position);
            if (position>2){
                point.setVisibility(View.GONE);
            }else {
                point.setVisibility(View.VISIBLE);
            }
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (delperson!=null){
                        delperson.del(del,position,PersonType);
                    }
                }
            });

            userName.setText(item.getReal_name());
            Glide.with(mcontext).load(item.getFace_img()).transform(new CenterCrop(mcontext), new GlideRoundTransform(mcontext, 3)).error(R.mipmap.oa_plus).into(face);
            if (onItemClickListener!=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(itemView,position);
                    }

                });
            }
        }
    }

    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }

    public void setDelperson(oaDelPerson del){
        delperson = del;
    }
}
