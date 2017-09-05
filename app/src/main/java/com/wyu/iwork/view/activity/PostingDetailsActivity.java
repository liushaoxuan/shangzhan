package com.wyu.iwork.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.ICommentView;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.presenter.CommentPresenterCompl;
import com.wyu.iwork.presenter.ICommentPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 发布评论
 */
public class PostingDetailsActivity extends BaseActivity implements ICommentView{

    /**
     * 发表
     */
    @BindView(R.id.activity_posting_details_release)
    TextView release;

    /**
     * 发表内容
     */
    @BindView(R.id.activity_posting_details_content)
    EditText release_content;
    ICommentPresenter iCommentPresenter;

    //动态id
    private String dynamic_id;

    UserInfo userInfo;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posting_details);
        getSupportActionBar().hide();
        getDynamicId();
        ButterKnife.bind(this);
        iCommentPresenter = new CommentPresenterCompl(this);
        userInfo = AppManager.getInstance(this).getUserInfo();


    }

    //获取动态id
    private void getDynamicId(){
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            dynamic_id = bundle.getString("dynamic_id");
        }
    }

    /**
     * 点击事件注入
     */
    @OnClick({R.id.activity_posting_details_back, R.id.activity_posting_details_release})
    void Click(View view){
        switch (view.getId()){
            case R.id.activity_posting_details_back://返回
                onBackPressed();
                break;
            case R.id.activity_posting_details_release://发布
                String text = release_content.getText().toString().trim();
                if (text==null ||"".equals(text)){
                    Toast.makeText(PostingDetailsActivity.this,"请输入评论内容",Toast.LENGTH_LONG).show();
                   break;
                }
                release.setClickable(false);
                iCommentPresenter.releaseComment(text);

                break;
        }
    }

    @Override
    public void clear() {
        release_content.setText("");
    }

    @Override
    public void releaseComment(String text) {
        String url = Constant.URL_DYNAMIC_RELEASE;
        String F = Constant.F;
        String V = Constant.V;
        String user_id = userInfo==null?"":userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + dynamic_id + user_id+text);
        Map<String, String> map = new HashMap<String, String>();
        map.put("dynamic_id", dynamic_id);
        map.put("user_id", user_id);
        map.put("text", text);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url,map);
        Logger.e("releasecom----------->",murl);
        OkGo.get(url)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .params("dynamic_id",dynamic_id)
                .params("user_id",user_id)
                .params("text",text)
                .params("F",F)
                .params("V",V)
                .params("RandStr",RandStr)
                .params("Sign",sign)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        release.setClickable(true);
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            release.setClickable(true);
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)){
                                finish();
                            }else {
                                Toast.makeText(PostingDetailsActivity.this,object.optString("msg"),Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Logger.e("relese_s------------>",s);
                        release.setClickable(true);
                    }
                });
    }

}

