package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/20.
 * 日报
 */

public class DailyReport {

    private String code;
    private String msg;
    private ArrayList<Data> data;

    public class Data{
        private String face_img,id,name,time,user_id,status,finish_work;

        public String getStatus() {
            return status;
        }

        public String getFinish_work() {
            return finish_work;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getTime() {
            return time;
        }

        public String getUser_id() {
            return user_id;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "face_img='" + face_img + '\'' +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", time='" + time + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "DailyReport{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
