package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/5/9.
 * sxliu
 * 采购订单详情model
 */

public class PurchaseDetailModel implements Serializable {
    /**
     * 采购订单ID
     */
    private String   id;

    /**
     *  唯一订单号
     */
    private String  sole_id ;
    /**
     *  创建人ID
     */
    private String  user_id ;
    /**
     *  创建人姓名
     */
    private String  user_name ;
    /**
     *  采购类型 0：其他 1：销售商品 2：日常用品
     */
    private String  type ;
    /**
     *  商品id
     */
    private String  goods_id ;
    /**
     *  商品名称
     */
    private String  goods_name ;
    /**
     *  商品价格 单位：元
     */
    private String  goods_price ;

    /**
     *  商品数量
     */
    private String  goods_num ;

    /**
     *  采购商品折扣
     */
    private String  discount ;

    /**
     *  总金额 单位：元
     */
    private String  goods_amount ;

    /**
     *  供货商id
     */
    private String  supplier_id ;

    /**
     *  供货商名称
     */
    private String  supplier_name ;

    /**
     *  供货商联系人
     */
    private String  supplier_contacst ;

    /**
     *  供货商联系电话
     */
    private String  supplier_phone ;

    /**
     *  运货方式
     */
    private String  freight ;

    /**
     *  预计到货时间 格式：2017-02-21
     */
    private String  arrive_time ;

    /**
     *   添加时间 格式：2017-01-12 12:12:12
     */
    private String  add_time ;

    public PurchaseDetailModel() {
    }

    public PurchaseDetailModel(String id, String sole_id, String user_id, String user_name, String type, String goods_id, String goods_name, String goods_price, String goods_num, String discount, String goods_amount, String supplier_id, String supplier_name, String supplier_contacst, String supplier_phone, String freight, String arrive_time, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.type = type;
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_price = goods_price;
        this.goods_num = goods_num;
        this.discount = discount;
        this.goods_amount = goods_amount;
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.supplier_contacst = supplier_contacst;
        this.supplier_phone = supplier_phone;
        this.freight = freight;
        this.arrive_time = arrive_time;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
    }

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getSupplier_contacst() {
        return supplier_contacst;
    }

    public void setSupplier_contacst(String supplier_contacst) {
        this.supplier_contacst = supplier_contacst;
    }

    public String getSupplier_phone() {
        return supplier_phone;
    }

    public void setSupplier_phone(String supplier_phone) {
        this.supplier_phone = supplier_phone;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
