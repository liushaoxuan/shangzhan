package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lx on 2017/1/18.
 * 常用应用
 */

public class ApplicatCommonModel implements Serializable{
    /**
     * 常用应用id
     */
    private String id;

    /**
     * 常用应用名称
     */
    private String text;

    /**
     * 常用应用图标
     */
    private String icon;

    /**
     * 常用应用跳转地址
     */
    private String url;

    /**
     * 常用应用子列表
     */
    private List<ApplicationItemModel> list;

    public ApplicatCommonModel() {
    }

    public ApplicatCommonModel(String id, String text, String icon, String url, List<ApplicationItemModel> list) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.url = url;
        this.list = list;
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

    public List<ApplicationItemModel> getList() {
        return list;
    }

    public void setList(List<ApplicationItemModel> list) {
        this.list = list;
    }
}
