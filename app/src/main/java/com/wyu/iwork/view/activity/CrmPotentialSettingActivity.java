package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 客户-设置公海超出时间
 */
public class CrmPotentialSettingActivity extends BaseActivity {

    private static final String TAG = CrmPotentialSettingActivity.class.getSimpleName();
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.tv_activity_crm_potential_setting)
    TextView tv_activity_crm_potential_setting;
    private String mDefConf;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm_potential_setting);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    //获取之前设置的默认公海时限
    private void getExtras(){
        mDefConf = getIntent().getStringExtra("conf");
    }

    private void initView(){
        tv_edit.setText("保存");
        tv_edit.setVisibility(View.VISIBLE);
        tv_title.setText("设置");
        tv_activity_crm_potential_setting.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        if(!TextUtils.isEmpty(mDefConf)){
            tv_activity_crm_potential_setting.setText(mDefConf);
        }
    }

    @OnClick({R.id.tv_edit,R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.tv_edit:
                commitData();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    //上传公海设置
    private void commitData(){
        /**
         * URL_UPDATE_HIGH_SEAS_CONF
         * user_id	是	int[11]用户ID
         conf	是	int[2]设置超出时间
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.conf)
         */
        String conf = tv_activity_crm_potential_setting.getText().toString();
        if(TextUtils.isEmpty(conf)){
            MsgUtil.shortToastInCenter(this,"请填写期限!");
            return;
        }
        /**
         * user_id	是	int[11]用户ID
         conf	是	int[2]设置超出时间
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id.conf)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(Constant.F+Constant.V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+conf);
        OkGo.get(Constant.URL_UPDATE_HIGH_SEAS_CONF).tag(this).cacheMode(CacheMode.DEFAULT)
                .params("user_id",AppManager.getInstance(this).getUserInfo().getUser_id())
                .params("conf",conf)
                .params("F",Constant.F)
                .params("V",Constant.V)
                .params("RandStr",RandStr)
                .params("Sign",Sign)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            if("0".equals(object.getString("code"))){
                                finish();
                            }else {
                                MsgUtil.shortToastInCenter(CrmPotentialSettingActivity.this,object.getString("msg"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
