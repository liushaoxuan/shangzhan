package com.wyu.iwork.model;

import com.bigkoo.pickerview.model.IPickerViewData;

import java.io.Serializable;

/**
 * Created by lx on 2017/5/4.
 */

public class GoodsTypeModel implements Serializable,IPickerViewData{

    /**
     * 分类ID
     */
    private String id;
    /**
     * 分类名称
     */
    private String name;

    public GoodsTypeModel() {
    }

    public GoodsTypeModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        if (name==null){
            name = "";
        }
        return name;
    }
}
