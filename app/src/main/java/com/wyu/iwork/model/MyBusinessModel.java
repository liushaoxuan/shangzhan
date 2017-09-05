package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/6.
 * 我的名片
 */

public class MyBusinessModel implements Serializable {
    private String type;//分类
    private String name;//名称
    private String content;//内容

    public MyBusinessModel() {
    }

    public MyBusinessModel(String type, String name, String content) {
        this.type = type;
        this.name = name;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
