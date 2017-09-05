package com.wyu.iwork.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
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
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CardModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MineCardActivity;
import com.wyu.iwork.widget.swipedelete.SwipeLayout;
import com.wyu.iwork.widget.swipedelete.SwipeLayoutManager;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/3/28.
 */

public class CardAddAdapter extends BaseAdapter {

    private static final String TAG = CardAddAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<CardModel.Data.Card> list;
    private UserInfo mUser;
    private int type;

    public CardAddAdapter(Context context, ArrayList<CardModel.Data.Card> list,int type){
        this.context = context;
        this.list = list;
        this.type = type;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CardModel.Data.Card getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.card_swipdelete,null);
            holder.card = (ImageView) convertView.findViewById(R.id.iv_card);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.position = (TextView) convertView.findViewById(R.id.tv_position);
            holder.company = (TextView) convertView.findViewById(R.id.tv_company);
            holder.iv_delete = (TextView) convertView.findViewById(R.id.iv_delete);
            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
            holder.item_card_content = (AutoRelativeLayout) convertView.findViewById(R.id.item_card_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(getItem(position).getName());
        holder.position.setText(getItem(position).getJob());
        holder.company.setText(getItem(position).getCompany());
        final int itemId = position;
        final ViewHolder finalHolder = holder;
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MsgUtil.shortToastInCenter(context,"delete"+itemId);
                //进行删除操作
                //finalHolder.swipeLayout.closeCurrentLayout();
                closeSwipeLayout();
                deleteCard(getItem(itemId).getId(),itemId);
            }
        });

        holder.item_card_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MsgUtil.shortToastInCenter(context,"CONTENT"+itemId);
                closeSwipeLayout();
                startDetailPage(itemId);
            }
        });
        Glide.with(context).load(getItem(position).getCard_img()).dontAnimate().transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).placeholder(R.mipmap.def_card).into(holder.card);

        return convertView;
    }

    public void closeSwipeLayout(){
        SwipeLayoutManager.getInstance().closeCurrentLayout();
        SwipeLayoutManager.getInstance().clearCurrentLayout();
    }

    private void startDetailPage(int itemId){
        Intent detailIntent = new Intent(context, MineCardActivity.class);
        /**
        Bundle bundle = new Bundle();
        bundle.putSerializable("card", getItem(itemId));
        detailIntent.putExtras(bundle);*/
        detailIntent.putExtra("id",getItem(itemId).getId());
        detailIntent.putExtra("type","2");
        context.startActivity(detailIntent);
    }

    private void deleteCard(String card_id, final int itemId){
        /**
         * user_id	是	int[11]用户自己的ID
         card_id	是	int[11]名片的ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.card_id)
         */

        if(mUser == null)
        mUser =  MyApplication.userInfo;
        String F = "ANDROID";
        String V = "1.0.1";
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+mUser.getUser_id()+card_id);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",mUser.getUser_id());
        data.put("card_id",card_id);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_CARD,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(context) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                list.remove(itemId);
                                notifyDataSetInvalidated();/**
                                if(type == 1 && list.size()>0){
                                    ((CardNewActivity)context).measureAgainListView();
                                }else if(type == 2 && list.size()>0){
                                    ((CardBagActivity)context).measureAgainListView();
                                }
                                if(list.size() == 0){
                                    if(type == 1){
                                        ((CardNewActivity)context).showContent(true);
                                    }else{
                                        ((CardBagActivity)context).showContent(true);
                                    }
                                }*/
                            }
                            MsgUtil.shortToastInCenter(context,object.getString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    class ViewHolder{
        ImageView card;
        TextView name;
        TextView position;
        TextView company;
        TextView iv_delete;
        AutoRelativeLayout item_card_content;
        SwipeLayout swipeLayout;
    }
}
