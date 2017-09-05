package com.wyu.iwork.interfaces;

/**
 * Created by lx on 2016/12/27.
 *  动态——评论
 */

public interface ICommentView {
    //清空输入框
    public void clear();

    //发布评论
    public void releaseComment(String text);

}
