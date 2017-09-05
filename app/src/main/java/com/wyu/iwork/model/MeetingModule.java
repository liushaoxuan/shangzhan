package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/14.
 */

public class MeetingModule {
    /**
     * Array
     (
     private String code = "" ;// 请求结果：数字 0 代表请求成功，非 0 代表请求失败
     private String msg = "" ;// 请求结果状态说明，可以提示给用户
     private String data = "" ;// Array
     (
     private String id = "" ;// 会议id
     private String text = "" ;// 会议内容
     private String sender_id = "" ;// 发送者用户ID
     private String sender_name = "" ;// 发送者用户姓名
     private String face_img = "" ;// 发送者用户头像
     private String add_time = "" ;// 发布时间
     )

     )
     */
    private int code;
    private String msg;
    private ArrayList<Meeting> data;

    public MeetingModule() {
    }

    public MeetingModule(int code, String msg, ArrayList<Meeting> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public class Meeting {

        private String id = "";// 会议id
        private String text = "";// 会议内容
        private String address = "";// 会议地址
        private String user_id = "";// 发送者用户ID
        private String user_name = "";// 发送者用户姓名
        private String face_img = "";// 发送者用户头像
        private int status;// 通知的阅读状态，0：未读；1：已读
        private String time = "";// 会议开始时间 格式2017-01-03 21:00:00
        private String apply_type = "";// 0:会议 1:请假 2:加班 3:出差 4:报销
        private String type = "";// 0:我发起的 1:待我审批的 2:我已经审批的 3:抄送我的
        private String detail_url = "";// 当type为5时显示会议详情页面URL地址,否则显示关联id

        public Meeting() {
        }

        public Meeting(String id, String text, String address, String user_id, String user_name, String face_img, int status, String time, String apply_type, String type, String detail_url) {
            this.id = id;
            this.text = text;
            this.address = address;
            this.user_id = user_id;
            this.user_name = user_name;
            this.face_img = face_img;
            this.status = status;
            this.time = time;
            this.apply_type = apply_type;
            this.type = type;
            this.detail_url = detail_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getFace_img() {
            return face_img;
        }

        public void setFace_img(String face_img) {
            this.face_img = face_img;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getApply_type() {
            return apply_type;
        }

        public void setApply_type(String apply_type) {
            this.apply_type = apply_type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDetail_url() {
            return detail_url;
        }

        public void setDetail_url(String detail_url) {
            this.detail_url = detail_url;
        }

        @Override
        public String toString() {
            return "Meeting{" +
                    "id='" + id + '\'' +
                    ", text='" + text + '\'' +
                    ", address='" + address + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", face_img='" + face_img + '\'' +
                    ", status=" + status +
                    ", time='" + time + '\'' +
                    ", apply_type='" + apply_type + '\'' +
                    ", type='" + type + '\'' +
                    ", detail_url='" + detail_url + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Meeting> getData() {
        return data;
    }

    public void setData(ArrayList<Meeting> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MeetingModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
