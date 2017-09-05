package com.wyu.iwork.presenter;

import com.wyu.iwork.interfaces.IPresenter;
import com.wyu.iwork.interfaces.IView;

/**
 * Created by jhj_Plus on 2016/10/27.
 */
public abstract class BasePresenter<V extends IView, T> implements IPresenter<V> {
    private static final String TAG = "BasePresenter";
    /**
     * 当前 Presenter 所关联的 View
     */
    protected V view;
    /**
     * 缓存的数据
     */
    protected T cachedData;

    public T getCachedData() {
        return cachedData;
    }

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

}
