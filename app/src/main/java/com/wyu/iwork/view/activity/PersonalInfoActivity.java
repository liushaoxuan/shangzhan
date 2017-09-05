package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.net.GsonManager;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.zhy.autolayout.AutoLayoutActivity;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author  sxliu
 * 个人资料
 */
public class PersonalInfoActivity extends AutoLayoutActivity   {

    private static String TAG = PersonalInfoActivity.class.getSimpleName();
    /**
     * 头像
     */
    @BindView(R.id.activity_personal_info_head)
    CircleImageView head_icon;
    /**
     * 昵称
     */
    @BindView(R.id.activity_personal_info_nickname)
    TextView nikeName;
    /**
     * 公司职务
     */
    @BindView(R.id.activity_personal_info_company_jobs)
    TextView companyJobs;

    /**
     * 姓名
     */
    @BindView(R.id.activity_personal_info_name)
    TextView name;

    /**
     * 手机
     */
    @BindView(R.id.activity_personal_info_phone)
    TextView phone;

    /**
     * 邮箱
     */
    @BindView(R.id.activity_personal_info_email)
    TextView email;

    /**
     * 微信
     */
    @BindView(R.id.activity_personal_info_wechat)
    TextView wechat;

    /**
     * 职位
     */
    @BindView(R.id.activity_personal_info_job)
    TextView job;

    /**
     * 公司
     */
    @BindView(R.id.activity_personal_info_company)
    TextView company;

    /**
     * 部门
     */
    @BindView(R.id.activity_personal_info_department)
    TextView department;

    /**
     * 地址
     */
    @BindView(R.id.activity_personal_info_addr)
    TextView address;
    /**
     * 标题
     */
    @BindView(R.id.layout_my_title_bar_title)
    TextView title;
    /**
     * 编辑
     */
    @BindView(R.id.layout_my_title_bar_save)
    TextView edit;
    private UserInfo userInfo;//最新获取的用户信息
    private UserInfo olduserInfo;//用户信息
    /**
     *  其他人的userid
     */
    private String otherUid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);
        olduserInfo =  AppManager.getInstance(this).getUserInfo();
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
             getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        edit.setText("编辑");
        title.setText("个人资料");

    }

    private void getUserId(){
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            otherUid = extras.getString("id");
        }
    }



    @OnClick({R.id.layout_my_title_bar_back,R.id.layout_my_title_bar_save})
    void Click(View v){
        switch (v.getId()){
            case R.id.layout_my_title_bar_back:
                onBackPressed();
                break;
            case R.id.layout_my_title_bar_save://编辑
                Intent intent = new Intent(this,EditPersonnalInfomationActivity.class);
                startActivity(intent);
                break;

        }
    }

    //初始化个人信息参数
    private void initviews(){
        Glide.with(getApplicationContext()).load(userInfo.getUser_face_img()).error(R.mipmap.head_icon_nodata).into(head_icon);
        nikeName.setText(userInfo.getReal_name());
        companyJobs.setText(userInfo.getCompany());
        name.setText(userInfo.getReal_name());
        phone.setText(userInfo.getUser_phone());
        email.setText(userInfo.getEmail());
        wechat.setText(userInfo.getWechat()==null?" ":userInfo.getWechat());
        job.setText(userInfo.getJob());
        company.setText(userInfo.getCompany()==""?" ":userInfo.getCompany());
        department.setText(userInfo.getDepartment());
        address.setText(userInfo.getAddress()==null?" ":userInfo.getAddress());


        if (otherUid!=null&&!"".equals(otherUid)){//如果是第三人称的个人资料，则不显示编辑资料
            edit.setVisibility(View.GONE);
        }

    }


    //获取用户信息
    private void getUserInfoRequest() {
//        loading.show(this.getSupportManager(), Constant.DIALOG_TAG_LOADING);
        String  userID = null;
        if (otherUid==null||"".equals(otherUid)){
            userID = olduserInfo.getUser_id();
        }
        else {
            userID = otherUid;
        }

        String url = Constant.URL_GET_USERINFO;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F + V + RandStr + userID);
        OkGo.get(url)
                .tag(TAG)
                .cacheMode(CacheMode.DEFAULT)
                .params("user_id", userID)
                .params("F", F)
                .params("V", V)
                .params("RandStr", RandStr)
                .params("Sign", Sign).execute(new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONObject data = new JSONObject(object.optString("data"));
                        userInfo = JSON.parseObject(data.toString(), UserInfo.class);
                        userInfo.setAbout_us(olduserInfo.getAbout_us());
                        userInfo.setShare(olduserInfo.getShare());
                        userInfo.setWelcome_sms_msg(olduserInfo.getWelcome_sms_msg());
                        if (otherUid==null||"".equals(otherUid)){
                            userInfo.saveOrUpdate("user_id=?",userInfo.getUser_id());
                        }
                        initviews();
                    } else {
                        MsgUtil.shortToast(PersonalInfoActivity.this, object.optString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAfter(@Nullable String s, @Nullable Exception e) {
                super.onAfter(s, e);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserId();
        getUserInfoRequest();
    }



}
