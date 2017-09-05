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
import com.wyu.iwork.model.Schedule;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.ScheduleActivity;
import com.wyu.iwork.view.activity.ScheduleDetailActivity;
import com.wyu.iwork.view.fragment.PersonScheduleFragment;
import com.wyu.iwork.view.fragment.WorkScheduleFragment;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by lx on 2017/3/14.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private static final String TAG = ScheduleListAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<Schedule.ScheduleBean> list;
    private LayoutInflater mInflater;
    private int type;

    public ScheduleListAdapter(Context context, ArrayList<Schedule.ScheduleBean> list,int type){
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_schedule,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        checkContent(list.get(position).getAdd_time(),holder.schedule_time);
        checkContent(list.get(position).getTitle(),holder.schedule_content);
        checkContent("开始时间:"+list.get(position).getBegin_time(),holder.schedule_name);
    }

    private void checkContent(String content,TextView textView){
        if(!TextUtils.isEmpty(content)){
            textView.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.schedule_time)
        TextView schedule_time;

        @BindView(R.id.schedule_delete)
        ImageView schedule_delete;

        @BindView(R.id.schedule_content)
        TextView schedule_content;

        @BindView(R.id.schedule_name)
        TextView schedule_name;

        @BindView(R.id.schedule_check_detail)
        TextView schedule_check_detail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            schedule_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(getLayoutPosition());
                }
            });
            schedule_check_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(context,ScheduleDetailActivity.class);
                    detailIntent.putExtra("id",list.get(getLayoutPosition()).getId());
                    context.startActivity(detailIntent);
                }
            });
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(final int itemId){
        new MyCustomDialogDialog(6, context, R.style.MyDialog, "确定要删除该日程吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteSchedule(itemId);
                dialog.dismiss();
            }
        }).show();
    }

    public void deleteSchedule(final int itemId) {
        /**
         * user_id	是	int[18]用户ID
         schedule_id	是	int[18]日程ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.schedule_id)
         */
        UserInfo user = AppManager.getInstance(context).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F + V + RandStr + user.getUser_id() + list.get(itemId).getId());

        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", user.getUser_id());
        data.put("schedule_id", list.get(itemId).getId());
        data.put("F", F);
        data.put("V", V);
        data.put("Sign", Sign);
        data.put("RandStr", RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_SCHEDULE, data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.i(TAG, s);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if ("0".equals(object.getString("code"))) {
                        list.remove(itemId);
                        notifyDataSetChanged();
                        if (list.size() == 0) {
                            if(type == 1){
                                ((WorkScheduleFragment)(((ScheduleActivity)context).getFragment(type))).showContentforFragment(false);
                            }else if(type == 2){
                                ((PersonScheduleFragment)(((ScheduleActivity)context).getFragment(type))).showContentforFragment(false);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                e.printStackTrace();
                Logger.i(TAG, e.toString());
            }
        });
    }
}
