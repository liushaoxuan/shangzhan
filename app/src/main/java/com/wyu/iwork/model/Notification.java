package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 *
 */
public class Notification {

    private int code;
    private String msg;

    private Data data;

    public class Data{
        private int count,page;
        private ArrayList<Notific> list;

        public class Notific{
            private int id;
            private String img,intro,time,title,url;

            public int getId() {
                return id;
            }

            public String getImg() {
                return img;
            }

            public String getIntro() {
                return intro;
            }

            public String getTime() {
                return time;
            }

            public String getTitle() {
                return title;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "Notific{" +
                        "id=" + id +
                        ", img='" + img + '\'' +
                        ", intro='" + intro + '\'' +
                        ", time='" + time + '\'' +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        public int getCount() {
            return count;
        }

        public int getPage() {
            return page;
        }

        public ArrayList<Notific> getList() {
            return list;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "count=" + count +
                    ", page=" + page +
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

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
