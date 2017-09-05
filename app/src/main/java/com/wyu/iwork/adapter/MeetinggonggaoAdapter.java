package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MessageGonggaoModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.WebActivity;
import com.wyu.iwork.view.fragment.MessageGonggaoFragment;
import com.wyu.iwork.widget.swipedelete.SwipeLayout;
import com.wyu.iwork.widget.swipedelete.SwipeLayoutManager;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/3/14.
 *
 * 消息通知公告adapter
 */

public class MeetinggonggaoAdapter extends BaseAdapter {

    private static final String TAG = MeetinggonggaoAdapter.class.getSimpleName();

    private Context context;
    private MessageGonggaoFragment fragment;
    private List<MessageGonggaoModel> list;

    public MeetinggonggaoAdapter(MessageGonggaoFragment context, List<MessageGonggaoModel> list){
        this.context = context.getActivity();
        this.list = list;
        fragment = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MessageGonggaoModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.meeting_swipdelete,null);
            holder = new ViewHolder();
            holder.item_notice = (AutoRelativeLayout) convertView.findViewById(R.id.item_notice);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv_red_point = (ImageView) convertView.findViewById(R.id.iv_red_point);
            holder.iv_delete = (TextView) convertView.findViewById(R.id.iv_delete);
            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if("0".equals(getItem(position).getIs_read() )){
            holder.iv_red_point.setVisibility(View.VISIBLE);
        }else{
            holder.iv_red_point.setVisibility(View.GONE);
        }
        Glide.with(context).load(getItem(position).getFace_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).dontAnimate().placeholder(R.mipmap.def_img_rect).into(holder.iv_avatar);
        if(!TextUtils.isEmpty(getItem(position).getTitle())){
            holder.tv_title.setText(getItem(position).getTitle());
        }
        if(!TextUtils.isEmpty(getItem(position).getContent())){
            holder.tv_desc.setText(getItem(position).getContent());
        }
        if(!TextUtils.isEmpty(getItem(position).getAdd_time())){
            holder.tv_time.setText(getItem(position).getAdd_time());
        }
        final int itemId = position;

        holder.item_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSwipe();
                Intent detailIntent = new Intent(context,WebActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",getItem(itemId).getUrl());
                detailIntent.putExtras(bundle);
                context.startActivity(detailIntent);
            }
        });


        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSwipe();
                deleteMeeting(itemId);
            }
        });

        return convertView;
    }

    public void closeSwipe(){
        SwipeLayoutManager.getInstance().closeCurrentLayout();
        SwipeLayoutManager.getInstance().clearCurrentLayout();
    }

    public void deleteMeeting(final int itemId){
        /**
         * user_id	是	int[11]用户ID
         meeting_id	是	string[150]会议地址
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.meeting_id)
         */
        //loadingDialog.show(((MeetingListActivity)context).getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
        UserInfo user = AppManager.getInstance(context).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+getItem(itemId).getNotice_id());

        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",user.getUser_id());
        data.put("notice_id",getItem(itemId).getNotice_id());
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_NOTICE, data);
        OkGo.get(murl)
                .tag(this)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(context) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        //loadingDialog.dismiss();
                        try {
                            JSONObject object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                list.remove(itemId);
                                notifyDataSetInvalidated();
                                //((NoticeActivity)context).removeNoticeData(VIEW_TYPE,itemId);
                                if(list.size() == 0){
                                    (fragment).showContent(false);
                                }
                            }
                            MsgUtil.shortToastInCenter(context,object.getString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        e.printStackTrace();
                        Logger.i(TAG,e.toString());
                    }
                });

    }
    class ViewHolder{
        ImageView iv_avatar,iv_red_point;
        TextView tv_title,tv_desc,tv_time,iv_delete;
        AutoRelativeLayout item_notice;
        SwipeLayout swipeLayout;
    }
}
