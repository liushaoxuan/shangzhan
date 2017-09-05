package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.interfaces.onItemClickListener;
import com.wyu.iwork.model.MeetingModule;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.fragment.MessageNoticeFragment;
import com.wyu.iwork.widget.swipedelete.SwipeLayout;
import com.wyu.iwork.widget.swipedelete.SwipeLayoutManager;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/3/14.
 */

public class MeetingListAdapter extends BaseAdapter {

    private static final String TAG = MeetingListAdapter.class.getSimpleName();

    private Context context;

    private MessageNoticeFragment fragment;
    private onItemClickListener onItemClickListener;
    /**
     * VIEW_TYPE == 1 代表工作事务
     * VIEW_TYPE == 2 代表个人事务
     */
    private int VIEW_TYPE;
    private ArrayList<MeetingModule.Meeting> list;
    //private LoadingDialog loadingDialog = new LoadingDialog();
    private static final String SCHEDULE = "SCHEDULE";

    public MeetingListAdapter(MessageNoticeFragment context, int VIEW_TYPE, ArrayList<MeetingModule.Meeting> list){
        this.context = context.getActivity();
        this.VIEW_TYPE = VIEW_TYPE;
        this.list = list;
        fragment = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MeetingModule.Meeting getItem(int position) {
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
        if(getItem(position).getStatus() == 0){
            holder.iv_red_point.setVisibility(View.VISIBLE);
        }else{
            holder.iv_red_point.setVisibility(View.GONE);
        }
        Glide.with(context).load(getItem(position).getFace_img()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).dontAnimate().placeholder(R.mipmap.def_img_rect).into(holder.iv_avatar);
        if(!TextUtils.isEmpty(getItem(position).getUser_name())){
            holder.tv_title.setText(getItem(position).getUser_name());
        }
        if(!TextUtils.isEmpty(getItem(position).getText())){
            holder.tv_desc.setText(getItem(position).getText());
        }
        if(!TextUtils.isEmpty(getItem(position).getTime())){
            String[] time = getItem(position).getTime().split(" ");
            String[] minutesTime = time[1].split(":");
            holder.tv_time.setText(minutesTime[0]+":"+minutesTime[1]);
        }
        final int itemId = position;

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSwipe();
                deleteMeeting(itemId);
            }
        });
        //是否要设置默认图片
        //Glide.with(context).load(getItem(position).getFace_img()).dontAnimate().placeholder(R.mipmap.img_def).into(holder.iv_avatar);

        if (onItemClickListener!=null){
            Logger.e("onItemClickListener",onItemClickListener.toString());
            final View finalConvertView = holder.item_notice;
            holder.item_notice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(finalConvertView,position);
                }

            });
        }
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
        String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+getItem(itemId).getId());

        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",user.getUser_id());
        data.put("meeting_id",getItem(itemId).getId());
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_MEETING, data);
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

    public void setOnItemClickListener(onItemClickListener listener){
        onItemClickListener = listener;
    }
}
