package com.wyu.iwork.model;

import com.github.huajianjiang.expandablerecyclerview.widget.Parent;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/10/28.
 */
public class OrgnzChild implements Parent<OrgnzChild.User> {
    private static final String TAG = "OrgnzParent";
    /**
     * 1. 用户 2. 部门
     */
    private int type;

    /**用户信息*/
    private String id;
    private String name;
    private String face_img;

    /**部门信息*/
    private String department_name;
    private String department_id;
    private List<User> user;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //-----------------user--------------------\\

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

    //-----------------organz--------------------\\

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

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    @Override
    public List<User> getChildren() {
        return user;
    }

    @Override
    public boolean isInitiallyExpandable() {
        return true;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }






    public static class Organz implements Parent<User> {
        private String department_name;
        private String department_id;
        private List<User> user;

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

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

        @Override
        public List<User> getChildren() {
            return user;
        }

        @Override
        public boolean isInitiallyExpandable() {
            return true;
        }

        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }
    }

    public static class User {
        private String id;
        private String name;
        private String face_img;

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
    }
}
