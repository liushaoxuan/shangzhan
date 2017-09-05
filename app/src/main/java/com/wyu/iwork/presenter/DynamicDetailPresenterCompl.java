package com.wyu.iwork.presenter;

import com.wyu.iwork.interfaces.IDynamicDetailView;

/**
 * Created by lx on 2016/12/27.
 */

public class DynamicDetailPresenterCompl implements IDynamicDetailPresenter {

    IDynamicDetailView iDynamicDetailView;

    public DynamicDetailPresenterCompl(IDynamicDetailView iDynamicDetailView) {
        this.iDynamicDetailView = iDynamicDetailView;
    }

    @Override
    public void goback() {
        iDynamicDetailView.goback();
    }

    @Override
    public void doPraise(String dynamic_id) {
        iDynamicDetailView.doPraise(dynamic_id);

    }

    @Override
    public void doComment(String dynamic_id) {
        iDynamicDetailView.doComment(dynamic_id);
    }

    @Override
    public void deleteComment(String comment_id,int position) {
        iDynamicDetailView.deleteComment(comment_id,position);
    }
}
