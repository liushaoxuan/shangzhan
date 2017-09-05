package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.AdTaskShareModel;
import com.wyu.iwork.util.AdShareUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.widget.ShareDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 广告领取成功
 */
public class DrawSuccessActivity extends BaseActivity {

    private static String TAG = DrawSuccessActivity.class.getSimpleName();
    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_title)
    TextView tv_title;

    //立即分享
    @BindView(R.id.share_now)
    TextView share_now;

    private AdTaskShareModel shareModel;//分享的model

    private String Id = "";//文章id

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_success);
        hideToolbar();
        ButterKnife.bind(this);
        getUrl();
        initView();
    }


    /**
     * 获取文章id
     */
    private void getUrl(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            Id = bundle.getString("id");
        }
    }

    private void initView(){
        tv_title.setText("领取成功");
        AdShareUtils.getShareData(Id==null?"":Id,callback());
    }

    @OnClick({R.id.ll_back,R.id.share_now})
    void Click(View v){
        switch (v.getId()){
            case R.id.share_now:
                //立即分享
                showShareDialog();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    //分享弹窗
    private void showShareDialog(){
        if (shareModel!=null){
            new ShareDialog(this,1,shareModel.getTitle(),shareModel.getMessage(),shareModel.getUrl(),shareModel.getImg_url()).show();
        }else {
            Toast.makeText(this,"分享失败",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 获取分享信息回调
     */
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.e(TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    String data = object.optString("data");
                    if ("0".equals(code)){
                        shareModel = JSON.parseObject(data.toString(),AdTaskShareModel.class);
                    }else {
                        Toast.makeText(DrawSuccessActivity.this,msg,Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }
}
