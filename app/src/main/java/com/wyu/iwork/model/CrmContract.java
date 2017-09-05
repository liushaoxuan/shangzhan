package com.wyu.iwork.model;

import java.util.ArrayList;

import static io.rong.imlib.statistics.UserData.name;

/**
 * Created by lx on 2017/6/9.
 */

public class CrmContract {

    private String code;
    private String msg;
    private ArrayList<Contract> data;

    public class Contract{
        private String id;
        private String customer_name;
        private String title;
        private String value;
        private String add_time;

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

        public String getAdd_time() {
            return add_time;
        }

        @Override
        public String toString() {
            return "Contract{" +
                    "id='" + id + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", title='" + title + '\'' +
                    ", value='" + value + '\'' +
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

    public ArrayList<Contract> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CrmContract{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
