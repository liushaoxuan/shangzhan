package com.wyu.iwork.presenter;

/**
 * Created by lx on 2016/12/27.
 * 动态详情逻辑层
 */

public interface IDynamicDetailPresenter {
    //返回
    public void goback();

    //点赞
    public void doPraise(String dynamic_id);

    //评论
    public void doComment(String dynamic_id);

    //删除评论
    public void deleteComment(String comment_id,int position);
}
