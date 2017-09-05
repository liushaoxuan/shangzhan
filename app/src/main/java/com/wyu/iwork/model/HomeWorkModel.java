package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/4/13.
 * 首页——工作
 */

public class HomeWorkModel implements Serializable {
    /**
     * 日程集合
     */
    private List<HomeWorkSchedule>  schedule;

    /**
     * 任务集合
     */
    private List<HomeTaskModel>  task;

    /**
     * 公告集合
     */
    private List<HomeWorkNoticeModel>  notcie;

    public HomeWorkModel() {
    }

    public HomeWorkModel(List<HomeWorkSchedule> schedule, List<HomeTaskModel> task, List<HomeWorkNoticeModel> notcie) {
        this.schedule = schedule;
        this.task = task;
        this.notcie = notcie;
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

    public List<HomeWorkNoticeModel> getNotcie() {
        return notcie;
    }

    public void setNotcie(List<HomeWorkNoticeModel> notcie) {
        this.notcie = notcie;
    }
}
