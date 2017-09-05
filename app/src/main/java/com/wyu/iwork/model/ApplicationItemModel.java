package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/1/18.
 * 单个应用的bean
 */

public class ApplicationItemModel implements Serializable{

    /**
     * 应用id
     */
    private String id;

    /**
     * 应用名称
     */
    private String text;

    /**
     * 应用图标
     */
    private String icon;

    /**
     * 应用跳转地址
     */
    private String url;

    public ApplicationItemModel() {
    }

    public ApplicationItemModel(String id, String text, String icon, String url) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ApplicationItemModel{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
