package com.wyu.iwork.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.NetIView;
import com.wyu.iwork.interfaces.OnBackPressedCallback;
import com.wyu.iwork.presenter.NetPresenter;
import com.wyu.iwork.util.Constant;
import com.wyu.iwork.util.Logger;
import com.wyu.iwork.util.MsgUtil;

import org.json.JSONObject;

/**
 * Created by jhj_Plus on 2016/10/24.
 */
public abstract class BaseFragment<T> extends Fragment
        implements NetIView<T>, View.OnClickListener, OnBackPressedCallback
{
    private static final String TAG = "BaseFragment";
    protected Object childTag;
    /**
     * 是否第一次加载数据
     */
    private boolean mFirstLoad = true;

    /**
     * 网路请求失败/数据加载失败界面
     */
    private View mError;

    /**
     * 网络相关处理 Presenter 类
     */
    private NetPresenter mPresenter;

    /**
     * 初化 Fragment contentView
     */
    protected void onInitView(View rootView){}
    protected boolean onDetachView(){return true;}
    protected boolean onCancelRequest(){return true;}

    public void setNetPresenter(NetPresenter presenter) {
        mPresenter = presenter;
    }

    public NetPresenter getNetPresenter() {
        return mPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Logger.e(TAG, "****onCreate***");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        Logger.e(TAG, "****onCreateView***"+childTag);
        if (!(container instanceof FrameLayout))
            throw new IllegalArgumentException("container should be FrameLayout");
        View content = inflater.inflate(R.layout.part_error, container, true);
        mError = content.findViewById(R.id.error_layout);
        mError.setOnClickListener(this);
        // 需要加载布局（loading/empty/net error）嵌入到 container 布局中?
        return content;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Logger.e(TAG, "****onViewCreated***"+childTag);
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) mPresenter.attachView(this);
        if (view != null) onInitView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.e(TAG, "****onActivityCreated***" + childTag + "," + mFirstLoad + "," +
                      getUserVisibleHint() + "," + (mPresenter == null) + "," +
                      (mPresenter != null ? mPresenter.getCachedData() == null : "T"));
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null && getUserVisibleHint() &&
            (mFirstLoad || mPresenter.getCachedData() == null))
        {
            Logger.e(TAG,"****onActivityCreated*** loading Data");
            mPresenter.onLoadData();
            mFirstLoad = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Logger.e(TAG, "************setUserVisibleHint***************" + "," +
                      "isVisibleToUser=" + isVisibleToUser +"," +childTag+","+mFirstLoad);
        super.setUserVisibleHint(isVisibleToUser);
        if (mPresenter != null && getUserVisibleHint() && isVisible() &&
            (mFirstLoad || mPresenter.getCachedData() == null))
        {
            Logger.e(TAG, "setUserVisibleHint===>" + "loading Data");
            mPresenter.onLoadData();
            mFirstLoad = false;
        }
    }

    @Override
    public void onRequestBefore() {
        //显示 loading?
        showLoading();
    }

    @Override
    public void onSuccess(T data, JSONObject origin) {
        //同步数据至具体界面
        Logger.e(TAG, "****onSuccess***");
        hideErrorView();
    }

    @Override
    public void onFailure(int errorCode, String errorMsg) {
        //数据加载失败 显示统一的 错误/无数据 界面？
        Logger.e(TAG, "****onFailure****>>>>" + errorCode + "\n" + errorMsg);
        if (errorCode == NetIView.ERROR_VOLLEY) {
            MsgUtil.shortToast(getActivity(), errorMsg);
        }
        showErrorView();
    }

    @Override
    public void onUnLogin() {
        //显示 未登录 snackbar？
    }

    @Override
    public void onRequestAfter() {
        dismissLoading();
    }

    protected void showLoading() {
//        LoadingDialog loadingDialog = (LoadingDialog) getActivity().getSupportFragmentManager()
//                .findFragmentByTag(Constant.DIALOG_TAG_LOADING);
//        if (loadingDialog == null) loadingDialog = new LoadingDialog();
//        if (!loadingDialog.isAdded()) {
//            Logger.e(TAG, "show loading--------");
//            loadingDialog.setBackPressedCallback(this);
//            loadingDialog
//                    .show(getActivity().getSupportFragmentManager(), Constant.DIALOG_TAG_LOADING);
//        }
    }

    protected boolean dismissLoading() {
        Logger.e(TAG,"dismissLoading_before");
        DialogFragment loading = (DialogFragment) getActivity().getSupportFragmentManager()
                .findFragmentByTag(Constant.DIALOG_TAG_LOADING);
        if (loading != null) {
            loading.dismissAllowingStateLoss();
            Logger.e(TAG,"dismissLoading_after");
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            if (onCancelRequest()) mPresenter.cancelRequest();
            if (onDetachView()) mPresenter.detachView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null && onDetachView()) mPresenter.detachView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null && onDetachView()) mPresenter.detachView();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.error_layout && mPresenter != null) {
            mPresenter.onLoadData();
        }
    }

    public void showErrorView() {
        if (mError != null && mError.getVisibility() == View.GONE) {
            mError.setVisibility(View.VISIBLE);
        }
    }

    public boolean hideErrorView() {
        if (mError != null && mError.getVisibility() == View.VISIBLE) {
            mError.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        Logger.e(TAG, "onBackPressed");
        if (mPresenter != null) mPresenter.cancelRequest();
        showErrorView();
        return true;
    }

    //根据状态显示当前显示内容还是显示暂无状态
    public void showContent(boolean flag,View content,View notavaliable){
        content.setVisibility(flag?View.VISIBLE:View.GONE);
        notavaliable.setVisibility(flag?View.GONE:View.VISIBLE);
    }
}
