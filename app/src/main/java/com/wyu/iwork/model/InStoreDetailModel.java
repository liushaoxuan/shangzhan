package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/10.
 * 入库详情model
 */

public class InStoreDetailModel implements Serializable {


    /**
     *  入库ID
     */
    private String id;

    /**
     * 入库唯一编号
     */
    private String  sole_id ;

    /**
     * 商品ID
     */
    private String  goods_id ;

    /**
     * 商品名称
     */
    private String  goods_name ;

    /**
     * 仓库ID
     */
    private String  store_id ;

    /**
     * 仓库唯一编号
     */
    private String  store_sole_id ;

    /**
     * 仓库名称
     */
    private String  store_name ;

    /**
     * 货位ID
     */
    private String  store_goods_id ;

    /**
     * 货位唯一编号
     */
    private String  store_goods_sole_id ;

    /**
     * 货位名称
     */
    private String  store_goods_name ;

    /**
     * 入库数量
     */
    private String  num ;

    /**
     * 单价
     */
    private String  price ;

    /**
     * 折扣
     */
    private String  discount ;

    /**
     * 总金额
     */
    private String  amount ;

    /**
     * 创建人ID
     */
    private String  creater_id ;

    /**
     * 创建人姓名
     */
    private String  creater_name ;

    /**
     * 添加时间 格式：2017-01-12
     */
    private String  add_time ;

    /**
     * 采购ID
     */
    private String  purchase_id ;

    /**
     * 采购唯一编号
     */
    private String  purchase_sole_id ;



    public InStoreDetailModel() {
    }


    public InStoreDetailModel(String id, String sole_id, String goods_id, String goods_name, String store_id, String store_sole_id, String store_name, String store_goods_id, String store_goods_sole_id, String store_goods_name, String num, String price, String discount, String amount, String creater_id, String creater_name, String add_time, String purchase_id, String purchase_sole_id) {
        this.id = id;
        this.sole_id = sole_id;
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.store_id = store_id;
        this.store_sole_id = store_sole_id;
        this.store_name = store_name;
        this.store_goods_id = store_goods_id;
        this.store_goods_sole_id = store_goods_sole_id;
        this.store_goods_name = store_goods_name;
        this.num = num;
        this.price = price;
        this.discount = discount;
        this.amount = amount;
        this.creater_id = creater_id;
        this.creater_name = creater_name;
        this.add_time = add_time;
        this.purchase_id = purchase_id;
        this.purchase_sole_id = purchase_sole_id;
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

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStore_sole_id() {
        return store_sole_id;
    }

    public void setStore_sole_id(String store_sole_id) {
        this.store_sole_id = store_sole_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_goods_id() {
        return store_goods_id;
    }

    public void setStore_goods_id(String store_goods_id) {
        this.store_goods_id = store_goods_id;
    }

    public String getStore_goods_sole_id() {
        return store_goods_sole_id;
    }

    public void setStore_goods_sole_id(String store_goods_sole_id) {
        this.store_goods_sole_id = store_goods_sole_id;
    }

    public String getStore_goods_name() {
        return store_goods_name;
    }

    public void setStore_goods_name(String store_goods_name) {
        this.store_goods_name = store_goods_name;
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

    public String getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(String purchase_id) {
        this.purchase_id = purchase_id;
    }

    public String getPurchase_sole_id() {
        return purchase_sole_id;
    }

    public void setPurchase_sole_id(String purchase_sole_id) {
        this.purchase_sole_id = purchase_sole_id;
    }
}
