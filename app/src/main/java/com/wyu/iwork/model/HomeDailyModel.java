package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/3.14:52
 * 邮箱：2587294424@qq.com
 * 工作 工作日报model
 */

public class HomeDailyModel implements Serializable {

    private String daily_id = "" ;// 日报ID
    private String finish_work = "" ;// 今日完成工作
    private String user_name = "" ;// 发布人
    private String add_time = "" ;// 发表时间

    public HomeDailyModel() {
    }


    public HomeDailyModel(String daily_id, String finish_work, String user_name, String add_time) {
        this.daily_id = daily_id;
        this.finish_work = finish_work;
        this.user_name = user_name;
        this.add_time = add_time;
    }


    public String getDaily_id() {
        return daily_id;
    }

    public void setDaily_id(String daily_id) {
        this.daily_id = daily_id;
    }

    public String getFinish_work() {
        return finish_work;
    }

    public void setFinish_work(String finish_work) {
        this.finish_work = finish_work;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
