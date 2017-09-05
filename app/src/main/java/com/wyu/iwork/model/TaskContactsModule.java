package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/4/13.
 */

public class TaskContactsModule implements Serializable{

    private int code;
    private String msg;
    private ArrayList<TaskContact> data;

    public class TaskContact implements Serializable{

        private String user_id,real_name,phone,face_img,pinyin;
        private boolean isSelected;

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getReal_name() {
            return real_name;
        }

        public String getPhone() {
            return phone;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getPinyin() {
            return pinyin;
        }

        @Override
        public String toString() {
            return "TaskContact{" +
                    "user_id='" + user_id + '\'' +
                    ", real_name='" + real_name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    ", isSelected=" + isSelected +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<TaskContact> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "TaskContactsModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
