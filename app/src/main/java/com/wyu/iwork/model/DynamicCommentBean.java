package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2016/12/23.
 * 动态——评论javabean
 */

public class DynamicCommentBean implements Serializable {
    /**
     * 评论ID
     */
    private String id;
    /**
     * 动态ID
     */
    private String dynamic_id;
    /**
     * 用户id
     */
    private String user_id;
    /**
     * 用户昵称
     */
    private String nick_name;
    /**
     * 评论内容
     */
    private String text;

    /**
     * 时间
     */
    private String add_time;

    /**
     * pid
     */
    private String pid;

    public DynamicCommentBean() {
    }

    public DynamicCommentBean(String id, String dynamic_id, String user_id, String nick_name, String text, String add_time, String pid) {
        this.id = id;
        this.dynamic_id = dynamic_id;
        this.user_id = user_id;
        this.nick_name = nick_name;
        this.text = text;
        this.add_time = add_time;
        this.pid = pid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
