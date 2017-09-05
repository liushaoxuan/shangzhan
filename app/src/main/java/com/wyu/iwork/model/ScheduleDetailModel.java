package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/18.
 */

public class ScheduleDetailModel implements Serializable {


    /**
     * id : 85
     * type : 1
     * add_time : 2017/04/18
     * title : 日程
     * text : 贴膜无聊咯么
     * begin_time : 04月18日 15:53
     * end_time : 04月18日 16:53
     */

    private String id;
    private String type;
    private String add_time;
    private String title;
    private String text;
    private String begin_time;
    private String end_time;

    public ScheduleDetailModel() {
    }

    public ScheduleDetailModel(String id, String type, String add_time, String title, String text, String begin_time, String end_time) {
        this.id = id;
        this.type = type;
        this.add_time = add_time;
        this.title = title;
        this.text = text;
        this.begin_time = begin_time;
        this.end_time = end_time;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getAdd_time() {
        return add_time;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public String getEnd_time() {
        return end_time;
    }
}
