package com.wyu.iwork.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.wyu.iwork.adapter.CardViewAdapter;
import com.wyu.iwork.interfaces.DialogCallback;
import com.wyu.iwork.model.CardNewModule;
import com.wyu.iwork.model.UserInfo;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.CustomUtils;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.Md5Util;
import com.wyu.iwork.util.MsgUtil;
import com.wyu.iwork.util.RequestUtils;
import com.wyu.iwork.util.ZxingCodeUtils;
import com.wyu.iwork.util.getEr_q;
import com.wyu.iwork.view.dialog.Er_QDialog;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 名片
 */
public class CardNewActivity extends BaseActivity {

    private static final String TAG = CardNewActivity.class.getSimpleName();

    @BindView(R.id.ll_scan)
    AutoLinearLayout ll_scan;

    @BindView(R.id.ll_new_card)
    AutoLinearLayout ll_new_card;

    @BindView(R.id.ll_card_bag)
    AutoLinearLayout ll_card_bag;

    @BindView(R.id.ll_mine_card)
    AutoLinearLayout ll_mine_card;

    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.tv_notavailable)
    TextView tishi;

    @BindView(R.id.ll_back)
    AutoLinearLayout back;

    @BindView(R.id.card_recycleview)
    RecyclerView card_recycleview;

    @BindView(R.id.card_notavaliable)
    AutoLinearLayout card_notavaliable;

    @BindView(R.id.card_refresh)
    BGARefreshLayout mRefreshLayout;

    //@BindView(R.id.card_scrollview)
    //ScrollView card_scrollview;

    //二维码
    @BindView(R.id.er_q)
    ImageView er_q;

    private Er_QDialog Dialog;

    //页码
    //private int page = 1;
    private UserInfo mUser;
    private Gson gson;
    private ArrayList<CardNewModule.Card> latelyCard = new ArrayList<>();
    private CardViewAdapter adapter;
    private static final int CODE_NEW_CARD = 100;

    private boolean isLoadingMore = false;
    private boolean isRefreshing = false;

    @Nullable
    @Override
    public Fragment getFragment() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        initView();
        hideToolbar();
    }

    private void initView() {
        title.setText("名片");
        er_q.setVisibility(View.GONE);
        tishi.setText("暂无名片");
        //initRefreshLayout();
    }

    //配置BGARefreshLayout
    /**
    private void initRefreshLayout(){
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(this,true);
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        //page = 1;
        initData();
    }

    private void initData() {
        mUser = AppManager.getInstance(this).getUserInfo();
        String F = Constant.F;
        String V = Constant.V;
        String RandStr = CustomUtils.getRandStr();
        String Sign = Md5Util.getSign(F + V + RandStr + mUser.getUser_id() + "1");
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", mUser.getUser_id());
        data.put("page", "1");
        data.put("F", F);
        data.put("V", V);
        data.put("RandStr", RandStr);
        data.put("Sign", Sign);
        String murl = RequestUtils.getRequestUrl(Constant.URL_LATELY_CARD, data);
        Logger.i(TAG,murl);
        OkGo.get(murl).tag(this).cacheMode(CacheMode.DEFAULT)
                .execute(new DialogCallback(this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        super.onSuccess(s,call,response);
                        Logger.i(TAG, s);
                        latelyCard.clear();
                        parseCardData(s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        endRefresh();
                    }
                });
        if(data != null){
            data.clear();
            data = null;
        }
    }

    private void endRefresh(){
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
    private void parseCardData(String data) {
        endRefresh();
        if(gson == null){
            gson = new Gson();
        }
        try {
            CardNewModule cardModel = gson.fromJson(data, CardNewModule.class);
            if ("0".equals(cardModel.getCode())) {
                if(latelyCard.size()>0){
                    if(cardModel.getData() != null && cardModel.getData().size()>0){

                        showContent(true,card_recycleview,card_notavaliable);
                        latelyCard.addAll(cardModel.getData());
                        setAdapter();
                        //page++;
                    }else{
                        MsgUtil.shortToastInCenter(CardNewActivity.this,"已加载全部最近的名片!");
                    }
                }else{
                    if(cardModel.getData() != null && cardModel.getData().size()>0){
                        //page++;
                        showContent(true,card_recycleview,card_notavaliable);
                        latelyCard.addAll(cardModel.getData());
                        setAdapter();
                    }else{
                        showContent(false,card_recycleview,card_notavaliable);
                    }
                }
            }else{
                MsgUtil.shortToastInCenter(this,cardModel.getMsg());
                showContent(false,card_recycleview,card_notavaliable);
            }
        }catch (Exception e){
            e.printStackTrace();
            showContent(false,card_recycleview,card_notavaliable);
        }
    }

    private void setAdapter() {
        if(adapter == null){
            adapter = new CardViewAdapter(this, latelyCard);
            card_recycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            card_recycleview.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @OnClick({R.id.ll_scan, R.id.er_q, R.id.ll_new_card, R.id.ll_card_bag, R.id.ll_mine_card, R.id.ll_back})
    void Click(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_scan:
                //扫描名片
                intent = new Intent(this, RectCameraActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_new_card:
                //新建名片
                intent = new Intent(this, CardEditActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_card_bag:
                intent = new Intent(this, CardBagActivity.class);
                startActivity(intent);
                //名片夹
                break;
            case R.id.ll_mine_card:
                //我的名片
                intent = new Intent(this, MineCardActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;

            case R.id.er_q://生成二维码
                getEr_q.initParama(this, "2", userInfo.getUser_id(), callback());
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    //获取用户二维码信息的接口回调
    private DialogCallback callback() {
        return new DialogCallback(this) {
            @Override
            public void onError(Call call, Response response, Exception e) {
                Logger.e(TAG, e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                super.onSuccess(s,call,response);
                try {
                    JSONObject object = new JSONObject(s);
                    String code = object.optString("code");
                    Logger.e("用户信息", s);
                    if ("0".equals(code)) {
                        String data = object.optString("data");
                        if (data != null) {
                            new Er_q().execute(data);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        };
    }
/**
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        page = 1;
        isRefreshing = true;
        initData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        isLoadingMore = true;
        initData();
        return true;
    }
*/
    public void showContentImpl(boolean flag){
        showContent(flag,card_recycleview,card_notavaliable);
    }

    //生成二维码 带logo
    private class Er_q extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bit = ZxingCodeUtils.generateBitmap(params[0], 1000, 1000);
            Bitmap logo_bit = ZxingCodeUtils.getBitmap(userInfo.getUser_face_img());
            Bitmap lo_bit = ZxingCodeUtils.addLogo(bit, logo_bit);
            return lo_bit;
        }


        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Dialog = new Er_QDialog(CardNewActivity.this, userInfo.getCompany(), userInfo.getUser_face_img(), bitmap, userInfo.getUser_name());
            Dialog.show();
        }
    }
}
