package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/5/16.
 */

public class Application {
    
    private String code;  // 请求结果：数字 0 代表请求成功，非 0 代表请求失败
    private String msg;  // 请求结果状态说明，可以提示给用户
    private Data data;

    public class Data{
        private ArrayList<ApplicationModel> OA;
        private ArrayList<ApplicationModel> CRM;
        private ArrayList<ApplicationModel> ERP;
        private ArrayList<BanModel> top_banner;

        private ArrayList<BanModel> bottom_banner;

        public class ApplicationModel{
            private String id;  //  ID
            private String text;  //  名称
            private String icon;  //  图标
            private String url;  //  链接
            private String is_auth;

            public String getIs_auth() {
                return is_auth;
            }

            public String getId() {
                return id;
            }

            public String getText() {
                return text;
            }

            public String getIcon() {
                return icon;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "ApplicationModel{" +
                        "id='" + id + '\'' +
                        ", text='" + text + '\'' +
                        ", icon='" + icon + '\'' +
                        ", url='" + url + '\'' +
                        ", is_auth='" + is_auth + '\'' +
                        '}';
            }
        }

        public class BanModel{
            private String img;
            private String url;

            public String getImg() {
                return img;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "BanModel{" +
                        "img='" + img + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        public ArrayList<ApplicationModel> getOA() {
            return OA;
        }

        public ArrayList<ApplicationModel> getCRM() {
            return CRM;
        }

        public ArrayList<ApplicationModel> getERP() {
            return ERP;
        }

        public ArrayList<BanModel> getTop_banner() {
            return top_banner;
        }

        public ArrayList<BanModel> getBottom_banner() {
            return bottom_banner;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "OA=" + OA +
                    ", CRM=" + CRM +
                    ", ERP=" + ERP +
                    ", top_banner=" + top_banner +
                    ", bottom_banner=" + bottom_banner +
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
        return "Application{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
