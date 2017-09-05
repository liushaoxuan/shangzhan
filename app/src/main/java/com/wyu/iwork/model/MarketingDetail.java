package com.wyu.iwork.model;

/**
 * Created by lx on 2017/6/14.
 */

public class MarketingDetail {

    private String code;
    private String msg;
    private MarketMessage data;

    public class MarketMessage{

        private String id ; //  活动ID
        private String title ; //  活动名称
        private String start_time ; //  开始时间
        private String end_time ; //  结束时间
        private String type ; //  活动类型 1:其他 2:促销活动 3:品牌活动 4:会议销售 5:搜索引擎 6:广告 7:营销
        private String address ; //  活动地点
        private String plan ; //  活动计划
        private String predict_cost ; //  预计成本
        private String cost ; //  实际成本
        private String predict_income ; //  预计收入
        private String income ; //  实际收入
        private String effect ; //  活动效果
        private String follow_user ; //  跟进人
        private String remark ; //  备注
        private String add_time ; //  添加时间 格式：2017-01-12 12:12:12

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getStart_time() {
            return start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getType() {
            return type;
        }

        public String getAddress() {
            return address;
        }

        public String getPlan() {
            return plan;
        }

        public String getPredict_cost() {
            return predict_cost;
        }

        public String getCost() {
            return cost;
        }

        public String getPredict_income() {
            return predict_income;
        }

        public String getIncome() {
            return income;
        }

        public String getEffect() {
            return effect;
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

        @Override
        public String toString() {
            return "MarketMessage{" +
                    "id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", start_time='" + start_time + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", type='" + type + '\'' +
                    ", address='" + address + '\'' +
                    ", plan='" + plan + '\'' +
                    ", predict_cost='" + predict_cost + '\'' +
                    ", cost='" + cost + '\'' +
                    ", predict_income='" + predict_income + '\'' +
                    ", income='" + income + '\'' +
                    ", effect='" + effect + '\'' +
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

    public MarketMessage getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MarketingDetail{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
