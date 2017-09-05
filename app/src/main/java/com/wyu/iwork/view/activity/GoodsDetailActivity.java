package com.wyu.iwork.view.activity;

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
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.GoodsDetailModel;
import com.wyu.iwork.model.GoodsTypeModel;
import com.wyu.iwork.model.InStoreDetailModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Erp_DeleteUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.UpdateGoodsUtil;
import com.wyu.iwork.util.getGoodsType;
import com.wyu.iwork.view.dialog.PickerViewDialog;
import com.wyu.iwork.widget.CustomeViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author  sxliu
 * 商品详情
 */
public class GoodsDetailActivity extends BaseActivity {

    private static final String TAG = GoodsDetailActivity.class.getSimpleName();

    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    @BindView(R.id.activity_goods_detail_ediet)
    TextView btn_edit;

    /**
     * 商品编号
     */
    @BindView(R.id.activity_goods_detail_goods_num)
    CustomeViewGroup goods_num;

    /**
     * 商品名称
     */
    @BindView(R.id.activity_goods_detail_goods_name)
    CustomeViewGroup goods_name;

    /**
     * 商品分类
     */
    @BindView(R.id.activity_goods_detail_goods_type)
    CustomeViewGroup goods_type;

    /**
     * 商品价格
     */
    @BindView(R.id.activity_goods_detail_goods_prise)
    CustomeViewGroup goods_prise;

    /**
     * 市场价格
     */
    @BindView(R.id.activity_goods_detail_goods_market_prise)
    CustomeViewGroup market_prise;

    /**
     * 创建人
     */
    @BindView(R.id.activity_goods_detail_releaser)
    CustomeViewGroup releaser;


    /**
     * 创建时间
     */
    @BindView(R.id.activity_goods_detail_releasertime)
    CustomeViewGroup releasertime;

    //商品id
    private String goods_id = "";

    //分类id
    private String type_id = "";

    //请求地址
    private String url = Constant.URL_GOODS_DETAIL;
    //删除url
    private String del_url = Constant.URL_GOODS_DEL;

    //商品详情model
    private GoodsDetailModel model;

    private List<CustomeViewGroup> viewGroups = new ArrayList<>();

    /**
     *  商品分类集合
     */
    private List<GoodsTypeModel> typeModels   = new ArrayList<>();

    //分类选择器
    private PickerViewDialog pvOptions;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        getId();
        getSupportActionBar().hide();
        init();
        initDetaiParama();
        getGoodsType.getGoodsType(this,getTypeCallback());
    }

    //获取商品id
    private  void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            goods_id = bundle.getString("id");
        }
    }

    //一些初始化操作
    private void init(){
        edit.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
        title.setText("商品详情");
        viewGroups.add(goods_name);
        viewGroups.add(goods_prise);
        viewGroups.add(market_prise);
    }

    /**
     *
     * 初始化分类选择器
     */
    private void initPicker(){
        pvOptions = new PickerViewDialog(this, typeModels, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = typeModels.get(options1).getPickerViewText();
                goods_type.setRightText(tx);
                type_id = typeModels.get(options1).getId();
            }
        });
    }

    @OnClick({R.id.action_back,R.id.action_edit,R.id.activity_goods_detail_ediet,R.id.activity_goods_detail_goods_type})
    void click(View v){
        String text = btn_edit.getText().toString();
        switch (v.getId()){
            case R.id.action_back://
                onBackPressed();
                break;
            case R.id.action_edit://删除
                Erp_DeleteUtil.init_delParama(this,del_url,"goods_id",goods_id,delcallback());
                break;
            case R.id.activity_goods_detail_goods_type://商品分类
                if ("保存".equals(text)){
                    if (pvOptions!=null){
                        pvOptions.show_Options();
                        Hideinputwindown(title);
                    }
                }

                break;
            case R.id.activity_goods_detail_ediet://编辑
                if ("编辑".equals(text)){
                    btn_edit.setText("保存");
                    for (int i=0;i<viewGroups.size();i++){
                        viewGroups.get(i).is_ShowInput(true);
                        viewGroups.get(i).isShowStar(true);
                        viewGroups.get(i).is_showRightText(false);
                        goods_type.isShowStar(true);
                        goods_type.is_Showarrow(true);
                        viewGroups.get(i).setInputContent(viewGroups.get(i).getRightText());
                        if (i!=0){
                            viewGroups.get(i).setInputType(InputType.TYPE_CLASS_NUMBER);
                        }

                    }
                }else {


                    String name = goods_name.getInput();
                    String price = goods_prise.getInput();
                    String mar_prise = market_prise.getInput();
                    if (name.isEmpty()){
                        Toast.makeText(GoodsDetailActivity.this,"商品名称不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (type_id.isEmpty()){
                        Toast.makeText(GoodsDetailActivity.this,"商品分类不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (price.isEmpty()){
                        Toast.makeText(GoodsDetailActivity.this,"商品价格不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (mar_prise.isEmpty()){
                        Toast.makeText(GoodsDetailActivity.this,"市场价格价格不能为空",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    UpdateGoodsUtil.initParama(this,editcallback(),name,price,mar_prise,type_id, goods_id);

                }
                break;
        }
    }


    //初始化请求参数
    private void initDetaiParama(){
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id + goods_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("goods_id", goods_id);
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //获取入库详情的请求回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = object.optJSONObject("data");
                        model = JSON.parseObject(data.toString(),GoodsDetailModel.class);
                        initviewDatas();
                    }else {
                        Toast.makeText(GoodsDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }


    //删除的请求回调
    private DialogCallback delcallback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {

                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                      finish();
                    }else {
                        Toast.makeText(GoodsDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            goods_num.setRightText(model.getSole_id());
            goods_num.setInputContent(model.getSole_id());

            goods_name.setRightText(model.getName());
            goods_name.setInputContent(model.getName());

            goods_type.setRightText(model.getType_name());
            goods_type.setInputContent(model.getType_name());
            goods_prise.setRightText(model.getPrice());
            goods_prise.setInputContent(model.getPrice());
            market_prise.setRightText(model.getMarket_price());
            market_prise.setInputContent(model.getMarket_price());
            releaser.setRightText(model.getCreater_name());
            releasertime.setRightText(model.getAdd_time());
            type_id = model.getType_id();
        }
    }




    //编辑商品回调
    private DialogCallback editcallback(){
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
                        btn_edit.setText("编辑");
                        for (int i=0;i<viewGroups.size();i++){
                            viewGroups.get(i).is_ShowInput(false);
                            viewGroups.get(i).is_showRightText(true);
                            viewGroups.get(i).isShowStar(false);
                            goods_type.isShowStar(false);
                            goods_type.is_Showarrow(false);
                            viewGroups.get(i).setInputContent(viewGroups.get(i).getRightText());
                            viewGroups.get(i).setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    }
                    Toast.makeText(GoodsDetailActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
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
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        JSONArray data = object.optJSONArray("data");
                        List<GoodsTypeModel> templist = JSON.parseArray(data.toString(),GoodsTypeModel.class);
                        typeModels.addAll(templist);
                        initPicker();
                    }
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
            btn_edit.setVisibility(View.VISIBLE);
        }
    }
}
