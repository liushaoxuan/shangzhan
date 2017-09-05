package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 *         VIP页面
 */
public class MVPMenberActivity extends BaseActivity {

    private static String TAG = MVPMenberActivity.class.getSimpleName();
    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;
    /**
     * 会员有效期
     */
    @BindView(R.id.activity_mvpmenber_usefull_time)
    TextView validityPeriod;

    /**
     * 公司名称
     */
    @BindView(R.id.activity_mvpmenber_companyName)
    TextView companyName;

    /**
     * 会员号
     */
    @BindView(R.id.activity_mvpmenber_menberId)
    TextView VIP_id;

    /**
     * 特权列表
     */
    @BindView(R.id.activity_mvpmenber_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.activity_mvpmenber_layout)
    RelativeLayout layout;

    @BindView(R.id.activity_mvpmenber)
    ScrollView thisView;

    /**
     * 暂无网络的布局
     */
    @BindView(R.id.net_has_lost)
    LinearLayout net_has_lost;

    private String url = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvpmenber);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
        initparama();
    }

    private void init() {
        title.setText("VIP会员");
        edit.setVisibility(View.GONE);
    }

    @OnClick({R.id.action_back,R.id.layout_net_has_lost_reload})
    void click(View v ) {
        switch (v.getId()){
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.layout_net_has_lost_reload://重新加载
                initparama();
                break;
        }

    }

    private void initparama() {
        url = Constant.URL_VIP;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        final String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String,String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("F", F);
        params.put("V", V);
        params.put("RandStr", RandStr);
        params.put("Sign", sign);
        url = RequestUtils.getRequestUrl(url,params);
        OkgoRequest(url, new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                Logger.e(TAG, e.getMessage());
                net_has_lost.setVisibility(View.VISIBLE);
                thisView.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.e(TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    JSONObject data = object.optJSONObject("data");
                    if ("0".equals(code)) {
                        /** [expire_time] => 到期时间 格式：2017.01.02
                         * [company] => 公司名称
                         * [card_id] => 会员卡ID
                         * [status] => 0：未有会员卡 1：已有会员卡
                         * */

                        String status = data.optString("status");
                        if ("1".equals(status)) {
                            JSONArray array = data.optJSONArray("privilege");
                            JSONObject card = data.optJSONObject("card");

                            String company = card.optString("company");
                            String card_id = card.optString("card_id");
                            String expire_time = card.optString("expire_time");
                            validityPeriod.setText("会员有效期 " + expire_time);
                            companyName.setText(company);
                            VIP_id.setText("ID:" + card_id);
                            layout.setBackgroundResource(R.mipmap.icon_vip);
                        }
                        net_has_lost.setVisibility(View.GONE);
                        thisView.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(MVPMenberActivity.this, msg, Toast.LENGTH_SHORT).show();
                        net_has_lost.setVisibility(View.VISIBLE);
                        thisView.setVisibility(View.GONE);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
