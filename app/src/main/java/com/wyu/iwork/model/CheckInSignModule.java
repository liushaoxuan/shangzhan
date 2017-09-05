package com.wyu.iwork.model;

/**
 * Created by lx on 2017/4/10.
 */

public class CheckInSignModule {
    private int code;
    private String msg;
    private Data data;

    /**
     *
     */
    public class Data{
        private SignIn sign_in_msg;
        private SignIn sign_out_msg;
        private String now_time;
        private int sign_type;
        private String user_name;
        private String department;
        private long unix_time;
        private String face_img;

        public String getFace_img() {
            return face_img;
        }

        public SignIn getSign_in_msg() {
            return sign_in_msg;
        }

        public SignIn getSign_out_msg() {
            return sign_out_msg;
        }

        public String getNow_time() {
            return now_time;
        }

        public int getSign_type() {
            return sign_type;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getDepartment() {
            return department;
        }

        public long getUnix_time() {
            return unix_time;
        }

        public class SignIn{

            private String time,sign_time,sign_address,sign_status;

            public String getTime() {
                return time;
            }

            public String getSign_time() {
                return sign_time;
            }

            public String getSign_address() {
                return sign_address;
            }

            public String getSign_status() {
                return sign_status;
            }

            @Override
            public String toString() {
                return "SignIn{" +
                        "time='" + time + '\'' +
                        ", sign_time='" + sign_time + '\'' +
                        ", sign_address='" + sign_address + '\'' +
                        ", sign_status='" + sign_status + '\'' +
                        '}';
            }
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
        return "CheckInSignModule{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
