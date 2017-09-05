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
import com.wyu.iwork.model.CargoModel;
import com.wyu.iwork.model.GoodsModel;
import com.wyu.iwork.model.StockDetailModel;
import com.wyu.iwork.model.StoreModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateStockUtil;
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
 * 新建库存
 */
public class BuildStockActivity extends BaseActivity {

    private static final String TAG = BuildStockActivity.class.getSimpleName();
    //商品编号
    public static final int CODE_GOODS_NUM = 0x0001;
    //仓库编号
    public static final int CODE_STORE_NUM = 0x0002;
    //货位编号
    public static final int CODE_CARGO_NUM = 0x0003;

    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;
    /**
     * 编辑 保存
     */
    @BindView(R.id.activity_build_stock_commit)
    TextView btn_edit;

    /**
     * 商品编号
     */
    @BindView(R.id.activity_build_stock_goodsNum)
    CustomeViewGroup goodsNum;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_stock_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_build_stock_goodsPrise)
    CustomeViewGroup goodsPrise;

    /**
     * 库存数量
     */
    @BindView(R.id.activity_build_stock_goodsCount)
    CustomeViewGroup goodsCount;

    /**
     * 仓库编号
     */
    @BindView(R.id.activity_build_stock_storeNum)
    CustomeViewGroup storeNum;

    /**
     * 货位编号
     */
    @BindView(R.id.activity_build_stockcargoNum)
    CustomeViewGroup stockcargoNum;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_stock_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_stock_releaseTime)
    CustomeViewGroup releaseTime;

    private UserInfo userInfo;

    //新增库存的model
    private StockDetailModel addModel = new StockDetailModel();

    //控件集合
     private List<CustomeViewGroup> viewGroupList = new ArrayList<>();


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_stock);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        getSupportActionBar().hide();
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("新建库存");
        btn_edit.setText("提交");
        edit.setVisibility(View.GONE);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        viewGroupList.add(goodsNum);
        viewGroupList.add(goodsName);
        viewGroupList.add(goodsPrise);
        viewGroupList.add(goodsCount);
        viewGroupList.add(storeNum);
        viewGroupList.add(stockcargoNum);
    }

    @OnClick({R.id.action_back,R.id.activity_build_stock_goodsNum,R.id.activity_build_stock_storeNum,R.id.activity_build_stockcargoNum,R.id.activity_build_stock_commit})
    void CLick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.activity_build_stock_goodsNum://商品编号
                intent = new Intent(this,GoodsManagerActivity.class);
                intent.putExtra("flag","BuildStockActivity");
                startActivityForResult(intent,CODE_GOODS_NUM);
                break;
            case R.id.activity_build_stock_storeNum://仓库编号
                intent = new Intent(this,StoresManagerActivity.class);
                intent.putExtra("flag","BuildStockActivity");
                startActivityForResult(intent,CODE_STORE_NUM);
                break;
            case R.id.activity_build_stockcargoNum://货位编号
                intent = new Intent(this,CargoLocationManagerActivity.class);
                intent.putExtra("flag","BuildStockActivity");
                startActivityForResult(intent,CODE_CARGO_NUM);
                break;
            case R.id.activity_build_stock_commit://提交
                for (int i = 0;i<viewGroupList.size();i++){
                    String text = viewGroupList.get(i).getRightText();
                    if (text.isEmpty()||"请选择".equals(text)){
                        Toast.makeText(BuildStockActivity.this,viewGroupList.get(i).getNametext()+"不能为空",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addModel.setId("0");
                UpdateStockUtil.initParama(this,callback(),addModel);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            switch (resultCode){
                case CODE_GOODS_NUM://商品编号
                    GoodsModel goodsModel = (GoodsModel) data.getExtras().get("model");
                    goodsNum.setRightText(goodsModel.getSole_id());
                    goodsName.setRightText(goodsModel.getName());
                    goodsPrise.setRightText(goodsModel.getPrice());
                    goodsCount.setRightText(goodsModel.getNum());
                    addModel.setGoods_num(goodsModel.getSole_id());
                    addModel.setGoods_id(goodsModel.getId());
                    break;
                case CODE_STORE_NUM://仓库编号
                    StoreModel storeModel = (StoreModel) data.getExtras().get("model");
                    storeNum.setRightText(storeModel.getSole_id());

                    addModel.setStore_id(storeModel.getId());
                    break;
                case CODE_CARGO_NUM://货位编号
                    CargoModel cargoModel = (CargoModel) data.getExtras().get("model");
                    stockcargoNum.setRightText(cargoModel.getSole_id());
                    addModel.setStore_goods_id(cargoModel.getId());
                    break;
            }
        }

    }


    //新建库存回调
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
                    Toast.makeText(BuildStockActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

}
