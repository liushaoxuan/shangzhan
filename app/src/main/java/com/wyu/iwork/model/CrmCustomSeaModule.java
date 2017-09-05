package com.wyu.iwork.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lx on 2017/5/12.
 */

public class CrmCustomSeaModule implements Serializable{
    private String code;
    private String msg;
    private Data data;

    public class Data implements Serializable{
        private String conf;
        private ArrayList<CustomSea> list;

        public class CustomSea implements Serializable{
            private String id ;   //  潜在客户ID
            private String name ;   //  潜在客户姓名
            private String follow_id ;   //  潜在客户跟进人ID
            private String follow_user ;   //  最近跟进人姓名
            private String follow_time ;   //  最近跟进时间 格式：2017-01-12
            private String follow_num ;   //  跟进次数
            private String follow_situation; //跟进情况
            private String follow_type; //跟进方式 0：其他 1：电话 2：短信 3：社交工具 4：会面
            private String phone;//联系方式


            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getFollow_id() {
                return follow_id;
            }

            public String getFollow_user() {
                return follow_user;
            }

            public String getFollow_time() {
                return follow_time;
            }

            public String getFollow_num() {
                return follow_num;
            }

            public String getFollow_situation() {
                return follow_situation;
            }

            public String getFollow_type() {
                return follow_type;
            }

            public String getPhone() {
                return phone;
            }

            @Override
            public String toString() {
                return "CustomSea{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        ", follow_id='" + follow_id + '\'' +
                        ", follow_user='" + follow_user + '\'' +
                        ", follow_time='" + follow_time + '\'' +
                        ", follow_num='" + follow_num + '\'' +
                        ", follow_situation='" + follow_situation + '\'' +
                        ", follow_type='" + follow_type + '\'' +
                        ", phone='" + phone + '\'' +
                        '}';
            }
        }

        public String getConf() {
            return conf;
        }

        public ArrayList<CustomSea> getList() {
            return list;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "conf='" + conf + '\'' +
                    ", list=" + list +
                    '}';
        }
    }

    public String getCode() {
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
        return "CrmCustomSeaModule{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
