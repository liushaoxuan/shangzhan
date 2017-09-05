package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/9.
 * 货位列表model
 */

public class CargoModel implements Serializable {
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
     *  添加时间 格式：2017-01-12
     */
    private String add_time;


    public CargoModel() {
    }

    public CargoModel(String id, String sole_id, String name, String store_id, String store_name, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.name = name;
        this.store_id = store_id;
        this.store_name = store_name;
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

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
