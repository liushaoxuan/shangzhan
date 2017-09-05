package com.wyu.iwork.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/7/12.10:10
 * 邮箱：2587294424@qq.com
 * 消息 助手的model
 */

public class AssistantModel extends DataSupport implements Serializable {

    /**
     * id
     * 1  任务助手
     * 2  日程通知
     * 3  公告助手
     * 4  会议通知
     * 5  审批助手
     */
    private int id;
    /**
     * 图标
     */
    private int icon;

    /**
     * 助手名称
     */
    private String name;

    /**
     * 最新一条内容
     */
    private String content;

    /**
     * 时间
     */
    private long time;

    /**
     * 未读消息数量
     */
    private int notes;

    public AssistantModel() {
    }

    public AssistantModel(int id, int icon, String name, String content, long time, int notes) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.content = content;
        this.time = time;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getNotes() {
        return notes;
    }

    public void setNotes(int notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AssistantModel{" +
                "id=" + id +
                ", icon=" + icon +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", notes=" + notes +
                '}';
    }
}
