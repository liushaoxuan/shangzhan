package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/6/7.11:59
 * 邮箱：2587294424@qq.com
 * 公司联系人model
 */

public class CompanyContacts implements Serializable {
    private String id = "";// 用户ID
    private String user_name = "";// 用户名称
    private String real_name = "";// 用户真实姓名
    private String pinyin = "";// 用户拼音
    private String face_img = "";// 用户头像
    private String department_id = "";// 用户部门ID
    private String department = "";// 用户部门名称
    private String role  = "";// 职位
    private boolean checked = false;// 是否选中
    private boolean haschecked = false;// 是否已经选择过了

    public CompanyContacts() {
    }

    public CompanyContacts(String id, String user_name, String real_name, String pinyin, String face_img, String department_id, String department, String role) {
        this.id = id;
        this.user_name = user_name;
        this.real_name = real_name;
        this.pinyin = pinyin;
        this.face_img = face_img;
        this.department_id = department_id;
        this.department = department;
        this.role = role;
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

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isHaschecked() {
        return haschecked;
    }

    public void setHaschecked(boolean haschecked) {
        this.haschecked = haschecked;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
