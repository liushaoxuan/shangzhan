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

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.GoodsDetailModel;
import com.wyu.iwork.model.OutStoreDetailModel;
import com.wyu.iwork.model.SaleOrderModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateOutStoreUtil;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 出库详情
 */

public class OutStoreDetailActivity extends BaseActivity {

    private static final String TAG = OutStoreDetailActivity.class.getSimpleName();

    public final static int OutStoreDetailCode = 0x1001;
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

    //正在揽货物
    private TextView sail_textview;
    //完成出库
    private TextView dayly_textview;
    //取消
    private TextView other_textview;

    //出库id
    private String store_out_id = "";

    private String url = Constant.URL_OUTSTORE_DETAIL;

    //出库详情model
    private OutStoreDetailModel model ;

    //编辑出库详情model
    private OutStoreDetailModel addmodel = new  OutStoreDetailModel();

    //销售订单id 出库状态
    private String sale_id = "",status = "";

    private String del_url = Constant.URL_OUTSTORE_DELETE;
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
        getId();
        getSupportActionBar().hide();
        init();
        initDetaiParama();    }

    //获取出库id
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            store_out_id = bundle.getString("id");
        }
    }
    /**
     * 一些初始化操作
     */
    private void init(){
        edit.setVisibility(View.GONE);
        btn_commit.setVisibility(View.GONE);
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("商品出库详情");
        outOrderNum.setVisibility(View.VISIBLE);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        btn_commit.setText("编辑");
        ProcurementPop();
        isedit(false);
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_build_goods_out_store_orderNum,R.id.activity_build_goods_out_store_out_state,R.id.activity_build_goods_out_store_committ})
    void Click(View v){
        String text = btn_commit.getText().toString();
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"store_out_id",store_out_id,del_callback());
                break;
            case R.id.activity_build_goods_out_store_orderNum://选择订单
                Intent intent = new Intent(this,SalesOrderActivity.class);
                intent.putExtra("flag","OutStoreDetailActivity");
                startActivityForResult(intent,OutStoreDetailCode);
                break;
            case R.id.activity_build_goods_out_store_out_state://选择出库状态
                if ("保存".equals(text)){
                    if (Procurement_dialog!=null){
                        Procurement_dialog.show();
                    }
                }
                break;
            case R.id.activity_build_goods_out_store_committ://提交
                if ("编辑".equals(text)){//编辑状态
                    btn_commit.setText("保存");
                    isedit(true);
                }else {


                    if (sale_id.isEmpty()){
                        Toast.makeText(OutStoreDetailActivity.this,"订单编号不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if (status.isEmpty()){
                        Toast.makeText(OutStoreDetailActivity.this,"出库状态不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    addmodel.setId(store_out_id);
                    UpdateOutStoreUtil.initParama(this,Editcallback(),addmodel);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==OutStoreDetailCode&&data!=null){
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
            addmodel.setSale_id(model.getGoods_id());
            addmodel.setSale_id(sale_id);
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
                status = "1";
            }
        });

        dayly_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Procurement_dialog.dismiss();
                out_state.setRightText("完成出库");
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

    //是否在编辑
    private void isedit(boolean isedit){
        if (isedit){
            OrderNum.is_Showarrow(true);
            out_state.is_Showarrow(true);
            OrderNum.isShowStar(true);
            out_state.isShowStar(true);
        }else {
            OrderNum.is_Showarrow(false);
            OrderNum.isShowStar(false);
            out_state.isShowStar(false);
            out_state.is_Showarrow(false);
        }
    }


    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + store_out_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("store_out_id", store_out_id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取出库详情的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    Logger.e("出库详情",s);
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),OutStoreDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(OutStoreDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //删除出库的请求回调
    private DialogCallback del_callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                      finish();
                    }else {
                        Toast.makeText(OutStoreDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }

    //控件赋值
    private void initviewDatas(){
        if (model!=null){
            getPermission(model.getCreater_id());
            outOrderNum.setRightText(model.getSole_id());
            OrderNum.setRightText(model.getSale_sole_id());
            GoodsName.setRightText(model.getGoods_name());
            goodsNum.setRightText(model.getGoods_num());
            customeName.setRightText(model.getCustomer_name());
            phone.setRightText(model.getCustomer_phone());
            releaser.setRightText(model.getCreater_name());
            releaseTime.setRightText(model.getAdd_time());


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


            //出库状态  status出库状态 1：正在拣货 2：完成出库
            switch (model.getStatus()){
                case "1":
                out_state.setRightText("正在拣货");
                    break;

                case "2":
                    out_state.setRightText("完成出库");
                    break;
            }
            addmodel.setDeliver(model.getDeliver());

            addmodel.setStatus(model.getStatus());
            status = model.getStatus();
            sale_id = model.getSale_id();
            addmodel.setSale_id(sale_id);
        }
    }



    //新建出库回调
    private DialogCallback Editcallback(){
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
                        btn_commit.setText("编辑");
                        isedit(false);
                    }
                    Toast.makeText(OutStoreDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //权限判断  是管理员或者是自己新建的则可以编辑和删除
    private void getPermission(String user_id){
        String admin = userInfo.getIs_admin(); //是否是管理员用户  0：否，1：是
        if ("1".equals(admin)||userInfo.getUser_id().equals(user_id)){
            edit.setVisibility(View.VISIBLE);
        }
    }

}
