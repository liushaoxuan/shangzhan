package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/4/13.
 * 首页——工作——日程
 */

public class HomeWorkSchedule implements Serializable {

    private String id = ""; // 日程ID
    private String type = ""; //  日程类型 1：工作事务 2：个人事务 3:上午签到 4：下午签到
    private String status = ""; //  上下午签到状态 0：正常签到 1：迟到 2：早退 9：未打卡 99：非工作日签到
    private String text = ""; //  日程具体事项
    private String begin_time = ""; //  日程开始时间  格式：23:11:00
    private String end_time = ""; //  日程结束时间  格式：23:11:00

    public HomeWorkSchedule() {
    }

    public HomeWorkSchedule(String id, String type, String status, String text, String begin_time, String end_time) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.text = text;
        this.begin_time = begin_time;
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
