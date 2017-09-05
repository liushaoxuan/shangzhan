package com.wyu.iwork.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.viewholder.BaseViewHolder;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.EditSearchInterface;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.view.activity.ContactsDetailActivity;
import com.wyu.iwork.view.activity.InvitaFriendsActivity;
import com.wyu.iwork.view.activity.OrganizationalStructureActivity;
import com.wyu.iwork.view.dialog.MyAlertDialog;
import com.wyu.iwork.view.fragment.ContactsFragment;
import com.wyu.iwork.view.fragment.HomeContactsFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sxliu on 2017/4/17.
 * 首页——联系人adapter
 */

public class ContactsAdapter extends DelegateAdapter.Adapter<BaseViewHolder> {
    private Activity mcontext;
    private HomeContactsFragment fragment;
    private List<ContactModel> list;
    private LayoutHelper layoutHelper;

    public ContactsAdapter(HomeContactsFragment mcontext, List<ContactModel> list, LayoutHelper layoutHelper) {
        this.fragment = mcontext;
        this.mcontext = mcontext.getActivity();
        this.list = list;
        this.layoutHelper = layoutHelper;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_home_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.setData(mcontext, position, list);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.item_home_contact_head)
        TextView head;
        @BindView(R.id.item_home_contact_name)
        TextView name;
        @BindView(R.id.item_home_contact_phone)
        TextView phone;
        @BindView(R.id.item_home_contact_py)
        TextView py;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void setData(Context context, int position, List t) {

            final ContactModel item = list.get(position);
            if (position == 0) {
                py.setVisibility(View.VISIBLE);
            } else {
                if (item.getPinyin().equals(list.get(position - 1).getPinyin())) {
                    py.setVisibility(View.GONE);
                } else {
                    py.setVisibility(View.VISIBLE);
                }
            }
            py.setText(item.getPinyin());
            name.setText(item.getReal_name());
            phone.setText(item.getPhone());
            try {
                head.setText(item.getReal_name().substring(0, 1));
            } catch (Exception e) {
                e.printStackTrace();
                head.setText("");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ContactsDetailActivity.class);
                    intent.putExtra("id", item.getUser_id());
                    mcontext.startActivity(intent);
                }
            });
        }
    }


}
