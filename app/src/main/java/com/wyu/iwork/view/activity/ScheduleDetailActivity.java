package com.wyu.iwork.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.ScheduleDetailModel;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 日程详情
 */
public class ScheduleDetailActivity extends BaseActivity {

    @BindView(R.id.schedule_detail_type)
    TextView schedule_detail_type;

    @BindView(R.id.schedule_detail_begin_time)
    TextView schedule_detail_begin_time;

    @BindView(R.id.schedule_detail_end_time)
    TextView schedule_detail_end_time;

    @BindView(R.id.schedule_detail_title)
    TextView schedule_detail_title;

    @BindView(R.id.schedule_detail_content)
    TextView schedule_detail_content;

    @BindView(R.id.tv_edit)
    TextView tv_edit;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;
    private ScheduleDetailModel scheduleBean;

    //日程id
    private String id = "";

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_detail);
        hideToolbar();
        ButterKnife.bind(this);
        getExtras();
    }

    private void getExtras(){

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            id = bundle.getString("id");
        }
    }

    private void initView(){
        tv_edit.setText("编辑");
        tv_title.setText("日程详情");
        if (scheduleBean!=null){
            if(!TextUtils.isEmpty(scheduleBean.getType())){
                schedule_detail_type.setText(transformType(scheduleBean.getType()));
            }
            if(!TextUtils.isEmpty(scheduleBean.getBegin_time())){
                schedule_detail_begin_time.setText(scheduleBean.getBegin_time());
            }
            if(!TextUtils.isEmpty(scheduleBean.getEnd_time())){
                schedule_detail_end_time.setText(scheduleBean.getEnd_time());
            }
            if(!TextUtils.isEmpty(scheduleBean.getTitle())){
                schedule_detail_title.setText(scheduleBean.getTitle());
            }
            if(!TextUtils.isEmpty(scheduleBean.getText())){
                schedule_detail_content.setText(scheduleBean.getText());
            }
        }
    }

    private String transformType(String type){
        //日程类型 1：工作事务 2：个人事务
        if("1".equals(type)){
            return "工作事务";
        }else if("2".equals(type)){
            return "个人事务";
        }
        return "";
    }

    @OnClick({R.id.ll_back})
    void Click(View view){
        switch(view.getId()){
            case R.id.ll_back:
                finish();
                break;
        }
    }

    //获取日程详情
    private void getDetail(){
        String url = Constant.URL_SCHEDULE_DETAIL;
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr +id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);

        OkGo.get(murl)
                .tag("DynamicFragment")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            String code = object.optString("code");
                            if ("0".equals(code)) {
                                JSONObject data = object.optJSONObject("data");
                                scheduleBean = JSON.parseObject(data.toString(),ScheduleDetailModel.class);
                                initView();
                            } else {
                                Toast.makeText(ScheduleDetailActivity.this, object.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Logger.e("response------------>", s);
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDetail();
    }
}
