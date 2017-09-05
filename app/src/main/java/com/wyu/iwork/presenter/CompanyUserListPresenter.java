package com.wyu.iwork.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wyu.iwork.model.PersonModel;
import com.wyu.iwork.net.NetParams;
import com.wyu.iwork.util.Constant;

import java.util.List;
import java.util.Map;

/**
 * Created by lx on 2017/1/4.
 */

public class CompanyUserListPresenter extends NetPresenter<List<PersonModel>> {
    private static final String TAG = CompanyUserListPresenter.class.getSimpleName();
    public CompanyUserListPresenter(Context ctxt) {
        super(ctxt,new TypeToken<List<PersonModel>>(){
        }.getType());
    }
    public void doSignIn(String company_id) {
        Map<String, String> params = new NetParams.Builder()
                .buildParams("company_id", company_id)
                .build();
        doGetRequest(true, Constant.URL_GET_COMPANYUSERLIST, params,null,TAG);
    }
}
