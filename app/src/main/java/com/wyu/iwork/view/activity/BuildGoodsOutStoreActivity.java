package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.OptionsModel;
import com.wyu.iwork.model.OutStoreDetailModel;
import com.wyu.iwork.model.SaleOrderModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateOutStoreUtil;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 新建商品出库
 */
public class BuildGoodsOutStoreActivity extends BaseActivity {
    private static final String TAG = BuildGoodsOutStoreActivity.class.getSimpleName();

    public final static int OutStoreCode = 0x101;
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    @BindView(R.id.activity_build_goods_out_store_committ)
    TextView btn_commit;

    /**
     * 出库编号
     */
    @BindView(R.id.activity_build_goods_out_store_outOrderNum)
    CustomeViewGroup outOrderNum;

    /**
     * 订单编号
     */
    @BindView(R.id.activity_build_goods_out_store_orderNum)
    CustomeViewGroup OrderNum;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_goods_out_store_GoodsName)
    CustomeViewGroup GoodsName;

    /**
     * 商品数量
     */
    @BindView(R.id.activity_build_goods_out_store_goodsNum)
    CustomeViewGroup goodsNum;

    /**
     * 客户名称
     */
    @BindView(R.id.activity_build_goods_out_store_customeName)
    CustomeViewGroup customeName;

    /**
     * 联系方式
     */
    @BindView(R.id.activity_build_goods_out_store_phone)
    CustomeViewGroup phone;

    /**
     * 发货方式
     */
    @BindView(R.id.activity_build_goods_out_store_send_type)
    CustomeViewGroup send_type;


    /**
     * 出库状态
     */
    @BindView(R.id.activity_build_goods_out_store_out_state)
    CustomeViewGroup out_state;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_goods_out_store_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_goods_out_store_releaseTime)
    CustomeViewGroup releaseTime;
    private UserInfo userInfo;

    /**
     * 出库状态的弹窗
     */
    private Dialog Procurement_dialog;

    /**
     * 出库状态的view
     */
    private View dialogView;

    //正在揽活
    private TextView sail_textview;
    //完成出库
    private TextView dayly_textview;
    //取消
    private TextView other_textview;

    //新增出库详情model
    private OutStoreDetailModel addmodel = new  OutStoreDetailModel();

    //销售订单id 出库状态
    private String sale_id = "",status = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_goods_out_store);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("新建出库");
        edit.setVisibility(View.GONE);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        ProcurementPop();
    }

    @OnClick({R.id.action_back,R.id.activity_build_goods_out_store_orderNum,R.id.activity_build_goods_out_store_out_state,R.id.activity_build_goods_out_store_committ})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.activity_build_goods_out_store_orderNum://选择订单
                Intent intent = new Intent(this,SalesOrderActivity.class);
                intent.putExtra("flag","BuildGoodsOutStoreActivity");
                startActivityForResult(intent,OutStoreCode);
            break;
            case R.id.activity_build_goods_out_store_out_state://选择出库状态
                if (Procurement_dialog!=null){
                    Procurement_dialog.show();
                }
            break;
            case R.id.activity_build_goods_out_store_committ://提交

                if (sale_id.isEmpty()){
                    Toast.makeText(BuildGoodsOutStoreActivity.this,"订单编号不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (status.isEmpty()){
                    Toast.makeText(BuildGoodsOutStoreActivity.this,"出库状态不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }

                addmodel.setId("0");
                UpdateOutStoreUtil.initParama(this,callback(),addmodel);

            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==OutStoreCode&&data!=null){
            SaleOrderModel model = (SaleOrderModel) data.getExtras().get("model");
            OrderNum.setRightText(model.getSole_id());
            GoodsName.setRightText(model.getGoods_name());
            goodsNum.setRightText(model.getGoods_num());
            customeName.setRightText(model.getCustomer_name());
            phone.setRightText(model.getCustomer_phone());
            //   发货方式 1：物流快递 2：上门自提 3送货上门
            switch (model.getDeliver()){
                case "1":
                    send_type.setRightText("物流快递");
                    break;

                case "2":
                    send_type.setRightText("上门自提");
                    break;

                case "3":
                    send_type.setRightText("3送货上门");
                    break;
            }

            sale_id = model.getId();
            addmodel.setSale_id(model.getId());
        }
    }

    /**
     * 出库状态的弹窗
     */
    private void ProcurementPop(){
        if(dialogView==null){
            dialogView = LayoutInflater.from(this).inflate(R.layout.layout_pop_procurement_type,null);
            sail_textview = (TextView) dialogView.findViewById(R.id.procurement_type_saild_type);
            dayly_textview  = (TextView) dialogView.findViewById(R.id.procurement_type_dayly_type);
            other_textview = (TextView) dialogView.findViewById(R.id.procurement_type_others);
            sail_textview.setText("正在拣货");
            dayly_textview.setText("完成出库");
            other_textview.setVisibility(View.GONE);
        }
        if (dialogView != null) {
            Procurement_dialog = new Dialog(this, R.style.custom_dialog);
            Procurement_dialog.setCancelable(true);//不能点外面取消,也不 能点back取消
            Procurement_dialog.setContentView(dialogView);
            Window dialogWindow = Procurement_dialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            Procurement_dialog.getWindow().setWindowAnimations(R.style.pickerview_dialogAnim1);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
        }
        sail_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
                out_state.setRightText("正在拣货");
                addmodel.setStatus("1");
                status = "1";
            }
        });

        dayly_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
                out_state.setRightText("完成出库");
                addmodel.setStatus("2");
              status = "2";
            }
        });
        other_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
            }
        });
    }


    //新建出库回调
    private DialogCallback callback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG,e.getMessage());
            }
            @Override
            public void onSuccess(String s, Call call, Response response) {

                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        finish();
                    }
                    Toast.makeText(BuildGoodsOutStoreActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
