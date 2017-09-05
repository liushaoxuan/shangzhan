package com.wyu.iwork.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CardNewModule;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.CardNewActivity;
import com.wyu.iwork.view.activity.MineCardActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by lx on 2017/7/13.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CardNewModule.Card> cardList;
    private LayoutInflater mInflater;

    public CardViewAdapter(Context context, ArrayList<CardNewModule.Card> cardList){
        this.context = context;
        this.cardList = cardList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_new_card,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            checkStr(cardList.get(position).getName().substring(0,1),holder.card_avatar);
            checkStr(cardList.get(position).getName(),holder.card_name);
            checkStr(cardList.get(position).getPhone(),holder.card_phone);
            checkStr(cardList.get(position).getCompany(),holder.card_company);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //头像文字
        @BindView(R.id.card_avatar)
        TextView card_avatar;

        //姓名
        @BindView(R.id.card_name)
        TextView card_name;

        //电话
        @BindView(R.id.card_phone)
        TextView card_phone;

        //删除按钮
        @BindView(R.id.card_delete)
        ImageView card_delete;

        //公司
        @BindView(R.id.card_company)
        TextView card_company;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            //查看详情
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDetailPage(getLayoutPosition());
                }
            });

            //删除该名片
            card_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(getLayoutPosition());
                }
            });
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(final int id){
        new MyCustomDialogDialog(6, context, R.style.MyDialog, "确定要删除该名片吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteCard(id);
                dialog.dismiss();
            }
        }).show();
    }

    private void deleteCard(final int id){
        /**
         * user_id	是	int[11]用户自己的ID
         card_id	是	int[11]名片的ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.card_id)
         */
        String F = "ANDROID";
        String V = "1.0.1";
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+  MyApplication.userInfo.getUser_id()+cardList.get(id).getId());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",  MyApplication.userInfo.getUser_id());
        data.put("card_id",cardList.get(id).getId());
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
                                cardList.remove(id);
                                notifyDataSetChanged();
                                if(cardList.size() == 0){
                                    ((CardNewActivity)context).showContentImpl(false);
                                }
                            }
                            MsgUtil.shortToastInCenter(context,object.getString("msg"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //查看名片详情
    private void startDetailPage(int id){
        Intent detailIntent = new Intent(context, MineCardActivity.class);
        detailIntent.putExtra("id",cardList.get(id).getId());
        detailIntent.putExtra("type","2");
        context.startActivity(detailIntent);
    }
}
