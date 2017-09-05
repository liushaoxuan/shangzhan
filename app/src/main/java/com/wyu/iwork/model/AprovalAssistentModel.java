package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/4.13:55
 * 邮箱：2587294424@qq.com
 * 审批助手model
 */

public class AprovalAssistentModel implements Serializable {
   private String apply_id = "";// 审批ID
    private String message_id = "";//  消息ID 用于删除
   private String type = "";// 类型  1:请假 2:加班 3:出差 4:报销
   private String title = "";// 标题
   private String text1 = "";// 内容1
   private String text2 = "";// 内容2
   private String add_time = "";// 发布时间，格式如：2017-03-02 02:02

    public AprovalAssistentModel() {
    }

    public AprovalAssistentModel(String apply_id, String message_id, String type, String title, String text1, String text2, String add_time) {
        this.apply_id = apply_id;
        this.message_id = message_id;
        this.type = type;
        this.title = title;
        this.text1 = text1;
        this.text2 = text2;
        this.add_time = add_time;
    }

    public String getApply_id() {
        return apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText1() {
        return text1;
    }

    public void setText1(String text1) {
        this.text1 = text1;
    }

    public String getText2() {
        return text2;
    }

    public void setText2(String text2) {
        this.text2 = text2;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
