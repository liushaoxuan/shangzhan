package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by sxliu on 2017/5/11.
 */

public class StockModel implements Serializable {

    /**
     *  库存ID
     */
    private String  id ;

    /**
     *  商品名称
     */
    private String  goods_name ;

    /**
     *  库存数量
     */
    private String  num ;

    /**
     *  添加时间 格式：2017-01-12
     */
    private String  add_time ;


    public StockModel() {
    }

    public StockModel(String id, String goods_name, String num, String add_time) {
        this.id = id;
        this.goods_name = goods_name;
        this.num = num;
        this.add_time = add_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
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
