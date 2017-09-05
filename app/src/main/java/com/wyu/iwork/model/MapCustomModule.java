package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/5/13.
 */

public class MapCustomModule implements Serializable{

    private String code ; // 请求结果：数字 0 代表请求成功，非 0 代表请求失败
    private String msg ; // 请求结果状态说明，可以提示给用户

    private ArrayList<Custom> data;

    public class Custom implements Serializable{
        private String id ;  // 客户ID
        private String type ;  // 0：潜在客户 1：客户
        private String name ;  // 客户姓名
        private String user_id ;  // 跟进人ID
        private String user_name ;  // 跟进人姓名
        private String source ;  // 客户来源  0：其他 1：广告 2：推广 3：搜索引擎 4：转介绍 5：固有资源 6：线上注册 7：线上咨询 8：预约上门
        private String grade ;  // 客户等级  1：一般客户 2：普通客户 3：重要客户
        private String phone ;  // 手机号
        private String province ;  // 省
        private String city ;  // 市
        private String district ;  // 区
        private String address ;  // 客户地址
        private String lng ;  // 经度
        private String lat ;  // 纬度
        private String fax ;  // 传真
        private String mail ;  // 邮箱
        private String url ;  // 网址
        private String remark ;  // 备注
        private String status ;  // 客户状态 1：初步沟通 2：意向沟通 3：方案报价 4：签订成交 5：客户停滞
        private String predict_date ;  // 预计成交日期
        private String predict_money ;  // 预计成交金额
        private String distance ;  // 用户距离当前客户距离 单位:米

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getSource() {
            return source;
        }

        public String getGrade() {
            return grade;
        }

        public String getPhone() {
            return phone;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public String getDistrict() {
            return district;
        }

        public String getAddress() {
            return address;
        }

        public String getLng() {
            return lng;
        }

        public String getLat() {
            return lat;
        }

        public String getFax() {
            return fax;
        }

        public String getMail() {
            return mail;
        }

        public String getUrl() {
            return url;
        }

        public String getRemark() {
            return remark;
        }

        public String getStatus() {
            return status;
        }

        public String getPredict_date() {
            return predict_date;
        }

        public String getPredict_money() {
            return predict_money;
        }

        public String getDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "Custom{" +
                    "id='" + id + '\'' +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", user_name='" + user_name + '\'' +
                    ", source='" + source + '\'' +
                    ", grade='" + grade + '\'' +
                    ", phone='" + phone + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    ", district='" + district + '\'' +
                    ", address='" + address + '\'' +
                    ", lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    ", fax='" + fax + '\'' +
                    ", mail='" + mail + '\'' +
                    ", url='" + url + '\'' +
                    ", remark='" + remark + '\'' +
                    ", status='" + status + '\'' +
                    ", predict_date='" + predict_date + '\'' +
                    ", predict_money='" + predict_money + '\'' +
                    ", distance='" + distance + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<Custom> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MapCustomModule{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
