package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MineCardModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * 扫描二维码结果
 */
public class ScanCardResultActivity extends BaseActivity {

    private static final String TAG = ScanCardResultActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.mine_name)
    TextView mine_name;

    @BindView(R.id.mine_company)
    TextView mine_company;

    @BindView(R.id.mine_telephone)
    TextView mine_telephone;

    @BindView(R.id.mine_email)
    TextView mine_email;

    @BindView(R.id.mine_avatar)
    CircleImageView mine_avatar;

    @BindView(R.id.civ_image)
    TextView civ_image;

    @BindView(R.id.edit)
    ImageView edit;

    @BindView(R.id.tv_add_card)
    TextView tv_add_card;

    private String mCardId;
    private String type;
    private Gson gson;
    private MineCardModel model;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_card_result);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    private void initView(){
        title.setText("扫描结果");
        edit.setVisibility(View.GONE);
    }

    private void getExtras(){
        /**
         * intent.putExtra("type","5");
         intent.putExtra("id",qrCodeModule.getData().getText());
         */
        mCardId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");//5 表示名片跳转    6表示用户个人信息跳转
        Logger.i(TAG,mCardId);
        if("5".equals(type)){
            //获取名片信息
            getCardMessage();
        }else{
            //获取个人信息
            getUserInfo();
        }
    }

    @OnClick({R.id.ll_back,R.id.tv_add_card})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_add_card:
                exchangeCard();
                break;
        }
    }

    private void getCardMessage(){
        /**
         * card_id	是	int[11]           名片的ID
         F	            是	string[18]        请求来源：IOS/ANDROID/WEB
         V	            是	string[20]        版本号如：1.0.1
         RandStr	      是	string[50]        请求加密随机数 time().|.rand()
         Sign	      是	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.card_id)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("card_id",mCardId);
        data.put("F",F);
        data.put("V",V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+mCardId);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_CARD_DETAIL,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s, call, response);
                Logger.i(TAG,s);
                parseCardData(s);
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

    private void parseCardData(String data){
        if(gson == null){
            gson = new Gson();
        }
        try {
            model = gson.fromJson(data,MineCardModel.class);
            setMineData(model);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMineData(MineCardModel model){
        checkStr(model.getData().getName(),mine_name);
        checkStr("联系电话："+model.getData().getPhone(),mine_telephone);
        checkStr(model.getData().getEmail(),mine_email);
        checkStr(model.getData().getCompany(),mine_company);
        if("5".equals(type)){
            mine_avatar.setVisibility(View.GONE);
            civ_image.setVisibility(View.VISIBLE);
            try {
                civ_image.setText(model.getData().getName().substring(0,1));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            mine_avatar.setVisibility(View.VISIBLE);
            civ_image.setVisibility(View.GONE);
            Glide.with(this).load(model.getData().getFace_img()).dontAnimate().placeholder(R.mipmap.def_img_rect).into(mine_avatar);
        }
    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    private void getUserInfo(){
        /**
         * user_id	是	string[18]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+mCardId);
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",mCardId);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_MINE_CARD,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT).execute(new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                Logger.i(TAG,s);
                parseCardData(s);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
            }
        });
    }

    //名片转存
    private void exchangeCard(){
        /**
         * URL_CARD_SHARD_DATA_IN_MYCARD
         * user_id	是	int[11]用户ID
         type	是	int[11]类别 1:保存的名片ID  2:保存的用户ID
         save_id	是	int[11]用户或名片ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.card_id)
         */
//5 表示名片跳转    6表示用户个人信息跳转
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        if("5".equals(type)){
            data.put("type","1");
        }else if("6".equals(type)){
            data.put("type","2");
        }
        data.put("save_id",mCardId);
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_CARD_SHARD_DATA_IN_MYCARD,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                startActivity(new Intent(ScanCardResultActivity.this,CardNewActivity.class));
                                finish();
                            }
                            MsgUtil.shortToast(ScanCardResultActivity.this,object.getString("msg"));
                        }catch (Exception e){
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
