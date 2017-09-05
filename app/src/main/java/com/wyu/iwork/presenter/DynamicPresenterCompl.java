package com.wyu.iwork.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wyu.iwork.R;
import com.wyu.iwork.interfaces.IDynamicView;
import com.wyu.iwork.model.DynamicBean;
import com.wyu.iwork.view.activity.PostingDetailsActivity;

/**
 * Created by lx on 2016/12/26.
 */

public class DynamicPresenterCompl implements IDynamicPresenter {

    IDynamicView iDynamicView;

    public DynamicPresenterCompl(IDynamicView iDynamicView) {
        this.iDynamicView = iDynamicView;
    }

    @Override
    public void goDynamicDetail(Context context,DynamicBean bean) {
        iDynamicView.goDynamicDetail(context,bean);
    }

    @Override
    public void doPraise(Context context,int position) {
        iDynamicView.doPraise(context,position);
    }

    @Override
    public void doComment(Context context,DynamicBean bean) {
        iDynamicView.doComment(context, bean);
    }

    @Override
    public void showAlert( Context context, DynamicBean bean,int position) {
        iDynamicView.showAlert(context,bean,position);
    }

    @Override
    public void deleteDynamic(int position) {
        iDynamicView.deleteDynamic(position);
    }
}
