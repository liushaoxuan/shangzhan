package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/7/31.13:43
 * 邮箱：2587294424@qq.com
 * 会议通知 model
 */

public class MettingAssistenModel implements Serializable {

    private String meeting_id = "";// 会议ID
    private String message_id = "";//  消息ID 用于删除
    private String title = "";// 标题
    private String content = "";// 通知内容
    private String is_read = "";// 已读未读  0：未读 1：已读
    private String add_time = "";// 会议发布时间，格式如：2017-03-02 02:02

    public MettingAssistenModel() {
    }

    public MettingAssistenModel(String meeting_id, String message_id, String title, String content, String is_read, String add_time) {
        this.meeting_id = meeting_id;
        this.message_id = message_id;
        this.title = title;
        this.content = content;
        this.is_read = is_read;
        this.add_time = add_time;
    }

    public String getMeeting_id() {
        return meeting_id;
    }

    public void setMeeting_id(String meeting_id) {
        this.meeting_id = meeting_id;
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

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
