package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/4/13.
 * 组织架构一级菜单model
 */

public class OrganizedModel implements Serializable {
    /**
     * 部门名称【1级】
     */
    private String  department_name;
    /**
     * 部门ID
     */
    private String  department_id;
    /**
     * 二级菜单集合
     */
    private List<OrganizeJuniorModel> junior;
    /**
     * 是否展开 默认收起
     */
    private boolean expand = false;

    public OrganizedModel() {
    }

    public OrganizedModel(String department_name, String department_id, List<OrganizeJuniorModel> junior) {
        this.department_name = department_name;
        this.department_id = department_id;
        this.junior = junior;
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

    public List<OrganizeJuniorModel> getJunior() {
        return junior;
    }

    public void setJunior(List<OrganizeJuniorModel> junior) {
        this.junior = junior;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
