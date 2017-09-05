package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * @author juxinhua
 *
 */

public class CrmOpportunity {

    private String code;
    private String msg;
    private ArrayList<Opportunity> data;

    public class Opportunity{

        private String id; // 商机ID
        private String title; // 商机名称
        private String customer_name; // 客户名称
        private String value; // 预计成交金额 单位：元
        private String status; // 阶段 1：验证客户 2：需求确定 3：方案/报价 4：谈判审核 5：赢单 6：输单 7：无效
        private String add_time; // 添加时间 格式：2017-01-12

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getValue() {
            return value;
        }

        public String getStatus() {
            return status;
        }

        public String getAdd_time() {
            return add_time;
        }

        @Override
        public String toString() {
            return "Opportunity{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", value='" + value + '\'' +
                    ", status='" + status + '\'' +
                    ", add_time='" + add_time + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Opportunity> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CrmOpportunity{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
