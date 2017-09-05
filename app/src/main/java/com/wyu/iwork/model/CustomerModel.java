package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/12.
 * 客户model
 */

public class CustomerModel implements Serializable {

    private String id;// 客户ID
    private String name;// 客户姓名
    private String source;// 客户来源  0：其他 1：广告 2：推广 3：搜索引擎 4：转介绍 5：固有资源 6：线上注册 7：线上咨询 8：预约上门
    private String grade;// 客户等级  1：一般客户 2：普通客户 3：重要客户
    private String phone;// 手机号
    private String province;// 省
    private String city;// 市
    private String district;// 区
    private String address;// 客户地址
    private String fax;// 传真
    private String mail;// 邮箱
    private String url;// 网址
    private String follow_user;// 跟进人
    private String remark;// 备注
    private String status;// 潜在客户时存在 客户状态 1：初步沟通 2：意向沟通 3：方案报价 4：签订成交 5：客户停滞
    private String predict_date;// 潜在客户时存在 预计成交日期
    private String predict_money;// 潜在客户时存在 预计成交金额

    public CustomerModel() {
    }

    public CustomerModel(String id, String name, String source, String grade, String phone, String province, String city, String district, String address, String fax, String mail, String url, String follow_user, String remark, String status, String predict_date, String predict_money) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.grade = grade;
        this.phone = phone;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.fax = fax;
        this.mail = mail;
        this.url = url;
        this.follow_user = follow_user;
        this.remark = remark;
        this.status = status;
        this.predict_date = predict_date;
        this.predict_money = predict_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFollow_user() {
        return follow_user;
    }

    public void setFollow_user(String follow_user) {
        this.follow_user = follow_user;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPredict_date() {
        return predict_date;
    }

    public void setPredict_date(String predict_date) {
        this.predict_date = predict_date;
    }

    public String getPredict_money() {
        return predict_money;
    }

    public void setPredict_money(String predict_money) {
        this.predict_money = predict_money;
    }
}
