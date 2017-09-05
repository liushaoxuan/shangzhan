package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/4/11.
 * 联系人model
 */

public class ContactModel implements Serializable {

    /**
     * 用户ID
     */
    private String user_id;
    /**
     * 姓名
     */
    private String real_name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String face_img;
    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 首字母
     */
    private String firstPY = "";

    public ContactModel() {
    }

    public ContactModel(String user_id, String real_name, String phone, String face_img, String pinyin) {
        this.user_id = user_id;
        this.real_name = real_name;
        this.phone = phone;
        this.face_img = face_img;
        this.pinyin = pinyin;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }
}
