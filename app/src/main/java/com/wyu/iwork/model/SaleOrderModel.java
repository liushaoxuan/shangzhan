package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/11.
 * 销售订单列表model
 */

public class SaleOrderModel implements Serializable {
    private String id;// 销售订单ID
    private String sole_id;// 唯一订单号
    private String goods_id;// 销售商品名称
    private String goods_name;// 销售商品名称
    private String goods_num;// 销售商品数量
    private String goods_amount;// 商品总金额 单位：元
    private String customer_id;// 客户ID
    private String customer_name;// 客户姓名
    private String customer_phone;// 客户联系方式
    private String status;// 状态 1：待审批 2：已审批
    private String  deliver ;//   发货方式 1：物流快递 2：上门自提 3送货上门
    private String add_time;// 添加时间 格式：2017-01-12

    public SaleOrderModel() {
    }

    public SaleOrderModel(String id, String sole_id, String goods_id, String goods_name, String goods_num, String goods_amount, String customer_id, String customer_name, String customer_phone, String status, String deliver, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_num = goods_num;
        this.goods_amount = goods_amount;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.status = status;
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

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_amount() {
        return goods_amount;
    }

    public void setGoods_amount(String goods_amount) {
        this.goods_amount = goods_amount;
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

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
