package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/4/13.
 * 组织架构二级菜单
 */

public class OrganizeJuniorModel implements Serializable {
    /**
     * 区别用户和部门，1:用户 2:部门
     */
    private String type;
    /**
     *  部门名称【2级】
     */
    private String department_name;
    /**
     *  部门ID
     */
    private String department_id;
    /**
     *  下级菜单集合
     */
    private List<OrganizeUserModel> user;
    /**
     *  用户id
     */
    private String id;
    /**
     *  用户昵称
     */
    private String name;
    /**
     *  用户头像
     */
    private String face_img;
    /**
     *  用户电话
     */
    private String phone;
    /**
     * 是否展开 默认收起
     */
    private boolean expand = false;

    public OrganizeJuniorModel() {
    }

    public OrganizeJuniorModel(String type, String department_name, String department_id, List<OrganizeUserModel> user, String id, String name, String face_image, String phone) {
        this.type = type;
        this.department_name = department_name;
        this.department_id = department_id;
        this.user = user;
        this.id = id;
        this.name = name;
        this.face_img = face_image;
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public List<OrganizeUserModel> getUser() {
        return user;
    }

    public void setUser(List<OrganizeUserModel> user) {
        this.user = user;
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

    public void setFace_img(String face_image) {
        this.face_img = face_image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
