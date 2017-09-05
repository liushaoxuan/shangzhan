package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CompanyContacts;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * OA——转交申请
 */
public class oaTransferApplyActivity extends BaseActivity {

    private static String TAG = oaTransferApplyActivity.class.getSimpleName();

    public static final int REQUESTCODE = 0x000111;
    @BindView(R.id.oa_title)
    TextView mTitle;

    /**
     * 意见
     */
    @BindView(R.id.activity_oa_transfer_apply_isure)
    EditText isure;

    /**
     * 提交
     */
    @BindView(R.id.activity_oa_transfer_apply_btn)
    Button btn;

    /**
     * 转交人头像
     */
    @BindView(R.id.activity_oa_transfer_apply_transfer)
    CircleImageView face_image;

    /**
     *
     */
    @BindView(R.id.activity_oa_transfer_apply_del)
    ImageView del;

    /**
     * 申请关联id
     */
    private String mId = "";
    /**
     * 转交人id
     */
    private String other_user_id = "";

    private String url = "";
    private List<CompanyContacts> list = new ArrayList<>();

    private int flag = -1;
    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_transfer_apply);
        getSupportActionBar().hide();
        getId();
        ButterKnife.bind(this);
        mTitle.setText("转交");
        btn.setText("转交");
        isure.setVisibility(View.VISIBLE);
    }

    //获取关联ID
    private void getId(){
        Bundle bundle = getIntent().getExtras();
        flag = getIntent().getFlags();
        if (bundle!=null){
            mId = bundle.getString("id");
            list = (List<CompanyContacts>) bundle.get("list");
        }
    }

    @Nullable
    @OnClick({R.id.oa_back,R.id.activity_oa_transfer_apply_btn,R.id.activity_oa_transfer_apply_transfer,R.id.activity_oa_transfer_apply_del})
    void Click(View v){
        switch (v.getId()){
            case R.id.oa_back:
                onBackPressed();
                break;
            case R.id.activity_oa_transfer_apply_btn:
                String  mIsure = isure.getText().toString().trim();
                if (other_user_id==null||"".equals(other_user_id)){
                    Toast.makeText(oaTransferApplyActivity.this, "请选择转交人", Toast.LENGTH_SHORT).show();
                    break;
                }
                initParama(mIsure);
                btn.setClickable(false);
                break;

            case R.id.activity_oa_transfer_apply_transfer://选择转交人
                if (other_user_id==null||"".equals(other_user_id)){
                    Intent intent = new Intent(this, oaAddPersonActivity.class);
                    intent.putExtra("contacts",(Serializable) list);
                    intent.setFlags(REQUESTCODE);
                    startActivityForResult(intent, REQUESTCODE);
                }else {
                    other_user_id = "";
                   Glide.with(this).load(R.mipmap.oa_plus).into(face_image);
                }

                break;

            case R.id.activity_oa_transfer_apply_del://删除转交人
                del.setVisibility(View.GONE);
                other_user_id="";
                Glide.with(this).load(R.mipmap.oa_plus).into(face_image);
                face_image.setClickable(true);
                break;
        }
    }

    /**
     * 初始化请求参数
     */
    private void initParama(String content){
        url  = Constant.URL_TRANSFER_APPLY;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String user_id = "";
        try {
            user_id = userInfo.getUser_id();
        } catch (Exception e) {
            user_id = DataSupport.findFirst(UserInfo.class).getUser_id();
        }
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr+user_id+other_user_id + mId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",user_id);
        map.put("contact_id",mId);
        map.put("content",content);
        map.put("other_user_id",other_user_id);
        map.put("F",Constant.F);
        map.put("V",Constant.V);
        map.put("RandStr",RandStr);
        map.put("Sign",sign);
        url = RequestUtils.getRequestUrl(url, map);
        Logger.e("oa_transfer_apply",url);
        OkgoRequest(url,callback());
    }

    private DialogCallback callback(){
        return new DialogCallback(this){
            @Override
            public void onError(Call call, Response response, Exception e) {
                btn.setClickable(true);
                Logger.e(TAG,e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    btn.setClickable(true);
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)){
                        Intent intent = null;
                        switch (flag){
                            case 1://请假详情
                                intent = new Intent(oaTransferApplyActivity.this,oaApplyForLeaveDetailActivity.class);
                                break;
                            case 2://加班详情
                                intent = new Intent(oaTransferApplyActivity.this,oaApplyDetailActivity.class);
                                break;
                            case 3://出差详情
                                intent = new Intent(oaTransferApplyActivity.this,oaApplyDetailActivity.class);
                                break;
                            case 4://报销详情
                                intent = new Intent(oaTransferApplyActivity.this,oaReimbursementDetailActivity.class);
                                break;
                        }
                        if (flag==10){

                        }else {

                        }

                        setResult(oaApplyDetailActivity.REQUESTCODE,intent);
                        finish();
                    }
                    Toast.makeText(oaTransferApplyActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                    Logger.e(TAG,s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }  ;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null&& REQUESTCODE==requestCode) {
            CompanyContacts item = (CompanyContacts) data.getExtras().get("model");
            if (item != null) {
                other_user_id = item.getId();
                Glide.with(this).load(item.getFace_img()).into(face_image);
            }

        }
    }
}
