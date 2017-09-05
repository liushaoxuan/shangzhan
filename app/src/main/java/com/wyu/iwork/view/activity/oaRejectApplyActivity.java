package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * OA——申请驳回
 */
public class oaRejectApplyActivity extends BaseActivity {

    private static String TAG = oaRejectApplyActivity.class.getSimpleName();
    @BindView(R.id.oa_title)
    TextView mTitle;

    /**
     * 意见
     */
    @BindView(R.id.activity_oa_agree_aplly_isure)
    EditText isure;

    /**
     * 提交
     */
    @BindView(R.id.activity_oa_agree_aplly_btn)
    Button btn;

    /**
     * 申请关联id
     */
    private String mId = "";

    private String url = "";
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_agree_aplly);
        getSupportActionBar().hide();
        getId();
        ButterKnife.bind(this);
        mTitle.setText("驳回");
        btn.setText("驳回");
    }

    //获取关联ID
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            mId = bundle.getString("id");
        }
    }

    @OnClick({R.id.oa_back,R.id.activity_oa_agree_aplly_btn})
    void Click(View v){
        switch (v.getId()){
            case R.id.oa_back:
                onBackPressed();
                break;
            case R.id.activity_oa_agree_aplly_btn:
                String  mIsure = isure.getText().toString().trim();
                initParama(mIsure);
                btn.setClickable(false);
                break;
        }
    }

    /**
     * 初始化请求参数
     */
    private void initParama(String content){
        url = Constant.URL_REJECT_APPLY;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String user_id = "";
        try {
            user_id = userInfo.getUser_id();
        } catch (Exception e) {
            user_id = DataSupport.findFirst(UserInfo.class).getUser_id();
        }
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr +user_id+ mId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("contact_id",mId);
        map.put("content",content);
        map.put("F",Constant.F);
        map.put("V",Constant.V);
        map.put("RandStr",RandStr);
        map.put("Sign",sign);
        url = RequestUtils.getRequestUrl(url, map);
        Logger.e("oa_agree_aplly",url);
        OkgoRequest(url,callback());
    }

    private DialogCallback callback(){
        return new DialogCallback(this){
            @Override
            public void onError(Call call, Response response, Exception e) {
                btn.setClickable(true);
                Logger.e(TAG,e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    btn.setClickable(true);
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        finish();
                    }
                    Toast.makeText(oaRejectApplyActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    Logger.e(TAG,s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
                ;
    }
}
