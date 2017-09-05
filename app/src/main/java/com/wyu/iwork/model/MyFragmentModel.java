package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/4/6.
 * 我的信息model，自己用到的
 */

public class MyFragmentModel implements Serializable {
    private int imgsrc;
    private String name;
    private String content = "";

    public MyFragmentModel() {
    }

    public MyFragmentModel(int imgsrc, String name, String content) {
        this.imgsrc = imgsrc;
        this.name = name;
        this.content = content;
    }

    public int getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(int imgsrc) {
        this.imgsrc = imgsrc;
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
