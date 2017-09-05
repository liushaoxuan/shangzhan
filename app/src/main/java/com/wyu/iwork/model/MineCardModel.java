package com.wyu.iwork.model;

import java.io.Serializable;

/**
 * Created by lx on 2017/3/29.
 * 我的-名片
 */

public class MineCardModel implements Serializable{

    private String code,msg;
    private MineCard data;

    public class MineCard implements Serializable{
        private String address,card_img,company,department,email,id,job,name,phone,user_id,wechat_id,face_img;
        private Share share;

        public class Share implements Serializable{
            private String icon,intro,title,url;

            public String getIcon() {
                return icon;
            }

            public String getIntro() {
                return intro;
            }

            public String getTitle() {
                return title;
            }

            public String getUrl() {
                return url;
            }

            @Override
            public String toString() {
                return "Share{" +
                        "icon='" + icon + '\'' +
                        ", intro='" + intro + '\'' +
                        ", title='" + title + '\'' +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

        public Share getShare() {
            return share;
        }

        public String getAddress() {
            return address;
        }

        public String getFace_img() {
            return face_img;
        }

        public String getCard_img() {
            return card_img;
        }

        public String getCompany() {
            return company;
        }

        public String getDepartment() {
            return department;
        }

        public String getEmail() {
            return email;
        }

        public String getId() {
            return id;
        }

        public String getJob() {
            return job;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getWechat_id() {
            return wechat_id;
        }

        @Override
        public String toString() {
            return "MineCard{" +
                    "address='" + address + '\'' +
                    ", card_img='" + card_img + '\'' +
                    ", company='" + company + '\'' +
                    ", department='" + department + '\'' +
                    ", email='" + email + '\'' +
                    ", id='" + id + '\'' +
                    ", job='" + job + '\'' +
                    ", name='" + name + '\'' +
                    ", phone='" + phone + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", wechat_id='" + wechat_id + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public MineCard getData() {
        return data;
    }

    @Override
    public String toString() {
        return "MineCardModel{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
