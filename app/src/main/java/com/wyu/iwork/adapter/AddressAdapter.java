package com.wyu.iwork.adapter;

import android.app.Dialog;
import android.content.Context;
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
import com.wyu.iwork.model.SignConf;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
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
 * Created by lx on 2017/7/25.
 * 考勤 - 设置地址
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private static final String TAG = AddressAdapter.class.getSimpleName();
    private Context context;
    private LayoutInflater mInflater;
    private ArrayList<SignConf.AddressConf> list;

    public AddressAdapter(Context context,ArrayList<SignConf.AddressConf> list){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_sign_add_address,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        checkStr(list.get(position).getBuilding(),holder.address_name);
        checkStr(list.get(position).getAddress(),holder.address_detail);
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.address_name)
        TextView address_name;

        @BindView(R.id.address_detail)
        TextView address_detail;

        @BindView(R.id.delete)
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(getLayoutPosition());
                }
            });
        }
    }

    //删除提醒弹窗
    private void showDeleteDialog(final int item){
        new MyCustomDialogDialog(6, context, R.style.MyDialog, "确定删除“"+list.get(item).getAddress()+"”吗？", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                deleteAddress(item);
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 删除办公地点
     */
    private void deleteAddress(final int item){
        /**
         * user_id	        是	        int[11]             用户ID
         sign_address_id	  是	        int[11]             公司办公地址ID
         F	                    是	        string[18]          请求来源：IOS/ANDROID/WEB
         V	                    是	        string[20]          版本号如：1.0.1
         RandStr	              是	        string[50]          请求加密随机数 time().|.rand()
         Sign	              是	        string[400]         请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ MyApplication.userInfo.getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",  MyApplication.userInfo.getUser_id());
        data.put("sign_address_id",list.get(item).getId());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_DELETE_SIGN_CONF_ADDRESS,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(context) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.i(TAG,s);
                super.onSuccess(s, call, response);
                JSONObject object = null;
                try {
                    object = new JSONObject(s);
                    if("0".equals(object.getString("code"))){
                        //删除
                        list.remove(item);
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
