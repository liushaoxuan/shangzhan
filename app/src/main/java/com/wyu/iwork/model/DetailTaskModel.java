package com.wyu.iwork.model;

import java.util.ArrayList;

/**
 * Created by lx on 2017/4/11.
 */

public class DetailTaskModel {

    private String code;
    private String msg;

    private Data data;

    public class Data{
        private String title,creater_id,creater,phone,creater_face_img,begin_time,end_time,intro,level,role,status;
        private ArrayList<PartIn> header;
        private ArrayList<PartIn> joiner;

        public String getCreater_face_img() {
            return creater_face_img;
        }

        public String getRole() {
            return role;
        }

        public String getStatus() {
            return status;
        }

        public class PartIn{
            private String face_img,id,name,phone;

            public String getFace_img() {
                return face_img;
            }

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            @Override
            public String toString() {
                return "PartIn{" +
                        "face_img='" + face_img + '\'' +
                        ", id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }

        public String getBegin_time() {
            return begin_time;
        }

        public String getCreater() {
            return creater;
        }

        public String getCreater_id() {
            return creater_id;
        }

        public String getEnd_time() {
            return end_time;
        }

        public String getIntro() {
            return intro;
        }

        public String getLevel() {
            return level;
        }

        public String getTitle() {
            return title;
        }

        public ArrayList<PartIn> getHeader() {
            return header;
        }

        public ArrayList<PartIn> getJoiner() {
            return joiner;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "begin_time='" + begin_time + '\'' +
                    ", creater='" + creater + '\'' +
                    ", creater_id='" + creater_id + '\'' +
                    ", end_time='" + end_time + '\'' +
                    ", intro='" + intro + '\'' +
                    ", level='" + level + '\'' +
                    ", title='" + title + '\'' +
                    ", header=" + header +
                    ", joiner=" + joiner +
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
        return "DetailTaskModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
