package com.wyu.iwork.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.callback.StringCallback;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.HomeWorkAdapter;
import com.wyu.iwork.application.MyApplication;
import com.wyu.iwork.model.HomeApllyModel;
import com.wyu.iwork.model.HomeDailyModel;
import com.wyu.iwork.model.HomeTaskModel;
import com.wyu.iwork.model.HomeWorkBean;
import com.wyu.iwork.model.HomeWorkNoticeModel;
import com.wyu.iwork.model.HomeWorkSchedule;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.CacheKey;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.view.activity.CheckWorkAttendanceActivity;
import com.wyu.iwork.view.activity.CreateNewTaskActivity;
import com.wyu.iwork.view.activity.CreateScheduleActivity;
import com.wyu.iwork.view.activity.MainActivity;
import com.wyu.iwork.view.activity.oaApprovalActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by sxliu on 2017/4/10.
 * 首页 —— 工作
 */

public class HomeWorkFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private static String TAG = HomeWorkFragment.class.getSimpleName();

    /**
     * 当前时间
     */
    @BindView(R.id.home_work_current_time)
    TextView currentTime;
    /**
     * 当前日期
     */
    @BindView(R.id.home_work_current_date)
    TextView currentDate;
    /**
     * 当前星期几
     */
    @BindView(R.id.home_work_current_week)
    TextView currentWeek;

    /**
     * 刷新控件
     */
    @BindView(R.id.home_work_refreshview)
    BGARefreshLayout refreshview;

    @BindView(R.id.fragment_home_work_page_recyclerview)
    RecyclerView recyclerView;

    private HomeWorkAdapter adapter;


    //日程安排list
    private List<HomeWorkSchedule> Schedulelist = new ArrayList<>();
    //任务list
    private List<HomeTaskModel> Tasklist = new ArrayList<>();
    //申请list
    private List<HomeApllyModel> Applylist = new ArrayList<>();
    //工作日报list
    private List<HomeDailyModel> Dailylist = new ArrayList<>();
    private Timer timer = new Timer();
    private TimerTask task;
    private UserInfo userInfo;
    private MainActivity activity;
    private List<List> list = new ArrayList<>();

    //实时更新当前时间
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    currentTime.setText(format.format(date).toString());
                    Logger.e("-------time-----", time + "");
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo = MyApplication.userInfo;
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_work_page, container, false);
        ButterKnife.bind(this, view);
        getcurrentTime();
        activity.setRefresh(refreshview);
        refreshview.setDelegate(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new HomeWorkAdapter(activity, list);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @OnClick({R.id.fragment_home_work_page_fast_sign, R.id.fragment_home_work_page_release_task, R.id.fragment_home_work_page_aplly_aproval, R.id.fragment_home_work_page_build_schedule})
    void Click(View v) {
        Intent intent = null;
        if (CustomUtils.showDialogForBusiness(getActivity())) {
            switch (v.getId()) {
                case R.id.fragment_home_work_page_fast_sign://快速签到
                    intent = new Intent(activity, CheckWorkAttendanceActivity.class);
                    break;
                case R.id.fragment_home_work_page_release_task://发布任务
                    intent = new Intent(activity, CreateNewTaskActivity.class);
                    break;
                case R.id.fragment_home_work_page_aplly_aproval://申请审批
                    intent = new Intent(activity, oaApprovalActivity.class);
                    break;
                case R.id.fragment_home_work_page_build_schedule://新建日程
                    intent = new Intent(activity, CreateScheduleActivity.class);
                    break;
            }
            activity.startActivity(intent);
        }
    }

    private void getcurrentTime() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        currentDate.setText(format.format(date).toString());
        format = new SimpleDateFormat("HH:mm");
        currentTime.setText(format.format(date).toString());
        format = new SimpleDateFormat("EEEE");
        currentWeek.setText(format.format(date).toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        getWorkData();
        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 5000, 5000);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (task != null) {
            task.cancel();
        }
    }

    //获取工作内容
    private void getWorkData() {
        String url = Constant.URL_HOME_WORK;

        String user_id = userInfo == null ? "" : userInfo.getUser_id();
        String company_id = userInfo == null ? "" : userInfo.getCompany_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id + company_id);
        Map<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);
        map.put("company_id", company_id);
        map.put("F", F);
        map.put("V", V);
        map.put("RandStr", RandStr);
        map.put("Sign", sign);

        //将参数拼接到url后面
        String murl = RequestUtils.getRequestUrl(url, map);
        Logger.e(TAG,murl);
        activity.doRequestget(murl, CacheKey.HOMEWORK, new StringCallback() {


            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                refreshview.endRefreshing();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Logger.e("homework",s);
                getdataSuccess(s);
            }

            @Override
            public void onCacheSuccess(String s, Call call) {
                getdataSuccess(s);
            }

            @Override
            public void onCacheError(Call call, Exception e) {
                super.onCacheError(call, e);
            }
        });

    }

    private void getdataSuccess(String s) {
        try {
            refreshview.endRefreshing();
            JSONObject object = new JSONObject(s);
            String code = object.optString("code");
            if ("0".equals(code)) {
                JSONObject data = object.optJSONObject("data");
                JSONArray schedule = data.optJSONArray("schedule");
                JSONArray task = data.optJSONArray("task");
                JSONArray apply = data.optJSONArray("apply");
                JSONArray daily = data.optJSONArray("daily");
                list.clear();
                if (schedule != null) {
                    Schedulelist = JSON.parseArray(schedule.toString(), HomeWorkSchedule.class);
                }
                if (task != null) {
                    Tasklist = JSON.parseArray(task.toString(), HomeTaskModel.class);
                }
                if (apply != null) {
                    Applylist = JSON.parseArray(apply.toString(), HomeApllyModel.class);
                }
                if (daily != null) {
                    Dailylist = JSON.parseArray(daily.toString(), HomeDailyModel.class);
                }
                list.add(Schedulelist);
                list.add(Tasklist);
                list.add(Applylist);
                list.add(Dailylist);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), object.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.e("response------------>", s);
    }


    //刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        getWorkData();
    }

    //加载更多
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }


}
