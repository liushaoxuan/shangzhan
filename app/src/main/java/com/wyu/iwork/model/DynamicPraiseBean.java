package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2016/12/23.
 * 动态点赞——javabean
 */

public class DynamicPraiseBean implements Serializable {
    /**
     * 用户ID
     */
    private String user_id;
    /**
     * 用户昵称
     */
    private String nick_name;

    public DynamicPraiseBean() {
    }

    public DynamicPraiseBean(String user_id, String nick_name) {
        this.user_id = user_id;
        this.nick_name = nick_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }
}
