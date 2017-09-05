package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/5/23.16:27
 * 邮箱：2587294424@qq.com
 * 消息公告model
 */

public class MessageGonggaoModel implements Serializable {

    private String face_img;// 通告发布者的头像
    private String notice_id;// 通告ID
    private String title;// 通告的标题
    private String content;// 通告的简要内容
    private String is_read;// 已读未读  0：未读 1：已读
    private String add_time;// 通告的发布时间，格式如：2017/03/02
    private String url;// 通告详情显示地址

    public MessageGonggaoModel() {
    }

    public MessageGonggaoModel(String face_img, String notice_id, String title, String content, String is_read, String add_time, String url) {
        this.face_img = face_img;
        this.notice_id = notice_id;
        this.title = title;
        this.content = content;
        this.is_read = is_read;
        this.add_time = add_time;
        this.url = url;
    }

    public String getFace_img() {
        return face_img;
    }

    public void setFace_img(String face_img) {
        this.face_img = face_img;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
