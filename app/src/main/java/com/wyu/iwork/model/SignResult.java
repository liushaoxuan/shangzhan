package com.wyu.iwork.model;

/**
 * Created by lx on 2017/8/2.
 */

public class SignResult {

    private String code;
    private String msg;
    private CurrentTime data;

    public class CurrentTime{
        /**
         * [data] => Array
         (
         [add_time] => 签到时间 格式：2017-12-09 02:12:12
         [sign_status] => 签到状态 0：正常签到  1：迟到  2：早退  99：非工作日签到
         [sign_address] => 签到地址
         )
         */
        private String add_time;
        private String sign_status;
        private String sign_address;

        public String getAdd_time() {
            return add_time;
        }

        public String getSign_status() {
            return sign_status;
        }

        public String getSign_address() {
            return sign_address;
        }

        @Override
        public String toString() {
            return "CurrentTime{" +
                    "add_time='" + add_time + '\'' +
                    ", sign_status='" + sign_status + '\'' +
                    ", sign_address='" + sign_address + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public CurrentTime getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SignResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
