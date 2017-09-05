package com.wyu.iwork.model;

/**
 * Created by lx on 2017/6/9.
 */

public class ContractDetail {

    private String code;
    private String msg;
    private Contract data;

    public class Contract{
        private String id; // 合同ID
        private String customer_name; // 客户名称
        private String customer_id;//客户ID
        private String title; // 合同标题
        private String value; // 合同金额 单位：元
        private String sole_number; // 合同编号
        private String start_time; // 开始时间 格式：2017-01-12
        private String end_time; // 结束时间 格式：2017-01-12
        private String follow_user; // 跟进人
        private String remark; // 备注
        private String add_time; // 添加时间 格式：2017-01-12 10:10:10

        public String getId() {
            return id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }

        public String getSole_number() {
            return sole_number;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getFollow_user() {
            return follow_user;
        }

        public String getRemark() {
            return remark;
        }

        public String getAdd_time() {
            return add_time;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        @Override
        public String toString() {
            return "Contract{" +
                    "id='" + id + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", title='" + title + '\'' +
                    ", value='" + value + '\'' +
                    ", sole_number='" + sole_number + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", follow_user='" + follow_user + '\'' +
                    ", remark='" + remark + '\'' +
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

    public Contract getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ContractDetail{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
