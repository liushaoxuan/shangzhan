package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/7/31.
 */

public class SignConf {

    private String code;
    private String msg;
    private SignConfModule data;

    public class SignConfModule{
        private String sign_time; //  上班签到时间,格式：09:00:00
        private String signout_time; //  下班签到时间,格式：09:00:00
        private String work_day; //  工作日,json string格式：[1,2,3,4,5]
        private ArrayList<AddressConf> address_list;

        public String getSign_time() {
            return sign_time;
        }

        public String getSignout_time() {
            return signout_time;
        }

        public String getWork_day() {
            return work_day;
        }

        public ArrayList<AddressConf> getAddress_list() {
            return address_list;
        }

        @Override
        public String toString() {
            return "SignConfModule{" +
                    "sign_time='" + sign_time + '\'' +
                    ", signout_time='" + signout_time + '\'' +
                    ", work_day='" + work_day + '\'' +
                    ", address_list=" + address_list +
                    '}';
        }
    }

    public class AddressConf{
        private String id; // 公司地址ID
        private String lng; // 经度，格式：112.123456
        private String lat; // 维度，格式：112.123456
        private String building; // 建筑物名称
        private String address; // 公司地址

        public String getId() {
            return id;
        }

        public String getLng() {
            return lng;
        }

        public String getLat() {
            return lat;
        }

        public String getBuilding() {
            return building;
        }

        public String getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return "AddressConf{" +
                    "id='" + id + '\'' +
                    ", lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    ", building='" + building + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SignConfModule getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SignConf{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
