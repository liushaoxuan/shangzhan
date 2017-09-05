package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/4/13.
 * 首页——工作——公告
 */

public class HomeWorkNoticeModel implements Serializable {


    /**
     * notice_id : 135
     * user_id : 23
     * user_name : 啊的
     * face_img :
     * title : 瑞文
     * text : 放逐之刃
     * add_time : 2017-04-13 09:51:44
     */

    private String notice_id;
    private String user_id;
    private String user_name;
    private String face_img;
    private String title;
    private String text;
    private String add_time;
    private String url;

    public HomeWorkNoticeModel() {
    }

    public HomeWorkNoticeModel(String notice_id, String user_id, String user_name, String face_img, String title, String text, String add_time, String url) {
        this.notice_id = notice_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.face_img = face_img;
        this.title = title;
        this.text = text;
        this.add_time = add_time;
        this.url = url;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
