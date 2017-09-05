package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/5/9.
 * sxliu
 * 采购订单列表model
 */

public class PurchaseModel implements Serializable {
    /**
     * 订单ID
     */
    private String   id;

    /**
     *  唯一订单号
     */
    private String  sole_id ;

    /**
     * 采购商品名称
     */
    private String   name;

    /**
     * 采购商品分类 分类类型  0：其他 1：销售商品 2：日常用品
     */
    private String   type;

    /**
     * 负责人
     */
    private String  user_name ;

    /**
     *  添加时间 格式：2017-01-12
     */
    private String   add_time;

    /**
     *  仓库唯一编号
     */
    private String   store_sole_id;

    /**
     *  货位唯一编号
     */
    private String   store_goods_sole_id;
    /**
     *  入库数量
     */
    private String   num;
    /**
     *  商品金额
     */
    private String   price;
    /**
     *  折扣
     */
    private String   discount;
    /**
     *  总金额
     */
    private String   amount;


    public PurchaseModel() {
    }

    public PurchaseModel(String id, String sole_id, String name, String type, String user_name, String add_time, String store_sole_id, String store_goods_sole_id, String num, String price, String discount, String amount) {
        this.id = id;
        this.sole_id = sole_id;
        this.name = name;
        this.type = type;
        this.user_name = user_name;
        this.add_time = add_time;
        this.store_sole_id = store_sole_id;
        this.store_goods_sole_id = store_goods_sole_id;
        this.num = num;
        this.price = price;
        this.discount = discount;
        this.amount = amount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getStore_sole_id() {
        return store_sole_id;
    }

    public void setStore_sole_id(String store_sole_id) {
        this.store_sole_id = store_sole_id;
    }

    public String getStore_goods_sole_id() {
        return store_goods_sole_id;
    }

    public void setStore_goods_sole_id(String store_goods_sole_id) {
        this.store_goods_sole_id = store_goods_sole_id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
