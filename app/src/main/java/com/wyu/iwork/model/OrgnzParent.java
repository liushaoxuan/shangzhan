package com.wyu.iwork.model;

import com.github.huajianjiang.expandablerecyclerview.widget.Parent;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/28.
 */
public class OrgnzParent implements Parent<OrgnzChild> {
    private static final String TAG = "OrgnzParent";
    private String department_name;
    private String department_id;
    private List<OrgnzChild> junior;

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


    public List<OrgnzChild> getJunior() {
        return junior;
    }

    public void setJunior(List<OrgnzChild> junior) {
        this.junior = junior;
    }

    @Override
    public List<OrgnzChild> getChildren() {
        return junior;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return true;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    @Override
    public String toString() {
        return "OrgnzParent{" +
                "department_name='" + department_name + '\'' +
                ", department_id='" + department_id + '\'' +
                ", junior=" + junior +
                '}';
    }
}
