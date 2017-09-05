package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/6/8.16:55
 * 邮箱：2587294424@qq.com
 * 我发起的、我审批的、抄送给我的  model
 */

public class oaAprovalSendLauchModel implements Serializable {

    private String id = "";// 关联id
    private String type = "";// 类型(请假,加班,出差,报销)
    private String apply_type = "";// 申请类型
    private String is_pass = "";// 审核状态(审批中,已通过,未通过,已撤销)
    private String create_time = "";// 添加时间
    private String start_time = "";// 开始时间(报销时为空)
    private String end_time = "";// 结束时间(报销时为空)
    private String number = "";// 时长(报销时代表金额)

    public oaAprovalSendLauchModel() {
    }

    public oaAprovalSendLauchModel(String id, String type, String apply_type, String is_pass, String create_time, String start_time, String end_time, String number) {
        this.id = id;
        this.type = type;
        this.apply_type = apply_type;
        this.is_pass = is_pass;
        this.create_time = create_time;
        this.start_time = start_time;
        this.end_time = end_time;
        this.number = number;
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

    public String getApply_type() {
        return apply_type;
    }

    public void setApply_type(String apply_type) {
        this.apply_type = apply_type;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
