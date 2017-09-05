package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/3/30.
 */

public class BusinessCardInfo implements Serializable{
    /**
     * Array
     (
     [code] => 请求结果：数字 0 代表请求成功，非 0 代表请求失败
     [msg] => 请求结果状态说明，可以提示给用户
     [data] => Array
     (
     [user_name] => 姓名
     [phone] => 手机号
     [email] => 邮箱
     [company_name] => 公司名称
     [address] => 地址
     [card_img] => 名片绝对地址
     )
     )
     */
    private String code;
    private String msg;
    private Info data;

    public class Info implements Serializable{

        private String user_name;
        private String phone;
        private String email;
        private String company_name;
        private String address;
        private String card_img;
        private String wechat;
        private String position;

        public String getUser_name() {
            return user_name;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getCompany_name() {
            return company_name;
        }

        public String getAddress() {
            return address;
        }

        public String getCard_img() {
            return card_img;
        }

        public String getWechat() {
            return wechat;
        }

        public String getPosition() {
            return position;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "user_name='" + user_name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", company_name='" + company_name + '\'' +
                    ", address='" + address + '\'' +
                    ", card_img='" + card_img + '\'' +
                    ", wechat='" + wechat + '\'' +
                    ", position='" + position + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Info getData() {
        return data;
    }

    @Override
    public String toString() {
        return "BusinessCardInfo{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
