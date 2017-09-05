package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/10.
 * 外出签到激励 */

public class CheckOutSignModule {

    private int code;
    private String msg;
    private CheckOut data;

    public class CheckOut{
        private String now_time;
        private String user_name;
        private String department;
        private ArrayList<CheckOutData> list;

        public class CheckOutData{
            private String address,text,time;

            public String getAddress() {
                return address;
            }

            public String getText() {
                return text;
            }

            public String getTime() {
                return time;
            }

            @Override
            public String toString() {
                return "CheckOutData{" +
                        "address='" + address + '\'' +
                        ", text='" + text + '\'' +
                        ", time='" + time + '\'' +
                        '}';
            }
        }

        public String getUser_name() {
            return user_name;
        }

        public String getDepartment() {
            return department;
        }

        public String getNow_time() {
            return now_time;
        }

        public ArrayList<CheckOutData> getList() {
            return list;
        }

        @Override
        public String toString() {
            return "CheckOut{" +
                    "now_time='" + now_time + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", department='" + department + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public CheckOut getData() {
        return data;
    }

    @Override
    public String toString() {
        return "CheckOutSignModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
