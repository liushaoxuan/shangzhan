package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.RegexUtils;
import com.wyu.iwork.util.TimeCount;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author sxliu
 * 注册第一步
 */
public class SignUpActivityStep1 extends BaseActivity {

    public static int REQUESCODE = 0x01123;
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

    @BindView(R.id.phone_del)
    ImageView phone_delete; //手机号删除


    @BindView(R.id.code_del)
    ImageView code_delete; //验证码删除

    //计时器
    private TimeCount timeCount;
    private UserInfo userInfo;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step1);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        title.setText("注册");
        edit.setVisibility(View.GONE);
        timeCount = new TimeCount(getcode,60000);
        TextWarcher();
    }


    @OnClick({R.id.tv_getcode,R.id.action_back,R.id.tv_next_step,R.id.phone_del,R.id.code_del})
    void CLick(View v){
        switch (v.getId()){

            case R.id.action_back:
                SignUpActivityStep1.this.onBackPressed();
                break;

            case R.id.phone_del:
                telnumber.setText("");
                phone_delete.setVisibility(View.INVISIBLE);
                break;

            case R.id.code_del:
                code.setText("");
                code_delete.setVisibility(View.INVISIBLE);
                break;

            case R.id.tv_getcode://获取验证码
                String phone = telnumber.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    Toast.makeText(SignUpActivityStep1.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!RegexUtils.checkMobile(phone)){
                    Toast.makeText(SignUpActivityStep1.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                    break;
                }
                timeCount.start();
                CustomUtils.getPhoneSign(telnumber.getText().toString(),SignUpActivityStep1.this);
                break;
            case R.id.tv_next_step://下一步
                String mphone = telnumber.getText().toString();
                String mcode = code.getText().toString();
                if (TextUtils.isEmpty(mphone)){
                    Toast.makeText(SignUpActivityStep1.this,"请输入手机号",Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!RegexUtils.checkMobile(mphone)){
                    Toast.makeText(SignUpActivityStep1.this,"请输入正确手机号",Toast.LENGTH_SHORT).show();
                    break;
                }
                if (TextUtils.isEmpty(mcode)){
                    Toast.makeText(SignUpActivityStep1.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    break;
                }

                Intent intent = new Intent(SignUpActivityStep1.this, RegisterActivity.class);
                intent.putExtra("phone",mphone);
                intent.putExtra("code",mcode);
                startActivityForResult(intent,REQUESCODE);
                break;
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

                if (s.length()==0){
                    phone_delete.setVisibility(View.INVISIBLE);
                }else {
                    phone_delete.setVisibility(View.VISIBLE);
                }
                String mcode = code.getText().toString();
                if (!mcode.isEmpty()){
                    String mphone = s.toString();
                    if (RegexUtils.checkMobile(mphone)){
                        next_step.setBackground(getResources().getDrawable(R.drawable.bg_login_normal));
                    }else {
                        next_step.setBackground(getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                    }
                }else {
                    next_step.setBackground(getResources().getDrawable(R.drawable.find_pass_cant_sclick));
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
                if (s.length()==0){
                    code_delete.setVisibility(View.INVISIBLE);
                }else {
                    code_delete.setVisibility(View.VISIBLE);
                }
                if (RegexUtils.checkMobile(mphone)){
                    String mcode = s.toString();
                    if (!mcode.isEmpty()){
                        next_step.setBackground(getResources().getDrawable(R.drawable.bg_login_normal));
                    }else {
                        next_step.setBackground(getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                    }
                }else {
                    next_step.setBackground(getResources().getDrawable(R.drawable.find_pass_cant_sclick));
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==REQUESCODE){
           finish();
        }
    }
}
