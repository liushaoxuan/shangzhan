package com.wyu.iwork.presenter;

import com.wyu.iwork.interfaces.ICommentView;

/**
 * Created by lx on 2016/12/27.
 * 动态——评论 实现层
 */

public class CommentPresenterCompl implements ICommentPresenter {

    ICommentView iCommentView;

    public CommentPresenterCompl(ICommentView iCommentView) {
        this.iCommentView = iCommentView;
    }

    @Override
    public void clear() {
        iCommentView.clear();
    }

    @Override
    public void releaseComment(String text) {
        iCommentView.releaseComment(text);
    }
}
