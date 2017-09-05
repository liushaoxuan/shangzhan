package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/6/9.14:38
 * 邮箱：2587294424@qq.com
 * OA——审核model
 */

public class oaAprovalModel implements Serializable {

    private String user_id = "";// 用户id
    private String face_img = "";// 用户头像
    private String user_name = "";// 用户名称
    private String status = "";// 审核状态
    private String content = "";// 意见
    private String create_time = "";// 时间

    public oaAprovalModel() {
    }

    public oaAprovalModel(String user_id, String face_img, String user_name, String status, String content, String create_time) {
        this.user_id = user_id;
        this.face_img = face_img;
        this.user_name = user_name;
        this.status = status;
        this.content = content;
        this.create_time = create_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }




}
