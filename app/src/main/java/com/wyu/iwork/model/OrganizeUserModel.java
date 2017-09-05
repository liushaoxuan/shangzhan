package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/13.
 * 组织架构三级菜单model
 */

public class OrganizeUserModel implements Serializable {
    /**
     * 用户ID
     */
    private String id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 用户头像
     */
    private String face_img;
    /**
     * 用户电话
     */
    private String phone;

    public OrganizeUserModel() {
    }

    public OrganizeUserModel(String id, String name, String face_img, String phone) {
        this.id = id;
        this.name = name;
        this.face_img = face_img;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
