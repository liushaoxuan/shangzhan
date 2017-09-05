package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/10.11:50
 * 邮箱：2587294424@qq.com
 * 广告联盟 分享信息model
 */

public class AdTaskShareModel implements Serializable {

    private String title = "";//分享标题
    private String message = "";//分享内容
    private String img_url = "";//分享的图片地址
    private String url = "";//跳转的分享详情页地址"

    public AdTaskShareModel() {
    }

    public AdTaskShareModel(String title, String message, String img_url, String url) {
        this.title = title;
        this.message = message;
        this.img_url = img_url;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
