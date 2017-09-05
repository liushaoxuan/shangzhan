package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.TaskAssistantAdapter;
import com.wyu.iwork.interfaces.DeleteListener;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.MessageNoticeAssistanBean;
import com.wyu.iwork.model.TaskAssistentModel;
import com.wyu.iwork.util.AssisttentDelUtil;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author sxliu
 *         任务助手
 */
public class TaskAssistantActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, DeleteListener {

    private static String TAG = TaskAssistantActivity.class.getSimpleName();

    @BindView(R.id.activity_task_assistant_title)
    TextView title;

    @BindView(R.id.activity_task_assistant_del_layout)
    RelativeLayout del_layout;

    @BindView(R.id.activity_task_assistant_bga)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_task_assistant_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;
    @BindView(R.id.nodata_text)
    TextView nodata_text;

    private TaskAssistantAdapter adapter;
    private List<TaskAssistentModel> list = new ArrayList<>();
    private String url = "";
    private int pageindex = 1;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assistant);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        setRefresh(bgaRefreshLayout);
        bgaRefreshLayout.setDelegate(this);
        init();
        initparama();
    }

    //初始化
    private void init() {
        title.setText("任务助手");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAssistantAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnDeletListener(this);
    }

    @OnClick({R.id.activity_task_assistant_back, R.id.activity_task_assistant_del})
    void Click(View v) {
        switch (v.getId()) {
            case R.id.activity_task_assistant_back:
                onBackPressed();
                break;

            case R.id.activity_task_assistant_del:
                del_layout.setVisibility(View.GONE);
                break;
        }
    }


    //初始化请求参数
    private void initparama() {
        url = Constant.URL_TASK_ASSISTENT;
        String user_id = null;
        user_id = userInfo == null ? "" : userInfo.getUser_id();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = System.currentTimeMillis() + "|" + Md5Util.getRandom();
        String sign = Md5Util.getSign(F + V + RandStr + user_id);
        HttpParams params = new HttpParams();
        params.put("user_id", user_id);
        params.put("page", pageindex + "");
        params.put("F", F);
        params.put("V", V);
        params.put("RandStr", RandStr);
        params.put("Sign", sign);
        OkGo.get(url)
                .params(params)
                .execute(callback());
    }

    /**
     * 请求回调
     */
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                bgaRefreshLayout.endRefreshing();
                bgaRefreshLayout.endLoadingMore();
                Logger.e(TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    JSONArray data = object.optJSONArray("data");
                    if ("0".equals(code)) {
                        List<TaskAssistentModel> templist = new ArrayList<>();
                        templist = JSON.parseArray(data.toString(), TaskAssistentModel.class);
                        list.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                    } else {
                        Toast.makeText(TaskAssistantActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    if (list==null||list.size()==0){
                        nodata_layout.setVisibility(View.VISIBLE);
                        del_layout.setVisibility(View.GONE);
                        nodata_text.setText(R.string.no_task_notification);

                    }else {
                        nodata_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                bgaRefreshLayout.endRefreshing();
                bgaRefreshLayout.endLoadingMore();
                Logger.e(TAG, e.getMessage());
            }
        };
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        list.clear();
        pageindex = 1;
        initparama();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        pageindex++;
        initparama();
        return true;
    }


    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void onDeleteListener(View view, int position) {

//类别 1：任务 2：公告 3：会议 4：审批
        AssisttentDelUtil.message_delete(this, "1", list.get(position).getMessage_id(), delCallback(position));
    }


    private DialogCallback delCallback(final int position) {
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
                    String msg = object.optString("msg");
                    if ("0".equals(code)) {
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                    }

                    Toast.makeText(TaskAssistantActivity.this, msg, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
