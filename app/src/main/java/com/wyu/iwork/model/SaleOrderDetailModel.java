package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/11.
 * 销售订单详情model
 */

public class SaleOrderDetailModel implements Serializable {

    /**
     * 销售订单ID
     */
    private String   id;

    /**
     * 唯一订单号
     */
    private String   sole_id;

    /**
     * 创建人ID
     */
    private String   user_id;

    /**
     * 创建人姓名
     */
    private String   user_name;

    /**
     * 客户ID
     */
    private String   customer_id;

    /**
     * 客户姓名
     */
    private String   customer_name;

    /**
     * 商品ID
     */
    private String   goods_id;

    /**
     * 商品名称
     */
    private String   goods_name;

    /**
     * 商品价格 单位：元
     */
    private String   goods_price;

    /**
     * 商品数量
     */
    private String   goods_num;

    /**
     * 采购商品折扣
     */
    private String   discount;

    /**
     * 总金额 单位：元
     */
    private String   goods_amount;

    /**
     * 订单状态  1：待审批 2：已审批
     */
    private String   status;

    /**
     * 下单日期
     */
    private String   place_time;

    /**
     * 发货方式 1：物流快递 2：上门自提 3：送货上门
     */
    private String   deliver;

    /**
     * 添加时间 格式：2017-01-12 12:12:12
     */
    private String   add_time;


    public SaleOrderDetailModel() {
    }

    public SaleOrderDetailModel(String id, String sole_id, String user_id, String user_name, String customer_id, String customer_name, String goods_id, String goods_name, String goods_price, String goods_num, String discount, String goods_amount, String status, String place_time, String deliver, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_price = goods_price;
        this.goods_num = goods_num;
        this.discount = discount;
        this.goods_amount = goods_amount;
        this.status = status;
        this.place_time = place_time;
        this.deliver = deliver;
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

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlace_time() {
        return place_time;
    }

    public void setPlace_time(String place_time) {
        this.place_time = place_time;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
