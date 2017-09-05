package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者： sxliu on 2017/7/28.14:02
 * 邮箱：2587294424@qq.com
 * 工作首页模型
 */

public class HomeWorkBean implements Serializable {
    private List<HomeWorkSchedule> schedule;//日程列表
    private List<HomeTaskModel> task;//任务列表
    private List<HomeWorkNoticeModel> aproval;//审批
    private List<HomeWorkNoticeModel> notice;//工作日报


    public HomeWorkBean() {
    }


    public HomeWorkBean(List<HomeWorkSchedule> schedule, List<HomeTaskModel> task, List<HomeWorkNoticeModel> aproval, List<HomeWorkNoticeModel> notice) {
        this.schedule = schedule;
        this.task = task;
        this.aproval = aproval;
        this.notice = notice;
    }

    public List<HomeWorkSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<HomeWorkSchedule> schedule) {
        this.schedule = schedule;
    }

    public List<HomeTaskModel> getTask() {
        return task;
    }

    public void setTask(List<HomeTaskModel> task) {
        this.task = task;
    }

    public List<HomeWorkNoticeModel> getAproval() {
        return aproval;
    }

    public void setAproval(List<HomeWorkNoticeModel> aproval) {
        this.aproval = aproval;
    }

    public List<HomeWorkNoticeModel> getNotice() {
        return notice;
    }

    public void setNotice(List<HomeWorkNoticeModel> notice) {
        this.notice = notice;
    }
}
