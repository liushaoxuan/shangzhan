package com.wyu.iwork.model;

/**
 * Created by lx on 2017/1/6.
 */

public class ChildTaskModel {
    private String task_id;       //任务ID
    private String task;          //任务名称
    private String header;        //负责人ID
    private String header_id;            //第一位负责人ID
    private String header_name;     //第一位负责人用户名
    private String face_img;      //用户头像
    private String begin_time;    //开始时间
    private String end_time;      //结束时间

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader_id() {
        return header_id;
    }

    public void setHeader_id(String header_id) {
        this.header_id = header_id;
    }

    public String getHeader_name() {
        return header_name;
    }

    public void setHeader_name(String header_name) {
        this.header_name = header_name;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
