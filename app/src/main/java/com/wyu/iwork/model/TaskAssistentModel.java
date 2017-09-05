package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/2.14:23
 * 邮箱：2587294424@qq.com
 * 任务助手model
 */

public class TaskAssistentModel implements Serializable {

    private String task_id = "";// 任务ID
    private String message_id = "";// 消息ID 用于删除
    private String title = "";// 标题
    private String content = "";// 简要内容
    private String add_time = "";// 任务的发布时间，格式如：2017-03-02 02:02
    private String url = "";// 任务详情显示地址

    public TaskAssistentModel() {
    }

    public TaskAssistentModel(String task_id, String message_id, String title, String content, String add_time, String url) {
        this.task_id = task_id;
        this.message_id = message_id;
        this.title = title;
        this.content = content;
        this.add_time = add_time;
        this.url = url;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
