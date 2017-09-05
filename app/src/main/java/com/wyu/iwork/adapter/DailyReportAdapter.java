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
import com.wyu.iwork.model.DailyReport;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.DetailDailyReportActivity;
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

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.ViewHolder> {

    private static final String TAG = DailyReportAdapter.class.getSimpleName();

    private Context context;
    private ArrayList<DailyReport.Data> list;
    private LayoutInflater mInflater;
    private int type;

    public DailyReportAdapter(Context context, ArrayList<DailyReport.Data> list,int type){
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_daily_report,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        checkString(list.get(position).getTime(),holder.schedule_time);
        checkString(list.get(position).getFinish_work(),holder.schedule_content);
    }

    private void checkString(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
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
                    Intent detailIntent = new Intent(context,DetailDailyReportActivity.class);
                    detailIntent.putExtra("daily_id",list.get(getLayoutPosition()).getId());
                    context.startActivity(detailIntent);
                }
            });
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(final int id){
        new MyCustomDialogDialog(6, context, R.style.MyDialog, "确定要删除该日报吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteDailyReport(id);
                dialog.dismiss();
            }
        }).show();
    }

    private void deleteDailyReport(final int id){
        /**
         * user_id	是	int[11] 用户ID
         daily_id	是	int[11]日报ID
         F	是	string[18]
         请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.daily_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+ MyApplication.userInfo.getUser_id()+list.get(id).getId());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", MyApplication.userInfo.getUser_id());
        data.put("daily_id",list.get(id).getId());
        data.put("F", Constant.F);
        data.put("V",Constant.V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_DAILY,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                Logger.i(TAG,s);
                JSONObject object ;
                try {
                    object = new JSONObject(s);
                    if("0".equals(object.getString("code"))){
                        list.remove(id);
                        notifyDataSetChanged();
                    }else{
                        MsgUtil.shortToastInCenter(context,object.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }
}
