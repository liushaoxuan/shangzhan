package com.wyu.iwork.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.wyu.iwork.AppManager;
import com.wyu.iwork.R;
import com.wyu.iwork.adapter.SalesLeadAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.Clue;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.widget.CrmCreateCustomDialog;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Call;
import okhttp3.Response;

import static com.wyu.iwork.util.Constant.F;
import static com.wyu.iwork.util.Constant.V;

/**
 * @author juxinhua
 * crm - 销售线索
 */
public class SalesLeadActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    private static final String TAG = SalesLeadActivity.class.getSimpleName();
    //列表容器
    @BindView(R.id.sales_lead_recycleview)
    RecyclerView recycleview;

    //返回
    @BindView(R.id.ll_back)
    AutoLinearLayout ll_back;

    //标题
    @BindView(R.id.tv_title)
    TextView tv_title;

    //添加
    @BindView(R.id.iv_add)
    ImageView iv_add;

    @BindView(R.id.crm_lead_refresh)
    BGARefreshLayout mRefreshLayout;

    @BindView(R.id.crm_lead_zanwu)
    AutoRelativeLayout crm_lead_zanwu;

    //提示文字
    @BindView(R.id.zanwu_textone)
    TextView zanwu_textone;

    @BindView(R.id.zanwu_textthree)
    TextView zanwu_textthree;

    private int page = 1;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;
    private Gson gson;
    private ArrayList<Clue.ClueMessage> clueList;
    private SalesLeadAdapter adapter;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_lead);
        hideToolbar();
        ButterKnife.bind(this);
        initRefreshLayout();
        clueList = new ArrayList<>();
        initView();
    }

    //配置BGARefreshLayout
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getLeadList();
    }

    private void initView(){
        iv_add.setVisibility(View.VISIBLE);
        tv_title.setText("销售线索");
        zanwu_textone.setText("您还没有销售线索，请在右上方“");
        zanwu_textthree.setText("”添加");
    }

    @OnClick({R.id.ll_back,R.id.iv_add})
    void Click(View v){
        switch (v.getId()){
            case R.id.ll_back:
                //返回
                onBackPressed();
                break;
            case R.id.iv_add:
                //新建销售线索
                showCreateDialog();
                break;
        }
    }

    //获取销售线索列表
    private void getLeadList(){
        /**
         *  user_id	是	int[11]       用户ID
            page	      是	int[11]       页码，获取当前页内容，默认传1
            F	      是	string[18]    请求来源：IOS/ANDROID/WEB
            V	      是	string[20]    版本号如：1.0.1
            RandStr	是	string[50]    请求加密随机数 time().|.rand()
            Sign	      是	string[400]   请求加密值 F_moffice_encode(F.V.RandStr.user_id.page)
         */
        HashMap<String,String> data = new HashMap<>();
        data.put("user_id", AppManager.getInstance(this).getUserInfo().getUser_id());
        data.put("page",page+"");
        data.put("F",F);
        data.put("V",V);
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F+V+RandStr+AppManager.getInstance(this).getUserInfo().getUser_id()+page);
        data.put("RandStr",RandStr);
        data.put("Sign",Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_GET_CLUE_LIST,data);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.i(TAG,s);
                        if(page == 1){
                            clueList.clear();
                        }
                        parseData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        endRefreshing();
                    }
                });
    }

    private void parseData(String data){
        endRefreshing();
        try {
            if(gson == null){
                gson = new Gson();
            }
            Clue clue = gson.fromJson(data,Clue.class);
            if("0".equals(clue.getCode())){
                if(clueList.size()>0){
                    //加载更多
                    if(clue.getData() != null && clue.getData().size()>0){
                        page++;
                        clueList.addAll(clue.getData());
                        showContent(true);
                        setAdapter();
                    }else{
                        MsgUtil.shortToastInCenter(this,"已经加载全部数据!");
                    }
                }else{
                    //首次加载
                    if(clue.getData() != null && clue.getData().size()>0){
                        page++;
                        clueList.addAll(clue.getData());
                        showContent(true);
                        setAdapter();
                    }else{
                        showContent(false);
                    }
                }
            }else{
                showContent(false);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false);
        }
    }

    private void endRefreshing(){
        if(isRefreshing){
            isRefreshing = false;
            mRefreshLayout.endRefreshing();
            Logger.i(TAG,"下拉刷新");
        }else if(isLoadingMore){
            isLoadingMore = false;
            mRefreshLayout.endLoadingMore();
            Logger.i(TAG,"加载更多");
        }
    }

    private void showContent(boolean flag){
        crm_lead_zanwu.setVisibility(flag?View.GONE:View.VISIBLE);
        recycleview.setVisibility(flag?View.VISIBLE:View.GONE);
    }

    private void setAdapter(){
        if(adapter == null){
            adapter = new SalesLeadAdapter(this,clueList);
            recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    //显示选择弹窗
    private void showCreateDialog(){
        new CrmCreateCustomDialog(this, new CrmCreateCustomDialog.DialogClickListener() {
            @Override
            public void oneClick(Dialog dialog) {
                //手动新建
                Intent intent = new Intent(SalesLeadActivity.this,SalesLeadDetailActivity.class);
                intent.putExtra("type","NEW");
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void twoClick(Dialog dialog) {
                //扫描新建
                Intent intent = new Intent(SalesLeadActivity.this,RectCameraActivity.class);
                intent.putExtra("type","NEW_SALES");
                startActivity(intent);
                dialog.dismiss();
            }

            @Override
            public void threeClick(Dialog dialog) {
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        getLeadList();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        getLeadList();
        return true;
    }
}
