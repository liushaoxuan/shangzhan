package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/11.
 * 库存详情model
 */

public class StockDetailModel implements Serializable {

    private String id ;// 库存ID
    private String goods_id ;// 商品ID
    private String goods_name ;// 商品名称
    private String goods_sole_id ;// 商品唯一编号
    private String goods_price ;// 商品价格
    private String goods_num ;// 商品数量
    private String store_id ;// 仓库ID
    private String store_sole_id ;// 仓库唯一编号
    private String store_name ;// 仓库名称
    private String store_goods_id ;// 货位ID
    private String store_goods_sole_id ;// 货位唯一编号
    private String store_goods_name ;// 货位名称
    private String creater_id ;// 创建人ID
    private String creater_name ;// 创建人姓名
    private String add_time ;// 添加时间 格式：2017-01-12

    public StockDetailModel() {
    }

    public StockDetailModel(String id, String goods_id, String goods_name, String goods_sole_id, String goods_price, String goods_num, String store_id, String store_sole_id, String store_name, String store_goods_id, String store_goods_sole_id, String store_goods_name, String creater_id, String creater_name, String add_time) {
        this.id = id;
        this.goods_id = goods_id;
        this.goods_name = goods_name;
        this.goods_sole_id = goods_sole_id;
        this.goods_price = goods_price;
        this.goods_num = goods_num;
        this.store_id = store_id;
        this.store_sole_id = store_sole_id;
        this.store_name = store_name;
        this.store_goods_id = store_goods_id;
        this.store_goods_sole_id = store_goods_sole_id;
        this.store_goods_name = store_goods_name;
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

    public String getGoods_sole_id() {
        return goods_sole_id;
    }

    public void setGoods_sole_id(String goods_sole_id) {
        this.goods_sole_id = goods_sole_id;
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
