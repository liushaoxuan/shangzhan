package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/4/11.
 * 手机通讯录model
 */

public class ContactsModel implements Serializable {
    /**
     * 用户名
     */
    private String name;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 首字母拼音
     */
    private String first_py;

    /**
     * 是否邀请
     */
    private boolean isinvite = false;

    public ContactsModel(String name, String phone, String first_py) {
        this.name = name;
        this.phone = phone;
        this.first_py = first_py;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst_py() {
        return first_py;
    }

    public void setFirst_py(String first_py) {
        this.first_py = first_py;
    }

    public boolean isinvite() {
        return isinvite;
    }

    public void setIsinvite(boolean isinvite) {
        this.isinvite = isinvite;
    }

    @Override
    public String toString() {
        return "ContactsModel{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", first_py='" + first_py + '\'' +
                ", isinvite=" + isinvite +
                '}';
    }
}
