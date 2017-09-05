package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/11.
 * 出库详情model
 */

public class OutStoreDetailModel implements Serializable {

    private String id;//出库ID
    private String sole_id;//出库唯一编号
    private String sale_id;//销售订单ID
    private String sale_sole_id;//销售订单唯一编号
    private String goods_id;//商品ID
    private String goods_name;//商品名称
    private String goods_num;//商品数量
    private String customer_id;//客户ID
    private String customer_name;//客户姓名
    private String customer_phone;//客户联系方式
    private String deliver;//发货方式 1：物流快递 2：上门自提 3送货上门
    private String status;//出库状态 1：正在拣货 2：完成出库
    private String creater_id;//创建人ID
    private String creater_name;//创建人姓名
    private String add_time;//添加时间 格式：2017-01-12

    public OutStoreDetailModel() {
    }

    public OutStoreDetailModel(String id, String sole_id, String sale_id, String sale_sole_id, String goods_id, String goods_name, String goods_num, String customer_id, String customer_name, String customer_phone, String deliver, String status, String creater_id, String creater_name, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.sale_id = sale_id;
        this.sale_sole_id = sale_sole_id;
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_num = goods_num;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_phone = customer_phone;
        this.deliver = deliver;
        this.status = status;
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

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public String getSale_sole_id() {
        return sale_sole_id;
    }

    public void setSale_sole_id(String sale_sole_id) {
        this.sale_sole_id = sale_sole_id;
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

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
