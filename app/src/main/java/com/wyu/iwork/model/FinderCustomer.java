package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/6/15.
 */

public class FinderCustomer {
    /**
     * Array
     (
     [code] => 请求结果：数字 0 代表请求成功，非 0 代表请求失败
     [msg] => 请求结果状态说明，可以提示给用户
     [data] => Array
     (
     [0] => Array
     (
     [id] => 客户ID
     [type] => 0：潜在客户 1：客户
     [name] => 客户名称 公司名称
     [phone] => 电话号码
     [add_time] => 添加时间 格式：2017-12-12 12:12:12
     )

     )

     )
     */
    private String code;
    private String msg;
    private ArrayList<Customer> data;

    public class Customer{
        private String id; //  客户ID
        private String type; //  0：潜在客户 1：客户
        private String name; //  客户名称 公司名称
        private String phone; //  电话号码
        private String add_time; //  添加时间 格式：2017-12-12 12:12:12

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getAdd_time() {
            return add_time;
        }

        @Override
        public String toString() {
            return "Customer{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
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

    public ArrayList<Customer> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "FinderCustomer{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
