package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/8/9.
 */

public class AdvertiseModel implements Serializable{

    private String code;
    private String msg;

    private ArrayList<Advertise> data;

    public class Advertise implements Serializable{
        private String id;//广告ID",
        private String company_name;//公司名",
        private String title;//广告标题",
        private String type;//广告类型（1：CPC 2：CPM）",
        private String unit_price;//单价",
        private String ad_pic_url;//广告图地址"
        private String ad_id;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAd_id() {
            return ad_id;
        }

        public String getId() {
            return id;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }

        public String getUnit_price() {
            return unit_price;
        }

        public String getAd_pic_url() {
            return ad_pic_url;
        }

        @Override
        public String toString() {
            return "Advertise{" +
                    "id='" + id + '\'' +
                    ", company_name='" + company_name + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", unit_price='" + unit_price + '\'' +
                    ", ad_pic_url='" + ad_pic_url + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Advertise> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "AdvertiseModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
