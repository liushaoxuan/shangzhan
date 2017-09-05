package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/5/9.
 */

public class CrmFollowCustom implements Serializable{

    private String code;
    private String msg;
    private ArrayList<FollowCustom> data;

    public class FollowCustom implements Serializable{
        private String add_time;
        private String address;
        private String customer_id;
        private String customer_name;
        private String demand;
        private String follow_time;
        private String follow_user;
        private String id;
        private String remark;
        private String situation;
        private String type;
        private String user_id;

        public String getAdd_time() {
            return add_time;
        }

        public String getAddress() {
            return address;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public String getDemand() {
            return demand;
        }

        public String getFollow_time() {
            return follow_time;
        }

        public String getFollow_user() {
            return follow_user;
        }

        public String getId() {
            return id;
        }

        public String getRemark() {
            return remark;
        }

        public String getSituation() {
            return situation;
        }

        public String getType() {
            return type;
        }

        public String getUser_id() {
            return user_id;
        }

        @Override
        public String toString() {
            return "FollowCustom{" +
                    "add_time='" + add_time + '\'' +
                    ", address='" + address + '\'' +
                    ", customer_id='" + customer_id + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", demand='" + demand + '\'' +
                    ", follow_time='" + follow_time + '\'' +
                    ", follow_user='" + follow_user + '\'' +
                    ", id='" + id + '\'' +
                    ", remark='" + remark + '\'' +
                    ", situation='" + situation + '\'' +
                    ", type='" + type + '\'' +
                    ", user_id='" + user_id + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<FollowCustom> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CrmFollowCustom{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
