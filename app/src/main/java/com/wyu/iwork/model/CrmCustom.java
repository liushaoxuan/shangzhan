package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/5/5.
 */

public class CrmCustom implements Serializable{
    private String code;
    private String msg;
    private ArrayList<Custom> data;

    public class Custom implements Serializable{
        private String address;
        private String fax;
        private String grade;
        private String id;
        private String mail;
        private String name;
        private String phone;
        private String predict_date;
        private String predict_money;
        private String remark;
        private String source;
        private String status;
        private String url;
        private String user_id;
        private String follow_user;
        private String province;
        private String city;
        private String district;

        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getAddress() {
            return address;
        }

        public String getFax() {
            return fax;
        }

        public String getGrade() {
            return grade;
        }

        public String getId() {
            return id;
        }

        public String getMail() {
            return mail;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setFax(String fax) {
            this.fax = fax;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setPredict_date(String predict_date) {
            this.predict_date = predict_date;
        }

        public void setPredict_money(String predict_money) {
            this.predict_money = predict_money;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public void setFollow_user(String follow_user) {
            this.follow_user = follow_user;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        @Override
        public String toString() {
            return "Custom{" +
                    "address='" + address + '\'' +
                    ", fax='" + fax + '\'' +
                    ", grade='" + grade + '\'' +
                    ", id='" + id + '\'' +
                    ", mail='" + mail + '\'' +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", predict_date='" + predict_date + '\'' +
                    ", predict_money='" + predict_money + '\'' +
                    ", remark='" + remark + '\'' +
                    ", source='" + source + '\'' +
                    ", status='" + status + '\'' +
                    ", url='" + url + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", user_name='" + follow_user + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    '}';
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getPredict_date() {
            return predict_date;
        }

        public String getPredict_money() {
            return predict_money;
        }

        public String getRemark() {
            return remark;
        }

        public String getSource() {
            return source;
        }

        public String getStatus() {
            return status;
        }

        public String getUrl() {
            return url;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getFollow_user() {
            return follow_user;
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Custom> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CrmCustom{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
