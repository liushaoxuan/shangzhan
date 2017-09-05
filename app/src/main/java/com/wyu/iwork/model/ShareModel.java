package com.wyu.iwork.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/13.
 */

public class ShareModel extends DataSupport implements Serializable {
    private String title;//分享使用标题
    private String intro;//分享使用描述
    private String icon;//分享使用图标
    private String url;//分享使用跳转链接

    public ShareModel() {
    }

    public ShareModel(String title, String intro, String icon, String url) {
        this.title = title;
        this.intro = intro;
        this.icon = icon;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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
}
