package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UnixStamp;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * 外出考勤打卡成功
 */
public class OutSignSuccessActivity extends BaseActivity {

    private static final String TAG = OutSignSuccessActivity.class.getSimpleName();

    //
    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.current_time)
    TextView current_time;

    @BindView(R.id.sign_address)
    TextView sign_address;

    @BindView(R.id.check_record)
    TextView check_record;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_sign_success);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
        initView();
    }

    private void initView(){
        title.setText("打卡");
    }

    private void getExtras(){
        Intent intent = getIntent();
        sign_address.setText(intent.getStringExtra("address"));
        getCurrentTime();
    }

    private void getCurrentTime(){
        /**
         * URL_NOW_UNIX_TIME
         * user_id	是	int[180]用户ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+ AppManager.getInstance(this).getUserInfo().getUser_id());
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_NOW_UNIX_TIME,data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s, call, response);
                        Gson gson = null;
                        try {
                            gson = new Gson();
                            UnixStamp stamp = gson.fromJson(s,UnixStamp.class);
                            if("0".equals(stamp.getCode())){
                                current_time.setText(new SimpleDateFormat("HH:mm").format(new Date(stamp.getData().getUnix_time() * 1000)));
                            }else {
                                MsgUtil.shortToastInCenter(OutSignSuccessActivity.this,stamp.getMsg());
                            }

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

    @OnClick({R.id.ll_back,R.id.check_record})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.check_record:
                startActivity(new Intent(this,OutRecordActivity.class));
                onBackPressed();
                break;
        }
    }
}
