package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RegexUtils;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.TimeCount;
import com.wyu.iwork.view.activity.FindPassStep1;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by lx on 2017/3/31.
 * 找回密码
 */

public class FindPassWordFragment extends Fragment {
    public static int REQUESCODE = 0x01122;
    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;
    @BindView(R.id.et_telnum)
    EditText telnumber; //用户名

    @BindView(R.id.et_code)
    EditText code;//验证码
    @BindView(R.id.tv_getcode)
    TextView getcode;//获取验证码

    @BindView(R.id.tv_next_step)
    TextView next_step; //下一步

    //计时器
    private TimeCount timeCount;
    private UserInfo userInfo;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = MyApplication.userInfo;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.find_password,container,false);
        ButterKnife.bind(this,view);
        title.setText("找回密码");
        edit.setVisibility(View.GONE);
        timeCount = new TimeCount(getcode,60000);
        TextWarcher();
        return view;
    }

    @OnClick({R.id.tv_getcode,R.id.action_back,R.id.tv_next_step})
    void CLick(View v){
        switch (v.getId()){

            case R.id.action_back:
                getActivity().onBackPressed();
                break;

            case R.id.tv_getcode://获取验证码
                String phone = telnumber.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(getActivity(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!RegexUtils.checkMobile(phone)){
                    Toast.makeText(getActivity(),"请输入正确手机号",Toast.LENGTH_SHORT).show();
                    break;
                }
                CustomUtils.getPhoneSign_SMS(telnumber.getText().toString(),getActivity(),timeCount);
                break;
            case R.id.tv_next_step://下一步
                String mphone = telnumber.getText().toString();
                String mcode = code.getText().toString();
                if (TextUtils.isEmpty(mphone)){
                    Toast.makeText(getActivity(),"请输入手机号",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!RegexUtils.checkMobile(mphone)){
                    Toast.makeText(getActivity(),"请输入正确手机号",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(mcode)){
                    Toast.makeText(getActivity(),"请输入验证码",Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent intent = new Intent(getActivity(), FindPassStep1.class);
                intent.putExtra("phone",mphone);
                intent.putExtra("code",mcode);
                startActivityForResult(intent,REQUESCODE);
                break;
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==REQUESCODE){
            getActivity().finish();
        }
    }

    //输入框监听
    private void TextWarcher(){

        //手机号输入监听
        telnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String mcode = code.getText().toString();
                if (!mcode.isEmpty()){
                    String mphone = s.toString();
                    if (RegexUtils.checkMobile(mphone)){
                        next_step.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_login_normal));
                    }else {
                        next_step.setBackground(getActivity().getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                    }
                }else {
                    next_step.setBackground(getActivity().getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                }
            }
        });

        //验证码输入监听
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String mphone = telnumber.getText().toString();
                if (RegexUtils.checkMobile(mphone)){
                    String mcode = s.toString();
                    if (!mcode.isEmpty()){
                        next_step.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_login_normal));
                    }else {
                        next_step.setBackground(getActivity().getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                    }
                }else {
                    next_step.setBackground(getActivity().getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                }
            }
        });
    }


}
