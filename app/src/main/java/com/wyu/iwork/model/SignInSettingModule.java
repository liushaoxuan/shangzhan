package com.wyu.iwork.model;

/**
 * Created by lx on 2017/4/17.
 */

public class SignInSettingModule {

    private int code;
    private String msg;
    private Data data;

    public class Data{
        private String address,sign_time,signout_time;

        public String getAddress() {
            return address;
        }

        public String getSign_time() {
            return sign_time;
        }

        public String getSignout_time() {
            return signout_time;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "address='" + address + '\'' +
                    ", sign_time='" + sign_time + '\'' +
                    ", signout_time='" + signout_time + '\'' +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "SignInSettingModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
