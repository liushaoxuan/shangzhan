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
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MeetingListMoudle;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.MettingDetailActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * Created by lx on 2017/8/12.
 */

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.ViewHolder> {

    private static final String TAG = MeetingAdapter.class.getSimpleName();

    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<MeetingListMoudle.Meet> list;

    public MeetingAdapter(Context context, ArrayList<MeetingListMoudle.Meet> list){
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_meeting_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            checkStr(list.get(position).getText(),holder.meeting_content);
            checkStr(list.get(position).getTime().split(" ")[0].replace("-","/"),holder.meeting_time);
            checkStr("发布人："+list.get(position).getUser_name(),holder.meeting_name);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkStr(String str,TextView textView){
        if(!TextUtils.isEmpty(str)){
            textView.setText(str);
        }else{
            textView.setText("");
            textView.setHint("未设置");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.meeting_time)
        TextView meeting_time;

        @BindView(R.id.meeting_delete)
        ImageView meeting_delete;

        @BindView(R.id.meeting_content)
        TextView meeting_content;

        @BindView(R.id.meeting_name)
        TextView meeting_name;

        @BindView(R.id.check_detail)
        TextView check_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            check_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MettingDetailActivity.class);
                    intent.putExtra("metting_id",list.get(getLayoutPosition()).getId());
                    context.startActivity(intent);
                }
            });
            meeting_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(list.get(getLayoutPosition()).getId(),getLayoutPosition());
                }
            });
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(final String meetId, final int id){
        new MyCustomDialogDialog(6, context, R.style.MyDialog, "确定要删除该会议吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteMeeting(meetId,id);
                dialog.dismiss();
            }
        }).show();
    }

    private void deleteMeeting(String meetingId, final int id){
        /**
         * user_id	    是       	int[11]           用户ID
         meeting_id	    是       	string[150]       会议地址
         F	                是       	string[18]        请求来源：IOS/ANDROID/WEB
         V	                是       	string[20]        版本号如：1.0.1
         RandStr	          是       	string[50]        请求加密随机数 time().|.rand()
         Sign	          是       	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.user_id.meeting_id)
         */
        UserInfo info = AppManager.getInstance(context).getUserInfo();
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+info.getUser_id()+meetingId);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",info.getUser_id());
        data.put("meeting_id",meetingId);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_MEETING,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(context) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                list.remove(id);
                                notifyDataSetChanged();
                            }
                            MsgUtil.shortToast(context,object.getString("msg"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }

    }
}
