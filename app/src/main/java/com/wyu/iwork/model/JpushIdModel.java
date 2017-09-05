package com.wyu.iwork.model;

import org.litepal.crud.DataSupport;

/**
 * 作者： sxliu on 2017/6/30.09:25
 * 邮箱：2587294424@qq.com
 */

public class JpushIdModel extends DataSupport implements Base {

    private String JpushId = "";

    public JpushIdModel() {
    }

    public JpushIdModel(String jpushId) {
        JpushId = jpushId;
    }

    public String getJpushId() {
        return JpushId;
    }

    public void setJpushId(String jpushId) {
        JpushId = jpushId;
    }

    @Override
    public String toString() {
        return "JpushIdModel{" +
                "JpushId='" + JpushId + '\'' +
                '}';
    }
}
