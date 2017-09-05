package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/3/15.
 */

public class Organization {
    private int code;
    private String msg;
    private ArrayList<FirstOrg> data;

    public class FirstOrg{
        private String department_id;
        private String department_name;
        private ArrayList<SecondOrg> junior;

        public class SecondOrg{
            private String department_id;
            private String department_name;
            private int type;

            public int getType() {
                return type;
            }

            public String getDepartment_id() {
                return department_id;
            }

            public String getDepartment_name() {
                return department_name;
            }

            @Override
            public String toString() {
                return "SecondOrg{" +
                        "department_id='" + department_id + '\'' +
                        ", department_name='" + department_name + '\'' +
                        ", type=" + type +
                        '}';
            }
        }

        public String getDepartment_id() {
            return department_id;
        }

        public String getDepartment_name() {
            return department_name;
        }

        public ArrayList<SecondOrg> getJunior() {
            return junior;
        }

        @Override
        public String toString() {
            return "FirstOrg{" +
                    "department_id='" + department_id + '\'' +
                    ", department_name='" + department_name + '\'' +
                    ", junior=" + junior +
                    '}';
        }
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ArrayList<FirstOrg> getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
