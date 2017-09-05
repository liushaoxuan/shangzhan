package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.BusinessCardInfo;
import com.wyu.iwork.model.MineCardModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.EditCardItem;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.id.edit_address;
import static com.wyu.iwork.R.id.edit_company;
import static com.wyu.iwork.R.id.edit_department;
import static com.wyu.iwork.R.id.edit_email;
import static com.wyu.iwork.R.id.edit_name;
import static com.wyu.iwork.R.id.edit_phone;
import static com.wyu.iwork.R.id.edit_position;
import static com.wyu.iwork.R.id.edit_wechat;
import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

public class CardEditNewActivity extends BaseActivity {

    private static final String TAG = CardEditNewActivity.class.getSimpleName();

    //姓名
    @BindView(edit_name)
    EditCardItem name;

    //电话
    @BindView(edit_phone)
    EditCardItem phone;

    //邮箱
    @BindView(edit_email)
    EditCardItem email;

    //微信
    @BindView(edit_wechat)
    EditCardItem wechat;

    //职位
    @BindView(edit_position)
    EditCardItem position;

    //公司
    @BindView(edit_company)
    EditCardItem company;

    //部门
    @BindView(edit_department)
    EditCardItem department;

    //地址
    @BindView(edit_address)
    EditCardItem address;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_edit)
    TextView edit;

    private Gson gson;
    private MineCardModel.MineCard model;
    private String cardId;
    private BusinessCardInfo info;
    private static final int RESULT_RECT_CAMERA = 200;


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit_new);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    private void getExtras(){
        Intent intent = getIntent();
        cardId = intent.getStringExtra("id");
        if("-1".equals(cardId)){
            info = (BusinessCardInfo) intent.getSerializableExtra("info");
            setData();
        }else{
            initMineCardMessage(cardId);
        }
    }

    private void setData(){
        try {
            setText(info.getData().getPhone(),phone);
            setText(info.getData().getUser_name(),name);
            setText(info.getData().getEmail(),email);
            setText(info.getData().getWechat(),wechat);
            setText(info.getData().getPosition(),position);
            setText(info.getData().getCompany_name(),company);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initMineCardMessage(String cardId){
        UserInfo user = AppManager.getInstance(this).getUserInfo();
        /**
         * user_id	是	int[11]用户自己的ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        /**
         * card_id	是	int[11]名片的ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.card_id)
         */
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = "";
        HashMap<String,String> data = new HashMap<>();
        data.put("card_id",cardId);
        Sign = Md5Util.getSign(F+V+RandStr+cardId);

        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = "";
        murl = RequestUtils.getRequestUrl(Constant.URL_CARD_DETAIL,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseAndSetMineData(s);
                    }
                });
    }

    private void parseAndSetMineData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            model = gson.fromJson(data, MineCardModel.class).getData();
            setMessage(model);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView(){

        title.setText("编辑名片");
        edit.setText("保存");
        edit.setVisibility(View.VISIBLE);

        //初始化每一个item的标题
        initTitle();
        //设置不显示底线的item
        initBottomLineGone();
        //设置每一个item对应的行数
        initLineNumber();
        //设置Grayity为right的item
        initGravityRight();

        setInputType();
    }

    @OnClick({R.id.ll_back,R.id.tv_edit})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_edit:
                saveData();
                break;
        }
    }

    private void initTitle(){
        name.setTitle("姓名");
        phone.setTitle("手机");
        email.setTitle("邮箱");
        wechat.setTitle("微信");
        position.setTitle("职位");
        company.setTitle("公司");
        department.setTitle("部门");
        address.setTitle("地址");
    }

    private void setInputType(){
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        email.setDiaitals();
        wechat.setDiaitals();
    }

    private void initBottomLineGone(){
        wechat.setBottomLineVisible(false);
    }

    private void initLineNumber(){
        name.setValueSingleLine();
        phone.setValueSingleLine();
        email.setValueSingleLine();
        wechat.setValueSingleLine();
        position.setValueSingleLine();
        company.setValueSingleLine();
        department.setValueSingleLine();
        address.setValueSingleLine();
    }

    private void initGravityRight(){
        address.setGravityRight();
    }

    private void setMessage(MineCardModel.MineCard model){
        setText(model.getPhone(),phone);
        setText(model.getName(),name);
        setText(model.getEmail(),email);
        setText(model.getWechat_id(),wechat);
        setText(model.getJob(),position);
        setText(model.getCompany(),company);
        setText(model.getDepartment(),department);
        setText(model.getAddress(),address);
    }

    private void setText(String str,EditCardItem item){
        if(!TextUtils.isEmpty(str)){
            item.setValue(str);
        }
    }

    private void saveData(){
        String nameStr = name.getValue();
        String phoneStr = phone.getValue();
        if(TextUtils.isEmpty(nameStr) || TextUtils.isEmpty(phoneStr)){
            showRedmineDialog();
            return;
        }
        /**
         * user_id	是	int[11]           用户自己的ID
         card_id	      是	int[11]           修改的名片ID  新增传0, 修改用户自己名片传0
         name	      是	string[8]         名片信息：姓名
         phone	      否	int[10]           名片信息：手机  1
         email	      否	string[30]        名片信息：邮箱  1
         wechat_id	否	string[30]        名片信息：微信  1
         job	      否	string[100]       名片信息：职位  1
         company	      否	string[100]       名片信息：公司  1
         department	否	string[100]       名片信息：部门  1
         address	      否	string[200]       名片信息：地址  1
         status	      是	int[1]            所编辑名片的状态（0：他人的名片，1：自己的名片） 1
         card_img	      否	string[200]       名片图片地址
         F	            是	string[18]        请求来源：IOS/ANDROID/WEB
         V	            是	string[20]        版本号如：1.0.1
         RandStr	      是	string[50]        请求加密随机数 time().|.rand()
         Sign	      是	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.user_id.status)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+"0");
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        if("-1".equals(cardId)){
            data.put("card_id","0");
        }else{
            data.put("card_id",cardId);
        }
        data.put("name",nameStr);
        data.put("phone",phoneStr);
        if(!TextUtils.isEmpty(email.getValue())){
            data.put("email",email.getValue());
        }
        if(!TextUtils.isEmpty(wechat.getValue())){
            data.put("wechat_id",wechat.getValue());
        }
        if(!TextUtils.isEmpty(position.getValue())){
            data.put("job",position.getValue());
        }
        if(!TextUtils.isEmpty(company.getValue())){
            data.put("company",company.getValue());
        }
        if(!TextUtils.isEmpty(department.getValue())){
            data.put("department",department.getValue());
        }
        if(!TextUtils.isEmpty(address.getValue())){
            data.put("address",address.getValue());
        }
        data.put("status","0");
        data.put("F", F);
        data.put("V", V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_EDIT_CARD,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        handleOption(s);
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void handleOption(String s){
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            if("0".equals(object.getString("code"))){
                if("-1".equals(cardId)){
                    setResult(RESULT_RECT_CAMERA);
                }
                finish();
            }else{
                MsgUtil.shortToastInCenter(this,object.getString("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, "名片修改信息未填写完整\n请完成填写并提交", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

}
