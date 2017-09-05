package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/18.
 * 用户头像昵称model
 */

public class UserModel implements Serializable {
    /**
     *
     */
    private String id;
    /**
     *
     */
    private String user_name;
    /**
     *
     */
    private String face_img;

    public UserModel() {
    }

    public UserModel(String id, String user_name, String face_img) {
        this.id = id;
        this.user_name = user_name;
        this.face_img = face_img;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
