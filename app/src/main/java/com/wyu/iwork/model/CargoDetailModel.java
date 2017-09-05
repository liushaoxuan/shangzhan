package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/9.
 * 货位详情model
 */

public class CargoDetailModel implements Serializable {
    /**
     *
     */

    /**
     *  货位ID
     */
    private String id;

    /**
     *  货位唯一编号
     */
    private String sole_id;

    /**
     *  货位名称
     */
    private String name;
    /**
     *  仓库ID
     */
    private String store_id;
    /**
     *  仓库名称
     */
    private String store_name;

    /**
     *  仓库管理人
     */
    private String manager;
    /**
     *  添加时间 格式：2017-01-12
     */
    private String add_time;

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
     *  创建人ID
     */
    private String creater_id;

    /**
     *  仓库唯一编号
     */
    private String store_sole_id ;
    /**
     *  创建人姓名
     */
    private String creater_name ;


    public CargoDetailModel() {
    }


    public CargoDetailModel(String id, String sole_id, String name, String store_id, String store_name, String manager, String add_time, String province, String city, String district, String address, String creater_id, String store_sole_id, String creater_name) {
        this.id = id;
        this.sole_id = sole_id;
        this.name = name;
        this.store_id = store_id;
        this.store_name = store_name;
        this.manager = manager;
        this.add_time = add_time;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;
        this.creater_id = creater_id;
        this.store_sole_id = store_sole_id;
        this.creater_name = creater_name;
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

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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

    public String getCreater_id() {
        return creater_id;
    }

    public void setCreater_id(String creater_id) {
        this.creater_id = creater_id;
    }

    public String getStore_sole_id() {
        return store_sole_id;
    }

    public void setStore_sole_id(String store_sole_id) {
        this.store_sole_id = store_sole_id;
    }

    public String getCreater_name() {
        return creater_name;
    }

    public void setCreater_name(String creater_name) {
        this.creater_name = creater_name;
    }
}
