package com.wyu.iwork.model;

import android.view.View;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/8/8.14:41
 * 邮箱：2587294424@qq.com
 * 广告任务设置 model
 */

public class AdTaskModel implements Serializable {


    private String  msg;// 文字内容或图片地址
    private String  local_path = "";//  本地图片地址
    private int type;// 1、添加文字  2、添加图片


    public AdTaskModel(String msg, int type) {
        this.msg = msg;
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLocal_path() {
        return local_path;
    }

    public void setLocal_path(String local_path) {
        this.local_path = local_path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
