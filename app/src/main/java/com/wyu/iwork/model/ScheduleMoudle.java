package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/1/9.
 */

public class ScheduleMoudle implements Serializable{
    private String id;
    private String type;
    private String begin_time;
    private String end_time;
    private String text;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ScheduleMoudle{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
