package com.wyu.iwork.model;

/**
 * Created by lx on 2017/7/28.
 */

public class UnixStamp {
    private String code;
    private String msg;
    private UnixTime data;

    public class UnixTime{
        private long unix_time;

        public long getUnix_time() {
            return unix_time;
        }

        @Override
        public String toString() {
            return "UnixTime{" +
                    "unix_time=" + unix_time +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public UnixTime getData() {
        return data;
    }

    @Override
    public String toString() {
        return "UnixStamp{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
