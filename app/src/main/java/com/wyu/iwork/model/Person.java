package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/3/21.
 */

public class Person implements Serializable{

    private int code;
    private String msg;
    private ArrayList<PersonMessage> data;

    public class PersonMessage implements Serializable{
        private String department,department_id,face_img,id,real_name,user_name,pinyin,role;
        private boolean isSelected;

        public String getPinyin() {
            return pinyin;
        }

        public String getRole() {
            return role;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getDepartment() {
            return department;
        }

        public String getDepartment_id() {
            return department_id;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getId() {
            return id;
        }

        public String getReal_name() {
            return real_name;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public void setDepartment_id(String department_id) {
            this.department_id = department_id;
        }

        public void setFace_img(String face_img) {
            this.face_img = face_img;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return "PersonMessage{" +
                    "department='" + department + '\'' +
                    ", department_id='" + department_id + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", id='" + id + '\'' +
                    ", real_name='" + real_name + '\'' +
                    ", user_name='" + user_name + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<PersonMessage> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Person{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
