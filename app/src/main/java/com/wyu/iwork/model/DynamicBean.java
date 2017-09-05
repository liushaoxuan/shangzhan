package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2016/12/23.
 * 动态——列表javabean
 */

public class DynamicBean implements Serializable {

    /**
     * 动态ID
     */
//    private String  dynamic_id;
    private String  id;
    /**
     * 用户ID
     */
    private String user_id ;
    /**
     * 用户头像
     */
    private String  user_face_img;
    /**
     * 用户昵称
     */
    private String  nick_name;
    /**
     * 用户手机号
     */
    private String  user_phone;
    /**
     * 动态内容
     */
    private String  text;
    /**
     * 赞数
     */
    private String  praise_count;
    /**
     * 评论数
     */
    private String  comment_count;
    /**
     * 添加时间 格式:2016-12-12 12:12
     */
    private String  add_time;
    /**
     * 是否点赞  0未点赞  1已经点赞
     */
    private String  is_praise;


    public DynamicBean() {
    }

    public DynamicBean(String id, String user_id, String user_face_img, String nick_name, String user_phone, String text, String praise_count, String comment_count, String add_time, String is_praise) {
        this.id = id;
        this.user_id = user_id;
        this.user_face_img = user_face_img;
        this.nick_name = nick_name;
        this.user_phone = user_phone;
        this.text = text;
        this.praise_count = praise_count;
        this.comment_count = comment_count;
        this.add_time = add_time;
        this.is_praise = is_praise;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_face_img() {
        return user_face_img;
    }

    public void setUser_face_img(String user_face_img) {
        this.user_face_img = user_face_img;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(String praise_count) {
        this.praise_count = praise_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getIs_praise() {
        return is_praise;
    }

    public void setIs_praise(String is_praise) {
        this.is_praise = is_praise;
    }
}
