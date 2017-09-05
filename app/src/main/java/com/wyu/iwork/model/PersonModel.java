package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2016/12/26.
 */

public class PersonModel implements Serializable{
    private String user_name;
    private String real_name;
    private String id;
    private String face_img;
    private String department_id;
    private String department;
    private Boolean isSelected;

    public PersonModel(String real_name, String face_img,String id) {
        this.real_name = real_name;
        this.face_img = face_img;
        this.id=id;
    }

    public PersonModel(String id) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "PersonModel{" +
                "user_name='" + user_name + '\'' +
                ", real_name='" + real_name + '\'' +
                ", id='" + id + '\'' +
                ", face_img='" + face_img + '\'' +
                ", department_id='" + department_id + '\'' +
                ", department='" + department + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
