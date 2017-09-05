package com.wyu.iwork.model;

/**
 * Created by lx on 2017/6/12.
 */

public class OpportunityDetail {

    private String code;
    private String msg;
    private Opportunity data;

    public class Opportunity{
        private String id; // 商机ID
        private String title; // 商机名称
        private String customer_id;//客户ID
        private String customer_name; // 客户名称
        private String predict_date; // 预计成交日期
        private String value; // 商机金额
        private String follow_user; // 跟进人
        private String remark; // 地址
        private String status; // 当前阶段 1：验证客户 2：需求确定 3：方案/报价 4：谈判审核 5：赢单 6：输单 7：无效
        private String add_time; // 添加时间 格式：2017-01-12

        public String getAdd_time() {
            return add_time;
        }

        public String getId() {
            return id;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public String getTitle() {
            return title;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getPredict_date() {
            return predict_date;
        }

        public String getValue() {
            return value;
        }

        public String getFollow_user() {
            return follow_user;
        }

        public String getRemark() {
            return remark;
        }

        public String getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return "Opportunity{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", predict_date='" + predict_date + '\'' +
                    ", value='" + value + '\'' +
                    ", follow_user='" + follow_user + '\'' +
                    ", remark='" + remark + '\'' +
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

    public Opportunity getData() {
        return data;
    }

    @Override
    public String toString() {
        return "OpportunityDetail{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
