package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/7/31.
 */

public class OutSignRecord {
    private String code;
    private String msg;
    private ArrayList<Record> data;

    public class Record{
        private String outsign_id ; //  外勤签到ID
        private String username ; //  姓名
        private String time ; //  时间
        private String text ; //  出勤事由
        private String img ; //  图片
        private String building ; //  建筑物名称
        private String address ; //  外勤签到地点
        private String face_img; //头像

        public String getOutsign_id() {
            return outsign_id;
        }

        public String getUsername() {
            return username;
        }

        public String getTime() {
            return time;
        }

        public String getText() {
            return text;
        }

        public String getImg() {
            return img;
        }

        public String getBuilding() {
            return building;
        }

        public String getAddress() {
            return address;
        }

        public String getFace_img() {
            return face_img;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "outsign_id='" + outsign_id + '\'' +
                    ", username='" + username + '\'' +
                    ", time='" + time + '\'' +
                    ", text='" + text + '\'' +
                    ", img='" + img + '\'' +
                    ", building='" + building + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Record> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "OutSignRecord{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
