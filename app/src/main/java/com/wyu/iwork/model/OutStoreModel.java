package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/11.
 * 出库列表model
 */

public class OutStoreModel implements Serializable {

     private String id;// 出库ID
     private String sole_id;// 出库唯一编号
     private String goods_name;// 商品名称
     private String goods_num;// 出库商品数量
     private String add_time;// 添加时间 格式：2017-01-12

    public OutStoreModel() {
    }

    public OutStoreModel(String id, String sole_id, String goods_name, String goods_num, String add_time) {
        this.id = id;
        this.sole_id = sole_id;
        this.goods_name = goods_name;
        this.goods_num = goods_num;
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

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }
}
