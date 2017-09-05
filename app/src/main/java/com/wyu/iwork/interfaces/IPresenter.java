package com.wyu.iwork.interfaces;

/**
 * Created by jhj_Plus on 2016/10/27.
 */

public interface IPresenter<V> {

    void attachView(V view);

    void detachView();
}
