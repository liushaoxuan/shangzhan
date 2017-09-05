package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/4/12.
 */

public class Schedule implements Serializable{

    private int code;
    private String msg;

    private ArrayList<ScheduleBean> data;

    public Schedule() {
    }

    public class ScheduleBean implements Serializable{
        private String add_time,begin_time,end_time,face_img,id,text,type,title;

        public String getAdd_time() {
            return add_time;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "ScheduleBean{" +
                    "add_time='" + add_time + '\'' +
                    ", begin_time='" + begin_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", id='" + id + '\'' +
                    ", text='" + text + '\'' +
                    ", type='" + type + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<ScheduleBean> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
