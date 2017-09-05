package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/5/8.
 */

public class SurpplierManagerModel implements Serializable {
    private String id;//供货商ID
    private String name;//供货商名称
    private String user_id;//创建人ID
    private String user_name;//创建人姓名
    private String type_name;//供货产品类型
    private String contacts;//联系人
    private String phone;//联系电话
    private String job;//职务
    private String province;//省
    private String city;//市
    private String district;//区
    private String address;//地址
    private String taxpayer;//纳税人识别号
    private String opening_bank;//开户行
    private String bank_number;//银行卡号
    private String add_time;//添加时间   格式：2017-01-12

    public SurpplierManagerModel() {
    }

    public SurpplierManagerModel(String id, String name, String user_id, String user_name, String type_name, String contacts, String phone, String job, String province, String city, String district, String address, String taxpayer, String opening_bank, String bank_number, String add_time) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.type_name = type_name;
        this.contacts = contacts;
        this.phone = phone;
        this.job = job;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.taxpayer = taxpayer;
        this.opening_bank = opening_bank;
        this.bank_number = bank_number;
        this.add_time = add_time;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
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

    public String getTaxpayer() {
        return taxpayer;
    }

    public void setTaxpayer(String taxpayer) {
        this.taxpayer = taxpayer;
    }

    public String getOpening_bank() {
        return opening_bank;
    }

    public void setOpening_bank(String opening_bank) {
        this.opening_bank = opening_bank;
    }

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
