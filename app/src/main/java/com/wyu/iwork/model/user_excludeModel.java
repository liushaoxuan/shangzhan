package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/6/28.15:14
 * 邮箱：2587294424@qq.com
 */

public class user_excludeModel implements Serializable {
    private String id = "";


    public user_excludeModel() {
    }

    public user_excludeModel(String id) {
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
