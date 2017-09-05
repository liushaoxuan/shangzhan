package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/6/5.17:10
 * 邮箱：2587294424@qq.com
 * 考勤统计model
 */

public class AttendanceModel implements Serializable {

    private String id = "";// 成员ID
    private String name = "";// 成员姓名
    private String attendance = "";// 出勤天数
    private String absence = "";// 缺勤次数
    private String late = "";// 迟到次数
    private String early = "";// 早退次数
    private String work_out = "";// 出勤次数
    private String work_on_business = "";// 出差时间(小时)
    private String work_leave = "";// 请假时间(小时)
    private String work_over = "";// 加班时间(小时)

    public AttendanceModel() {
    }

    public AttendanceModel(String id, String name, String attendance, String absence, String late, String early, String work_out, String work_on_business, String work_leave, String work_over) {
        this.id = id;
        this.name = name;
        this.attendance = attendance;
        this.absence = absence;
        this.late = late;
        this.early = early;
        this.work_out = work_out;
        this.work_on_business = work_on_business;
        this.work_leave = work_leave;
        this.work_over = work_over;
    }

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

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAbsence() {
        return absence;
    }

    public void setAbsence(String absence) {
        this.absence = absence;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getEarly() {
        return early;
    }

    public void setEarly(String early) {
        this.early = early;
    }

    public String getWork_out() {
        return work_out;
    }

    public void setWork_out(String work_out) {
        this.work_out = work_out;
    }

    public String getWork_on_business() {
        return work_on_business;
    }

    public void setWork_on_business(String work_on_business) {
        this.work_on_business = work_on_business;
    }

    public String getWork_leave() {
        return work_leave;
    }

    public void setWork_leave(String work_leave) {
        this.work_leave = work_leave;
    }

    public String getWork_over() {
        return work_over;
    }

    public void setWork_over(String work_over) {
        this.work_over = work_over;
    }

    @Override
    public String toString() {
        return "AttendanceModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", attendance='" + attendance + '\'' +
                ", absence='" + absence + '\'' +
                ", late='" + late + '\'' +
                ", early='" + early + '\'' +
                ", work_out='" + work_out + '\'' +
                ", work_on_business='" + work_on_business + '\'' +
                ", work_leave='" + work_leave + '\'' +
                ", work_over='" + work_over + '\'' +
                '}';
    }
}
