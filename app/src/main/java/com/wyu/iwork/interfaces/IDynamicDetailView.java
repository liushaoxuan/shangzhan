package com.wyu.iwork.interfaces;

/**
 * Created by lx on 2016/12/27.
 * 动态详情
 */

public interface IDynamicDetailView {

    //返回
    public void goback();

    //点赞
    public void doPraise(String dynamic_id);

    //评论
    public void doComment(String dynamic_id);

    //删除评论
    public void deleteComment(String comment_id,int position);


}
