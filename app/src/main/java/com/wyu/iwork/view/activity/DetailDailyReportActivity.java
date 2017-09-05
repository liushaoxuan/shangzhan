package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.glide.GlideRoundTransform;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.DailyReportDetail;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author  juxinhua
 * 日报详情
 */
public class DetailDailyReportActivity extends BaseActivity {

    private static final String TAG = DetailDailyReportActivity.class.getSimpleName();
    private static final String DAILY_ID = "daily_id";

    @BindView(R.id.daily_report_avatar)
    TextView avatar;

    @BindView(R.id.daily_report_name)
    TextView name;

    @BindView(R.id.daily_time_position)
    TextView position;//职位

    @BindView(R.id.tv_finish)
    TextView finish;//已完成工作

    @BindView(R.id.tv_undo)
    TextView undone;//未完成工作

    @BindView(R.id.tv_coordinate)
    TextView coordinate;//需协调的工作

    @BindView(R.id.tv_remark)
    TextView remark;//备注

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    @BindView(R.id.circle_imageview)
    CircleImageView circleImageView;

    private Gson gson;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_daily_report);
        ButterKnife.bind(this);
        initData(getIntent().getStringExtra(DAILY_ID));
        hideToolbar();
        tv_title.setText("日报详情");
    }

    public void initData(String dailyId){

        UserInfo user = AppManager.getInstance(this).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String Sign = Md5Util.getSign(F+V+RandStr+user.getUser_id()+dailyId);

        HashMap<String,String> data = new HashMap<>();
        data.put("user_id",user.getUser_id());
        data.put("daily_id",dailyId);
        data.put("F",F);
        data.put("V",V);
        data.put("Sign",Sign);
        data.put("RandStr",RandStr);
        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(Constant.URL_DETAIL_DAILY, data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG,s);
                        parseAndSetData(s);
                    }
                });
    }

    public void parseAndSetData(String data){
        try {
            if(gson == null){
                gson = new Gson();
            }
            DailyReportDetail detail = gson.fromJson(data,DailyReportDetail.class);
            if(detail.getCode() == 0){
                if(!TextUtils.isEmpty(detail.getData().getFace_img())){
                    Glide.with(this).load(detail.getData().getFace_img()).dontAnimate().transform(new CenterCrop(this), new GlideRoundTransform(this, 3)).placeholder(R.mipmap.def_img_rect).into(circleImageView);
                    avatar.setVisibility(View.GONE);
                }else{
                    avatar.setText(detail.getData().getName().substring(0,1));
                    circleImageView.setVisibility(View.GONE);
                }
                name.setText(detail.getData().getName());
                position.setText(detail.getData().getTime());
                finish.setText(detail.getData().getFinish_work());
                undone.setText(detail.getData().getUndone_work());
                coordinate.setText(detail.getData().getCoordinate_work());
                remark.setText(detail.getData().getRemark());
            }else{
                MsgUtil.shortToastInCenter(this,detail.getMsg());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_back})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                onBackPressed();
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
