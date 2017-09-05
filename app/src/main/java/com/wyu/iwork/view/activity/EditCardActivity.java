package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.BusinessCardInfo;
import com.wyu.iwork.model.MineCardModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.R.id.tv_retake;

/**
 * 名片编辑
 */
public class EditCardActivity extends BaseActivity implements View.OnTouchListener{

    private static final String TAG = EditCardActivity.class.getSimpleName();
    @BindView(tv_retake)
    TextView retakePhoto;//重新拍照

    @BindView(R.id.tv_save)
    TextView save;//信息保存

    @BindView(R.id.et_name)
    EditText et_name;

    @BindView(R.id.et_mail)
    EditText et_mail;

    @BindView(R.id.et_telphone)
    EditText et_telphone;

    @BindView(R.id.et_wechat)
    EditText et_wechat;

    @BindView(R.id.et_position)
    EditText et_position;

    @BindView(R.id.et_company)
    EditText et_company;

    @BindView(R.id.et_department)
    EditText et_department;

    @BindView(R.id.et_address)
    EditText et_address;

    @BindView(R.id.iv_image)
    ImageView image;

    @BindView(R.id.sv_scrollview)
    ScrollView sv_scrollview;

    private MineCardModel.MineCard card;
    private MineCardModel.MineCard model;
    private String mType;
    private HashMap<String, String> mData;
    private boolean isFlag = false;
    private Intent intent;
    private BusinessCardInfo info;
    private static final int RESULT_RECT_CAMERA = 200;
    private static String url = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);
        ButterKnife.bind(this);
        hideToolbar();
        getExtras();
        initView();
    }

    private void getExtras(){
        intent = getIntent();
        mType = intent.getStringExtra("type");//2 为他人名片   1为自己名片  3:拍完照的图片
        if(mType.equals("2")){
            card = (MineCardModel.MineCard) intent.getSerializableExtra("card");
            if(!TextUtils.isEmpty(card.getCard_img())){
                url = card.getCard_img();
            }
        }else if(mType.equals("3")){
            info = (BusinessCardInfo) intent.getSerializableExtra("info");
            if(!TextUtils.isEmpty(info.getData().getCard_img())){
                url = info.getData().getCard_img();
            }
        }else{
            model = (MineCardModel.MineCard) intent.getSerializableExtra("card");
            if(!TextUtils.isEmpty(model.getCard_img())){
                url = model.getCard_img();
            }
        }
    }
    private void initView(){
        sv_scrollview.setOnTouchListener(this);
        if(mType.equals("3")){
            retakePhoto.setText("返回");
            /**
             * [user_name] => 姓名
             [phone] => 手机号
             [email] => 邮箱
             [company_name] => 公司名称
             [address] => 地址
             [card_img] => 名片绝对地址
             */
            if(!TextUtils.isEmpty(info.getData().getWechat())){
                et_wechat.setText(info.getData().getWechat());
            }
            if(!TextUtils.isEmpty(info.getData().getPosition())){
                et_position.setText(info.getData().getPosition());
            }
            if(!TextUtils.isEmpty(info.getData().getUser_name())){
                et_name.setText(info.getData().getUser_name());
            }
            if(!TextUtils.isEmpty(info.getData().getPhone())){
                et_telphone.setText(info.getData().getPhone());
            }
            if(!TextUtils.isEmpty(info.getData().getEmail())){
                et_mail.setText(info.getData().getEmail());
            }
            if(!TextUtils.isEmpty(info.getData().getCompany_name())){
                et_company.setText(info.getData().getCompany_name());
            }
            if(!TextUtils.isEmpty(info.getData().getAddress())){
                et_address.setText(info.getData().getAddress());
            }
            Glide.with(this).load(url).dontAnimate().placeholder(R.mipmap.def_card).into(image);
        }else if(mType.equals("2")){

            if(!TextUtils.isEmpty(card.getName())){
                et_name.setText(card.getName());
            }
            if(!TextUtils.isEmpty(card.getEmail())){
                et_mail.setText(card.getEmail());
            }
            if(!TextUtils.isEmpty(card.getPhone())){
                et_telphone.setText(card.getPhone());
            }
            if(!TextUtils.isEmpty(card.getWechat_id())){
                et_wechat.setText(card.getWechat_id());
            }
            if(!TextUtils.isEmpty(card.getJob())){
                et_position.setText(card.getJob());
            }
            if(!TextUtils.isEmpty(card.getCompany())){
                et_company.setText(card.getCompany());
            }
            if(!TextUtils.isEmpty(card.getDepartment())){
                et_department.setText(card.getDepartment());
            }
            if(!TextUtils.isEmpty(card.getAddress())){
                et_address.setText(card.getAddress());
            }
            Glide.with(this).load(url).dontAnimate().placeholder(R.mipmap.def_card).into(image);
        }else{
            if(!TextUtils.isEmpty(model.getName())){
                et_name.setText(model.getName());
            }
            if(!TextUtils.isEmpty(model.getEmail())){
                et_mail.setText(model.getEmail());
            }
            if(!TextUtils.isEmpty(model.getPhone())){
                et_telphone.setText(model.getPhone());
            }
            if(!TextUtils.isEmpty(model.getWechat_id())){
                et_wechat.setText(model.getWechat_id());
            }
            if(!TextUtils.isEmpty(model.getJob())){
                et_position.setText(model.getJob());
            }
            if(!TextUtils.isEmpty(model.getCompany())){
                et_company.setText(model.getCompany());
            }
            if(!TextUtils.isEmpty(model.getDepartment())){
                et_department.setText(model.getDepartment());
            }
            if(!TextUtils.isEmpty(model.getAddress())){
                et_address.setText(model.getAddress());
            }
            Glide.with(this).load(url).dontAnimate().placeholder(R.mipmap.def_card).into(image);
        }

        setDiaitals();
    }

    //设置不能输入汉字
    public void setDiaitals(){
        String digits = "1234567890-abcdefghijklmnopqrstuvwxyz/\\|:><,?';\".~!@#$&%^*()ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        et_mail.setKeyListener(DigitsKeyListener.getInstance(digits));
    }

    private boolean isFirst = true;
    @OnClick({R.id.tv_retake,R.id.tv_save,R.id.iv_image,})
    void Click(View v){
        switch (v.getId()){
            case R.id.tv_retake:
                //返回
                if(mType.equals("3")){
                    setResult(RESULT_RECT_CAMERA);
                }
                finish();
                break;
            case R.id.tv_save:
                //保存
                saveInfo();
                break;

            case R.id.iv_image:
                String url = "";
                if("3".equals(mType)){
                    Logger.i("TAG,",info.getData().getCard_img());
                    url = info.getData().getCard_img();
                }else if("2".equals(mType)){
                    if(!TextUtils.isEmpty(card.getCard_img())){
                        Logger.i("TAG,",card.getCard_img());
                        url = card.getCard_img();
                    }
                }else{
                    if(!TextUtils.isEmpty(model.getCard_img())){
                        Logger.i("TAG,",model.getCard_img());
                        url = model.getCard_img();
                    }
                }
                Intent i = new Intent(this,ImageActivity.class);
                i.putExtra("url",url);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void saveInfo(){
        getHashMapData();
        if(isFlag){
            String murl = RequestUtils.getRequestUrl(Constant.URL_EDIT_CARD,mData);
            OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                    .execute(new DialogCallback(this) {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Logger.i(TAG,s);
                            JSONObject object = null;
                            try {
                                object = new JSONObject(s);
                                MsgUtil.shortToastInCenter(EditCardActivity.this,object.getString("msg"));
                                if(mType.equals("3")){
                                    setResult(RESULT_RECT_CAMERA);
                                }
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    private void getHashMapData(){
        /**
         * 拍照界面跳转   card_img一定会有
         * 添加的名片过来的  可能会有
         * 我的   可能会有
         */
        String name = et_name.getText().toString();
        String mail = et_mail.getText().toString();
        String phone = et_telphone.getText().toString();
        String wechat = et_wechat.getText().toString();
        String position = et_position.getText().toString();
        String company = et_company.getText().toString();
        String department = et_department.getText().toString();
        String address = et_address.getText().toString();
        if(TextUtils.isEmpty(name)){
            MsgUtil.shortToastInCenter(this,"姓名不能为空!");
            isFlag = false;
        }
        /**else if(TextUtils.isEmpty(mail)){
            MsgUtil.shortToastInCenter(this,"邮箱不能为空!");
            isFlag = false;
        }*/else if(TextUtils.isEmpty(phone)){
            MsgUtil.shortToastInCenter(this,"手机号不能为空!");
            isFlag = false;
        }/**else if(TextUtils.isEmpty(wechat)){
            MsgUtil.shortToastInCenter(this,"微信不能为空!");
            isFlag = false;
        }else if(TextUtils.isEmpty(position)){
            MsgUtil.shortToastInCenter(this,"职位不能为空!");
            isFlag = false;
        }else if(TextUtils.isEmpty(company)){
            MsgUtil.shortToastInCenter(this,"公司不能为空!");
            isFlag = false;
        }else if(TextUtils.isEmpty(department)){
            MsgUtil.shortToastInCenter(this,"部门不能为空!");
            isFlag = false;
        }else if(TextUtils.isEmpty(address)){
            MsgUtil.shortToastInCenter(this,"地址不能为空!");
            isFlag = false;
        }*/else{
            mData = new HashMap<>();
            UserInfo user = AppManager.getInstance(this).getUserInfo();
            String F = Constant.F;
            String V = Constant.V;
            String RandStr = CustomUtils.getRandStr();
            mData.put("user_id",user.getUser_id());
            if("3".equals(mType) || "1".equals(mType)){
                mData.put("card_id",0+"");
            }else{
                mData.put("card_id",card.getId());
            }
            if(!TextUtils.isEmpty(url)){
                mData.put("card_img",url);
            }
            mData.put("name",name);
            mData.put("phone",phone);
            if(!TextUtils.isEmpty(mail)){
                mData.put("email",mail);
            }
            if(!TextUtils.isEmpty(wechat)){
                mData.put("wechat_id",wechat);
            }
            if(!TextUtils.isEmpty(position)){
                mData.put("job",position);
            }
            if(!TextUtils.isEmpty(company)){
                mData.put("company",company);
            }
            if(!TextUtils.isEmpty(department)){
                mData.put("department",department);
            }
            if(!TextUtils.isEmpty(address)){
                mData.put("address",address);
            }
            String Sign = "";
            if ("2".equals(mType) || "3".equals(mType)) {
                mData.put("status",0+"");
                //(F.V.RandStr.user_id.status
                Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+0);
            }else{
                mData.put("status",1+"");
                Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+1);
            }
            mData.put("F",F);
            mData.put("V",V);
            mData.put("RandStr",RandStr);
            mData.put("Sign",Sign);
            isFlag = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Hideinputwindown(sv_scrollview);
        return false;
    }
}
