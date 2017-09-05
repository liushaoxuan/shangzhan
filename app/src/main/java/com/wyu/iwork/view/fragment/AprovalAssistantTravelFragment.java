package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.AprovalAssistantFragmentAdapter;
import com.wyu.iwork.interfaces.DeleteListener;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.AprovalAssistentModel;
import com.wyu.iwork.util.AssisttentDelUtil;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.view.activity.AprovalAssistantActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 作者： sxliu on 2017/7/21.11:08
 * 邮箱：2587294424@qq.com
 * 出差申请
 */

public class AprovalAssistantTravelFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate ,DeleteListener{

    private static String TAG = AprovalAssistantTravelFragment.class.getSimpleName();

    @BindView(R.id.fragment_aroval_assistant_refreshview)
    BGARefreshLayout refreshLayout;

    @BindView(R.id.fragment_aroval_assistant_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.nodata_layout)
    RelativeLayout nodata_layout;
    @BindView(R.id.nodata_text)
    TextView nodata_text;

    private AprovalAssistantActivity activity;
    private View view;

    private AprovalAssistantFragmentAdapter adapter;

    private List<AprovalAssistentModel> list = new ArrayList();

    private int index = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AprovalAssistantActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.fragment_aproval_assistant, null);
            ButterKnife.bind(this, view);
            activity.setRefresh(refreshLayout);
            refreshLayout.setDelegate(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            adapter = new AprovalAssistantFragmentAdapter(activity, list);
            recyclerView.setAdapter(adapter);
            adapter.setOnDeletListener(this);
        }
        activity.getData(index,"3",callback());
        return view;
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

        index = 1;
        list.clear();
        activity.getData(index,"3",callback());
        refreshLayout.endRefreshing();

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        index++;
        activity.getData(index,"3",callback());
        return true;
    }

    private DialogCallback callback() {
        return new DialogCallback(activity) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Logger.e(TAG, e.getMessage());
                refreshLayout.endRefreshing();
                refreshLayout.endLoadingMore();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                refreshLayout.endRefreshing();
                refreshLayout.endLoadingMore();
                Logger.e(TAG, s);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    String msg = object.optString("msg");
                    JSONArray data = object.optJSONArray("data");
                    if ("0".equals(code)) {
                        List<AprovalAssistentModel> templist = new ArrayList<>();
                        templist = JSON.parseArray(data.toString(), AprovalAssistentModel.class);
                        list.addAll(templist);
                        adapter.notifyDataSetChanged();
                        templist.clear();
                        templist = null;
                    } else {
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    }
                    if (list==null||list.size()==0){
                        nodata_layout.setVisibility(View.VISIBLE);
                        nodata_text.setText(R.string.no_aproval_notification);

                    }else {
                        nodata_layout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        };
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void onDeleteListener(View view, int position) {

//类别 1：任务 2：公告 3：会议 4：审批
        AssisttentDelUtil.message_delete(activity, "4", list.get(position).getMessage_id(), delCallback(position));
    }


    private DialogCallback delCallback(final int position) {
        return new DialogCallback(activity) {
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

                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
