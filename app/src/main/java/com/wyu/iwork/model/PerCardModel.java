package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/11.18:02
 * 邮箱：2587294424@qq.com
 */

public class PerCardModel implements Serializable {
    private String user_id = "";//用户自己的ID
    private String name = "";//名片信息：名字
    private String face_img = "";//头像地址
    private String phone = "";//名片信息：手机
    private String email = "";//名片信息：邮箱
    private String wechat_id = "";//名片信息：微信
    private String job = "";//名片信息：职位
    private String company = "";//名片信息：公司
    private String department = "";//名片信息：部门
    private String address = "";//名片信息：地址
    private ShareModel share;//

    public PerCardModel() {
    }

    public PerCardModel(String user_id, String name, String face_img, String phone, String email, String wechat_id, String job, String company, String department, String address, ShareModel share) {
        this.user_id = user_id;
        this.name = name;
        this.face_img = face_img;
        this.phone = phone;
        this.email = email;
        this.wechat_id = wechat_id;
        this.job = job;
        this.company = company;
        this.department = department;
        this.address = address;
        this.share = share;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWechat_id() {
        return wechat_id;
    }

    public void setWechat_id(String wechat_id) {
        this.wechat_id = wechat_id;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ShareModel getShare() {
        return share;
    }

    public void setShare(ShareModel share) {
        this.share = share;
    }
}
