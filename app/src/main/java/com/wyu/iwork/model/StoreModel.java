package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/9.
 * 仓库列表model
 */

public class StoreModel implements Serializable {

    /**
     * 仓库ID
     */
    private String  id;
    /**
     * 仓库唯一编号
     */
    private String  sole_id;

    /**
     * 仓库名称
     */
    private String  name;
    /**
     *  省
     */
    private String province;
    /**
     *  市
     */
    private String city;
    /**
     *  区
     */
    private String district;
    /**
     *  具体地址
     */
    private String address;

    /**
     * 仓库管理人
     */
    private String  user_name;

    /**
     * 添加时间 格式：2017-01-12
     */
    private String  add_time;

    public StoreModel() {
    }

    public StoreModel(String id, String sole_id, String name, String province, String city, String district, String address, String user_name, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.name = name;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.user_name = user_name;
        this.add_time = add_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSole_id() {
        return sole_id;
    }

    public void setSole_id(String sole_id) {
        this.sole_id = sole_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
