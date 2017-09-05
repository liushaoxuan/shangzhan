package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.model.DynamicBean;

/**
 * Created by lx on 2016/12/26.
 */

public interface IDynamicPresenter {
    //跳转动态详情
    public void goDynamicDetail(Context context,DynamicBean bean);

    //点赞
    public void doPraise(Context context,int position);

    //评论
    public void doComment(Context context,DynamicBean bean);

    //会话、拨打电话。。。
    public void showAlert(Context context,DynamicBean bean,int position);
    //删除动态
    public void deleteDynamic(int position);
}
