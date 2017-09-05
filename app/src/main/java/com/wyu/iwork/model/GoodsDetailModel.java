package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by  sxliu on 2017/5/10.
 * 商品详情model
 */

public class GoodsDetailModel implements Serializable {

    /**
     * 商品ID
     */
    private String   id  ;

    /**
     * 商品唯一编号
     */
    private String    sole_id ;

    /**
     * 商品名称
     */
    private String   name  ;

    /**
     * 商品分类ID
     */
    private String   type_id  ;

    /**
     * 商品分类名称
     */
    private String     type_name;

    /**
     * 商品价格 单位：元
     */
    private String     price;

    /**
     * 市场价格 单位：元
     */
    private String    market_price ;


    /**
     * 创建人ID
     */
    private String    creater_id ;


    /**
     * 创建人姓名
     */
    private String    creater_name ;


    /**
     * 添加时间 格式：2017-01-12
     */
    private String   add_time  ;


    public GoodsDetailModel() {
    }

    public GoodsDetailModel(String id, String sole_id, String name, String type_id, String type_name, String price, String market_price, String creater_id, String creater_name, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.name = name;
        this.type_id = type_id;
        this.type_name = type_name;
        this.price = price;
        this.market_price = market_price;
        this.creater_id = creater_id;
        this.creater_name = creater_name;
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

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getCreater_id() {
        return creater_id;
    }

    public void setCreater_id(String creater_id) {
        this.creater_id = creater_id;
    }

    public String getCreater_name() {
        return creater_name;
    }

    public void setCreater_name(String creater_name) {
        this.creater_name = creater_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
