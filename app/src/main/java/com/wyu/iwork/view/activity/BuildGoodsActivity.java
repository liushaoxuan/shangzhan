package com.wyu.iwork.view.activity;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CargoModel;
import com.wyu.iwork.model.GoodsTypeModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.DateUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.UpdateGoodsUtil;
import com.wyu.iwork.util.getGoodsType;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
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
 * 新建商品
 */
public class BuildGoodsActivity extends BaseActivity {

    private static final String TAG = BuildGoodsActivity.class.getSimpleName();
    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_build_goods_goodsName)
    CustomeViewGroup goodsName;

    /**
     * 商品分类
     */
    @BindView(R.id.activity_build_goods_goodsType)
    CustomeViewGroup goodsType;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_build_goods_goodsprise)
    CustomeViewGroup goodsprise;

    /**
     * 市场价格
     */
    @BindView(R.id.activity_build_goods_marketPrise)
    CustomeViewGroup marketPrise;

    /**
     * 创建人
     */
    @BindView(R.id.activity_build_goods_releaser)
    CustomeViewGroup releaser;

    /**
     * 创建时间
     */
    @BindView(R.id.activity_build_goods_releaseTime)
    CustomeViewGroup releaseTime;

    private UserInfo userInfo;
    /**
     * 分类选择器
     */
    private PickerViewDialog pvOptions;
    /**
     *  商品分类集合
     */
    private List<GoodsTypeModel> typeModels  = new ArrayList<>();

    //分类id
    private String type_id = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_goods);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        init();
        getGoodsType.getGoodsType(this,getTypeCallback());
    }

    /**
     * 一些初始化操作
     */
    private void init(){
        typeModels = new ArrayList<>();
        userInfo = AppManager.getInstance(this).getUserInfo();
        title.setText("新建商品");
        edit.setVisibility(View.GONE);
        releaser.setRightText(userInfo.getUser_name());
        releaseTime.setRightText(DateUtil.ChartTime(System.currentTimeMillis()));
        goodsprise.setInputType(InputType.TYPE_CLASS_NUMBER);
        marketPrise.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    @OnClick({R.id.action_back,R.id.activity_build_goods_goodsType,R.id.activity_build_goods_commit})
    void Click(View v){
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.activity_build_goods_goodsType://商品分类
                if (pvOptions!=null){
                    pvOptions.show_Options();
                    Hideinputwindown(title);
                }
                break;

            case R.id.activity_build_goods_commit://提交
                String name = goodsName.getInput();
                String price = goodsprise.getInput();
                String mar_prise = marketPrise.getInput();
                if (name.isEmpty()){
                    Toast.makeText(BuildGoodsActivity.this,"商品名称不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (type_id.isEmpty()){
                    Toast.makeText(BuildGoodsActivity.this,"商品分类不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (price.isEmpty()){
                    Toast.makeText(BuildGoodsActivity.this,"商品价格不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (mar_prise.isEmpty()){
                    Toast.makeText(BuildGoodsActivity.this,"市场价格价格不能为空",Toast.LENGTH_SHORT).show();
                    break;
                }

                UpdateGoodsUtil.initParama(this,callback(),name,price,mar_prise,type_id,"0");
                break;
        }
    }
    private void initCustomOptionPicker() {//条件选择器初始化，自定义布局
        pvOptions = new PickerViewDialog(this, typeModels, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = typeModels.get(options1).getPickerViewText();
                goodsType.setRightText(tx);
                type_id = typeModels.get(options1).getId();
            }
        });

    }

    //新建商品回调
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
                    Toast.makeText(BuildGoodsActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    //获取商品分类回调
    private DialogCallback getTypeCallback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    Logger.e("goods",s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        JSONArray data = object.optJSONArray("data");
                        List<GoodsTypeModel> templist = JSON.parseArray(data.toString(),GoodsTypeModel.class);
                        typeModels.addAll(templist);
                        initCustomOptionPicker();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
