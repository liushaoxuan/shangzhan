package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.PerCardModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.ShareDialog;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 *         通讯录详情
 */
public class ContactsDetailActivity extends BaseActivity {

    private static String TAG = ContactsDetailActivity.class.getSimpleName();

    /**
     * 标题
     */
    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView textView;

    /**
     * 用户名
     */
    @BindView(R.id.activity_contacts_detail_userName)
    TextView userName;

    /**
     * 公司名称
     */
    @BindView(R.id.activity_contacts_detail_company_name)
    TextView companyName;

    /**
     * 联系电话
     */
    @BindView(R.id.activity_contacts_detail_phone)
    TextView phone;

    /**
     * 工作邮箱
     */
    @BindView(R.id.activity_contacts_detail_work_email)
    TextView email;

    /**
     * 用户头像
     */
    @BindView(R.id.activity_contacts_detail_userHead)
    CircleImageView face_img;

    private String user_id = "";

    private ShareDialog shareDialog;

    private String url = Constant.URL_MINE_CARD;

    private PerCardModel cardModel;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);
        getModel();
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
    }

    //获取 cardModel
    private void getModel() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id =   extras.getString("id");
        }
    }

    private void init() {
        textView.setVisibility(View.GONE);
        title.setText("详细资料");
        getuserInfo();
    }

    //注入事件
    @OnClick({R.id.action_back, R.id.activity_contacts_detail_share, R.id.activity_contacts_detail_sendmessage, R.id.activity_contacts_detail_call})
    void Click(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.action_back:
                onBackPressed();
                break;

            case R.id.activity_contacts_detail_share:// 分享
                if (hasData()){
                    shareDialog = null;
                    shareDialog = new ShareDialog(this, 1, cardModel == null ? "" : cardModel.getShare().getTitle(),cardModel.getShare().getIntro(), cardModel.getShare().getUrl(), cardModel.getShare().getIcon());
                    shareDialog.show();
                }

                break;

            case R.id.activity_contacts_detail_sendmessage:// 发消息
                if (hasData()){
                    RongIM.getInstance().startPrivateChat(this, cardModel == null ? "" : cardModel.getUser_id(), cardModel == null ? "" : cardModel.getName());
                }
                break;

            case R.id.activity_contacts_detail_call:// 拨打电话
                if (hasData()){
                    String phone = cardModel == null ? "" : cardModel.getPhone();
                    intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 暂无数据
     */
    private boolean hasData(){
        if (cardModel==null){
            MsgUtil.shortToastInCenter(this,"暂无数据，请检查网络");
            return false;
        }
        return true;
    }

    /**
     * 获取用户信息
     */
    private void getuserInfo() {

        url = Constant.URL_MINE_CARD;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        url = RequestUtils.getRequestUrl(url, map);
        Logger.e("****UpdateSale****", url);
        OkgoRequest(url, new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    JSONObject object = new JSONObject(s);

                    int code = object.optInt("code");
                    String msg = object.optString("msg");
                    JSONObject data = object.optJSONObject("data");
                    if (code == 0) {
                        cardModel = JSON.parseObject(data.toString(), PerCardModel.class);
                        if (cardModel != null) {
                            userName.setText(cardModel.getName());
                            phone.setText("联系电话:"+cardModel.getPhone());
                            email.setText("工作邮箱:"+cardModel.getEmail());
                            companyName.setText(cardModel.getCompany());
                            Glide.with(ContactsDetailActivity.this).load(cardModel.getFace_img()).error(R.mipmap.head_icon_nodata).into(face_img);
                        }

                    } else {
                        Toast.makeText(ContactsDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
