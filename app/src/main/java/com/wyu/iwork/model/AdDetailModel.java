package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/10.10:08
 * 邮箱：2587294424@qq.com
 * 广告详情model
 */

public class AdDetailModel implements Serializable {
    private String company_name = "";//公司名 ,
    private String title = "";//广告标题 ,
    private String type = "";//广告类型（1：CPC 2：CPM） ,
    private String unit_price = "";//单价 ,
    private String ad_pic_url = "";//广告图地址 ,
    private String ad_link = "";//广告图链接

    public AdDetailModel() {
    }

    public AdDetailModel(String company_name, String title, String type, String unit_price, String ad_pic_url, String ad_link) {
        this.company_name = company_name;
        this.title = title;
        this.type = type;
        this.unit_price = unit_price;
        this.ad_pic_url = ad_pic_url;
        this.ad_link = ad_link;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(String unit_price) {
        this.unit_price = unit_price;
    }

    public String getAd_pic_url() {
        return ad_pic_url;
    }

    public void setAd_pic_url(String ad_pic_url) {
        this.ad_pic_url = ad_pic_url;
    }

    public String getAd_link() {
        return ad_link;
    }

    public void setAd_link(String ad_link) {
        this.ad_link = ad_link;
    }
}
