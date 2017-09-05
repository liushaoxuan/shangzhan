package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/8/9.
 */

public class MineAdvertiseModel {

    private String code;
    private String msg;
    private AdvertiseMessage data;

    public class AdvertiseMessage{

        private ArrayList<AdvertiseTask> task_list;
        private Mall mall;

        public class AdvertiseTask{

            private String ad_id;
            private String company_name;
            private String title;
            private String unit_price;
            private String ad_pic_url;
            private String type;
            private String url;
            private String ad_receive_id;

            public String getAd_receive_id() {
                return ad_receive_id;
            }

            public String getUrl() {
                return url;
            }

            public String getAd_id() {
                return ad_id;
            }

            public String getCompany_name() {
                return company_name;
            }

            public String getTitle() {
                return title;
            }

            public String getUnit_price() {
                return unit_price;
            }

            public String getAd_pic_url() {
                return ad_pic_url;
            }

            public String getType() {
                return type;
            }

            @Override
            public String toString() {
                return "AdvertiseTask{" +
                        "ad_id='" + ad_id + '\'' +
                        ", company_name='" + company_name + '\'' +
                        ", title='" + title + '\'' +
                        ", unit_price='" + unit_price + '\'' +
                        ", ad_pic_url='" + ad_pic_url + '\'' +
                        ", type='" + type + '\'' +
                        ", url='" + url + '\'' +
                        ", ad_receive_id='" + ad_receive_id + '\'' +
                        '}';
            }
        }

        public class Mall{
            private double mail_count;
            private double all_mail_count;
            private double this_mail_count;

            public double getMail_count() {
                return mail_count;
            }

            public double getAll_mail_count() {
                return all_mail_count;
            }

            public double getThis_mail_count() {
                return this_mail_count;
            }

            @Override
            public String toString() {
                return "Mall{" +
                        "mail_count=" + mail_count +
                        ", all_mail_count=" + all_mail_count +
                        ", this_mail_count=" + this_mail_count +
                        '}';
            }
        }

        public ArrayList<AdvertiseTask> getTask_list() {
            return task_list;
        }

        public Mall getMall() {
            return mall;
        }

        @Override
        public String toString() {
            return "AdvertiseMessage{" +
                    "task_list=" + task_list +
                    ", mall=" + mall +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public AdvertiseMessage getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MineAdvertiseModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
