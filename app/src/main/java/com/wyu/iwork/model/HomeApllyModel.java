package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/3.14;49
 * 邮箱：2587294424@qq.com
 * 工作首页 申请model
 */

public class HomeApllyModel implements Serializable {

    private String apply_id = "";// 审批ID
    private String type = "";// 类型 1;请假 2;加班 3;出差 4;报销
    private String apply_type = "";// 申请类型
    private String number = "";// 时长（报销时为金额）
    private String start_time = "";// 开始时间（报销时为空）
    private String end_time = "";// 结束时间（报销时为空）
    private String content = "";// 事由（报销为费用用途）
    private String user_name = "";// 申请人

    public HomeApllyModel() {
    }

    public HomeApllyModel(String apply_id, String type, String apply_type, String number, String start_time, String end_time, String content, String user_name) {
        this.apply_id = apply_id;
        this.type = type;
        this.apply_type = apply_type;
        this.number = number;
        this.start_time = start_time;
        this.end_time = end_time;
        this.content = content;
        this.user_name = user_name;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApply_type() {
        return apply_type;
    }

    public void setApply_type(String apply_type) {
        this.apply_type = apply_type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
