package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CardModel;
import com.wyu.iwork.model.MineCardModel;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.ShareUtil;
import com.wyu.iwork.util.ZxingCodeUtils;
import com.wyu.iwork.util.getEr_q;
import com.wyu.iwork.view.dialog.Er_QDialog;
import com.wyu.iwork.widget.CardItem;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我的名片
 */
public class MineCardActivity extends BaseActivity {

    private static final String TAG = MineCardActivity.class.getSimpleName();

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.tv_edit)
    TextView edit;

    @BindView(R.id.tv_title)
    TextView title;

    @BindView(R.id.er_q)
    ImageView er_q;

    @BindView(R.id.ll_share_qq_qzone)
    AutoLinearLayout ll_share_qq_qzone;

    @BindView(R.id.ll_share_qq_friend)
    AutoLinearLayout ll_share_qq_friend;

    @BindView(R.id.ll_share_wechat_friend)
    AutoLinearLayout ll_share_wechat_friend;

    @BindView(R.id.ll_share_wechat)
    AutoLinearLayout ll_share_wechat;

    //手机号
    @BindView(R.id.item_telephone)
    CardItem item_telephone;

    //邮箱
    @BindView(R.id.item_mail)
    CardItem item_mail;

    //微信
    @BindView(R.id.item_wechat)
    CardItem item_wechat;

    //职位
    @BindView(R.id.item_position)
    CardItem item_position;

    //公司
    @BindView(R.id.item_company)
    CardItem item_company;

    @BindView(R.id.item_department)
    CardItem item_department;


    @BindView(R.id.mine_name)
    TextView name;

    @BindView(R.id.mine_company)
    TextView company;

    @BindView(R.id.mine_telephone)
    TextView telephone;

    @BindView(R.id.mine_email)
    TextView email;

    @BindView(R.id.mine_avatar)
    CircleImageView avatar;

    @BindView(R.id.edit)
    ImageView img_edit;

    @BindView(R.id.civ_image)
    TextView civ_image;

    @BindView(R.id.tv_share)
    TextView tv_share;

    @BindView(R.id.tv_mine_address)
    TextView tv_mine_address;

    private String type;
    private CardModel.Data.Card card;
    private MineCardModel.MineCard model;
    private Intent mIntent;
    private String mId;
    private Gson gson;

    private Er_QDialog Dialog;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_card);
        hideToolbar();
        ButterKnife.bind(this);
        initView();
        mIntent = getIntent();
        type = mIntent.getStringExtra("type");
    }

    @Override
    protected void onStart() {
        super.onStart();
        getExtras();
    }

    private void initView(){
        initItemTitle();
    }

    private void initItemTitle(){
        item_mail.setTitle("邮箱");
        item_wechat.setTitle("微信");
        item_position.setTitle("职位");
        item_company.setTitle("公司");
        item_department.setTitle("部门");
    }

    private void getExtras(){
        if("2".equals(type)){
            //用户添加的名片
            title.setText("名片详情");
            tv_share.setText("分享TA的名片");
            mId = mIntent.getStringExtra("id");
            civ_image.setVisibility(View.VISIBLE);
            avatar.setVisibility(View.GONE);
            er_q.setVisibility(View.VISIBLE);
            initMineCardMessage();
        }else{
            //用户自己的名片
            title.setText("我的名片");
            initMineCardMessage();
            //取消编辑设置
            edit.setVisibility(View.GONE);
            img_edit.setVisibility(View.GONE);
            er_q.setVisibility(View.VISIBLE);
        }
    }

    private void initMineCardMessage(){
        UserInfo user = AppManager.getInstance(this).getUserInfo();
        /**
         * user_id	是	int[11]用户自己的ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.user_id)
         */
        /**
         * card_id	是	int[11]名片的ID
         F	是	string[18]请求来源：IOS/ANDROID/WEB
         V	是	string[20]版本号如：1.0.1
         RandStr	是	string[50]请求加密随机数 time().|.rand()
         Sign	是	string[400]请求加密值 F_moffice_encode(F.V.RandStr.card_id)
         */
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = "";
        HashMap<String,String> data = new HashMap<>();
        if("2".equals(type)){
            data.put("card_id",mId);
            Sign = Md5Util.getSign(F+V+RandStr+mId);
        }else{
            data.put("user_id",user.getUser_id());
            Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id());
        }

        data.put("F",F);
        data.put("V",V);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = "";
        if("2".equals(type)){
            murl = RequestUtils.getRequestUrl(Constant.URL_CARD_DETAIL,data);
        }else{
            murl = RequestUtils.getRequestUrl(Constant.URL_MINE_CARD,data);
        }

        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        parseAndSetMineData(s);
                    }
                });
    }

    private void parseAndSetMineData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            model = gson.fromJson(data, MineCardModel.class).getData();
            setMineData();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setMineData(){
        setText(model.getPhone(),item_telephone);
        setText(model.getEmail(),item_mail);
        setText(model.getWechat_id(),item_wechat);
        setText(model.getJob(),item_position);
        setText(model.getCompany(),item_company);
        setText(model.getDepartment(),item_department);
        if(!TextUtils.isEmpty(model.getAddress())){
            tv_mine_address.setText(model.getAddress());
        }else{
            tv_mine_address.setText("");
            tv_mine_address.setHint("暂无");
        }
        checkStr(model.getName(),name);
        checkStr(model.getCompany(),company);
        checkStr("联系电话："+model.getPhone(),telephone);
        checkStr(model.getEmail(),email);
        if("2".equals(type)){
            try {
                civ_image.setText(model.getName().substring(0,1));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            Glide.with(this).load(model.getFace_img()).dontAnimate().placeholder(R.mipmap.def_img_rect).into(avatar);
        }

    }

    private void checkStr(String str,TextView view){
        if(!TextUtils.isEmpty(str)){
            view.setText(str);
        }
    }

    private void setText(String str,CardItem item){
        if(!TextUtils.isEmpty(str)){
            item.setValue(str);
        }else{
            item.getTv_value().setText(null);
            item.setValueHint("暂无");
        }
    }


    @OnClick({R.id.ll_back,R.id.tv_edit,R.id.er_q,R.id.ll_share_wechat,R.id.ll_share_wechat_friend,R.id.ll_share_qq_friend,R.id.ll_share_qq_qzone,R.id.edit})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                finish();
                break;
            /**
            case R.id.ll_call:
                //拨打电话
                if(type.equals("2")){
                    Intent intent = new Intent();
                    intent.setData(Uri.parse("tel:" + model.getPhone()));
                    intent.setAction(Intent.ACTION_DIAL);
                    startActivity(intent);
                }
                break;*/
            case R.id.ll_share_wechat:
                ShareUtil.shareCardWechat(null,this,model.getShare().getTitle(),model.getShare().getIntro(),model.getShare().getUrl(),model.getShare().getIcon());
                break;
            case R.id.ll_share_wechat_friend:
                ShareUtil.shareCardWechatFriend(null,this,model.getShare().getTitle(),model.getShare().getIntro(),model.getShare().getUrl(),model.getShare().getIcon());
                break;
            case R.id.ll_share_qq_friend:
                ShareUtil.shareCardQQ(this,null,model.getShare().getTitle(),model.getShare().getIntro(),model.getShare().getUrl(),model.getShare().getIcon());
                break;
            case R.id.ll_share_qq_qzone:
                ShareUtil.shareCardQZone(this,null,model.getShare().getTitle(),model.getShare().getIntro(),model.getShare().getUrl(),model.getShare().getIcon());
                break;

            case R.id.edit:
                Intent intent = new Intent(this,CardEditNewActivity.class);
                intent.putExtra("id",mId);
                startActivity(intent);
                break;

            //我的名片 生成二维码
            case R.id.er_q:
                if("2".equals(type)){
                    getEr_q.initParama(MineCardActivity.this, "3", mId==null?"":mId, callback());
                }else{
                    getEr_q.initParama(MineCardActivity.this, "2", AppManager.getInstance(this).getUserInfo().getUser_id(), callback());
                }
                break;
        }
    }

    //获取用户二维码信息的接口回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    Logger.e("用户信息", s);
                    if ("0".equals(code)) {
                        String data = object.optString("data");
                        if (data != null) {
                            new Er_q().execute(data);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        };
    }

    //生成二维码 带logo
    private class Er_q extends AsyncTask<String,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bit = ZxingCodeUtils.generateBitmap(params[0], 1000, 1000);
            Bitmap logo_bit = null;
            if("2".equals(type)){
                logo_bit = ZxingCodeUtils.getBitmap(model.getShare().getIcon());
            }else{
                logo_bit = ZxingCodeUtils.getBitmap(userInfo.getUser_face_img());
            }
            Bitmap lo_bit = ZxingCodeUtils.addLogo(bit, logo_bit);
            return lo_bit;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if("2".equals(type)){
                Dialog = new Er_QDialog(MineCardActivity.this, model.getCompany()+"", model.getShare().getIcon(), bitmap, model.getName());
            }else{
                Dialog = new Er_QDialog(MineCardActivity.this, userInfo.getCompany(), userInfo.getUser_face_img(), bitmap, userInfo.getUser_name());
            }
            Dialog.show();
        }
    }
}
