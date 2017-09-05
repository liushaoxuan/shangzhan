package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/10.
 * 商品列表model
 */

public class GoodsModel implements Serializable {

    /**
     * 商品ID
     */
    private String  id ;

    /**
     * 商品唯一编号
     */
    private String  sole_id ;
    /**
     * 商品名称
     */
    private String  name ;
    /**
     * 商品价格 单位：元
     */
    private String  price ;
    /**
     * 库存数量
     */
    private String  num ;

    /**
     *  添加时间 格式：2017-01-12
     */
    private String  add_time ;


    public GoodsModel() {
    }

    public GoodsModel(String id, String sole_id, String name, String price, String num, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.name = name;
        this.price = price;
        this.num = num;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
