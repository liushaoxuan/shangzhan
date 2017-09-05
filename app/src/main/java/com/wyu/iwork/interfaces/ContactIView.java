package com.wyu.iwork.interfaces;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/3.
 */
public interface ContactIView<T> extends IView {
    void onLoadFinished(T data);
}
