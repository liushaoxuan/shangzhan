package com.wyu.iwork.interfaces;

/**
 * @author HuaJian Jiang.
 *         Date 2017/1/16.
 */
public interface DataIView<T> extends IView {

    void onReadFinished(boolean success, T data);

    void onWriteFinished(boolean success);
}
