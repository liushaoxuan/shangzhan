package com.wyu.iwork.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.wyu.iwork.R;
import com.wyu.iwork.view.activity.InvitaFriendsActivity;
import com.wyu.iwork.view.activity.OrganizationalStructureActivity;
import com.wyu.iwork.view.fragment.HomeContactsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： sxliu on 2017/8/23.14:15
 * 邮箱：2587294424@qq.com
 *
 */

public class ContactInviteAdapter extends DelegateAdapter.Adapter<ContactInviteAdapter.ViewHolder> {
    private HomeContactsFragment fragment;
    private LayoutHelper layoutHelper;
    private ViewHolder holder;
    public ContactInviteAdapter(HomeContactsFragment fragment,LayoutHelper layoutHelper) {
        this.fragment = fragment;
        this.layoutHelper = layoutHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_home_contact_invate,parent,false);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setdata();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

   public class ViewHolder extends RecyclerView.ViewHolder{

        /**
         * 邀请好友
         */
        @BindView(R.id.item_home_contact_invite)
        RelativeLayout invate;

        /**
         *  组织架构
         */
        @BindView(R.id.item_home_contact_orgnize)
        RelativeLayout orgnize;

        @BindView(R.id.item_home_contact_search)
        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        private void setdata(){
            invate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(fragment.getActivity(), InvitaFriendsActivity.class);
                    fragment.getActivity().startActivity(intent);
                }
            });

            orgnize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(fragment.getActivity(), OrganizationalStructureActivity.class);
                    fragment.getActivity().startActivity(intent);
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    fragment.getSearch(s,editText.getText().toString());
                }
            });
        }

       private void clearSearch(){
           editText.setText("");
       }
    }

    public void cleanSearch(){
        holder.clearSearch();
    }
}
