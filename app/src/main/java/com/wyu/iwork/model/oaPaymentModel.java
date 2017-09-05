package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * 作者： sxliu on 2017/6/9.14:40
 * 邮箱：2587294424@qq.com
 * 报销的model费用model
 */

public class oaPaymentModel implements Serializable {

     
    private String cost_type = "";// 费用类型
    private String start_time = "";// 发生时间
    private String money = "";// 费用金额
    private String content = "";// 费用说明

    public oaPaymentModel() {
    }

    public oaPaymentModel(String cost_type, String start_time, String money, String content) {
        this.cost_type = cost_type;
        this.start_time = start_time;
        this.money = money;
        this.content = content;
    }

    public String getCost_type() {
        return cost_type;
    }

    public void setCost_type(String cost_type) {
        this.cost_type = cost_type;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
