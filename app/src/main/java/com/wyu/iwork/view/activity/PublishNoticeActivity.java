package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.MyCustomDialogDialog;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.id.et_title;

public class PublishNoticeActivity extends BaseActivity{

    @BindView(R.id.tv_cancel)
    TextView cancel;//取消

    @BindView(R.id.notice_commit)
    TextView commit;//确定

    @BindView(et_title)
    EditText title;//发布的公告标题

    @BindView(R.id.et_content)
    EditText content;//发布的公告正文

    private static final String TAG = PublishNoticeActivity.class.getSimpleName();

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_notice);
        ButterKnife.bind(this);
        hideToolbar();
    }

    @OnClick({R.id.tv_cancel,R.id.notice_commit, et_title,R.id.et_content})
    void Click(View v){
        switch (v.getId()){
            case R.id.tv_cancel:
                //取消发布公告
                onBackPressed();
                break;
            case R.id.notice_commit:
                //提交公告
                commitNotice();
                break;
        }
    }


    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, "您的公告信息未填写完整\n请完成填写并提交", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }

    //发布公告
    public void commitNotice(){
        String noticeTitle = title.getText().toString();
        String noticeContent = content.getText().toString();
        if(TextUtils.isEmpty(title.getText().toString())){
            //showRedmineDialog();
            MsgUtil.shortToastInCenter(this,"请填写公告标题!");
            return;
        }else if(TextUtils.isEmpty(content.getText().toString())){
            MsgUtil.shortToastInCenter(this,"请填写公告内容!");
            return;
        }
        UserInfo user = AppManager.getInstance(this).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        Logger.i(TAG,RandStr);
        String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+noticeTitle);

        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",user.getUser_id());
        data.put("title",noticeTitle);
        data.put("text",noticeContent);
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_PUBLISH_NOTICE, data);
        OkGo.get(murl)
                .tag(this)
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        //loadingDialog.dismiss();
                        JSONObject object = null;
                        try {
                            object = new JSONObject(s);
                            String code = object.getString("code");
                            String msg = object.getString("msg");
                            if("0".equals(code)){
                                finish();
                            }
                            MsgUtil.shortToastInCenter(PublishNoticeActivity.this,msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
