package com.wyu.iwork.adapter;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.ContactModel;
import com.wyu.iwork.model.ContactsModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.view.activity.BaseActivity;

import java.util.List;

/**
 * Created by sxliu on 2017/4/11.
 * 手机通讯录adapter
 */

public class ContactsFriendsAdapter extends BaseAdapter {
    private BaseActivity context;
    private List<ContactsModel> list;
    private UserInfo userInfo;

    public ContactsFriendsAdapter(BaseActivity context, List<ContactsModel> list) {
        this.context = context;
        this.list = list;
        userInfo =  MyApplication.userInfo;
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

    @SuppressLint("WrongViewCast")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_contacts,parent,false);
            holder.head = (TextView) convertView.findViewById(R.id.item_home_contact_head);
            holder.name = (TextView) convertView.findViewById(R.id.item_home_contact_name);
            holder.phone = (TextView) convertView.findViewById(R.id.item_home_contact_phone);
            holder.py = (TextView) convertView.findViewById(R.id.item_home_contact_py);
            holder.invite = (TextView) convertView.findViewById(R.id.item_contacts_invite);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.invite.setVisibility(View.VISIBLE);
        holder.head.setVisibility(View.GONE);

        final ContactsModel item = list.get(position);
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());

        if (item.getFirst_py()==null||"".equals(item.getFirst_py())){
            holder.py.setVisibility(View.GONE);
        }else {
            holder.py.setVisibility(View.VISIBLE);
        }
        holder.py.setText(item.getFirst_py());


        if (item.isinvite()){
            holder.invite.setSelected(true);
        }else {
            holder.invite.setSelected(false);
        }

        //邀请
        holder.invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSendSMSTo(item.getPhone(),userInfo.getWelcome_sms_msg());
                list.get(position).setIsinvite(true);
                notifyDataSetChanged();

            }
        });
        return convertView;
    }

    private class ViewHolder{
        private TextView head;
        private TextView phone;
        private TextView name;
        private TextView py;
        //邀请
        private TextView invite;
    }

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     * @param message
     */



    private void doSendSMSTo(String phoneNumber,String message){
        Uri uri2 = Uri.parse("smsto:"+phoneNumber);

      //  2、创建意图。
        Intent intent = new Intent(Intent.ACTION_VIEW,uri2);
        intent.putExtra("sms_body", message);
       // 3、打开系统短信界面，号码已经填写，只需填写要发送
        context.startActivity(intent);
    }


}
