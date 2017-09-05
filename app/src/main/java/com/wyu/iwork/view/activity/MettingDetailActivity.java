package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RegexUtils;
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
 *         会议详情
 */
public class MettingDetailActivity extends BaseActivity {

    private static String TAG = MettingDetailActivity.class.getSimpleName();

    @BindView(R.id.action_title)
    TextView title;
    @BindView(R.id.action_edit)
    TextView edit;

    /**
     * 会议地点
     */
    @BindView(R.id.activity_metting_detail_addr)
    TextView addr;

    /**
     * 会议时间
     */
    @BindView(R.id.activity_metting_detail_time)
    TextView mtime;

    /**
     * 会议内容
     */
    @BindView(R.id.activity_metting_detail_content)
    TextView content;

    /**
     * 会议id
     */
    private String meeting_id = "";

    private String url = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metting_detail);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        getMettingId();
        init();
        initparama();
    }

    //初始化
    private void init() {
        title.setText("会议内容");
        edit.setVisibility(View.GONE);

    }

    /**
     * 获取会议id】
     */
    private void getMettingId() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            meeting_id = bundle.getString("metting_id");
        }
    }

    @OnClick(R.id.action_back)
    void click() {
        onBackPressed();
    }

    private void initparama() {
        url = Constant.URL_METTINGDETAIL;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("meeting_id", meeting_id);
        params.put("F", F);
        params.put("V", V);
        params.put("RandStr", RandStr);
        params.put("Sign", sign);
        url = RequestUtils.getRequestUrl(url, params);
        Logger.e(TAG, url);
        OkGo.get(url)
                .params(params)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Logger.e(TAG, e.getMessage());
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
                                String meeting_id = data.optString("meeting_id");
                                String user_id = data.optString("user_id");
                                String user_name = data.optString("user_name");
                                String text = data.optString("text");
                                String address = data.optString("address");
                                String time = data.optString("time");

                                content.setText(text);
                                addr.setText(address);
                                mtime.setText(time);
                            } else {
                                Toast.makeText(MettingDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
