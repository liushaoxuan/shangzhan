package com.wyu.iwork.presenter;

/**
 * Created by lx on 2016/12/27.
 * 动态——评论逻辑层
 */

public interface ICommentPresenter {
    //清空输入框
    public void clear();

    //发布评论
    public void releaseComment(String text);
}
