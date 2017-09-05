package com.wyu.iwork.view.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wyu.iwork.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * @author  sxliu
 */
public class oaApplyHistoryActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    @BindView(R.id.action_title)
    TextView title;

    @BindView(R.id.action_edit)
    TextView edit;

    @BindView(R.id.activity_oa_history_bga)
    BGARefreshLayout bgaRefreshLayout;

    @BindView(R.id.activity_oa_history_recyclerview)
    RecyclerView recyclerView;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_apply_history);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        init();
    }

    //初始化
    private void init(){
        title.setText("历史记录");
        edit.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bgaRefreshLayout.setDelegate(this);
    }

    //返回
    @OnClick(R.id.action_back)
    void Click(){
        onBackPressed();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
