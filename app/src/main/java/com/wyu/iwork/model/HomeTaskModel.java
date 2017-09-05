package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/4/13.
 * 首页——工作——任务model
 */

public class HomeTaskModel implements Serializable {

    private String task_id = "";// 任务ID
    private String title = "";// 任务标题
    private String status = "";// 任务状态 2：待执行；3：已取消；4：已过期 100：任务完成
    private String begin_time = "";// 开始时间  格式：2001-12-12 22:11:00
    private String end_time = "";// 结束时间  格式：2001-12-12 22:11:00

    public HomeTaskModel() {
    }

    public HomeTaskModel(String task_id, String title, String status, String begin_time, String end_time) {
        this.task_id = task_id;
        this.title = title;
        this.status = status;
        this.begin_time = begin_time;
        this.end_time = end_time;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
