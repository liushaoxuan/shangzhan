package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.PurchaseInModel;
import com.wyu.iwork.model.PurchaseModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateInStoreUtil;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 新建商品入库
 */
public class BuildGoodsInstoreActivity extends BaseActivity {

    private static final String TAG = BuildGoodsInstoreActivity.class.getSimpleName();
    public final static int InStoreCode = 0x1;
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;
    /**
     * 编辑保存
     */
    @BindView(R.id.activity_build_goods_instore_committ)
    TextView btn_edit;

    /**
     * 入库单号
     */
    @BindView(R.id.activity_build_goods_instore_inOrderNum)
    CustomeViewGroup inOrderNum;

    /**
     * 采购单号
     */
    @BindView(R.id.activity_build_goods_instore_buy_order)
    CustomeViewGroup buy_order;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_goods_instore_GoodsName)
    CustomeViewGroup GoodsName;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_build_goods_instore_StoreNum)
    CustomeViewGroup StoreNum;

    /**
     * 货位编号
     */
    @BindView(R.id.activity_build_goods_instore_CargoNum)
    CustomeViewGroup CargoNum;

    /**
     * 入库数量
     */
    @BindView(R.id.activity_build_goods_instore_instoreNums)
    CustomeViewGroup instoreNums;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_build_goods_instore_Goodsprise)
    CustomeViewGroup Goodsprise;

    /**
     * 折扣
     */
    @BindView(R.id.activity_build_goods_instore_disscount)
    CustomeViewGroup disscount;

    /**
     * 总金额
     */
    @BindView(R.id.activity_build_goods_instore_total)
    CustomeViewGroup total;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_goods_instore_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_goods_instore_releaseTime)
    CustomeViewGroup releaseTime;

    private UserInfo userInfo;

    //采购订单号
    private String purchase_id = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_goods_instore);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("新建入库");
        btn_edit.setText("提交");
        edit.setVisibility(View.GONE);
        inOrderNum.setVisibility(View.GONE);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
    }

    @OnClick({R.id.action_back,R.id.activity_build_goods_instore_buy_order,R.id.activity_build_goods_instore_committ})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.activity_build_goods_instore_buy_order://采购单号
                Intent intent = new Intent(this,PurchaseOrderInActivity.class);
                intent.putExtra("flag","BuildGoodsInstoreActivity");
                startActivityForResult(intent,InStoreCode);
                break;

            case R.id.activity_build_goods_instore_committ://提交
                if (purchase_id.isEmpty()){
                    Toast.makeText(BuildGoodsInstoreActivity.this,"采购订单号不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                UpdateInStoreUtil.initParama(this,callback(),purchase_id,"0");
                break;
        }
    }

    //新建入库回调
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
                    Toast.makeText(BuildGoodsInstoreActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==InStoreCode){
            PurchaseInModel model = (PurchaseInModel) data.getExtras().get("model");
            buy_order.setRightText(model.getSole_id());
            GoodsName.setRightText(model.getName());
            StoreNum.setRightText(model.getStore_sole_id());
            CargoNum.setRightText(model.getStore_goods_sole_id());
            instoreNums.setRightText(model.getNum());
            Goodsprise.setRightText(model.getPrice());
            disscount.setRightText(model.getDiscount());
            total.setRightText(model.getAmount());
            releaser.setRightText(model.getUser_name());
            releaseTime.setRightText(model.getAdd_time());
            purchase_id = model.getId();
        }
    }
}
