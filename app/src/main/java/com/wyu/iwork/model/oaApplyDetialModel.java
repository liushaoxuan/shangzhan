package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者： sxliu on 2017/6/9.14:45
 * 邮箱：2587294424@qq.com
 * OA——请假、加班、报销、出差 详情的model
 */

public class oaApplyDetialModel implements Serializable {
    private String contact_id = "";// 关联id
    private String user_id = "";// 用户id
    private String face_img = "";// 用户头像
    private String user_name = "";// 用户名称
    private String user_job = "";// 职位
    private String type = "";// 类型(请假,加班,出差,报销)
    private String is_pass = "";// 审核状态(审批中,已通过,未通过,已撤销)
    private String start_time = "";// 开始时间(不为报销时返回)
    private String end_time = "";// 结束时间(不为报销时返回)
    private String apply_type = "";// 申请类型
    private String number = "";// 时长(报销时代表金额)
    private String content = "";// 事由
    private List<oaAprovalModel> approval_list = new ArrayList<>();// Array
    private String copy_list = "";// 抄送人名称用逗号隔开
    private String way = "";// way字段仅为加班时返回
    private String user_id_list = "";// 已经审批人的id集合，以逗号分隔
    private List<oaPaymentModel> payment = new ArrayList<>();// Array

    public oaApplyDetialModel() {
    }

    public oaApplyDetialModel(String contact_id, String user_id, String face_img,String user_name, String user_job, String type, String is_pass, String start_time, String end_time, String apply_type, String number, String content, List<oaAprovalModel> approval_list, String copy_list, String way, String user_id_list, List<oaPaymentModel> payment) {
        this.contact_id = contact_id;
        this.user_id = user_id;
        this.face_img = face_img;
        this.user_name = user_name;
        this.user_job = user_job;
        this.type = type;
        this.is_pass = is_pass;
        this.start_time = start_time;
        this.end_time = end_time;
        this.apply_type = apply_type;
        this.number = number;
        this.content = content;
        this.approval_list = approval_list;
        this.copy_list = copy_list;
        this.way = way;
        this.user_id_list = user_id_list;
        this.payment = payment;
    }

    public String getUser_job() {
        return user_job;
    }

    public void setUser_job(String user_job) {
        this.user_job = user_job;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_pass() {
        return is_pass;
    }

    public void setIs_pass(String is_pass) {
        this.is_pass = is_pass;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<oaAprovalModel> getApproval_list() {
        return approval_list;
    }

    public void setApproval_list(List<oaAprovalModel> approval_list) {
        this.approval_list = approval_list;
    }

    public String getCopy_list() {
        return copy_list;
    }

    public void setCopy_list(String copy_list) {
        this.copy_list = copy_list;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getUser_id_list() {
        return user_id_list;
    }

    public void setUser_id_list(String user_id_list) {
        this.user_id_list = user_id_list;
    }

    public List<oaPaymentModel> getPayment() {
        return payment;
    }

    public void setPayment(List<oaPaymentModel> payment) {
        this.payment = payment;
    }
}
