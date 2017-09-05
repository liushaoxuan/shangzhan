package com.wyu.iwork.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.model.Application;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.BusinessAttestationActivity;
import com.wyu.iwork.view.activity.CardNewActivity;
import com.wyu.iwork.view.activity.CargoLocationManagerActivity;
import com.wyu.iwork.view.activity.CheckWorkAttendanceActivity;
import com.wyu.iwork.view.activity.CrmBusinessOpportunityActivity;
import com.wyu.iwork.view.activity.CrmContractActivity;
import com.wyu.iwork.view.activity.CrmCustomFollowActivity;
import com.wyu.iwork.view.activity.CrmCustomMapActivity;
import com.wyu.iwork.view.activity.CustomDuplicateCheckingActivity;
import com.wyu.iwork.view.activity.DailyReportActivity;
import com.wyu.iwork.view.activity.DepartmentManagerActivity;
import com.wyu.iwork.view.activity.GoodsInStoreActivity;
import com.wyu.iwork.view.activity.GoodsManagerActivity;
import com.wyu.iwork.view.activity.GoodsOutStoreActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.MarketingActivity;
import com.wyu.iwork.view.activity.MeetingListActivity;
import com.wyu.iwork.view.activity.OrganizationalStructureActivity;
import com.wyu.iwork.view.activity.PotentialCustomerManagerActivity;
import com.wyu.iwork.view.activity.PublishNoticeActivity;
import com.wyu.iwork.view.activity.PurchaseOrderActivity;
import com.wyu.iwork.view.activity.SalesLeadActivity;
import com.wyu.iwork.view.activity.SalesOrderActivity;
import com.wyu.iwork.view.activity.ScheduleActivity;
import com.wyu.iwork.view.activity.StockManagerActivity;
import com.wyu.iwork.view.activity.StoresManagerActivity;
import com.wyu.iwork.view.activity.SupplierManagerActivity;
import com.wyu.iwork.view.activity.TaskActivity;
import com.wyu.iwork.view.activity.WebActivity;
import com.wyu.iwork.view.activity.oaApprovalActivity;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lx on 2017/5/16.
 */

public class SelectedApplicationAdapter extends RecyclerView.Adapter<SelectedApplicationAdapter.ViewHolder> {

    private static final String TAG = SelectedApplicationAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<Application.Data.ApplicationModel> list;
    private LayoutInflater mInflater;
    private static final String TYPE_CUSTOM = "CUSTOM";//客户管理
    private static final String TYPE_POTENTIAL = "POTENTIAL";//潜在客户管理
    private boolean flag;

    public SelectedApplicationAdapter(Context context,ArrayList<Application.Data.ApplicationModel> list){
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
    }

    public SelectedApplicationAdapter(Context context,ArrayList<Application.Data.ApplicationModel> list,boolean flag){
        this.context = context;
        this.list = list;
        this.mInflater = LayoutInflater.from(context);
        this.flag = flag;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_selected_applicat,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getIcon()).transform(new CenterCrop(context), new GlideRoundTransform(context, 5)).into(holder.item_selected_applicat_image);
        holder.item_selected_applicat_text.setText(list.get(position).getText());
    }

    @Override
    public int getItemCount() {
        if(flag == true){
            if("0".equals(AppManager.getInstance(context).getUserInfo().getIs_admin())){
                for(int i = 0;i<list.size();i++){
                    if("42".equals(list.get(i).getId())){
                        list.remove(i);
                    }
                }
            }
            return list.size();
        }else{
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_selected_applicat_image)
        ImageView item_selected_applicat_image;

        @BindView(R.id.item_selected_applicat_text)
        TextView item_selected_applicat_text;

        @BindView(R.id.applicat_item_gridview)
        AutoRelativeLayout applicat_item_gridview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            applicat_item_gridview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.i(TAG,"onClick");
//                    String company_auth = AppManager.getInstance(context).getUserInfo().getCompany_auth();
                    String company_auth = MyApplication.userInfo.getCompany_auth();
                    Logger.i(TAG,company_auth);

                    if("10".equals(list.get(getLayoutPosition()).getId())){
                        startActivityForId(list.get(getLayoutPosition()).getId(),getLayoutPosition());
                    }else if("3".equals(company_auth) || "0".equals(company_auth)){
                        showDialog();
                    }else if("2".equals(company_auth)){
                        showAuthingDialog("您的企业正在认证中,预计需要\n1-3个工作日,请耐心等候!");
                    }else if("1".equals(company_auth)){
                        //判断是否有权限
                        if("1".equals(list.get(getLayoutPosition()).getIs_auth())){
                            startActivityForId(list.get(getLayoutPosition()).getId(),getLayoutPosition());
                        }else{
                            //弹窗提示没有权限
                            showAuthingDialog("没有操作权限，请联系管\n理员进行开通!");
                        }
                    }
                }
            });
        }
    }

    //企业认证提示框
    public void showDialog(){
        new MyCustomDialogDialog(7, (MainActivity)context, R.style.MyDialog, "您的账号尚未完成企业认证\n完成认证可享受跟多特权\n是否认证？", "去认证", "残忍拒绝", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                //拒绝
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                //认证
                context.startActivity(new Intent((MainActivity)context, BusinessAttestationActivity.class));
                dialog.dismiss();
            }
        }).show();
    }

    //提示框
    public void showAuthingDialog(String str){
        new MyCustomDialogDialog(8, context, R.style.MyDialog, str, new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    public void startActivityForId(String id,int itemId){
        //判断是否进行了企业认证
        Intent intent = null;

            switch (id){
                /** OA */
                case "1":
                    //手机考勤
                    intent = new Intent(context, CheckWorkAttendanceActivity.class);
                    context.startActivity(intent);
                    break;
                case "64":
                    intent = new Intent(context, MeetingListActivity.class);
                    context.startActivity(intent);
                    //会议通知
                    break;
                case "63":
                    //公告
                    intent = new Intent(context, PublishNoticeActivity.class);
                    context.startActivity(intent);
                    break;
                case "4": //组织架构
                    intent = new Intent(context, OrganizationalStructureActivity.class);
                    context.startActivity(intent);
                    break;
                case "5":
                    //任务
                    intent = new Intent(context, TaskActivity.class);
                    context.startActivity(intent);
                    break;
                case "6":
                    //日报
                    intent = new Intent(context, DailyReportActivity.class);
                    context.startActivity(intent);
                    break;
                case "8":
                    //日程管理
                    intent = new Intent(context, ScheduleActivity.class);
                    context.startActivity(intent);
                    break;
                case "9":
                    //邮件
                    break;
                case "57"://审批
                    intent = new Intent(context, oaApprovalActivity.class);
                    context.startActivity(intent);
                    break;
                case "10":
                    //微名片
                    intent = new Intent(context, CardNewActivity.class);
                    context.startActivity(intent);
                    break;
                /** CRM */
                case "42"://部门管理
                    intent = new Intent(context, DepartmentManagerActivity.class);
                    context.startActivity(intent);
                    break;
                case "47"://潜在客户公海
                    intent = new Intent(context,CrmCustomFollowActivity.class);
                    intent.putExtra("type","OPENSEA");
                    context.startActivity(intent);
                    break;
                case "46"://潜在客户跟进
                    intent = new Intent(context,CrmCustomFollowActivity.class);
                    intent.putExtra("type","FOLLOW");
                    context.startActivity(intent);
                    break;
                case "45"://潜在客户管理
                    intent = new Intent(context,PotentialCustomerManagerActivity.class);
                    intent.putExtra("type",TYPE_POTENTIAL);
                    context.startActivity(intent);
                    break;
                case "43"://客户地图
                    intent = new Intent(context,CrmCustomMapActivity.class);
                    context.startActivity(intent);
                    break;
                case "44"://客户管理
                    intent = new Intent(context,PotentialCustomerManagerActivity.class);
                    intent.putExtra("type",TYPE_CUSTOM);
                    context.startActivity(intent);
                    break;
                case "58"://合同
                    intent = new Intent(context,CrmContractActivity.class);
                    context.startActivity(intent);
                    break;
                case "59"://商机
                    intent = new Intent(context,CrmBusinessOpportunityActivity.class);
                    context.startActivity(intent);
                    break;
                case "60"://市场活动
                    intent = new Intent(context,MarketingActivity.class);
                    context.startActivity(intent);
                    break;
                case "61"://销售线索
                    intent = new Intent(context,SalesLeadActivity.class);
                    context.startActivity(intent);
                    break;
                case "62":
                    intent = new Intent(context,CustomDuplicateCheckingActivity.class);
                    context.startActivity(intent);
                    break;
                /** ERP */
                case "48"://供应商管理
                    intent = new Intent(context, SupplierManagerActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "49"://采购订单
                    intent = new Intent(context, PurchaseOrderActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "50"://仓库管理
                    intent = new Intent(context, StoresManagerActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "51"://货位管理
                    intent = new Intent(context, CargoLocationManagerActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "52"://商品管理
                    intent = new Intent(context, GoodsManagerActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "53"://库存管理
                    intent = new Intent(context, StockManagerActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "54"://商品入库
                    intent = new Intent(context, GoodsInStoreActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "55"://商品出库
                    intent = new Intent(context, GoodsOutStoreActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                case "56"://销售订单
                    intent = new Intent(context, SalesOrderActivity.class);
                    intent.putExtra("flag","");
                    context.startActivity(intent);
                    break;
                default:
                    intent = new Intent(context, WebActivity.class);
                    intent.putExtra("url",list.get(itemId).getUrl());
                    context.startActivity(intent);
                    break;
           // }
        }
    }
}
