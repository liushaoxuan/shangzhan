package com.wyu.iwork.presenter;

import android.content.Context;

import com.wyu.iwork.view.activity.LoginActivity;

/**
 * Created by lx on 2016/12/26.
 * 登录——逻辑
 */

public interface ILoginPresnter {
    void clear();
    void doLogin(LoginActivity context, String name, String passwd);
}
