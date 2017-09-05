package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AttendanceStatisticsAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.AttendanceModel;
import com.wyu.iwork.model.StoreModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 * 考勤统计
 */
public class AttendanceStatisticsActivity extends BaseActivity {

    private static final  String TAG = AttendanceStatisticsActivity.class.getSimpleName();

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;


    @BindView(R.id.activity_attendance_statistics_recyclerview)
    RecyclerView recyclerview;


    private AttendanceStatisticsAdapter adapter;

    private List<AttendanceModel> datas = new ArrayList<>();

    //请求地址
    private String url = Constant.URL_TEAM_MONTH;


    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_statistics);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
        initParama();
    }

    //初始化
    private void init(){
        title.setText("考勤统计表");
        edit.setVisibility(View.GONE);
        adapter = new AttendanceStatisticsAdapter(this,datas);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
    }


    @OnClick(R.id.action_back)
    void Click(){
        onBackPressed();
    }


    //初始化请求参数
    private void initParama() {
        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(Constant.F + Constant.V + RandStr + user_id);

        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("F", Constant.F);
        map.put("V", Constant.V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);
        //将参数拼接到url后面
        url = RequestUtils.getRequestUrl(url, map);
        OkgoRequest(url,callback());
    }

    //请求回调
    private DialogCallback callback(){
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {

                Logger.e(TAG,e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    Logger.e(TAG,s);
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    if ("0".equals(code)) {
                        JSONArray data = object.optJSONArray("data");
                        List<AttendanceModel> templist = JSON.parseArray(data.toString(),AttendanceModel.class);
                        datas.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;

                    }else {
                        Toast.makeText(AttendanceStatisticsActivity.this,object.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
