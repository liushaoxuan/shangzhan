package com.wyu.iwork.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.stor.Prefs;
import com.wyu.iwork.util.ActivityManager;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Exit_app;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.ShareUtil;
import com.wyu.iwork.view.activity.AboutActivity;
import com.wyu.iwork.view.activity.AdTaskSettingsActivity;
import com.wyu.iwork.view.activity.AdvertiseActivity;
import com.wyu.iwork.view.activity.ChangePasswordActivity;
import com.wyu.iwork.view.activity.EditPersonnalInfomationActivity;
import com.wyu.iwork.view.activity.InvitaFriendsActivity;
import com.wyu.iwork.view.activity.InviteColleaguesActivity;
import com.wyu.iwork.view.activity.MVPMenberActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.PersonalInfoActivity;
import com.wyu.iwork.view.activity.SettingActivity;
import com.wyu.iwork.view.activity.SigninActivity;
import com.wyu.iwork.view.dialog.NewStyleDialog;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.wyu.iwork.widget.MyDialog;
import com.wyu.iwork.widget.ShareDialog;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lx on 2017/4/6.
 * 我的
 */

public class Fragment_my_new extends Fragment {
    private static final String TAG = Fragment_my_new.class.getSimpleName();
    /**
     * 头像
     */
    @BindView(R.id.home_fragment_mine_head)
    CircleImageView headicon;
    /**
     * 昵称
     */
    @BindView(R.id.home_fragment_mine_nikeName)
    TextView nikaName;
    /**
     * 公司
     */
    @BindView(R.id.home_fragment_mine_company)
    TextView company;
    /**
     * 缓存
     */
    @BindView(R.id.home_fragment_mine_cash)
    TextView cash;
    private UserInfo userInfo;
    private MainActivity activity;

    private String size;//缓存大小

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_new, container, false);
        ButterKnife.bind(this, view);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.hide();
        try {
            size = CustomUtils.getTotalCacheSize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    //点击事件
    @OnClick({R.id.home_fragment_mine_edit, R.id.home_fragment_mine_vip, R.id.home_fragment_mine_Invite, R.id.home_fragment_mine_change_pass, R.id.home_fragment_mine_unbond,
            R.id.home_fragment_mine_clean_cash, R.id.home_fragment_mine_share_app, R.id.home_fragment_mine_about_us})
    void Click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.home_fragment_mine_vip://vip
                intent = new Intent(getActivity(), MVPMenberActivity.class);
                startActivity(intent);
                break;
            case R.id.home_fragment_mine_edit://个人资料
                intent = new Intent(getActivity(), EditPersonnalInfomationActivity.class);
                startActivity(intent);
                break;
            case R.id.home_fragment_mine_Invite://邀请同事
                intent = new Intent(getActivity(), InviteColleaguesActivity.class);
                startActivity(intent);

                break;
            case R.id.home_fragment_mine_change_pass://广告联盟
                intent = new Intent(getActivity(), AdvertiseActivity.class);
                startActivity(intent);
                break;
            case R.id.home_fragment_mine_unbond://解除企业绑定
                if (!CustomUtils.showDialogForBusiness(getActivity())) {
                    return;
                }
                unbind();
                break;

            case R.id.home_fragment_mine_clean_cash://清除缓存
                cleanCach();
                break;
            case R.id.home_fragment_mine_share_app://分享应用
                new ShareDialog(getActivity(), 2).show();
                break;
            case R.id.home_fragment_mine_about_us://设置
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }


    private void initData() {
        nikaName.setText(userInfo.getReal_name());
        String company_auth = userInfo.getCompany_auth();
        switch (company_auth) {//0:认证失败 1：认证成功 2：认证中 3：未认证
            case "0"://
                company.setText("认证失败");
                break;
            case "1"://
                company.setText(userInfo.getCompany() == "" ? "" : userInfo.getCompany());
                break;
            case "2"://
                company.setText("认证中");
                break;
            case "3"://
                company.setText("未认证");
                break;
        }
        cash.setText((size == null ? "" : size));
        Glide.with(getActivity()).load(userInfo.getUser_face_img()).dontAnimate().placeholder(R.mipmap.head_icon_nodata).into(headicon);
    }

    //解除企业绑定
    public void unBondCallback() {

        String url = Constant.URL_UPDATE_USERPMOVEBINDING;
        String user_id = null;
        user_id = userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        OkGo.get(murl)
                .cacheMode(CacheMode.DEFAULT)
                .tag(TAG)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
                            if ("0".equals(code)) {
                                company.setText("未认证");
                                MyApplication.userInfo.setCompany_auth("3");
                                MyApplication.userInfo.setCompany("");
                                MyApplication.saveUserInfo(MyApplication.userInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //清除缓存
    private void cleanCach() {
        String title = "清除缓存";
        String tv_Content = "当前缓存大小为：" + size + "，您确定要清除缓存吗？";
        new NewStyleDialog(getActivity(), R.mipmap.icon_jinggao, title, tv_Content, false, new NewStyleDialog.DialogInterface() {
            @Override
            public void FalseCancel(AlertDialog dialog) {
            }

            @Override
            public void FalseSure(AlertDialog dialog) {
                CustomUtils.clearAllCache(getActivity());
                size = "0K";
                cash.setText(size);
            }

            @Override
            public void Ture_sure(AlertDialog dialog) {

            }
        }).show();

    }


    //解除企业绑定
    private void unbind() {
        String title = "确定要解除企业绑定么";
        String tv_Content = "解除后你只能看到部分应用";
        new NewStyleDialog(getActivity(), R.mipmap.icon_jinggao, title, tv_Content, false, new NewStyleDialog.DialogInterface() {
            @Override
            public void FalseCancel(AlertDialog dialog) {
            }

            @Override
            public void FalseSure(AlertDialog dialog) {
                if (!userInfo.getUser_id().isEmpty()) {
                    unBondCallback();
                }
            }

            @Override
            public void Ture_sure(AlertDialog dialog) {

            }
        }).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        userInfo = MyApplication.userInfo;
        if (userInfo == null) {
            userInfo = MyApplication.userInfo;
        }
        initData();
    }
}
