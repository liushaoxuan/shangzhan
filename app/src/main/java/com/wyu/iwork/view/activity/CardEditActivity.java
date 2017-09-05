package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.CardExpandLayout;
import com.wyu.iwork.widget.MyCustomDialogDialog;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;


/**
 * 编辑名片
 */
public class CardEditActivity extends BaseActivity implements View.OnTouchListener,View.OnClickListener{

    private static final String TAG = CardEditActivity.class.getSimpleName();
    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_edit)
    TextView edit;

    @BindView(R.id.edit_name)
    EditText edit_name;

    @BindView(R.id.edit_phone)
    CardExpandLayout edit_phone;

    @BindView(R.id.edit_email)
    CardExpandLayout edit_email;

    @BindView(R.id.edit_wechat)
    CardExpandLayout edit_wechat;

    @BindView(R.id.edit_head_company)
    AutoRelativeLayout edit_head_company;

    @BindView(R.id.edit_iv_dismiss)
    ImageView edit_iv_dismiss;

    @BindView(R.id.edit_body_company)
    AutoLinearLayout edit_body_company;

    @BindView(R.id.edit_position)
    EditText edit_position;

    @BindView(R.id.edit_company)
    EditText edit_company;

    @BindView(R.id.edit_department)
    EditText edit_department;

    @BindView(R.id.edit_address)
    EditText edit_address;

    @BindView(R.id.activity_card_edit_scroolview)
    ScrollView activity_card_edit_scroolview;

    private static final int RESULT_CODE = 101;


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_edit);
        ButterKnife.bind(this);
        hideToolbar();
        initView();
    }

    private void initView(){
        title.setText("新建名片");
        edit.setVisibility(View.VISIBLE);
        edit.setText("保存");
        activity_card_edit_scroolview.setOnTouchListener(this);
        setDiaitals();
        initItemView();
    }

    private void initItemView(){
        edit_email.setTopLineGone();
        edit_wechat.setTopLineGone();
        edit_phone.setTitle("添加联系电话");
        edit_phone.setBodyTitle("电话");
        edit_email.setTitle("添加工作邮箱");
        edit_email.setBodyTitle("邮箱");
        edit_wechat.setTitle("添加微信");
        edit_wechat.setBodyTitle("微信");

        edit_phone.setInputType(InputType.TYPE_CLASS_PHONE);
        edit_email.setDiaitals();
        edit_wechat.setDiaitals();
    }

    //设置不能输入汉字
    public void setDiaitals(){
        String digits = "1234567890-abcdefghijklmnopqrstuvwxyz/\\|:><,?';\".~!@#$&%^*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        edit_email.getEditTextValue().setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    @OnClick({R.id.tv_edit,R.id.ll_back,R.id.edit_body_company,R.id.edit_head_company,R.id.edit_iv_dismiss})
    void Click(View v){
        switch (v.getId()){
            case R.id.tv_edit:
                saveData();
                break;
            case R.id.ll_back:
                finish();
                break;
            case R.id.edit_head_company:
                if(edit_body_company.getVisibility() == View.GONE){
                    edit_body_company.setVisibility(View.VISIBLE);
                    edit_iv_dismiss.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.edit_iv_dismiss:
                if(edit_body_company.getVisibility() == View.VISIBLE){
                    edit_body_company.setVisibility(View.GONE);
                    edit_iv_dismiss.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(activity_card_edit_scroolview);
        return false;
    }

    private void saveData(){
        String nameStr = edit_name.getText().toString();
        String phoneStr = edit_phone.getValueStr();
        if(TextUtils.isEmpty(nameStr)){
            MsgUtil.shortToastInCenter(this,"请填写名片姓名!");
            return;
        }else if(TextUtils.isEmpty(phoneStr)){
            MsgUtil.shortToastInCenter(this,"请填写名片电话号码!");
            return;
        }
        /**
         RandStr	      是	string[50]        请求加密随机数 time().|.rand()
         Sign	      是	string[400]       请求加密值 F_moffice_encode(F.V.RandStr.user_id.status)
         */
            HashMap<String,String> data = new HashMap<>();
            data.put("user_id",AppManager.getInstance(this).getUserInfo().getUser_id());
            data.put("card_id","0");
            data.put("name",nameStr);
            data.put("phone",phoneStr);
            if(!TextUtils.isEmpty(edit_email.getValueStr())){
                data.put("email",edit_email.getValueStr());
            }
            if(!TextUtils.isEmpty(edit_wechat.getValueStr())){
                data.put("wechat_id",edit_wechat.getValueStr());
            }
            if(!TextUtils.isEmpty(edit_position.getText().toString())){
                data.put("job",edit_position.getText().toString());
            }
            if(!TextUtils.isEmpty(edit_company.getText().toString())){
                data.put("company",edit_company.getText().toString());
            }
            if(!TextUtils.isEmpty(edit_department.getText().toString())){
                data.put("department",edit_department.getText().toString());
            }
            if(!TextUtils.isEmpty(edit_address.getText().toString())){
                data.put("address",edit_address.getText().toString());
            }
            data.put("status","0");
            String RandStr = CustomUtils.getRandStr();
            String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+"0");
            data.put("F", F);
            data.put("V", V);
            data.put("RandStr",RandStr);
            data.put("Sign",Sign);
            String murl = RequestUtils.getRequestUrl(Constant.URL_EDIT_CARD,data);
            OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                    .execute(new DialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Logger.i(TAG,s);
                            handleOption(s);
                        }
                    });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void handleOption(String s){
        JSONObject object = null;
        try {
            object = new JSONObject(s);
            if("0".equals(object.getString("code"))){
                setResult(RESULT_CODE);
                MsgUtil.shortToastInCenter(this,object.getString("msg"));
                finish();
            }else{
                MsgUtil.shortToastInCenter(this,object.getString("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //提醒弹窗
    private void showRedmineDialog(){
        new MyCustomDialogDialog(5, this, R.style.MyDialog, "需要添加的名片信息未填写完整\n请完成填写并提交", new MyCustomDialogDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {

            }
        }).show();
    }


}
